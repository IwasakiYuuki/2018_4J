#include <stdio.h>
#include <stdlib.h>

#define RIFF_OFFSET 0
#define FMT_OFFSET 12
#define DATA_HEADER_OFFSET 36
#define DATA_OFFSET 44
#define outerr() fprintf(stderr, "[ERROR]:Error has occured in func <%s> at line %d.\n", __func__, __LINE__)

typedef struct{
	char id[4];
	unsigned int size;
	char type[4];
}riff_chunk;

typedef struct{
	char id[4];
	unsigned int size;
	unsigned short format_id;
	unsigned short chanel;
	unsigned int fs;
	unsigned int byte_sec;
	unsigned short byte_samp;
	unsigned short bit;
}fmt_chunk;

typedef struct{
	char id[4];
	unsigned int size;
	unsigned short *data;
}data_chunk;

//各ヘッダ情報
riff_chunk *riff;
fmt_chunk *fmt;
data_chunk *data;

//各ヘッダ情報のテンプレート
riff_chunk riff_temp = {"RIFF", 0, "WAVE"};
fmt_chunk fmt_temp = {"fmt\x20", 16, 1, 1, 11025, 22050, 2, 16};
data_chunk data_temp = {"data", 0, NULL};

void get_header(riff_chunk *riff, fmt_chunk *fmt, FILE *fp);
void get_riff(riff_chunk *riff, FILE *fp);
void get_fmt(fmt_chunk *fmt, FILE *fp);
void get_data_wave(data_chunk *data, FILE *fp);
void get_from_text(riff_chunk *riff, data_chunk *data, FILE *fp);
void get_data_text(data_chunk *data, FILE *fp);
void set_riff_size(riff_chunk *riff, int size);
void append_null(char *in, char *out, int n);
void output_text(data_chunk *data, char *filename);
void output_wave(riff_chunk *riff, fmt_chunk *fmt, data_chunk *data, char *filename);
void test_print(riff_chunk *riff, fmt_chunk *fmt, data_chunk *data);

int main(){
	FILE *infp;
	char input_filename[256], output_filename[256];
	int mode;

	riff = (riff_chunk *)malloc(sizeof(riff_chunk));
	fmt = (fmt_chunk *)malloc(sizeof(fmt_chunk));
	data = (data_chunk *)malloc(sizeof(data_chunk));

	puts("[usage]");
	puts("1: WAVE -> TEXT");
	puts("2: TEXT -> WAVE");
	do{
		printf("pls select mode >>");
		scanf("%d", &mode);
	}while(mode != 1 && mode != 2);
	printf("pls input read-file name >>");
	scanf("%s", input_filename);
	printf("pls input write-file name >>");
	scanf("%s", output_filename);

	switch(mode){
	case 1:
		infp = fopen(input_filename, "rb");
		if(infp == NULL){
			outerr();
			exit(1);
		}
		get_data_wave(data, infp);
		printf("%d\n", data->size);
		output_text(data, output_filename);
		break;

	case 2:
		infp = fopen(input_filename, "r");
		if(infp == NULL){
			outerr();
			exit(1);
		}
		get_from_text(&riff_temp, &data_temp, infp);
		output_wave(&riff_temp, &fmt_temp, &data_temp, output_filename);
		break;
	}

	test_print(&riff_temp, &fmt_temp, &data_temp);

	fclose(infp);
	return 0;
}

void get_header(riff_chunk *riff, fmt_chunk *fmt, FILE *fp){
	get_riff(riff, fp);
	get_fmt(fmt, fp);
	return;
}

void get_riff(riff_chunk *riff, FILE *fp){
	fseek(fp, RIFF_OFFSET, SEEK_SET);
	fread((void *)riff->id, sizeof(char), 4, fp);
	fread((void *)&riff->size, sizeof(char), 4, fp);
	fread((void *)riff->type, sizeof(char), 4, fp);
	return;
}

void get_fmt(fmt_chunk *fmt, FILE *fp){
	fseek(fp, FMT_OFFSET, SEEK_SET);
	fread((void *)fmt->id, sizeof(char), 4, fp);
	fread((void *)&fmt->size, sizeof(char), 4, fp);
	fread((void *)&fmt->format_id, sizeof(char), 2, fp);
	fread((void *)&fmt->chanel, sizeof(char), 2, fp);
	fread((void *)&fmt->fs, sizeof(char), 4, fp);
	fread((void *)&fmt->byte_sec, sizeof(char), 4, fp);
	fread((void *)&fmt->byte_samp, sizeof(char), 2, fp);
	fread((void *)&fmt->bit, sizeof(char), 2, fp);
	return;
}

void get_data_wave(data_chunk *data, FILE *fp){
	fseek(fp, DATA_HEADER_OFFSET, SEEK_SET);
	fread((void *)data->id, sizeof(char), 4, fp);
	fread((void *)&data->size, sizeof(char), 4, fp);
	data->data = (void *)malloc(data->size);
	fread((void *)data->data, sizeof(char), data->size, fp);
	return;
}

void get_from_text(riff_chunk *riff, data_chunk *data, FILE *fp){
	get_data_text(data, fp);
	set_riff_size(riff, data->size + DATA_OFFSET - 8);
	return;
}

