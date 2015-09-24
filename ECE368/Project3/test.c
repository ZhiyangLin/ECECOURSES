//test
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#define IN 10000
#include <string.h>
int main(int argc, char * argv[]){
	char * buffer = malloc(20*sizeof(char));
	float x = 185.368;
	sprintf(buffer,"%.2f",x);
	x = atoi(buffer);
	printf("%f",x);
	return 0;
}
