#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#define IN 10000
#include <string.h>

/*******************************************************************************************
Headers for project 3
Do not modify this part of comments
*******************************************************************************************/

/*******************************************************************************************
            Structs
*******************************************************************************************/
typedef struct Profile
{
    int Id;
    int Age;
    int Gender;
    int M_Status;
    int Race;
    int Bir_Place;
    int Language;
    int Occupation;
    int Income;
} Prof;

/*******************************************************************************************
        Function Headers
*******************************************************************************************/
Prof createp(int Id, int Age, int Gender, int M_Status, int Race, int Bir_Place, int Language, int Occupation, int Income);
void printp(Prof * prof, int NoU);

/*******************************************************************************************
    Functions for project 3
    Do not modify this part of comments
*******************************************************************************************/

Prof createp(int Id, int Age, int Gender, int M_Status, int Race, int Bir_Place, int Language, int Occupation, int Income)
{
    Prof prof;
    prof.Id = Id;
    prof.Age = Age ;
    prof.Gender = Gender;
    prof.M_Status = M_Status;
    prof.Race = Race;
    prof.Bir_Place = Bir_Place;
    prof.Language = Language;
    prof.Occupation = Occupation;
    prof.Income = Income;
    return prof;
}

float calULab(Prof prof1, Prof prof2)
{
    float ULab = 0;
    float a = pow((prof1.Age - prof2.Age),2);
    float g = pow((prof1.Gender - prof2.Gender),2);
    float m = pow((prof1.M_Status - prof2.M_Status),2);
    float r = pow((prof1.Race - prof2.Race),2);
    float b = pow((prof1.Bir_Place - prof2.Bir_Place),2);
    float l = pow((prof1.Language - prof2.Language),2);
    float o = pow((prof1.Occupation - prof2.Occupation),2);
    float in = pow((prof1.Income - prof2.Income),2);
    float sum = a + g + m + r + b + l + o + in;
    ULab = sqrt(sum);
    return ULab;
}
void printp(Prof * prof, int NoU)
{
    int i = 0;
    for(i = 0; i < NoU; i++)
    {
        printf("Id=%d, Age=%d, Gender=%d, M_Status=%d, Race=%d, Bir_Place=%d, Language=%d, Occupation=%d, Income=%d ",prof[i].Id, prof[i].Age,prof[i].Gender,prof[i].M_Status,prof[i].Race,prof[i].Bir_Place,prof[i].Language,prof[i].Occupation,prof[i].Income);
        printf("\n");
    }
}

void printm(float * * matrix1,int NoU)
{
    int i = 1;
    int j = 1;
    for (i = 1; i <= NoU; i++)
    {
        for(j = 1; j <= NoU; j++)
        {
            printf("matrix[%d][%d]=%f\n", i,j,matrix1[i][j]);
        }
        printf("\n");
    }
}

void matrixnorm( float ** matrix1, float ** matrix2, float ** graph1, float ** graph2, int NoU, float Lmax, float delta1, float delta2)
{
    int i = 1;
    int j = 1;
    int temp;
    for(i = 1; i <= NoU; i++)
    {
        for(j = 1; j <= NoU; j++)
        {
            if(i == j)
            {
                matrix1[i][j] = IN;
                matrix2[i][j] = IN;
            }
            else
            {
                temp = (1 - matrix1[i][j]/Lmax)*100;
                matrix1[i][j] = (float)temp/100;
                matrix2[i][j] = matrix1[i][j];
                if (matrix1[i][j] <= delta1)
                {
                    matrix1[i][j] = IN;
                    graph1[i][j] = IN;
                }
                else
                {
                    graph1[i][j] = 1;
                }
                if (matrix2[i][j] <= delta2)
                {
                    matrix2[i][j] = IN;
                    graph2[i][j] = IN;
                }
                else
                {
                    graph2[i][j] = 1;
                }
            }
        }
    }
}
float dijsktra(float ** matrix,int source,int target, int NoU)
{
    int selected[NoU+1];
    int i, m, start;
    float dist[NoU+1], min, d;

    for(i = 1; i <= NoU; i++)
    {
        dist[i] = 2;
        //prev[i] = -1;
        selected[i] = 0;
    }
    start = source;
    selected[start] = 1;
    dist[start] = 0;
    while(selected[target] == 0)
    {
        min = IN;
        m = 0;
        for(i = 1; i <= NoU;i++)
        {
            d = dist[start] + matrix[start][i];
            if(d < dist[i]&&selected[i]==0)
            {
                dist[i] = d;
                //prev[i] = start;
            }
            if(min > dist[i] && selected[i]==0)
            {
                min = dist[i];
                m = i;
            }
        }
        start = m;
        selected[start] = 1;
    }
    return dist[target];
}
int searchim(float ** graph1,int NoQ, int *imnb, int NoU, int Noim)
{   
    int j;
    for(j = 1; j <= NoU; j++)
    {
        if(graph1[NoQ][j] == 1)
        {
            imnb[Noim++] = j;
        }
    }
    return Noim;
}

