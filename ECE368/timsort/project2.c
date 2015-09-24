#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define MIN_sublist_SIZE 8
#define STACK_SIZE 66
void display(int list[], int length)
{
    int i;
    printf(">");
    for (i = 0; i < length; i++)
    {
        printf(" %d", list[i]);
    }
    printf("\n");
}

typedef struct {
    int *index;
    int length;
} Sublist;

typedef struct {
    int *storage;
    Sublist sublists[STACK_SIZE];
    int stack_height;
    int *partitioned;
    int *list;
    int length;
} Sort_stack;

typedef Sort_stack *sort_stack;
void insertion_sort(int list[], int n);
void merge(int biggerlist[], int sublist1[], int l1, int sublist2[], int l2, int storage[]);
void swap(int list[], long int left, long int right);
void boost_sublist_length(sort_stack stack, Sublist *sublist);
int partition(sort_stack stack);
int should_collapse(sort_stack stack);
void merge_collapse(sort_stack stack);


void sort(int list[], int n)
{
    Sort_stack stack;
    stack.storage = malloc(sizeof(int) * n);
    stack.stack_height = 0;
    stack.partitioned = list;
    stack.list = list;
    stack.length = n;
    while(partition(&stack))
    {
        while(should_collapse(&stack))
        {
            merge_collapse(&stack);
        }
    }
    while(stack.stack_height > 1)
    {
        merge_collapse(&stack);
    }
    free(stack.storage);
}


void swap(int list[], long int left, long int right)
{
    int temp;
    temp = list[left];
    list[left] = list[right];
    list[right] = temp;
}

void reverse(int list[], int n)
{
    int cd = n-1;
    int i = 0;
    while(i<cd)
        {
            swap(list,i,cd);
            i++;
            cd--;
        }
}

void boost_sublist(sort_stack stack, Sublist *sublist)
{
    int length = stack->length - (sublist->index - stack->list);
    if(length > MIN_sublist_SIZE)
    {
        length = MIN_sublist_SIZE;
    }
    insertion_sort(sublist->index, length);
    sublist->length = length;
}

int partition(sort_stack stack){
    if(stack->partitioned >= stack->list + stack->length) return 0;
    int *start_index = stack->partitioned;
    int *next_start_index = start_index + 1;
    if(next_start_index < stack->list + stack->length)
    {
        if(*next_start_index < *start_index)
        {
            while(next_start_index < stack->list + stack->length && *next_start_index < *(next_start_index - 1))
            {
                next_start_index++;
            }
            reverse(start_index, next_start_index - start_index);
        } 
        else 
        {
            while(next_start_index < stack->list + stack->length && *next_start_index >= *(next_start_index - 1))
            {
                next_start_index++;
        
            }
        }
    }
    Sublist sublist_to_add;
    sublist_to_add.index = start_index;
    sublist_to_add.length = (next_start_index - start_index);
    if(sublist_to_add.length < MIN_sublist_SIZE)
    {
        boost_sublist(stack, &sublist_to_add);
    }
    stack->partitioned = start_index + sublist_to_add.length;
    stack->sublists[stack->stack_height] = sublist_to_add;
    stack->stack_height++;
    return 1;
}

int should_collapse(sort_stack stack)
{
    if (stack->stack_height <= 2)
    {
        return 0;
    }
    int h = stack->stack_height - 1;
    int head_length = stack->sublists[h].length;
    int next_length = stack->sublists[h-1].length;
    return 2 * head_length > next_length;
}

void merge_collapse(sort_stack stack)
{
    Sublist A = stack->sublists[stack->stack_height - 2];
    Sublist B = stack->sublists[stack->stack_height - 1];
    merge(A.index, A.index, A.length, B.index, B.length, stack->storage);
    stack->stack_height--;
    A.length += B.length;
    stack->sublists[stack->stack_height - 1] = A;
}

void merge(int biggerl[], int subl1[], int l1, int subl2[], int l2, int storage[])
{
    int *merge_to = storage;
    int i1, i2;
    i1 = i2 = 0;
    int *next_merge_element = merge_to;

    while(i1 < l1 && i2 < l2)
    {
        if(subl1[i1] <= subl2[i2])
        {
            *next_merge_element = subl1[i1];
            i1++;
        }
        else 
        {
            *next_merge_element = subl2[i2];
            i2++;
        }
        next_merge_element++;
    }
    if(i1 < l1 && i2 >= l2)
    {
        memcpy(next_merge_element, subl1 + i1, sizeof(int) * (l1 - i1));
    }
    else if (i1 >= l1 && i2 < l2)
    {
        memcpy(next_merge_element, subl2 + i2, sizeof(int) * (l2 - i2));
    }
    memcpy(biggerl, merge_to, sizeof(int) * (l1 + l2));
}


void insertion_sort(int list[], int n)
{   
    int i, j ;
    
    for (i = 1; i < n; i++) {
        j = i;
        while (j > 0 && list[j - 1] > list[j]) {
            
            swap(list,j-1,j);
            j--;
        }
    }
}