void get_data_text(data_chunk *data, FILE *fp){
	int i = 0, size = 0;
	short buf;
	fseek(fp ,0, SEEK_SET);
	while(fscanf(fp, "%hd", &buf) != EOF){
		size++;
	}
	data->data = (void *)malloc(size * sizeof(unsigned short));
	printf("%d\n",size);
	data->size = size * sizeof(unsigned short);
	fseek(fp ,0, SEEK_SET);
	while(fscanf(fp, "%hd", &data->data[i]) != EOF){
		i++;
	}
	return;
}

void set_riff_size(riff_chunk *riff, int size){
	riff->size = size;
	return;
}

void append_null(char *in, char *out, int n){
	int i = 0;
	for(i=0; i<n; i++){
		out[i] = in[i];
	}
	out[i] = '\0';
	return;
}

void output_text(data_chunk *data, char *filename){
	FILE *fp;
	int i = 0;
	int n = data->size/sizeof(data->data[0]);
	fp = fopen(filename, "w");
	if(fp == NULL){
		outerr();
		exit(1);
	}
	for(i=0; i<n; i++){
		fprintf(fp, "%d\n", data->data[i]);
	}
	fclose(fp);
	return;
}

void output_wave(riff_chunk *riff, fmt_chunk *fmt, data_chunk *data, char *filename){
	FILE *fp;
	fp = fopen(filename, "wb");
	if(fp == NULL){
		outerr();
		exit(1);
	}
	fseek(fp, 0, SEEK_SET);
	//ヘッダ情報の書き込み
	fwrite(riff->id, sizeof(char), 4, fp);
	fwrite(&riff->size, sizeof(char), 4, fp);
	fwrite(riff->type, sizeof(char), 4, fp);
	fwrite(fmt->id, sizeof(char), 4, fp);
	fwrite(&fmt->size, sizeof(char), 4, fp);
	fwrite(&fmt->format_id, sizeof(char), 2, fp);
	fwrite(&fmt->chanel, sizeof(char), 2, fp);
	fwrite(&fmt->fs, sizeof(char), 4, fp);
	fwrite(&fmt->byte_sec, sizeof(char), 4, fp);
	fwrite(&fmt->byte_samp, sizeof(char), 2, fp);
	fwrite(&fmt->bit, sizeof(char), 2, fp);
	fwrite(data->id, sizeof(char), 4, fp);
	fwrite(&data->size, sizeof(char), 4, fp);
	//データの書き込み
	if(fwrite(data->data, 2, data->size/2, fp) < data->size/sizeof(unsigned short)){
		outerr();
		exit(1);
	}
	fclose(fp);
	return;
}

void test_print(riff_chunk *riff, fmt_chunk *fmt, data_chunk *data){

	int i = 0;
	char *riff_id, *riff_type, *fmt_id, *data_id;
	riff_id = (char *)malloc(sizeof(riff->id)+1);
	riff_type = (char *)malloc(sizeof(riff->type)+1);
	fmt_id = (char *)malloc(sizeof(fmt->id)+1);
	data_id = (char *)malloc(sizeof(data->id)+1);

	//nullを追加
	append_null(riff->id, riff_id, sizeof(riff->id));
	append_null(riff->type, riff_type, sizeof(riff->type));
	append_null(fmt->id, fmt_id, sizeof(fmt->id));
	append_null(data->id, data_id, sizeof(data->id));

	puts("===================================");
	puts(" RIFF CHUNK");
	puts("===================================");
	printf("\t\t%s\t[%lu byte]\rID :\n", riff_id, sizeof(riff->id));
	printf("\t\t%d\t[%lu byte]\rSIZE :\n", riff->size, sizeof(riff->size));
	printf("\t\t%s\t[%lu byte]\rTYPE :\n", riff_type, sizeof(riff->type));
	puts("-----------------------------------");
	puts("");
	puts("===================================");
	puts(" FMT CHUNK");
	puts("===================================");
	printf("\t\t%s\t[%lu byte]\rID :\n", fmt_id, sizeof(fmt->id));
	printf("\t\t%d\t[%lu byte]\rSIZE :\n", fmt->size, sizeof(fmt->size));
	printf("\t\t%d\t[%lu byte]\rFORMAT_ID :\n", fmt->format_id, sizeof(fmt->format_id));
	printf("\t\t%d\t[%lu byte]\rCHANEL :\n", fmt->chanel, sizeof(fmt->chanel));
	printf("\t\t%d\t[%lu byte]\rFS :\n", fmt->fs, sizeof(fmt->fs));
	printf("\t\t%d\t[%lu byte]\rBYTE_SEC :\n", fmt->byte_sec, sizeof(fmt->byte_sec));
	printf("\t\t%d\t[%lu byte]\rBYTE_SAMP :\n", fmt->byte_samp, sizeof(fmt->byte_samp));
	printf("\t\t%d\t[%lu byte]\rBIT :\n", fmt->bit, sizeof(fmt->bit));
	puts("-----------------------------------");
	puts("");
	puts("===================================");
	puts(" DATA CHUNK");
	puts("===================================");
	printf("\t\t%s\t[%lu byte]\rID :\n", data_id, sizeof(data->id));
	printf("\t\t%d\t[%lu byte]\rSIZE :\n", data->size, sizeof(data->size));
	puts("-----------------------------------");
	return;
}
