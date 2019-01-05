//2018年度・課題５・出席番号４番

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#define NUM 1024
#define EPSILON 1e-5

typedef struct{
	double re;
	double im;
}comp;

void inputData(comp *data, char *filename, int N);
void outputData(comp *data, char *filename, int N);
void dft(comp *xn, int N, comp *Xk, int a, int b);
void ampSpectrum(comp *Xk, int N, double *spec);
void phaSpectrum(comp *Xk, int N, double *spec);
void hamming(comp *x1, comp *x2, int N);

comp xn[NUM];
comp Xk[NUM];
char *filename = "txt/04岩崎_dsp1-9.txt";

int main(){
	
	int i = 0;
	int N = NUM;
	double buf[NUM]={0}, pha_buf[NUM]={0};
	comp a[NUM], chache[NUM];
	FILE *fp;

	for(i=0;i<N;i++){
		Xk[i].re = 0;
		Xk[i].im = 0;
		xn[i].re = 0;
		xn[i].im = 0;
		a[i].re = 0;
		a[i].im = 0;
		chache[i].re = 0;
		chache[i].im = 0;
	}

	inputData(xn, filename, N);
	printf("2\n");
//	hamming(chache, xn, N);
	printf("3\n");
//	xn[0].re = 3;
//	xn[1].im = 3;
//	xn[2].re = -1;
//	xn[3].im = -1;
//	xn[4].re = 1;
//	xn[5].re = sqrt(2);
//	xn[6].re = 1;
//	xn[7].re = sqrt(2);

	dft(xn, N, Xk, 1, 1);
//	printf("4\n");
	ampSpectrum(Xk, N, buf);
//	printf("5\n");
//	phaSpectrum(Xk, N, pha_buf);
//	printf("6\n");
//	dft(xn, N, a, -1, N);
	printf("7\n");

//	for(i=0;i<N;i++){
//		printf("xn[%d].re\t=%12lf\n", i, xn[i].re);
//		printf("xn[%d].im\t=%12lf\n", i, xn[i].im);
//		printf("a[%d].re\t=%12lf\n", i, a[i].re);
//		printf("a[%d].im\t=%12lf\n", i, a[i].im);
//		printf("Xk[%d].re\t=%12lf\n",i ,Xk[i].re);
//		printf("Xk[%d].im\t=%12lf\n",i ,Xk[i].im);
//		printf("amp[%d]=%12lf\n", i, buf[i]);
//		printf("pha[%d]=%12lf\n", i, pha_buf[i]);
//	}

//	outputData(xn, "xn", N);
//	outputData(a, "txt/a", N);
//	outputData(Xk, "Xk", N);
//	outputData(chache, "chache", N);
	
	fp = fopen("txt/amp.txt", "w");
	for(i = 0; i < N; i++){
		if(fprintf(fp, "%lf\n", buf[i]) < 0 ){
			printf("ERROR\n");
			return 0;
		}
	}
//
//	fp = fopen("pha.txt", "w");
//
//	for(i = 0; i < N; i++){
//		if(fprintf(fp, "%lf\n", pha_buf[i]) < 0 ){
//			printf("ERROR\n");
//			return 0;
//		}
//	}

	printf("2018年度・課題５・出席番号４番\n");
	printf("usage:内部の定数を変化させ実行するとDFTします。\n");
	printf("ファイルに結果を出力しました\n");

	return 0;
}

void inputData(comp *data, char *filename, int N){
	FILE *fp;
	int i=0;

	fp = fopen(filename, "r");
	if(fp == NULL)printf("aaa\n");
	for(i=0;i<N;i++){
		if(fscanf(fp, "%lf\n", &data[i].re)==EOF){
			printf("[ERROR] cant read file to the end.\n");
			exit(1);
		}
	}
	printf("1\n");
}

void outputData(comp *data, char *filename, int N){
	FILE *fp_re, *fp_im;
	int i = 0;
	char filename_re[256];
	char filename_im[256];

	strcpy(filename_re, filename);
	strcpy(filename_im, filename);
	strcat(filename_re, "_re.txt");
	strcat(filename_im, "_im.txt");

	fp_re = fopen(filename_re, "w");
	fp_im = fopen(filename_im, "w");
	if(fp_re == NULL){
		printf("cant open file.\n");
	}
	if(fp_im == NULL){
		printf("cant open file.\n");
	}

	for(i = 0; i < N; i++){
		if(fprintf(fp_re, "%lf\n", data[i].re) < 0 ){
			printf("ERROR\n");
			return;
		}
		if(fprintf(fp_im, "%lf\n", data[i].im) < 0){
			printf("[ERROR] cant write file to the end.");
			return;
		}
	}

	fclose(fp_re);
	fclose(fp_im);
}

void dft(comp *xn, int N, comp *Xk, int a, int b){
	int i,k;

	for(k=0;k<N;k++){
		for(i=0;i<N;i++){
			Xk[k].re += (xn[i].re * cos((2*M_PI/N)*i*k) + a * (xn[i].im * sin((2*M_PI/N)*i*k))) / b;
			Xk[k].im += (xn[i].im * cos((2*M_PI/N)*i*k) - a * (xn[i].re * sin((2*M_PI/N)*i*k))) / b;
		}
	}
}

void ampSpectrum(comp *Xk, int N, double *spec){
	int k=0;

	for(k=0;k<N;k++){
		spec[k] = sqrt(Xk[k].re*Xk[k].re + Xk[k].im*Xk[k].im);
		if(spec[k] < EPSILON){
			spec[k] = 0;
		}else{
			spec[k] = 20 * log10(spec[k]);
		}
	}
}

void phaSpectrum(comp *Xk, int N, double *spec){
	int k=0;
	for(k=0;k<N;k++){
		spec[k] = atan(Xk[k].im/Xk[k].re);
	}
}

void hamming(comp *x1, comp *x2, int N){
	int i=0;
	for(i=0;i<N;i++){
		x2[i].re = x1[i].re * (0.54 - 0.46*cos(2*M_PI*i/N));
		x2[i].im = x1[i].im * (0.54 - 0.46*cos(2*M_PI*i/N));
	}
}
