/**********************************************************
*                                                         *
*  IMAIoT - Infrastructure Monitoring Agent for IoT       *
*                                                         *
*  Author: Alexandre Heideker                             *
*  e-mail: alexandre.heideker@ufabc.edu.br                *
*  UFABC - 2019                                           *
*  Ver. 0.2                                               *
*                                                         *
*  Version History                                        *
*  Ver. 0.1 - First release                               *
*  Ver. 0.2 - 2019-02-27 TCP server suport & minors fixes *
*                                                         *
***********************************************************/

/************************************
 *  ToDo list:
 *  - Storage stats
 *  - Improve docker stats collector
 *  - MySQL log
 *  - Subscriber in Orion to receive actuator commands
 *  - 
 *  - 
 * 
 ************************************/


#include <thread>
#include <unistd.h>
#include <iostream>
#include <fstream>
#include <ctime>
#include <string.h>
#include <stdlib.h>
#include <sstream>
#include <curl/curl.h>
#include <sys/sysinfo.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <vector>
#include <mutex>
#define SEM_WAIT mt.lock();
#define SEM_POST mt.unlock();
#include "swissknife.h"
#include "monitor2.h"

using namespace std;

std::mutex mt;

void logOut(std::string, MonData);
void logHeader();
void logTXT();
void logSQL();
void logJSON();
void thrSampling();
void thrLog();
void thrTCPServer();
string getJSONstats();
string getTXTstats();
string getSQLstats();
bool stayRunning = true;
bool comma = false;

int main(int argc, char *argv[]) {
    readSetup();
    if (argc>1) {
        if (!readSetupFromCL(argc, argv)) return -1;
    }
    MFstats.debug = debugMode;
    //MFstats.setVar(MFstats.IMvar);
    std::thread refresh (thrSampling);
    std::thread tLog (thrLog);
    std::thread tServer (thrTCPServer);

    if (ExpTime > 0) {
        sleep(ExpTime);
        stayRunning = false;
    }

    tLog.join();
    tServer.join();
    refresh.join();
    return 0;
}

void thrSampling(){
    while (stayRunning) {
        std::time_t tsIni = std::time(0);
        SEM_WAIT
        if (debugMode) cout << "Sampling..." << tsIni << endl;
        MFstats.Refresh();
        SEM_POST
        if (SincMode) {
            if (debugMode) cout << "Updating Log-file..." << endl;
            if (LogType=="JSON")
                logJSON();
            else
            if (LogType=="TXT")
                logTXT();
            else
            if (LogType=="SQL")
                logSQL();
        }
        int taskTime = (int) (std::time(0) - tsIni);
        int sleepTime = SampplingTime - taskTime;
        if (taskTime <= SampplingTime && stayRunning) 
            sleep(sleepTime);
    }
}

void thrTCPServer(){
    if (ServerMode!=1) return;
    int opt = 1;
    int client;
    struct sockaddr_in SvrAddr, CliAddr;
    socklen_t sin_len = sizeof(CliAddr);
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        cout << "Error: can't open socket!" << endl;
        return;
    }
    setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
    SvrAddr.sin_family = AF_INET;
    SvrAddr.sin_addr.s_addr = INADDR_ANY;
    SvrAddr.sin_port = htons(ServerPort);
    if (bind(sock, (struct sockaddr *) &SvrAddr, sizeof(SvrAddr)) == -1) {
        close(sock);
        cout << "Error: can't bind socket!" << endl;
        return;
    }
    listen(sock, 5);
    while (stayRunning){
        client = accept(sock, (struct sockaddr *) &CliAddr, &sin_len);        
        if (debugMode) cout << "Got a connection..." << endl;
        string response = getJSONstats();
        send(client, response.c_str(), strlen(response.c_str()), 0);
        close(client);
    }
}

void thrLog(){
    if (LogType == "") return;
    logHeader();
    if (SincMode) return;
    while (stayRunning) {
        std::time_t tsIni = std::time(0);
        if (debugMode) cout << "Updating Log-file..." << endl;
        if (LogType=="JSON")
            logJSON();
        else
        if (LogType=="TXT")
            logTXT();
        else
        if (LogType=="SQL")
            logSQL();
        int taskTime = (int) (std::time(0) - tsIni);
        int sleepTime = LogIntervall - taskTime;
        if (taskTime <= LogIntervall && stayRunning) 
            sleep(sleepTime);
    }
}


std::string getJSONstats(){
    ostringstream json;
    SEM_WAIT
    json << "{\"id\":\"" << NodeName << "\", \"Architecture\":{\"type\":\"Text\", \"value\":\"" << MFstats.arch 
            << "\"},\"MemorySize\":{\"type\":\"Integer\", \"value\": " << MFstats.MemorySize 
            << "},\"MemoryAvailable\":{\"type\":\"Integer\", \"value\": " << MFstats.MemoryAvailable
            << "},\"LocalTimestamp\":{\"type\":\"Integer\", \"value\": " << MFstats.Timestamp
            << "},\"CPU\":{\"type\":\"Integer\", \"value\": " << MFstats.cpuLevel //:" << MFstats.getJsonCpu() 
            << "},\"Storage\":" << MFstats.getJsonStorage() 
            << ",\"NetworkStats\":" << MFstats.getJsonNetworkStats() 
            << ",\"NetworkAdapters\":" << MFstats.getJsonNetworkAdapters() 
            << ",\"Process\":" << MFstats.getJsonProcess() 
            << "}";
    SEM_POST
    return json.str();
}

