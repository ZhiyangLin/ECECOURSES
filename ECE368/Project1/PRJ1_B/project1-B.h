/*
Header file for project 1 B
Do not modify this part of comments
Teammates : Zhiyang Lin(lin382@purdue.edu) Yiwen Gong(gong32@purdue.edu)
*/
#ifndef project1_H
#define project1_H

//Model1 queue
typedef struct task_s
{
	int priority;
	int type;
	float iatime;
	float artime;
    float nofstasks;
    float timelq;
    float * ststime;
    struct task_s * prev;
    struct task_s * next;
} Task;

typedef struct server_s
{	
	int flag;
	float busytime;
	float endtime;
} Server;

float calbal(Task * task,float avgst);

float calindbal(Task * queue, float avgst);

float calavgst(Task * queue);

Server createserver();

float *  getA(Task * queue);

Task * createdq(float endtime);

Task * mergeFEL(Task * fel, Task * task);

Task * createtask(float lamda, float u, int priority);

Task * insertmodel1(Task * head,  float lamda, float u, float artime , int priority);

Task * insertmodel2(Task * head, float artime, int priority, int nofstasks, float *stsime);

Task * createnode(float artime, int priority,int nofstasks , float * ststime);

Task * queueup(Task * fel, Server * servers);

float  caliatime(float lamda0);

float  calstime(float u);

void task_destroy(Task * task);

float * calsstime(int nofstasks,float u);

Task * findhead(Task * task);

float  calwaitT(Task * task, int priority);

Task * recordQ(Task * queue, Task * task);

void deltask(Task * task);

int  setA(float * arrt, int size);

float qLength(Task * queue);

Task * copyQ(Task * task);

float * copyA(float * ststime, int nofstasks);

void printlink (Task * head);

void printarry (float * array, int arrysize);

Task * dequeue(Task * currentQ, int remS);

void mergedq (Task * temp, Task * fel, float currentT);

Server * busyS(Server * server, Task* task, float currenT);

Server * freeS(Server * server, float currenT);

float calutil(Server * servers);

float qlen(Task * queue,float * arrt, int i);

#endif 