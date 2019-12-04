
/*
* Library for Base64 conversion based on RFC1421
* Author: Alexandre Heideker
*/
#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
//#include <string.h>

void prtBits(uint32_t n) {
    while (n) {
        if (n & (uint32_t)1)
            printf("1");
        else
            printf("0");

        n >>= 1;
    }
    printf("\n");
}


char B64Code2Char(uint8_t cod) {
    /*
    * Return Base64 char for a byte given
    */
   // printf("cod: %d |", cod);
    if (cod < 26) {
        return 'A' + cod;
    } else if (cod < 52) {
        return 'a' + (cod - 26);
    } else if (cod < 62) {
        return '0' + (cod - 52);
    } else if (cod == 62) {
        return '+';
    } else if (cod == 63) {
        return '/';
    } else {
        printf("ERROR: Base64 expect 0-63 code. Received %i\n", cod);
        exit(1);
    }
}

uint8_t B64Char2Code(char simbol) {
    /*
    * Return byte for a Base64 char given
    */
    if ((simbol >= 'A') && (simbol <= 'Z')) {
        return (uint8_t)simbol - (uint8_t)'A';
    } else if ((simbol >= 'a') && (simbol <= 'z')) {
        return (uint8_t)simbol - (uint8_t)'a' + 26;
    } else if ((simbol >= '0') && (simbol <= '9')) {
        return (uint8_t)simbol - (uint8_t)'0' + 52;
    } else if (simbol == '+') {
        return 62;
    } else if (simbol == '/') {
        return 63;
    } else {
        printf("ERROR: Invalid Base64 character. Received %c\n", simbol);
        exit(1);
    }
}

int SizeOfB64encode(int sizeOfIn){
    /*
    * Return nubem of bytes for output Base64 encoded sequence
    */
    if (sizeOfIn<=0) return -1;
    int blocks = sizeOfIn / 3;
    int rem = (sizeOfIn % 3);
    return 4 * (blocks + (rem>0? 1:0));
}

int Convert2B64(const uint8_t * in, int size, char * Buffer, int maxBuffer) {
    /*
    * Encode in block in Base64 to out Buffer
    * 
    */
    int lastChar = SizeOfB64encode(size);
    if (lastChar+1 > maxBuffer) {
        printf("ERROR: Given buffer for Base64 output is too small\n");
        return -1;
    }
    int blocks = size / 3;
    int rem = size % 3;
    int i;
    uint32_t line;
    uint32_t lineB;
    memset(Buffer, 0, maxBuffer);
    for (i=0; i < blocks; i++) {
        line = 0;
        line |= in[3*i] << 16;
        line |= in[3*i+1] << 8;
        line |= in[3*i+2];
        Buffer[4*i + 0] = B64Code2Char((line >> 18) & 0x3F);
        Buffer[4*i + 1] = B64Code2Char((line >> 12) & 0x3F);
        Buffer[4*i + 2] = B64Code2Char((line >> 6) & 0x3F);
        Buffer[4*i + 3] = B64Code2Char(line & 0x3F);
    }
    line = 0;
    i = blocks;
    if (rem == 1) {
        line = in[3*i] << 16;
        Buffer[4*i + 0] = B64Code2Char((line >> 18) & 0x3F);
        Buffer[4*i + 1] = B64Code2Char((line >> 12) & 0x3F);
        Buffer[4*i + 2] = '=';
        Buffer[4*i + 3] = '=';
    }
    if (rem == 2) {
        line = 0;
        line |= in[3*i] << 16;
        line |= in[3*i+1] << 8;
        Buffer[4*i + 0] = B64Code2Char((line >> 18) & 0x3F);
        Buffer[4*i + 1] = B64Code2Char((line >> 12) & 0x3F);
        Buffer[4*i + 2] = B64Code2Char((line >> 6) & 0x3F);
        Buffer[4*i + 3] = '=';
   }
   return lastChar;
}



/*

Test

*
int main( int argc, const char* argv[] )
{
    int j = SizeOfB64encode(strlen(argv[1]))+1;
    char * buffer = malloc(j);
    int i = Convert2B64(argv[1], strlen(argv[1]), buffer, j);
	printf( "Original: %s\nSize of original: %d\nBase64: %s\nSize of B64: %d\n", argv[1], strlen(argv[1]), buffer, i);
}
*/



