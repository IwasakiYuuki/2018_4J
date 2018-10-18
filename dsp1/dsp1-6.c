#include <stdio.h>
#include <stdlib.h>
#include <string.h>

FILE *readFP(char fileName[]);
FILE *writeFP(char fileName[]);
void readChunkRIFF(FILE *fp);
void readChunkFMT(FILE *fp);
void readDatas(FILE *fpIN,FILE *fpOUT);
void textToWave(FILE *fpIN,FILE *fpOUT);
void printChunkRIFF(void);
void printChunkFMT(void);
void printForDrill(void);

//RIFFチャンク、WAVEフォームタイプ構造体
typedef struct{
    char id[4];             //"RIFF"
    unsigned long size;     //ファイルサイズ-8バイト
    char form[4];           //"WAVE"
}riff_chunk;

//fmtチャンク構造体
typedef struct{
    char id[4];                 //"fmt "スペース含めて4文字
    unsigned long size;         //fmt領域のサイズ
    unsigned short format_id;   //フォーマットID(PCM:1)
    unsigned short channel;     //チャンネル数（モノラル:1 ステレオ:2）
    unsigned long fs;           //サンプリング周波数
    unsigned long byte_sec;     //1秒あたりのバイト数（fs * byte_samp）/平均データ速度
    unsigned short byte_samp;   //1要素あたりのバイト数（channel * (bit/8)）
    unsigned short bit;         //量子化ビット数(8 or 16)
}fmt_chunk;

riff_chunk riff;
fmt_chunk fmt;
unsigned long dataSize;     //データサイズ


/************* main *************/
void main(){
    FILE *fpIN,*fpOUT;
    fpIN = readFP("ara11.wav");
    fpOUT = writeFP("ara11.txt");
    readDatas(fpIN,fpOUT);
    readChunkRIFF(fpIN);
    readChunkFMT(fpIN);
    fpIN = readFP("data11025.txt");
    fpOUT = writeFP("out.wav");
    printForDrill();
    textToWave(fpIN,fpOUT);
}
/********************************/

/************* Functions *************/
FILE *readFP(char fileName[]){
    FILE *fp;
    fp = fopen(fileName,"r");
    if(fp==NULL){
        printf("Error : Cannot Read File[%s]\n",fileName);
        exit(1);
    }
    return fp;
}

FILE *writeFP(char fileName[]){
    FILE *fp;
    fp = fopen(fileName,"w");
    if(fp==NULL){
        printf("Error : Cannot Write File[%s]\n",fileName);
        exit(1);
    }
    return fp;
}

void readChunkRIFF(FILE *fp){
    fseek(fp,0,SEEK_SET);   //ストリームをファイルの先頭に持ってくる
    fread(&riff.id,1,4,fp);
    fread(&riff.size,4,1,fp);
    fread(&riff.form,1,4,fp);
}

void writeChunkRIFF(FILE *fp,riff_chunk writeRIFF){
    fseek(fp,0,SEEK_SET);   //ストリームをファイルの先頭に持ってくる
    fwrite(&writeRIFF.id,1,4,fp);
    fwrite(&writeRIFF.size,4,1,fp);
    fwrite(&writeRIFF.form,1,4,fp);
}

void readChunkFMT(FILE *fp){
    fseek(fp,12,SEEK_SET);  //ストリームをfmtチャンクの先頭に持ってくる
    fread(&fmt.id,1,4,fp);
    fread(&fmt.size,4,1,fp);
    fread(&fmt.format_id,2,1,fp);
    fread(&fmt.channel,2,1,fp);
    fread(&fmt.fs,4,1,fp);
    fread(&fmt.byte_sec,4,1,fp);
    fread(&fmt.byte_samp,2,1,fp);
    fread(&fmt.bit,2,1,fp);
}

