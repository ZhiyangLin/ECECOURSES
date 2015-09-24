#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TRUE 1
#define FALSE 0

void printHelp()
{
  printf("Usage: cat-lite [--help] [FILE]...\n"
	 "With no FILE, or when FILE is -, read standard input.\n"

	 "Examples:\n"
	 "cat-lite README   Print the file README to standard output.\n"
	 "cat-lite f - g    Print f's contents, then standard input,\n" 
	 "                  then g's contents.\n"
	 "cat-lite          Copy standard input to standard output.\n\n");
}

int catfile(const char* filename, FILE * fout)
{
  int ch;
  FILE*fin;
  int switchd = (strcmp(filename,"-") == 0);
  if (switchd)
  {
    fin = stdin;
  }
  else
  {
    fin = fopen(filename,"r");
    if(fin)
      {
       while((ch = fgetc(fin)) !=EOF)
      {
	fputc(ch,fout);
      }
    }
  }
  if(fin==NULL)
  {
    return FALSE;
  }
 while((ch = fgetc(fin)) !=EOF)
  {
    fputc(ch,fout);
  }
  if(!switchd) 
  {
    fclose(fin);
  }
  return TRUE;
}

int main(int argc, char * * argv)
{
  
  int ind;
  for(ind = 1; ind < argc; ind++)
  {
    if (strcmp(argv[ind],"--help")==0)
    {
      printHelp();
      return EXIT_SUCCESS;
    }
  }
  
  for(ind = 1;ind < argc; ind++)
  {
    if(catfile(argv[ind],stdout) == FALSE)
    {
      fprintf(stderr,"cat cannot open %s\n",argv[ind]);
      return EXIT_FAILURE;
    }
  }
  if(argc == 1)
  {
    int ch;
    FILE*fin = stdin;
    while((ch = fgetc(fin)) !=EOF)
      {    
	printf("%c",ch);
      }
  }
  return EXIT_SUCCESS;
}