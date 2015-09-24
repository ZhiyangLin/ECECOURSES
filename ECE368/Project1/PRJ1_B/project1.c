/*
  main funciton of project1 -B

  Zhiyang Lin(lin382@purdue.edu)  
  Yiwen Gong(gong32@purdue.edu)
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#define MAX_LINE_LENGTH 10000000
#include "project1-B.h" 
#include <time.h>


int main(int argc, char * argv[])
{
  float lamda_0 = 0.5;
  float lamda_1 = 0.2;
  float u = 1;
  int num = 5;
  srand(time(NULL));
  float artime;
  Task * fel = NULL;
  Task * queue = NULL;
  Task * task0 = NULL;
  Task * task1 = NULL;
  int i;
  int j;
  float avgwaitT0 = 0;
  float avgwaitT1 = 0;
  float utilization = 0;
  float * arrt;
  float avql = 0;
  float qL = 0;
  int size = 0;
  float avgst;
  float balance;
  Server servers[64];
  for (i=0; i<64; i++)
  {
    servers[i] = createserver();
  }
 
  if(argc == 2)
  {
    float * atime = malloc(sizeof(float)*MAX_LINE_LENGTH);
    int * priority = malloc(sizeof(int)*MAX_LINE_LENGTH);
    float * nofstasks = malloc(sizeof(float)*MAX_LINE_LENGTH);
    float ** ststime =malloc(sizeof(float*)*MAX_LINE_LENGTH);
    FILE * fp;
    char* filename;
    filename = argv[1];
    fp = fopen(filename, "r");
    i=0;
    while(!feof(fp))//not reaching end of file
    {
        fscanf(fp,"%f %d %f",&atime[i],&priority[i],&nofstasks[i]);
        ststime[i] = malloc(sizeof(float)*nofstasks[i]);
        for(j = 0; j < nofstasks[i]; j++)
        {
           fscanf(fp,"%f",&ststime[i][j]);
        }
        i++;
    }
      num = i;
      num = num/2;
      for(i=0; i<num*2; i++)
      {
        if(priority[i]==0)
        {
          task0 = insertmodel2(task0, atime[i], priority[i], nofstasks[i], ststime[i]);
        }
        else
        {
          task1 = insertmodel2(task1, atime[i], priority[i], nofstasks[i], ststime[i]);
        }
      }
      fel = mergeFEL(task1,task0);
      //printf("Final Queue da la !!!!!\n");
      queue = queueup(fel,servers);
      queue = findhead(queue);
      arrt = getA(queue);
      size = setA(arrt,2*num);
      for(i = 0; i < size; i ++)
      {
        qL += qlen(queue,arrt,i);
      }
      avql = qL/(2*num);
      avgwaitT0 = calwaitT(queue,0)/num;
      avgwaitT1 = calwaitT(queue,1)/num;
      utilization = calutil(servers);
      avgst = calavgst(queue);
      balance = calbal(queue,avgst)/num/2;

      free(arrt);
      task_destroy(queue);
      free(atime);
      free(priority);
      free(nofstasks);
      free(ststime);
      fclose(fp);
      FILE* fout = fopen("proj1-b_output","w");
      fprintf(fout,"%f\n%f\n%f\n%f\n%f\n",avgwaitT0, avgwaitT1, avql, utilization,balance);
  }
  else if(argc == 5)
  {
    lamda_0 = atof(argv[1]);
    lamda_1 = atof(argv[2]);
    u = atof(argv[3]);
    num = atoi(argv[4]);
    task0 = createtask(lamda_0, u,0);
    artime = 0;
    task0 ->iatime = 0;
    task0 ->artime = 0;
    for (i=0 ; i<num-1 ; i++)
    {
      task0 = insertmodel1(task0, lamda_0, u, artime,0);
    }
    task1 = createtask(lamda_1, u, 1);
    artime = 0;
    task1 ->iatime = 0;
    task1 ->artime = 0;
    for (i=0 ; i<num-1 ; i++)
    {
      task1 = insertmodel1(task1, lamda_1, u, artime,1);
    }
    fel = mergeFEL(task1,task0);
    // printf("Final Queue da la !!!!!\n");
    queue = queueup(fel,servers);
    queue = findhead(queue);
    avgst = calavgst(queue);
    balance = calbal(queue,avgst);
    arrt = getA(queue);
    size = setA(arrt,2*num);
    for(i = 0; i < size; i ++)
    {
       qL += qlen(queue,arrt,i);
    }
    avql = qL/(2*num);
    // printf("avql= %f\n", avql);
    free(arrt);
    avgwaitT0 = calwaitT(queue,0)/num;
    avgwaitT1 = calwaitT(queue,1)/num;
    utilization = calutil(servers);
    task_destroy(queue);

    FILE* fout = fopen("proj1-b_output","w");
  fprintf(fout,"%f\n%f\n%f\n%f",avgwaitT0, avgwaitT1, avql, utilization);
  }
  else  
  {
    printf("Wrong inputs"); 
  }
 
  return 0;
}
