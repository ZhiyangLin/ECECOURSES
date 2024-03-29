/*
	Functions for project 1 A
	Do not modify this part of comments
	Teammates : Zhiyang Lin(lin382@purdue.edu) Yiwen Gong(gong32@purdue.edu)
*/
#include "project1-B.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#define MAX_LINE_LENGTH 10000000

//model1 functions
Task * createtask(float lamda, float u,int priority)
{
	Task * task = malloc(sizeof(Task));
	task -> type = 1;
	task -> priority = priority ;
	task -> iatime = caliatime(lamda);
	task -> artime = 0;
	task -> nofstasks = ceil(32*((float)rand()/(RAND_MAX)));
	task -> timelq = 0;// time the task leve queue
	task -> ststime = calsstime(task->nofstasks, u); 
	task -> next = NULL;
	task -> prev = NULL;
	return task;
}

Task * createnode(float artime, int priority,int nofstasks , float * ststime)
{
  	Task * task = malloc(sizeof(Task));
	task -> type = 1;
	task -> priority = priority ;
	task -> iatime = 0;
	task -> artime = artime;
	task -> nofstasks = nofstasks;
	task -> timelq = 0;// time the task leve queue
	task -> ststime = ststime; 
	task -> next = NULL;
	task -> prev = NULL;
	return task;
} 

Task * createdq(float endtime)
{
	Task * dq = malloc(sizeof(Task));
	dq -> type = 0;
	dq -> priority = 0;
	dq -> iatime = 0 ;
	dq -> artime = endtime;
	dq -> nofstasks = 0;
	dq -> timelq = 0;// time the task leve queue
	dq -> ststime = NULL;
	dq -> next = NULL;
	dq -> prev = NULL;
	return dq;
}

Server  createserver()//create an array of server with type Server
{
		Server servers;
		servers.busytime = 0;
		servers.flag = 1;
		servers.endtime = 0;
	return servers;
}

Task * insertmodel1(Task * head, float lamda, float u, float artime, int priority)// create the queue for task0 or task 1
{
	if(head == NULL)
	{	
		 
		head = createtask(lamda, u,priority);
		head -> artime = artime + head -> iatime;
		return head;
	}
	head -> next = insertmodel1( head-> next ,lamda, u , head -> artime, priority);
	head ->next -> prev = head;
	return head;
}

Task * insertmodel2(Task * head, float artime, int priority, int nofstasks, float *ststime)
{	
   	if(head == NULL)
	{	
		head = createnode(artime, priority, nofstasks, ststime);
		return head;
	}
	head -> next = insertmodel2(head-> next , artime, priority, nofstasks , ststime);
	head -> next -> prev = head;
	return head;
}

Task * mergeFEL(Task * fel, Task * task)
{
	Task * tail= NULL;
	Task * head = NULL;
	while(fel != NULL && task != NULL)
	{
		if(fel->artime < task -> artime)
		{
			if(head != NULL)
			{
				tail->next=fel;
				fel->prev = tail;
			}
			else
			{
				head = fel;
			}
			fel->prev = tail;
			tail = fel;
			fel = fel->next;
		}
		else
		{
			if(head != NULL)
			{
				tail->next=task;
				task->prev = tail;
			}
			else
			{
				head = task;
			}
			tail = task;
			task = task->next;
		}
	}
	if(fel != NULL)
	{
		tail->next = fel;
		fel->prev = tail;
	}
	if(task != NULL)
	{
		tail->next = task;
		task->prev = tail;
	}
	return head;
}

Task * copyQ(Task * task)// duplicate the current task  
{
	Task * currentQ = malloc(sizeof(Task));
	currentQ -> priority = task -> priority;
	currentQ -> iatime = task -> iatime;
	currentQ -> artime = task -> artime;
	currentQ -> nofstasks = task -> nofstasks;
	currentQ -> timelq = task -> timelq;
	currentQ -> ststime = copyA(task->ststime,task->nofstasks);
	currentQ -> next = NULL;
	currentQ -> prev = NULL;
	return currentQ;
}

float * copyA(float * ststime, int nofstasks) // copy an arrary
{
	int i;
	float * qststime=malloc(sizeof(float)*nofstasks);
	for(i = 0;i< nofstasks; i++){
			qststime[i]=ststime[i];
		}
	return qststime;
}

