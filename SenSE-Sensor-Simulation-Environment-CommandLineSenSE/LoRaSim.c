
/*
* LoRaSim - LoRaWan end node simulator
* Author: Alexandre Heideker
*/
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Base64Lib.c"
#include <math.h>
#include <mcrypt.h>

int isLittleEndian(){
    //return 0 if BigEndian and 1 if LittleEndian
    volatile uint32_t i=0x01234567;
    return (*((uint8_t*)(&i))) == 0x67;
}
void MakeUpPayload(char * , char * , int, char * , int, u_int32_t , u_int32_t );
void dump( u_int8_t * , int);

u_int8_t _debugMode;
int main( int argc, const char* argv[] )
{
   //usage: mkpayload -f N -a AF99C012 -k <16hex> -d <payload data>
    _debugMode = 0;
    u_int32_t FRMCount;
    u_int32_t DevAddr;
    char appSKey[16];
    char * data;
    int inSize;
   if (argc!=9) {
        printf("usage: mkpayload -f N -a AF99C012 -k <16hex> -d <payload data>\n");
        return -1;
    }
    for (int i=1; i<argc; i++){
        if (strcmp(argv[i],"-?")==0){
            printf("usage: mkpayload -f N -a AF99C012 -k <16hex> -d <payload data>\n");
            return 0;
        } else 
        if (strcmp(argv[i],"-f")==0){
            //frame numbem
            i++;
            if (i<argc) {
                FRMCount = atoi(argv[i]);
            } else {
                printf("Error: Frame numbem required!\n");
                return -1;                
            }
        } else 
         if (strcmp(argv[i],"-a")==0){
            //device address
            i++;
            if (i<argc) {
                DevAddr = (int)strtol(argv[i], NULL, 16);
            } else {
                printf("Error: Device Address required!\n");
                return -1;                
            }
        } else 
         if (strcmp(argv[i],"-k")==0){
            //appSKey
            i++;
            if (i<argc) {
                if (strlen(argv[i])!=32) {
                    printf("Error: Wrong AppKey size!\n");
                    return -1;
                }
                char h[2];
                for (int j=0; j<strlen(argv[i]); j=j+2) {
                    h[0]=argv[i][j];
                    h[1]=argv[i][j+1];
                    appSKey[(int)j/2] = (u_int8_t)strtol(h, NULL, 16);
                }
            } else {
                printf("Error: AppKey required!\n");
                return -1;                
            }
        } else 
        if (strcmp(argv[i],"-d")==0){
            //data
            i++;
            if (i<argc) {
                inSize = strlen(argv[i]);
                data = malloc(inSize);
                memcpy(data, argv[i], inSize);
    //            printf("data: %s\n",data);
            } else {
                printf("Error: Payload Data required!\n");
                return -1;                
            }
        }
    }
    int outSize = ((int)inSize/16) * 16;
    if ((inSize % 16) >0) outSize += 16;
    char * out;
    out = malloc(outSize);
    MakeUpPayload(appSKey, data, inSize, out, outSize, DevAddr, FRMCount);
    char * outB64;
    int outB64size = SizeOfB64encode(outSize)+1;
    //printf("%d\n", outB64size);
    outB64 = malloc(outB64size);
    int i = Convert2B64(out, outSize, outB64, outB64size);
    printf("%s", outB64);
}


void dump( u_int8_t * vector, int size){
    for (int i=0; i<size; i++)
        printf("|%2x",(u_int8_t) vector[i]);
    printf("|\n");
}


void MakeUpPayload(char * appSKey, char * inPayload, int inSize, char * outPayload, int maxOutSize, u_int32_t DeviceAdress, u_int32_t FrameCount ){
    //appSKey size must be 16
    MCRYPT td;
 //   char* IV = "AAAAAAAAAAAAAAAA";
 //   int lilEnd = isLittleEndian();
    u_int8_t Ai[16];
    u_int8_t Di[16];
    u_int8_t Si[16];
    u_int8_t block;
    u_int8_t cntBlocks = ceil(inSize/16)+1;
    td = mcrypt_module_open("rijndael-128", NULL, "ecb", NULL);
    mcrypt_generic_init(td, appSKey, 16, NULL);//IV);
    int s;
    memset(outPayload, 0, maxOutSize);
    if (_debugMode) dump(outPayload, maxOutSize);
    for (block=1; block<=cntBlocks; block++){
        memset(Ai, 0, 16);
        memset(Di, 0, 16);
        memset(Si, 0, 16);
        if (block*16>inSize) s = inSize-(block-1)*16; else s = 16;
        memcpy(Di, inPayload+((block-1)*16), s);
        Ai[0] = 1;
//        if (lilEnd) {
            memcpy(Ai+6, &DeviceAdress, 4);
            memcpy(Ai+10, &FrameCount, 4);
//        } else {
//            memcpyBE(Ai+6, &DeviceAdress, 4);
//            memcpyBE(Ai+10, &FrameCount, 4);
//        }
        Ai[15] = block;
        if (_debugMode) dump(&Ai,16);
        mcrypt_generic(td, Ai, 16);
        if (_debugMode) dump(&Ai,16);
        //xor com bloco de dados..
        for (int i=0; i<16; i++)
            Si[i] = Ai[i] ^ Di[i];
        memcpy(outPayload+((block-1)*16), Si, 16);
        if (_debugMode) dump(&Di,16);
        if (_debugMode) dump(&Si,16);
        if (_debugMode) printf("-------------------------------------------------------------\n");
        if (_debugMode) dump(outPayload, maxOutSize);
    }
    mcrypt_generic_deinit (td);
    mcrypt_module_close(td);
}


