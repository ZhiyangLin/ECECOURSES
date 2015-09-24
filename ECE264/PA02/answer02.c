#include "answer02.h"

size_t my_strlen(const char * str)//Calculate the length of a string
{
  size_t len = 0;
  while (*str != '\0')
  {
    len++;
    str++;
  }
  return len;
}

int my_countchar(const char * str, char ch)//Count a ch in a string
{
  int count = 0;
  while(*str !='\0')
  {
    if(*str == ch)
    {
      count++;
    }
    str++;
  }
  return count;
}

char * my_strchr(const char * str, int ch)//
{
  int ind;
  int len;
  len = my_strlen(str);
  for(ind = 0; ind < len+1; ind++)
  {
    if(*str == ch)
    {
      return (char *) str;
    }
    str++;
  }
  return NULL;
}

char * my_strrchr(const char * str, int ch)
{
  int ind;
  int len;
  len = my_strlen(str);
  str += len;
  for(ind = 0;ind < len+1;ind++)
  {
    if(*str == ch)
    {
      return (char *)str;
    }
    str--;
  }
  return NULL;
}

char * my_strstr(const char * haystack, const char * needle)
{
  size_t lennee = my_strlen(needle);
  int same = 0;
  const char * nee_start= needle;
  if (*needle =='\0')
  {
    return(char*) haystack;
  }
     while(*haystack)
      {
	while((*needle == *haystack)&&(*needle))
	{
	  haystack++;
	  needle++;
	  same++;
	}
	if(same == lennee)
	  {
	    return (char *) (haystack -=lennee);
	  }
	else
	  {
	   needle = nee_start;
	   same = 0;
	   haystack++;
	  }
      }	
  return NULL;
}


char * my_strcpy(char * dest, const char * src)
{
  int ind = 0;
  int len;
  len = my_strlen(src);
  while(ind <= len)
  {
    *dest = *src;
    dest++;
    src++;
    ind++;
  }
  dest -=ind;
  return (char *) dest;
}

char * my_strcat(char * dest, const char * src)
{
  int ind;
  int lendest;
  int lensrc;
  lendest = my_strlen(dest);
  lensrc = my_strlen(src);
  for(ind = 0; ind < lensrc+1; ind++)
  {
    dest[ind + lendest] = src[ind];
  }
  return (char *) dest;
}
int my_isspace(int ch)
{
  if ((9 <= ch && ch <= 13)||ch == 32)
  {
    return 1;
  }
  return 0;
}

int my_atoi(const char * str)
{
  int minus = 1;
  int ind;
  int ret = 0;
  int len;
  len = my_strlen(str);
  for(ind = 0; ind < len; ind++)
  {
    if ((*str <= 9 || 13 <= *str) && *str != 32)
    {
      if(*str == '-')
      {
	minus = -1;
      }
      else if('0' <= *str && *str <= '9')
      {
	ret *= 10;
	ret += (*str - '0');
      }
    }
    str++;
  }
  return ret*minus;
}
