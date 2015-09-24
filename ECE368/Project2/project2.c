#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void sort(int list[], int n);

void swap(int list[], long int left, long int right);

void quicksort(int list[], long int left, long int right);


/* Declarations */
void display(int array[], int length)
{
	int i;
	printf(">");
	for (i = 0; i < length; i++)
	{
		printf(" %d", array[i]);
	}
	printf("\n");
}

void swap(int list[], long int left, long int right)
{
	int temp;
	temp = list[left];
	list[left] = list[right];
	list[right] = temp;
}

int partition(int list[], long int left, long int right, long int pivot_Index) 
{
	int pivot;
    long int i, j;
	pivot = list[pivot_Index];
	swap(list,pivot_Index, right);
	i = left;
	for(j = left; j < right; j++) 
	{
		if(list[j] <= pivot) 
		{
			swap(list,i++,j);
		}
	}
	swap(list, right ,i);
	return i;
}

void quicksort(int list[], long int start, long int end) {
	long int p, pivot;
	if(start < end) 
	{
		pivot = (end+start)/2;
		p = partition(list, start, end, pivot);
		quicksort(list, start, p-1);
		quicksort(list, p+1, end);
	}
}


void sort(int list[], int n)
{
	int i = 0;
	int cd = n-1;
	printf("sort");
	while(list[i]<=list[i+1])
	{
		i++;
		if(i == n-1)
		{
			return;
		}
	}
	i = 0;
	while(list[i]>=list[i+1])
	{
		i++;
		if(i == n-1)
		{
			i = 0;
			while(i<cd)
			{
				swap(list,i,cd);
				i++;
				cd--;
			}
			return; 
		}
	}
	quicksort(list,0,n-1);
}