void freematrix(float ** matrix, int NoU)
{
    while(NoU>=0)
    {
        free(matrix[NoU]);
        NoU--;
    }
    free(matrix);
}

/*******************************************************************************************
  Main Funciton of Project3
  Zhiyang Lin(lin382@purdue.edu)  
*******************************************************************************************/
int main(int argc, char * argv[])
{
    if(argc == 2)
    {
        FILE * fout = fopen("output.txt","w");
        int Id, Age, Gender, M_Status, Race, Bir_Place, Language, Occupation, Income;
        int NoU = 0;
        float delta1 = 0;
        float delta2 = 0;
        int NoQ = 0;
        float alpha = 0;
        int i = 0;
        int j  = 0;
        int NoN = 0;
        float Lmax = 0;
        FILE * fp;
        char c;
        char * filename;
        filename = argv[1];
        fp = fopen(filename, "r");
        if(!fp)
        {
            return 1; 
        }
        fscanf(fp,"%d%c%f%c%f%c%d%c%f", &NoU, &c, &delta1, &c, &delta2, &c, &NoQ, &c, &alpha);
        Prof profile[NoU+1];
        for(i = 1; i < NoU+1; i++)
        {
            fscanf(fp,"%d%c%d%c%d%c%d%c%d%c%d%c%d%c%d%c%d", &Id, &c, &Age, &c, &Gender, &c, &M_Status, &c, &Race, &c, &Bir_Place, &c, &Language,&c, &Occupation,&c, &Income);
            profile[i] = createp(Id, Age, Gender, M_Status, Race, Bir_Place, Language, Occupation, Income);
        }
        fclose(fp);
        float **matrix1;
        matrix1  = (float **)malloc((NoU+1)*sizeof(float*));
        for(i = 0; i <= NoU; i++)
        {
            matrix1[i] = (float *)malloc((NoU+1)*sizeof(float));
        }
        float **graph1;
        graph1  = (float **)malloc((NoU+1)*sizeof(float*));
        for(i = 0; i <= NoU; i++)
        {
            graph1[i] = (float *)malloc((NoU+1)*sizeof(float));
        }
        float **matrix2;
        matrix2  = (float **)malloc((NoU+1)*sizeof(float*));
        for(i = 0; i <= NoU; i++)
        {
            matrix2[i] = (float *)malloc((NoU+1)*sizeof(float));
        }
        float **graph2;
        graph2  = (float **)malloc((NoU+1)*sizeof(float*));
        for(i = 0; i <= NoU; i++)
        {
            graph2[i] = (float *)malloc((NoU+1)*sizeof(float));
        }
        for(i = 1; i <= NoU; i++)
        {
             for(j = 1; j <= NoU; j++)
             {
                matrix1[i][j] = calULab(profile[i],profile[j]);
                if( matrix1[i][j] >= Lmax)
                {
                    Lmax = matrix1[i][j];
                }
             }
        }
        //printm(matrix1,NoU);
        matrixnorm(matrix1,matrix2,graph1,graph2,NoU,Lmax,delta1,delta2);
        //printp(profile, NoU);
        //printf("matrix1\n");
        //printm(graph1,NoU);
        //printm(graph2,NoU);
        //printf("matrix2\n");
        //printm(matrix2,NoU);
        /****************************************************************************
        Query 1 with graph1(20 pints): 
        ****************************************************************************/
        float min = IN;
        float dist[NoU+1];
        for(i = 1; i <= NoU; i++)
        {
            dist[i] = dijsktra(matrix1, NoQ, i, NoU);
            if(i != NoQ && dist[i] < min)
            {
                min = dist[i];
            }
        }
        fprintf(fout,"%0.2f",min);
        for (i = 1; i <= NoU; i++)
        {
            if(dist[i] == min)
            {
                fprintf(fout,",%d",i);
            }
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 2 with graph1(20 pints): 
        ****************************************************************************/
        for(i = 1; i <= NoU; i++)
        {
            if(dist[i] < alpha && i != NoQ)
            {
                NoN++;
            }
        }
        fprintf(fout,"%d\n",NoN);
        /****************************************************************************
        Query 3 with graph1(20 pints): 
        ****************************************************************************/
        int selected[NoU+1];
        int imnb[NoU];
        int Noim = 0;
        
        Noim = searchim(graph1,NoQ,imnb,NoU,Noim);
        fprintf(fout,"%d",Noim);
        for(i = 0; i < Noim; i++)
        {
            fprintf(fout,",%d",imnb[i]);
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 4 with graph1(20 pints): 
        ****************************************************************************/
        int No2w = 0;
        int n;
        for (i = 1; i <= NoU; i++)
        {
            selected[i] = 0;
        }
        selected[NoQ] = 1;
        for(i = 0; i < Noim; i++)
        {
            n = imnb[i];
            for(j = 1; j <= NoU; j++)
            {
                if(graph1[n][j] == 1 && selected[j] != 1)
                {
                    No2w++;
                    selected[j] = 1;
                }
                if(No2w == NoU-1)
                {
                    break;
                }
            }
        }
        fprintf(fout,"%d",No2w);
        for(i = 1; i <= NoU; i++)
        {
            if(i!= NoQ && selected[i] == 1)
            {
                fprintf(fout,",%d",i);
            }
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 5 with graph1(10 pints): 
        ****************************************************************************/
        int ttimnb = 0;
        for(i = 1; i <= NoU; i++)
        {
            Noim = 0;
            Noim = searchim(graph1,i,imnb,NoU,Noim);
            ttimnb = ttimnb + Noim;
        }
        double avgimnb1 = (double)ttimnb/(double)NoU;
        
        int temp = (int)(avgimnb1 * 1000);
        temp = temp / 10;
        avgimnb1 = (double)temp/100;
        fprintf(fout,"%0.2f\n",avgimnb1);
        /****************************************************************************
        Query 6 with graph1(10 pints): 
        ****************************************************************************/
        int ind;
        int tt2wnb = 0;
        for (i = 1; i <= NoU; i++) 
        {
            Noim = 0;
            No2w = 0;
            for (j = 1; j <= NoU; j++)
            {
                selected[j] = 0;
            }
            selected[i]= 1;
            Noim = searchim(graph1,i,imnb,NoU,Noim);
            for(j = 0; j < Noim; j++)
            {
                n = imnb[j];
                for(ind = 1; ind <= NoU; ind++)
                {
                    if(graph1[n][ind] == 1 && selected[ind] != 1)
                    {
                        No2w++;
                        selected[ind] = 1;
                    }
                    if(No2w == NoU-1)
                    {
                        break;
                    }
                }
            }
            //printf("%d %d\n",i,No2w);
            tt2wnb = tt2wnb + No2w;
        }
        double avg2wnb1 = (double)tt2wnb/(double)NoU;
        
        temp = (int)(avg2wnb1 * 1000);
        temp = temp / 10; 
        avg2wnb1 = (double)temp/100;
        fprintf(fout,"%0.2f\n", avg2wnb1);
        fprintf(fout,"\n");
        /****************************************************************************
        Query 1 with graph2(20 pints): 
        ****************************************************************************/
        min = IN;
        for(i = 1; i <= NoU; i++)
        {
            dist[i] = dijsktra(matrix2, NoQ, i, NoU);
            if(i != NoQ && dist[i] < min)
            {
                min = dist[i];    
            }
        }
        fprintf(fout,"%0.2f",min);

        for (i = 1; i <= NoU; i++)
        {
            if(dist[i] == min)
            {
                fprintf(fout,",%d",i);
            }
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 2 with graph2(20 pints): 
        ****************************************************************************/
        NoN = 0;
        for(i = 1; i <= NoU; i++)
        {
            if(dist[i] < alpha && i != NoQ)
            {
                NoN++;
            }
        }
        fprintf(fout,"%d\n",NoN);
        /****************************************************************************
        Query 3 with graph2(20 pints): 
        ****************************************************************************/
        Noim = 0;
        Noim = searchim(graph2,NoQ,imnb,NoU,Noim);
        fprintf(fout,"%d",Noim);
        for(i = 0; i < Noim; i++)
        {
            fprintf(fout,",%d",imnb[i]);
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 4 with graph2(20 pints): 
        ****************************************************************************/
        No2w = 0;
        for (i = 1; i <= NoU; i++)
        {
            selected[i] = 0;
        }
        selected[NoQ] = 1;
        for(i = 0; i < Noim; i++)
        {
            n = imnb[i];
            for(j = 1; j <= NoU; j++)
            {
                if(graph2[n][j] == 1 && selected[j] != 1)
                {
                    No2w++;
                    selected[j] = 1;
                }
                if(No2w == NoU-1)
                {
                    break;
                }
            }
        }
        fprintf(fout,"%d",No2w);
        for(i = 1; i <= NoU; i++)
        {
            if(i!= NoQ && selected[i] == 1)
            {
                fprintf(fout,",%d",i);
            }
        }
        fprintf(fout,"\n");
        /****************************************************************************
        Query 5 with graph2(10 pints): 
        ****************************************************************************/
        ttimnb = 0;
        for(i = 1; i <= NoU; i++)
        {
            Noim = 0;
            Noim = searchim(graph2,i,imnb,NoU,Noim);
            ttimnb = ttimnb + Noim;
        }
        double avgimnb2 = (double)ttimnb/(double)NoU;
        
        temp = (int)(avgimnb2 * 1000);
        temp = temp / 10;
        avgimnb2 = (double)temp/100;
        fprintf(fout,"%0.2f\n", avgimnb2);
        /****************************************************************************
        Query 6 with graph2(10 pints): 
        ****************************************************************************/
        tt2wnb = 0;
        for (i = 1; i <= NoU; i++) 
        {
            Noim = 0;
            No2w = 0;
            for (j = 1; j <= NoU; j++)
            {
                selected[j] = 0;
            }
            selected[i]= 1;
            Noim = searchim(graph2,i,imnb,NoU,Noim);
            for(j = 0; j < Noim; j++)
            {
                n = imnb[j];
                for(ind = 1; ind <= NoU; ind++)
                {
                    if(graph2[n][ind] == 1 && selected[ind] != 1)
                    {
                        No2w++;
                        selected[ind] = 1;
                    }
                    if(No2w == NoU-1)
                    {
                        break;
                    }
                }
            }
            //printf("%d %d\n",i,No2w);
            tt2wnb = tt2wnb + No2w;
        }
        double avg2wnb2 = (double)tt2wnb/(double)NoU;
        temp = (int)(avg2wnb2 * 1000);
        temp = temp / 10;
        avg2wnb2 = (float)temp/100;
        fprintf(fout,"%0.2f\n", avg2wnb2);
        /****************************************************************************
         Free Everything After We Are Done!
        ****************************************************************************/
        freematrix(matrix1,NoU);
        freematrix(matrix2,NoU);
        freematrix(graph1,NoU);
        freematrix(graph2,NoU);
    }

    return 0;

}