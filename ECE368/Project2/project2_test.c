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

  srand(time(NULL));
  for(i = 0;i < n; i++)
    {
      list[i] = rand()%1000000;
    }

  double t;
  t = clock();

  sort(list, n);

  t = clock() - t;

  t = t / CLOCKS_PER_SEC;

  printf("%f\n",t);

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

  printf("reversed case\n");

  for(i=0;i < n; i++)
  {
    list[i]=count--;
  }
  //display(list,n);
  t = clock();

  sort(list, n);

  t = clock() - t;

  t = t / CLOCKS_PER_SEC;

  printf("%f\n",t);


  //display(list,n);
  return 0;
}