typedef struct Queue_s{
  struct listnode * next;
  float atime;
  float stime;
}Queue;

Queue* creatnode(float atime, float stime)
{
  Queue *queue = malloc(sizeof(List));
  queue -> atime = atime;
  queue -> stime = stime;
  queue -> next = NULL;
  return queue;
}

void insertmode2(Queue ** list0,Queue ** list1, float * atime, int* priority,float *stime,int num)
{
  for(i = 0; i< num, i++)
  {
    if(priority[i] == 0)
    {
      *list0 = createnode(atime[i],stime[i]);
      *list0 = *list0 -> next;
     }
    else
    {
      *list1 = createnode(atime[i],stime[i]);
      *list1 = *list1 -> next;
    }
  }
}
	
	
      
       
 