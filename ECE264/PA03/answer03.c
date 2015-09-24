#include <stdlib.h>
#include "answer03.h"
#include <string.h>

char * strcat_ex(char * * dest, int * n, const char * src)
{
  if(*dest == NULL)
  {
    * n = 1 +  2 * strlen(src);
    char * buffer = malloc(sizeof(char)*(*n));
    *buffer = '\0';
    strcat(buffer,src);
    free(*dest);
    *dest = buffer;
  }
  else if(*n<(strlen(*dest)+strlen(src)+1))
  {
    * n = 1 + 2 * (strlen(*dest) + strlen(src));
    char * buffer = malloc(sizeof(char)*(*n));
    *buffer = '\0';
    strcat(buffer,*dest);
    strcat(buffer,src);
    free(*dest);
    *dest = buffer;
  }
  else
  {
    strcat(*dest,src); 
  }
  return *dest;

}
  
 
char * * explode(const char * str, const char * delims, int * arrLen)
{	
  int ind;
  int N = 0;
  int arrind = 0;
  int last = 0;
  
  for(ind = 0;ind < strlen(str); ind++)
  {
    if(strchr(delims,str[ind]) != NULL)
    {
      N++;
    }
  }
  *arrLen = N+1;
  char * * strArr = malloc((*arrLen) * sizeof(char *));
  if(N != 0)
  {
    while(arrind < N)
    {
      for(ind=last; ind < strlen(str); ind++)
      {
	if(strchr(delims,str[ind]) != NULL)
	{
	  strArr[arrind] = malloc(sizeof(char)*(ind-last)+1);
	  memcpy(strArr[arrind],str+last,ind-last);
	  strArr[arrind][ind-last]='\0';
	  last = ind+1;
	  arrind++;
	}
      }
    }
	  strArr[arrind] = malloc(sizeof(char)*(ind-last)+1);
	  memcpy(strArr[arrind],str+last,ind-last);
	  strArr[arrind][ind-last]='\0';
  }
  else
  {
    strArr[arrind] = malloc(sizeof(char)*(ind-last)+1);
    memcpy(strArr[arrind],str,strlen(str));
    strArr[arrind][ind-last]='\0';
  }
  return  strArr;
}


char * implode(char * * strArr, int len, const char * glue)
{ 
  int arrind;
  char * str = NULL;
  int arrLen = len;
  for(arrind = 0; arrind < arrLen-1; arrind++)
  {
    strcat_ex( &str,  &len, strArr[arrind]);
    strcat_ex( &str,  &len, glue);
  }
  strcat_ex( &str,  &len, strArr[arrind]);
  return str;
}


int strsort(const void *a, const void *b)
{
  const char **ia = (const char **)a;
  const char **ib = (const char **)b;
  return strcmp(*ia, *ib);
}
void sortStringArray(char * * arrString, int len)
{
  qsort(arrString,len,sizeof(char*),strsort);
}


int strcsort(const void *c1,const void *c2)
{
  const char com1 = *(char*)c1;
  const char com2 = *(char*)c2;
  return((int)com1 - (int)com2);
}

void sortStringCharacters(char * str)
{
  qsort(str,strlen(str),sizeof(char),strcsort);
  return;
}



void destroyStringArray(char * * strArr, int len)
{
  int ind;
  for(ind=0;ind<len;ind++)
  {
    free(strArr[ind]);
  }
  free(strArr);
  return ;
}
