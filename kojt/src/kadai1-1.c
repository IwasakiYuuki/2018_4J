#include <stdio.h>
#include <math.h>

#define F_SAMP 200

int main(){

    return 0;
}

double create_sin(int n, double a, double f, double p){
    double *ans;
    ans = (double *)malloc(sizeof(double)*n);
    for(i=0; i<n; i++){
        ans[i] = a*sin((2*PI*f*n)/(F_SAMP))
    }

}