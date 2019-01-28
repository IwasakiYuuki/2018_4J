//2018年度・課題１０・出席番号４番

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

#define XN_SIZE 11025
#define WN_SIZE 50
#define outerr() fprintf(stderr, "[ERROR] IN FUNC %s ,LINE %d.\n", __func__, __LINE__)

double create_nor_random(void);
double get_inner_product(double *a1, double *a2, int n);

int main(){
    FILE *fp_in, *fp_wn, *fp_ans;
    char *filename_in = "input.txt", *filename_wn = "wn.txt", *filename_ans = "ans.txt";
    double *xn;
    double *hn;
    double *wn;
    double d, y, e, xnorm, step=1.0, e_2;
    int i, sample, a;

    srand((unsigned)time(NULL));
    fp_in = fopen(filename_in, "r");
    if(fp_in == NULL){
        outerr();
        exit(1);
    }
    fp_wn = fopen(filename_wn, "r");
    if(fp_wn == NULL){
        outerr();
        exit(1);
    }
    fp_ans = fopen(filename_ans, "w");
    if(fp_ans == NULL){
        outerr();
        exit(1);
    }
    xn = (double *)malloc(sizeof(double)*WN_SIZE);
    hn = (double *)malloc(sizeof(double)*WN_SIZE);
    for(i=0; i<WN_SIZE; i++){
        xn[i] = 0;
    }
    for(i=0; i<WN_SIZE; i++){
        hn[i] = 0;
    }
    wn = (double *)malloc(sizeof(double)*WN_SIZE);
    for(i=0; i<WN_SIZE; i++){
        fscanf(fp_wn, "%lf\n", &wn[i]);
    }
    for(sample=0; sample<XN_SIZE; sample++){
        for(i=WN_SIZE-1; i>0; i--){
            xn[i] = xn[i-1];
        }
        //fscanf(fp_in, "%lf\n", &xn[0]);
        xn[0] = create_nor_random();
        d = get_inner_product(xn, wn, WN_SIZE);
        y = get_inner_product(xn, hn, WN_SIZE);
        if((d-y) == 0){
        }else{
            e = d - y;
        }
        xnorm = get_inner_product(xn, xn, WN_SIZE);
        for(i=0; i<WN_SIZE; i++){
            hn[i] += (step*xn[i]*e)/(xnorm+0.0000001);
        }
        e_2 = 10*log10(e * e);
        fprintf(fp_ans, "%lf\n", e_2);
    }
    fclose(fp_in);
    fclose(fp_wn);
    fclose(fp_ans);
    return 0;
}

double create_nor_random(void){
    return sqrt(-2.0*log(rand()/(double)RAND_MAX))*cos((2.0*M_PI*rand())/(double)RAND_MAX);
}

double get_inner_product(double *a1, double *a2, int n){
    int i;
    double ans=0;
    for(i=0; i<n; i++){
        ans += a1[i]*a2[i];
    }
    return ans;
}