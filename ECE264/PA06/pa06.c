#include <stdio.h>
#include <stdlib.h>
#include "answer06.h"

int main(int argc, char * * argv)
{
  
  partitionAll(5);
  partitionIncreasing(7);
   partitionDecreasing(7);
    printf("partitionOdd(1)\n");
    partitionOdd(1);
    printf("partitionOdd(2)\n");
    partitionOdd(2);
    printf("partitionOdd(3)\n");
    partitionOdd(3);
    printf("partitionOdd(4)\n");
    partitionOdd(4);
    printf("partitionOdd(5)\n");
    partitionOdd(5);
    printf("partitionOdd(6)\n");
    partitionOdd(6);
    printf("partitionOdd(7)\n");
    partitionOdd(7);
    printf("partitionOdd(8)\n");
    partitionOdd(8);
    printf("partitionEven(1)\n");
    partitionEven(1);
    printf("partitionEven(2)\n");
    partitionEven(2);
    printf("partitionEven(3)\n");
    partitionEven(3);
    printf("partitionEven(4)\n");
    partitionEven(4);
    printf("partitionEven(5)\n");
    partitionEven(5);
    printf("partitionEven(6)\n");
    partitionEven(6);
    printf("partitionEven(7)\n");
    partitionEven(7);
    printf("partitionEven(8)\n");
    partitionEven(8);
    printf("partitionOddAndEven(1)\n");
    partitionOddAndEven(1);
    printf("partitionOddAndEven(2)\n");
    partitionOddAndEven(2);
    printf("partitionOddAndEven(3)\n");
    partitionOddAndEven(3);
    printf("partitionOddAndEven(4)\n");
    partitionOddAndEven(4);
    printf("partitionOddAndEven(5)\n");
    partitionOddAndEven(5);
    printf("partitionOddAndEven(6)\n");
    partitionOddAndEven(6);
    printf("partitionOddAndEven(7)\n");
    partitionOddAndEven(7);
    printf("partitionOddAndEven(8)\n");
    partitionOddAndEven(8);
    printf("partitionPrime(1)\n");
    partitionPrime(1);
    printf("partitionPrime(2)\n");
    partitionPrime(2);
    printf("partitionPrime(3)\n");
    partitionPrime(3);
    printf("partitionPrime(4)\n");
    partitionPrime(4);
    printf("partitionPrime(5)\n");
    partitionPrime(5);
    printf("partitionPrime(6)\n");
    partitionPrime(6);
    printf("partitionPrime(7)\n");
    partitionPrime(7);
    printf("partitionPrime(8)\n");
    partitionPrime(8);
    int ture; 
    ture = CheckPrime(6); 
    printf("%d\n",ture);
    ture = CheckPrime(2); 
    printf("%d\n",ture);
    ture = CheckPrime(3); 
    printf("%d\n",ture);
    ture = CheckPrime(4); 
    printf("%d\n",ture);
    ture = CheckPrime(5); 
    printf("%d\n",ture);
    
    
    
    
  return EXIT_SUCCESS;
}