void logJSON(){
    ofstream File;
    File.open (LogFileName.c_str(), std::ofstream::out | std::ofstream::app);
    File << getJSONstats() << "\n";
    File.close();
    return;
}

void logHeader(){
    ofstream File;
    File.open (LogFileName.c_str(), std::ofstream::out);
    if (LogType == "SQL") {
        File << "insert into monitor (exp, nodetype, tStamp, cpu, MemTotal, Mem, TcpTxQueue, TcpRxQueue, "
            << "UdpTxQueue, UdpRxQueue, TcpWindow, erro) values " << endl;
    } else
    if (LogType == "TXT") {
        File << "tag;tStamp;cpu;MemTotal;Mem;TcpTxQueue;TcpRxQueue;UdpTxQueue;UdpRxQueue;"
            << "TcpWindow;erro;[Processes/Dockers(Kind;Name;CPU;Memory)]" << endl;
    }
    File.close();
    return;
}

std::string getTXTstats(){
    ostringstream txt;
    SEM_WAIT
    txt << tag << ";" << MFstats.Timestamp << ";" 
            << MFstats.cpuLevel << ";" << MFstats.MemorySize << ";" << 
            (MFstats.MemorySize - MFstats.MemoryAvailable) << ";" 
            << MFstats.netData.TCPtxQueue << ";" << MFstats.netData.TCPrxQueue << ";" 
            << MFstats.netData.UDPtxQueue << ";" << MFstats.netData.UDPrxQueue << ";"
            << MFstats.netData.TCPMaxWindowSize << ";OK";
    //log watched processes
    for (auto p: MFstats.Processes) {
        txt << ";process;" << p.Name << ";" << p.cpu << ";" << p.DataMem;
    }    

    //log watched dockers
    for (auto p: MFstats.Dockers) {
        txt << ";docker;" << p.Name << ";" << p.cpu << ";" << p.DataMem;
    }    
    SEM_POST
    return txt.str();
}

std::string getSQLstats(){
    ostringstream txt;
    SEM_WAIT
    txt << " ('" << tag << "', 'machine', " << MFstats.Timestamp << ", " 
            << MFstats.cpuLevel << ", " << MFstats.MemorySize << ", " << 
            (MFstats.MemorySize - MFstats.MemoryAvailable) << ", " 
            << MFstats.netData.TCPtxQueue << ", " << MFstats.netData.TCPrxQueue << ", " 
            << MFstats.netData.UDPtxQueue << ", " << MFstats.netData.UDPrxQueue << ", "
            << MFstats.netData.TCPMaxWindowSize << ", 'OK')";
    
    //log watched processes
    for (auto p: MFstats.Processes) {
        txt << ", ('" << tag << "', '" << p.Name << "', " << MFstats.Timestamp << ", " 
                << p.cpu << ", " << MFstats.MemorySize << ", " << p.DataMem << ", " 
                << MFstats.netData.TCPtxQueue << ", " << MFstats.netData.TCPrxQueue << ", " 
                << MFstats.netData.UDPtxQueue << ", " << MFstats.netData.UDPrxQueue << ", "
                << MFstats.netData.TCPMaxWindowSize << ", 'Process')";
    }    

    //log watched dockers
    for (auto p: MFstats.Dockers) {
        txt << ", ('" << tag << "', '" << p.Name << "', " << MFstats.Timestamp << ", " 
                << p.cpu << ", " << MFstats.MemorySize << ", " << p.DataMem << ", " 
                << MFstats.netData.TCPtxQueue << ", " << MFstats.netData.TCPrxQueue << ", " 
                << MFstats.netData.UDPtxQueue << ", " << MFstats.netData.UDPrxQueue << ", "
                << MFstats.netData.TCPMaxWindowSize << ", 'Docker')";
    }    
    SEM_POST
    txt << std::flush;
    return txt.str();
}

void logTXT(){
    ofstream File;
    File.open (LogFileName.c_str(), std::ofstream::out | std::ofstream::app);
    File << getTXTstats() << endl;
    File.close();
    return;
}
void logSQL(){
    ofstream File;
    File.open (LogFileName.c_str(), std::ofstream::out | std::ofstream::app);
    File << (comma ? "," : "") << getSQLstats() << endl;
    comma = true;
    File.close();
    return;
}

string getDockerProcesses(){
    string Cmd("docker stats --all --no-stream --format \";{{.Name}};{{.CPUPerc}};{{.MemUsage}}\"");
    string ret(run(Cmd));
    replaceAll(ret, "\n", ";");
    return ret;
}






