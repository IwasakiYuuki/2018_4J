#include <stdio.h>
#include <stdlib.h>

#define USHRT_MAX 65535

typedef struct{
    int num;
    double *data;
}data;

#define outerr() fprintf(stderr, "[ERROR] IN FUNC %s ,LINE %d.\n", __func__, __LINE__)

data input(char *filename);
void output(data x, char *filename);
double inner_product(data d1, data d2);
double diviation_inner_product(data src, data target, int div);
data reverse_data(data d);
data filter(data h, data x);

int main(){
    int n, i;
    data ans;
    data d;
    data h;
    data x;
    h = input("h.txt");
    x = input("ara11_s.txt");
    ans = filter(h, x);
    output(ans, "ara11_s_d.txt");

    return 0;
}

data input(char *filename){
    FILE *fp;
    int cnt=0, i=0;
    double buf;
    data h;

    fp = fopen(filename, "r");
    if(fp==NULL){
        outerr();
        exit(1);
    }
    while(fscanf(fp, "%lf\n", &buf) != EOF){
        cnt++;
    }
    printf("num = %d\n", cnt);
    h.data = (double *)malloc(sizeof(double)*cnt);
    h.num = cnt;
    fseek(fp, 0, SEEK_SET);
    while(fscanf(fp, "%lf\n", &(h.data[i])) != EOF){
        i++;
    }
    fclose(fp);
    return h;
}

void output(data x, char *filename){
    FILE *fp;
    int i;

    fp = fopen(filename, "w");
    for(i=0; i<x.num; i++){
//        if(x.data[i] < USHRT_MAX){
            fprintf(fp, "%d\n", (short)(x.data[i]));
//        }else{
//            fprintf(fp, "%u\n", (unsigned short)USHRT_MAX);
//        }
    }
    fclose(fp);
    return;
}

double inner_product(data d1, data d2){
    int i;
    double ans=0;
    for(i=0; i<d1.num; i++){
        ans += d1.data[i]*d2.data[i];
    }
    return ans;
}

double diviation_inner_product(data src, data target, int div){
    int i;
    data buf;
    buf.num = target.num;
    buf.data = (double *)malloc(sizeof(double)*target.num);
    for(i=0; i<target.num; i++){
        if(0 <= (i-div) && (i-div) < target.num){
            buf.data[i] = target.data[i-div];
        }else{
            buf.data[i] = 0;
        }
    }
    return inner_product(src, buf);
}

data reverse_data(data d){
    printf("%d\n", d.num);
    int i;
    data buf;
    buf.data = (double *)malloc(sizeof(double)*d.num);
    buf.num = d.num;

    for(i=0; i<d.num; i++){
        buf.data[i] = d.data[d.num-i-1];
    }
    return buf;
}

data filter(data h, data x){
    int i, num, cnt=0;
    data buf, ans;
    buf = reverse_data(x);
    ans.data = (double *)malloc(sizeof(double)*(x.num*2-1));
    ans.num = (x.num*2-1);
    for(i=(-h.num+1); i<h.num; i++){
        ans.data[cnt] = diviation_inner_product(h, buf, i);
        cnt++;
    }
    return ans;
}