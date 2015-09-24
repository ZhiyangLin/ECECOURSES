#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

void display(int array[], int length);
void sort(int list[], int n);

int main(int argc, char** argv)
{
  int i;
  int n = 1000000;
  int list[n];
  int count = 1000000;
  int max = 0;
  int c = 0;

  srand(time(NULL));
  for(i = 0;i < n; i++)
  {
    list[i] = rand()%1000000;
  }
  for(i=0;i<n;i++)
  {
    if(list[i]>=list[i+1])
    {
      c++;
      if(c>max)
      {
        max=c;
      }
    }
    else
    {
      c = 0;
    }
  }
  printf("max:%d\n",max);
  double t;
  t = clock();

  sort(list, n);

  t = clock() - t;

  t = t / CLOCKS_PER_SEC;

  printf("%f\n",t); 
  for(i=0;i<n-1;i++)
  {
    if(list[i+1]<list[i])
    {
      printf("false");
    }
  }
  //display(list,n);

  printf("sorted case\n");

  for(i=0;i < n; i++)
  {
    list[i]=i;
  }

  t = clock();

  sort(list, n);

  t = clock() - t;

  t = t / CLOCKS_PER_SEC;

  printf("%f\n",t);

   for(i=0;i<n-1;i++)
  {
    if(list[i+1]<list[i])
    {
      printf("false");
    }
  }

  printf("reversed case\n");

  for(i=0;i < n; i++)
  {
    list[i]=count--;
  }
 
  t = clock();

  sort(list, n);

  t = clock() - t;

  t = t / CLOCKS_PER_SEC;

  printf("%f\n",t);
   for(i=0;i<n-1;i++)
  {
    if(list[i+1]<list[i])
    {
      printf("false");
    }
  }

  return 0;
}