void writeChunkFMT(FILE *fp,fmt_chunk writeFMT){
    fseek(fp,12,SEEK_SET);  //ストリームをfmtチャンクの先頭に持ってくる
    fwrite(&writeFMT.id,1,4,fp);
    fwrite(&writeFMT.size,4,1,fp);
    fwrite(&writeFMT.format_id,2,1,fp);
    fwrite(&writeFMT.channel,2,1,fp);
    fwrite(&writeFMT.fs,4,1,fp);
    fwrite(&writeFMT.byte_sec,4,1,fp);
    fwrite(&writeFMT.byte_samp,2,1,fp);
    fwrite(&writeFMT.bit,2,1,fp);
}

void readDatas(FILE *fpIN,FILE *fpOUT){
    unsigned long i;
    short tmp;
    fseek(fpIN,40,SEEK_SET);
    fseek(fpOUT,0,SEEK_SET);
    fread(&dataSize,4,1,fpIN);
    dataSize = dataSize/2;
    for(i=0;i<dataSize;i++){
        fread(&tmp,2,1,fpIN);
        fprintf(fpOUT,"%d\n",tmp);
    }
}

void textToWave(FILE *fpIN,FILE *fpOUT){
    char d[4]="data";
    int counter=0;
	int buf = 0;
    short tmp;
    unsigned long fs=11025;
    riff_chunk writeRIFF={
        "RIFF",
        0,
        "WAVE"
    };
    fmt_chunk writeFMT={
        "fmt ",
        16,
        1,
        1,
        0,
        0,
        2,
        16,
    };
    fseek(fpIN,0,SEEK_SET);     //読み取るtextデータ用
    fseek(fpOUT,44,SEEK_SET);   //waveに書き込むデータ用
    fscanf(fpIN,"%hd",&tmp);
    printf("%d",tmp);
    while(fscanf(fpIN,"%hd ",&tmp)!=EOF){
        fwrite(&tmp,2,1,fpOUT);
		buf++;
        printf("%d , %d\n", buf, tmp);
    }
    printf("num=%d",counter);
    writeRIFF.size=counter+36;
    writeFMT.byte_samp=fs;
    writeFMT.byte_sec=fs*2;
    writeChunkRIFF(fpOUT,writeRIFF);
    writeChunkFMT(fpOUT,writeFMT);
    fseek(fpOUT,36,SEEK_SET);
    fwrite(&d,4,1,fpOUT);
    fwrite(&counter,4,1,fpOUT);
}


/************* Display Functions *************/
void printChunkRIFF(){
    char id[5],form[5];
    sprintf(id,"%s",riff.id);
    sprintf(form,"%s",riff.form);
    id[4]='\0';
    form[4]='\0';
    printf("----- RIFF -----\n");
    printf("ID:%s\n", id);
    printf("FileSize:%lu\n",riff.size);
    printf("form:%s\n",form);
}
void printChunkFMT(){
    char id[5];
    sprintf(id,"%s",fmt.id);
    id[4]='\0';
    printf("----- FMT -----\n");
    printf("ID:%s\n",id);
    printf("FMTSize:%lu\n",fmt.size);
    printf("formatID:%u\n",fmt.format_id);
    printf("channel:%u\n",fmt.channel);
    printf("SamplingHz:%lu\n",fmt.fs);
    printf("byte/sec:%lu\n",fmt.byte_sec);
    printf("byte/ele:%u\n",fmt.byte_samp);
    printf("bit:%u\n",fmt.bit);
}

void printForDrill(){
    printf("読み取り結果\n");
    printf("ファイルサイズ(-8バイト)：%lu\n",riff.size);
    printf("チャンネル数：%u\n",fmt.channel);
    printf("サンプリング周波数：%lu[Hz]\n",fmt.fs);
    printf("量子化ビット数：%u[bit]\n",fmt.bit);
    printf("データ数：%lu[sample]\n",dataSize);
    printf("録音時間：%lf[s]\n",(double)(1.0/fmt.fs)*dataSize);  
}