Task * queueup(Task * fel,Server * servers) // keep track of the task that gets in to the server and form one final queue
{
	float currentT = 0;
	int remS = 64;
	Task * newQ =  NULL;
	Task * currentQ0 = createdq(-1);
	Task * currentQ1 = createdq(-1);
	Task * queue = NULL;
	Task * temp = NULL;

	while(fel != NULL)
	{
		currentT = fel->artime;
		if(fel -> type == 0)//this is a dq event
		{
			remS++;
			servers = freeS(servers, currentT);
			while(currentQ0 != NULL)
			{	
				temp = dequeue(currentQ0,remS);
				if (temp != NULL)
				{
					remS -= temp->nofstasks;
					servers = busyS(servers,temp,currentT); //set servers to work
					temp->timelq = currentT;
					queue = recordQ(queue,temp);
					mergedq(temp, fel ,currentT);
					deltask(temp);
				}
				else
				{
					break;
				}
			}
			while(currentQ1 != NULL)
			{	
				temp = dequeue(currentQ1,remS);
				if (temp != NULL)
				{
					remS -= temp->nofstasks;	
					servers = busyS(servers,temp,currentT);
					temp->timelq = currentT;
					queue = recordQ(queue,temp);
					mergedq(temp,fel,currentT);
					deltask(temp);
				}
				else
				{
					break;
				}
			}
		}
		else if (fel->type == 1)//if the head of the fel is a task
		{
			
			if(fel->nofstasks <= remS)
			{
				remS -= fel->nofstasks;
				servers = busyS(servers,fel,currentT);
				fel->timelq = currentT;
				queue = recordQ(queue,fel);
				mergedq(fel,fel,currentT);
			}			
			else
			{
				newQ = copyQ(fel);
				if(fel->priority ==	0)
				{
					currentQ0 = mergeFEL(currentQ0,newQ);//mergeFEL will return the head of currentQ
				}
				else
				{
					currentQ1 = mergeFEL(currentQ1,newQ);
				}
			}
		}
		temp = fel->next;
		deltask(fel);
		fel = temp;
	}
	task_destroy(findhead(currentQ0));
	task_destroy(findhead(currentQ1));
	return queue;
}


float  calwaitT(Task * queue, int priority)
{
	float waitT = 0;
	while(queue!=NULL)
	{
		if(queue->priority == priority)
		{
			waitT += (queue->timelq-queue->artime);
		}
		queue = queue->next;
	}
	return waitT;
}

//Do not change
Task * findhead(Task * task)
{
	if(task!=NULL){
		while(task->prev!=NULL){
			task=task->prev;
		}
	}
	return task;
}

//Do not change 
void deltask(Task * task) // delete a node from a doubly linked list
{	
	Task * temp;
	if(task->prev==NULL){//delete a head
		temp = task->next;
		free(task->ststime);
		free(task);
		task = temp;
		if(task != NULL){//check whether or not this is the only node
			task->prev =NULL;
		}
	}
	else if(task->next==NULL){//delete a tail
		temp = task->prev;
		free(task->ststime);
		free(task);
		task = temp;
		if(task != NULL){//check whether or not this is the only node
			task->next =NULL;
		}
	}
	else{//delete a nod from mid
		temp = task->next;
		temp->prev = task->prev;
		temp->prev->next = temp;
		free(task->ststime);
		free(task);
		task = temp; 
	}
}

Task * recordQ(Task * queue, Task * task)// keep track the final queue in order to calculate other data
{	
	if(queue==NULL){
		queue = copyQ(task);
		queue->next = NULL;
	}
	else{
		queue->next = copyQ(task);
		queue->next->prev = queue;
		queue = queue->next;
		queue->next = NULL;
	}
	return queue;
}

float qLength(Task * queue)
{
	float ql = 0; 
	while(queue != NULL)
	{
		ql++;
		queue= queue->next;
	}
	return ql;
}

int setA(float * arrt, int size)
{
	int i,j,k;
	for (i = 0; i < size; i++) 
	{
      for (j = i + 1; j < size;j++) 
      {
         if (arrt[j] == arrt[i]) 
         {
            for (k = j; k < size; k++) 
            {
               arrt[k] = arrt[k + 1];
            }
            size--;
         } 
      }
   	}
   	return size;
}


