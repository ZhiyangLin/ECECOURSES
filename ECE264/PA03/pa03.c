#include <stdio.h>
#include <string.h>

#include "answer03.h"

int main(int argc, char * * argv)
{
    printf("Welcome to PA03.\n"
	   "\n");
    //test cases for function 1
    printf("Test Cases for Function 1\n\n");
    int n = 15;
    //case1
    char *f1str1;
    f1str1 = NULL; 
    strcat_ex(&f1str1, &n, "hehe");
    printf("case1\n%s\n",f1str1);
    free(f1str1);
    
    
    //case2
    char * f1str2 = malloc(sizeof(char) * 15);
    strcpy(f1str2,"wqnmlgbanidaye");
    strcat_ex(&f1str2, &n, "hehe");
    printf("case2\n%s\n",f1str2);
    free(f1str2);
  
    //case3
    char * f1str3 = malloc(sizeof(char)*15);
    strcpy(f1str3,"kengdie");
    strcat_ex(&f1str3, &n, "hehe");
    printf("case3\n%s\n",f1str3);
    free(f1str3);

    //test cases for function 2
    int f2ind;
    printf("Test Case for function 2\n\n");
    int f2arrLen = 0;
    const char *f2str1 ="what the hell";
    const char *f2str2 ="what the hell    ";
    const char *delims =" \f\n\r\t\v";
    
    printf("case1\n");
    char ** f2strArr1 = explode(f2str1,delims,&f2arrLen);
    for( f2ind=0 ; f2ind < f2arrLen;f2ind++)
    {
      printf("\"%s\"",f2strArr1[f2ind]);
    }
    destroyStringArray(f2strArr1, f2arrLen);
    
    printf("case2\n");
    char ** f2strArr2 = explode(f2str2,delims,&f2arrLen);
    for(f2ind=0 ;f2ind < f2arrLen;f2ind++)
    {
      printf("\"%s\"",f2strArr2[f2ind]);
    }
    destroyStringArray(f2strArr2, f2arrLen);
    
    //Test Cases for Function 3
    printf("Test Cases for Function3\n\n");
    int f3arrLen;
    //Case1
    printf("case1\n");
    char * * f3strArr1 = explode("100 224 147 80", " ", &f3arrLen);
    char * f3str1 = implode(f3strArr1, f3arrLen, ", ");
    printf("(%s)\n", f3str1); // (100, 224, 147, 80)
    destroyStringArray(f3strArr1, f3arrLen);
    free(f3str1);
    //Case2
    printf("case2\n");
    char * * f3strArr2 = explode("what the hell is this", " ",&f3arrLen);
    char * f3str2 = implode(f3strArr2, f3arrLen, "_");
    printf("(%s)\n", f3str2);
    destroyStringArray(f3strArr2, f3arrLen);
    free(f3str2);
    
    
    //Test Cases for Function 4
    printf("Test Cases for Function4\n\n");
    printf("case1\n");
    int f4arrLen;
    char * * f4strArr1 = explode("lady beatle brew", " ", &f4arrLen);
   
    sortStringArray(f4strArr1, f4arrLen);
    char * f4str1 = implode(f4strArr1, f4arrLen, " ");
    printf("%s\n",f4str1); // beatle brew lady
    destroyStringArray(f4strArr1, f4arrLen);
    free(f4str1);
    
    //Test Cases for Function 5
    char * f5str = strdup("How did it get so late so soon?");
    sortStringCharacters(f5str);
    printf("\"%s\"\n",f5str);
    free(f5str);
   return EXIT_SUCCESS;
}

