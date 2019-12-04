/**********************************************************
*                                                         *
*  IMAIoT - Infrastructure Monitoring Agent for IoT       *
*                                                         *
*  Author: Alexandre Heideker                             *
*  e-mail: alexandre.heideker@ufabc.edu.br                *
*  UFABC - 2019                                           *
*  Utility library                                        *
***********************************************************/

using namespace std;

string trim(const string& str){
    size_t first = str.find_first_not_of(' ');
    if (string::npos == first)
    {
        return str;
    }
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last - first + 1));
}

std::vector<std::string> splitString(std::string strToSplit, char delimeter) {
    std::stringstream ss(strToSplit);
    std::string item;
    std::vector<std::string> splittedStrings;
    while (std::getline(ss, item, delimeter))
    {
       splittedStrings.push_back(item);
    }
    return splittedStrings;
}

string UCase(string strIn) {
    string p;
    for (unsigned int i=0; i<strIn.length(); i++) {
        p += toupper(strIn[i]);
    }
    return p;
}

string run(string command) {
    ostringstream out;
    FILE *in;
    char buff[512];
    if(!(in = popen(command.c_str(), "r"))){
        return NULL;
    }
    while(fgets(buff, sizeof(buff), in)!=NULL)
        out << buff;
    pclose(in);
    return out.str();
}

void replaceAll(string& str, const string& from, const string& to) {
    if(from.empty())
        return;
    size_t start_pos = 0;
    while((start_pos = str.find(from, start_pos)) != std::string::npos) {
        str.replace(start_pos, from.length(), to);
        start_pos += to.length();
    }
}

std::string ReplaceForbidden(std::string s){
    if (s.find("("))
        replaceAll(s, "(", " "); 
    if (s.find(")"))
        replaceAll(s, ")", " "); 
    if (s.find("<"))
        replaceAll(s, "<", " "); 
    if (s.find(";"))
        replaceAll(s, ";", " "); 
    if (s.find("'"))
        replaceAll(s, "'", " "); 
    if (s.find("\""))
        replaceAll(s, "\"", " "); 
    if (s.find("="))
        replaceAll(s, "=", " "); 
    return s;
}