void printlink (Task * head)
{
	while (head != NULL)
  {
    printf("priority %d \n", head->priority);
    //printf("iatime %f \n", head->iatime);
    printf("artime %f \n",head->artime);
    printf("nofstask %f \n", head->nofstasks);
    printf("timelq %f \n\n", head->timelq);
    printarry(head->ststime,head->nofstasks);
    head = head->next;
  }
}

void printarry(float * array, int arrysize)
{	
	int i;
	for(i=0;i < arrysize; i++){
		printf("subtask%d service time %f\n", i+1, array[i]);
	}
	printf("\n");
}

float caliatime(float lamda)
{	
	float x = ((float) rand()/(RAND_MAX));
	float iatime = ( -1 / lamda ) * log( 1 - x );
	return ceil(iatime);
}

float  calstime(float u)
{
	float x = ((float) rand()/(RAND_MAX));
	float stime = ( -1 / u ) * log( 1 -x );
	return stime;
}

void task_destroy(Task * task)
{
	Task * p = NULL;
	while(task != NULL)
	{
		p = task -> next;
		free(task->ststime);
		free(task);
		task = p;
	}
}

float calindbal(Task * queue)
{
	int i;
	float maxs = queue->ststime[0];
	float mins = queue->ststime[0];
	float ttst = 0;
	float balance = 0;
	for(i = 0; i < queue->nofstasks; i++)
	{
		if(maxs < queue->ststime[i])
		{
			maxs = queue->ststime[i];
		}
		if(mins>queue->ststime[i])
		{
			mins = queue->ststime[i];
		}
		ttst += queue->ststime[i];
	}
	balance = (maxs-mins)/(ttst/queue->nofstasks);

	return balance;
}
float calbal(Task * task)
{
	float balance = 0;
	while(task != NULL)
	{
		balance += calindbal(task);
		task = task->next;
	}
	return balance;
}

float * calsstime(int nofstasks,float u)
{
	int i;
	float * ststime=malloc(sizeof(float)*nofstasks);
	for(i = 0;i< nofstasks; i++){
			ststime[i]=calstime(u);
		}
	return ststime;
}

Task * dequeue(Task * currentQ, int remS)
{
	Task * temp = NULL;
	currentQ = currentQ->next;
	while(currentQ != NULL)
	{
		if(currentQ->nofstasks <= remS)//if find a priority 0 task which can be fitted in
		{
				temp = currentQ;
				break;
		}
		currentQ = currentQ-> next;
	}
	return temp;
}

void mergedq (Task * temp, Task * fel,float currentT)
{ 	
	int i;
	float endtime = 0;
	Task * dq = NULL;
	for(i=0; i< temp->nofstasks; i++)
	{	
		endtime = currentT + temp->ststime[i];
		dq = createdq(endtime);
		fel = mergeFEL(fel,dq);
	}
}

Server * busyS(Server * servers, Task * task , float currentT)// set servers to work
{
	int i = 0;
	int j = 0;
	int nofstasks = 0;
	nofstasks=task->nofstasks;
	while (nofstasks > 0){
		if(servers[i].flag==1)
		{
			servers[i].busytime += task->ststime[j];
			servers[i].flag = 0;
			servers[i].endtime=currentT + task->ststime[j];
			nofstasks -= 1;
			j++;
		}
		i++;
	}
	return servers;
}

Server * freeS(Server * servers, float currentT) // set servers free when it's done
{	
	int i;
	for(i=0; i<64; i++){
		if(servers[i].endtime == currentT && servers[i].flag == 0)
		{
			servers[i].flag = 1;
			break;
		}
	}
	return servers;
}

float calutil(Server * servers)
{
	int i;
	float utl = 0;
	for(i = 0; i< 64; i++)
	{
		if(servers[i].endtime != 0)
		{
			utl += (servers[i].busytime/servers[i].endtime);
		}
	} 
	utl = utl / 64;
	return utl;
}

float *  getA(Task * queue)
{
	int i = 0;
	float * arrt = malloc(sizeof(int)*MAX_LINE_LENGTH);
	while(queue!= NULL)
	{
		arrt[i] = queue->artime;
		i++;
		queue = queue->next;
	}
	return arrt;
}

float qlen(Task * queue,float * arrt, int i)
{
	float q = 0;
	while(queue != NULL)
	{
		if(queue->artime <= arrt[i] && queue->timelq >arrt[i])
		{
			q++;
		}
		queue = queue->next;
	}
	return q;
}