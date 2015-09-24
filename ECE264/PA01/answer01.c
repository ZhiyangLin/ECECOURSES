#include <stdlib.h>
#include "answer01.h"

int arraySum(int * array, int len)
{
  int sum = 0;
  int ind;
  for(ind = 0; ind < len; ind ++)
  {
    sum += array[ind];
  }
    return sum;
}

int arrayCountNegative(int * array, int len)
{
  int count = 0;
  int ind;
  for(ind = 0; ind < len; ind ++)
  {
    if(array[ind] < 0)
    {
      count ++;
    }
  }
    return count;
}

int arrayIsIncreasing(int * array, int len)
{   
  int ind;
  int isInc = 1;
    for(ind = 0; ind < len-1; ind ++)
    {
      if(array[ind] > array[ind + 1])
      {
	isInc = 0;
      }
    }
      return isInc;
}

int arrayIndexRFind(int needle, const int * haystack, int len)
{
  int ind;
  int x = -1;
  for(ind = 0; ind < len; ind ++)
  {
    if(haystack[ind] == needle)
    {
      x = ind;
    }
  }
    return x;
}

int arrayFindSmallest(int * array, int len)
{
  int ind;
  int min = array[0];
  int smallind = 0;
  if(len != 0)
  {
    for(ind = 0; ind < len; ind ++)
    {
      if(array[ind] < min)
      {
	min = array[ind];
	smallind = ind;
      }
    }
  }
    return smallind;
}
