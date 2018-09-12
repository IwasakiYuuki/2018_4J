#include <stdio.h>
#include <stdlib.h>

#define SAMPLE_NUM 3000
#define N 50
#define A 20
#define B 85
#define C 10
#define C_A 30
#define C_B 75
#define C_A_B 95
#define D 95
#define D_C 80

int rand_bin(int prob);
int check_rv(unsigned char rv, int pattern);
double dispersion(double *prob, double ave, int n);

int main(){
	
	int i=0, k=0;
	//確率変数A,B,C,D
	unsigned char rv = 0;
	int count_1[N]={0}, count_1_1[N]={0}, count_2[N]={0}, count_2_1[N]={0}, count_3[N]={0}, count_3_1[N]={0};
	double prob_1[N]={0}, prob_2[N]={0}, prob_3[N]={0};
	double sum_1=0, sum_2=0, sum_3=0, disp=0;

	srand((unsigned)time(NULL));
	for(k=0;k<N;k++){
		for(i=0; i<SAMPLE_NUM; i++){
			rv = 0;
			rv |= (rand_bin(A) << 3) + (rand_bin(B) << 2);
			if(check_rv(rv, 0b1100))
				rv |= rand_bin(C_A_B) << 1;
			else if(check_rv(rv, 0b1000))
				rv |= rand_bin(C_A) << 1;
			else if(check_rv(rv, 0b0100))
				rv |= rand_bin(C_B) << 1;
			else
				rv |= rand_bin(C) << 1;
			

			if(check_rv(rv, 0b0010))
				rv |= rand_bin(D_C);
			else
				rv |= rand_bin(D);
			
			if(check_rv(rv, 0b0100)){
				count_1_1[k]++;
				if(check_rv(rv, 0b0001)){
					count_1[k]++;
				}
			}
			if(check_rv(rv, 0b0001) && !check_rv(rv, 0b1000)){
				count_2_1[k]++;
				if(!check_rv(rv, 0b0010)){
					count_2[k]++;
				}
			}
			if(check_rv(rv, 0b1000)){
				count_3_1[k]++;
				if(!check_rv(rv, 0b0001)){
					count_3[k]++;
				}
			}
		}
		prob_1[k] = (double)(count_1[k])/(double)(count_1_1[k]);
		prob_2[k] = (double)(count_2[k])/(double)(count_2_1[k]);
		prob_3[k] = (double)(count_3[k])/(double)(count_3_1[k]);

		sum_1 += prob_1[k];
		sum_2 += prob_2[k];
		sum_3 += prob_3[k];
		printf("P(D=1|B=1)=\t%.5lf\n", prob_1[k]);
		printf("P(C=0|A=0,D=1)=\t%.5lf\n", prob_2[k]);
		printf("P(D=0|A=1)=\t%.5lf\n", prob_3[k]);
	}
	
	disp = dispersion(prob_1, sum_1/N, N);
	printf("\n\ndisp=\t\t%lf\n", disp);	
	return 0;
}

int rand_bin(int prob){
	return ((rand() % 100) + 1) <= prob ? 0: 1;
}

int check_rv(unsigned char rv, int pattern){
	return (rv&pattern) == pattern ? 1: 0;
}

double dispersion(double *prob, double ave, int n){
	int i=0;
	double sum=0;
	for(i=0;i<n;i++){
		sum += (prob[n]-ave)*(prob[n]-ave);
	}
	return sum/n;
}
