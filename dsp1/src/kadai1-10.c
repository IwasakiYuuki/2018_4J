//2018年度・課題１０・出席番号４番

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

#define outerr() fprintf(stderr, "[ERROR] IN FUNC %s ,LINE %d.\n", __func__, __LINE__)

double create_nor_random(void);

int main(){
    FILE *fp_white, *fp_color;
    int i = 0;
    short int *white;
    short int *color;
    short int old, cur;

    white = (short int *)malloc(sizeof(short int)*11025);
    color = (short int *)malloc(sizeof(short int)*11025);
    fp_white = fopen("./txt/kadai1-10_white.txt", "w");
    fp_color = fopen("./txt/kadai1-10_color.txt", "w");
    srand((unsigned)time(NULL));
    for(i=0; i<11025; i++){
        white[i] = (short int)(create_nor_random()*1000);
    }
    for(i=0; i<11025; i++){
        if(i==0){
            color[i] = white[i];
        }else{
            color[i] = 0.99 * color[i-1] + white[i];
        }
    }
    for(i=0; i<11025; i++){
        fprintf(fp_white, "%hd\n", white[i]);
        fprintf(fp_color, "%hd\n", color[i]);
    }
    fclose(fp_white);
    fclose(fp_color);
    return 0;
}

double create_nor_random(void){
    return sqrt(-2.0*log(rand()/(double)RAND_MAX))*cos((2.0*M_PI*rand())/(double)RAND_MAX);
}
