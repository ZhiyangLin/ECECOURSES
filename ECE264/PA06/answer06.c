#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "answer06.h"
//Print function
void printPartition ( int * partition , int length )
  {
    int ind;
    printf("= ");
    for(ind = 0; ind < length; ind++)
    {
      if(ind != 0)
      {
	printf(" + ");
      }
      printf("%d", partition[ind]);
    }
    printf("\n");
}

//Print All
void partitionAllHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    partition[pos] = spending;
    partitionAllHelper(budget - spending, partition, pos+1);
  }
}


void partitionAll(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionAllHelper(value,partition,0);
  free(partition);
}

//Print Increase
void partitionInHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    partition[pos] = spending;
    if(pos>0)
    {
      if(partition[pos]>partition[pos-1])
      {
	partitionInHelper(budget - spending, partition, pos+1);
      }
    }
    else
    {
      partitionInHelper(budget - spending,partition, pos+1);
    }
  }
}

void partitionIncreasing(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionInHelper(value,partition,0);
  free(partition);
}

//Print Decrease
void partitionDeHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    partition[pos] = spending;
    if(pos>0)
    {
      if(partition[pos]<partition[pos-1])
      {
	partitionDeHelper(budget - spending, partition, pos+1);
      }
    }
    else
    {
      partitionDeHelper(budget - spending,partition, pos+1);
    }
  }
}


void partitionDecreasing(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionDeHelper(value,partition,0);
  free(partition);
}


//Print Odd
void partitionOdHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    if(spending%2 != 0)
    {
      partition[pos] = spending;
      partitionOdHelper(budget - spending, partition, pos+1);
    }
  }
}


void partitionOdd(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionOdHelper(value,partition,0);
  free(partition);
}

//Print Even
void partitionEvHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    if(spending%2 == 0)
    {
      partition[pos] = spending;
      partitionEvHelper(budget - spending, partition, pos+1);
    }
  }
}


void partitionEven(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionEvHelper(value,partition,0);
  free(partition);
}

//Print Even and Odd
void partitionOEHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 1; spending <= budget; spending++)
  {
    partition[pos] = spending;
    if(pos>0)
    { 
      if(partition[pos]%2 != partition[pos-1]%2)
      {
	partitionOEHelper(budget - spending, partition, pos+1);
      }
    }
    else
    {
      partitionOEHelper(budget - spending, partition, pos+1);
    }
  }
}


void partitionOddAndEven(int value)
{
  int *partition = malloc(sizeof(int) * value);
  partitionOEHelper(value,partition,0);
  free(partition);
}

//Print Prime
int CheckPrime(int value)
{
  int ind = 2;
  /*{
    for(ind = 2; ind< value; ind++)
    {
      ture = ture*(value%ind);
    }
  }
    return ture;*/
 
    for(ind = 2; ind< value; ind++)
    {
      if (value%ind ==0)
      {
	return 0;
      }
    }

    return 1;
}

void partitionPHelper(int budget, int *partition, int pos)
{
  if(budget == 0)
  {
    printPartition(partition,pos);
    return;
  }
  int spending;
  for (spending = 2; spending <= budget; spending++)
  {   
      if(CheckPrime(spending))
      {
	partition[pos] = spending;
	partitionPHelper(budget - spending, partition, pos+1);
      }
  }
}

void partitionPrime(int value)
{
  if (value == 1)
  {
    return;
  }
  int *partition = malloc(sizeof(int) * value);
  partitionPHelper(value,partition,0);
  free(partition);
}