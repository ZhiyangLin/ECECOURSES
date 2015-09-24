#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TRUE 1
#define FALSE 0
#define blen 2001
#define ERROR 2


void printHelp()
{
  printf("Usage: grep-lite [OPTION]... PATTERN\n"
	 "Search for PATTERN in standard input. PATTERN is a\n"
	 "string. grep-lite will search standard input line by\n"
	 "line, and (by default) print out those lines which\n"
	 "contain pattern as a substring.\n"

	 "-v, --invert-match     print non-matching lines\n"
	 "-n, --line-number      print line number with output\n"
	 "-q, --quiet            suppress all output\n"

	  "Exit status is 0 if any line is selected, 1 otherwise;\n"
	  "if any error occurs, then the exit status is 2.\n\n");
}


int main(int argc, char * * argv)
{
  int switchh = FALSE;
  int switchv = FALSE; 
  int switchn = FALSE;
  int switchq = FALSE;
  int ind;
  const char*pattern = argv[argc - 1];

  
  for(ind=1; ind < argc-1; ind++) 
    {
      if(strcmp(argv[ind],"--help")==0)
      {
	switchh = TRUE;
      }
      else if(strcmp(argv[ind], "-v") == 0) 
      {
	switchv = TRUE;
      }
      else if(strcmp(argv[ind], "--invert-match") == 0) 
      {
	switchv = TRUE;
      }
      else if(strcmp(argv[ind], "-n") == 0) 
      {
	switchn = TRUE;
      }
      else if(strcmp(argv[ind], "--line-number") == 0) 
      {
	switchn = TRUE;
      }
      else if(strcmp(argv[ind], "-q") == 0)
      {
	switchq = TRUE;
      }
      else if(strcmp(argv[ind], "--quiet") == 0) 
      {
	switchq = TRUE;
      }
      else
      {
	fprintf(stderr, "error!\n");
	return ERROR;
      }
    }
  if(switchh||strcmp(pattern,"--help")==0)
  {
    printHelp();
    return EXIT_SUCCESS;
  }
  
  if(argc==1)
  {
    fprintf(stderr,"error!\n");
    return ERROR;
  }
  char buffer[blen];
  int ifmatch = FALSE;
  int linno = 0;
  
  while (fgets(buffer,blen,stdin)!= NULL)
  {
    linno++;
    int mline = strstr(buffer,pattern)!=NULL;
    if((mline && !switchv)||(!mline && switchv))
    {
      ifmatch = TRUE;
      if(!switchq)
      {
	if(switchn)
	{
	  printf("%d:",linno);
	}
	printf("%s",buffer);
      }
    }
  }
    return ifmatch ? 0:1;
}