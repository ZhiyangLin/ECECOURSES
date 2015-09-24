#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "answer05.h"



Image *Image_load(const char *filename)
{
  FILE * fp = fopen(filename,"rb");
  int eror = fp == NULL;
  Image * tmp_im = NULL, * im = NULL;

  if (eror) fprintf(stderr,"Unable to open the file %s!\n",filename);
  
  ImageHeader header;
  size_t read;
  
  if(!eror) {
    read = fread(&header, sizeof(ImageHeader),1,fp);
    eror = read != 1 ||
      header.magic_number != ECE264_IMAGE_MAGIC_NUMBER ||
      header.width == 0 ||
      header.height == 0 ||
      header.comment_len == 0;
      if (eror) fprintf(stderr,"corrupt header\n");
  }
 

  if(!eror){
    tmp_im = malloc(sizeof(Image));
    
    eror = tmp_im == NULL;
    if(eror) fprintf(stderr,"failed to allocate memory\n");
  }
  
  

  if (!eror){
    tmp_im->width = header.width;
    tmp_im->height = header.height;
    tmp_im->comment = malloc(sizeof(char) * header.comment_len);
    tmp_im->data = malloc(sizeof(uint8_t)*header.width*header.height);
    
    eror = tmp_im->comment ==NULL ||
      tmp_im->data == NULL;
    if(eror) fprintf(stderr,"failed to allocate memory to pixels and comments\n");
   }
  
  if(!eror){
    read = fread(tmp_im->comment,sizeof(char),header.comment_len,fp);
    eror = ((read != header.comment_len) || tmp_im->comment[header.comment_len - 1] !='\0');
    if(eror) fprintf(stderr, "failed to read the comment\n");
  }

  if(!eror){
    read = fread(tmp_im->data,sizeof(uint8_t),header.width * header.height,fp);
    eror = read != header.width * header.height;
    if(eror) fprintf(stderr, "failed to read the pixels\n");
  }

  if(!eror) {
    fgetc(fp);
    eror = !feof(fp);
    if(eror) fprintf(stderr,"failed finishing file reading\n");
  }

  if(!eror){
    im = tmp_im;
    tmp_im = NULL;
    return im;
  }
  
   if (fp != NULL) fclose(fp);


   Image_free(tmp_im);
   
   return NULL;
}


int Image_save(const char * filename, Image * image)

  {
    FILE * fp = NULL;
    fp = fopen(filename,"wb");
    if (fp == NULL)
    {
      fprintf(stderr,"Unable to open the file %s!\n",filename);
      return 0;
    }
    ImageHeader hsaved;
    hsaved.magic_number = ECE264_IMAGE_MAGIC_NUMBER;
    hsaved.width = image-> width;
    hsaved.height = image-> height;
    hsaved.comment_len = strlen(image->comment)+1;
    
    
    fwrite(&hsaved, sizeof(ImageHeader),1,fp);
    fwrite(image->comment,sizeof(char),hsaved.comment_len,fp);
    fwrite(image->data,sizeof(uint8_t),hsaved.width * hsaved.height,fp);
    if (fp != NULL) fclose(fp);
  

  return 1;
 
}

void Image_free(Image * image)

{
  if(image != NULL)
  {
    free(image->comment);
    free(image->data);
    free(image);
  }
}  


void linearNormalization(int width, int height, uint8_t * intensity)
{
    int numpixels = width * height;
    int ind;
    int max = 0;
    int min = intensity[0];
    for(ind = 0; ind < numpixels; ind++)
    {
      if(intensity[ind]> max)
      {
	max = intensity[ind];
      }
      if(intensity[ind]< min)
      {
	min = intensity[ind];
      }
    }
    for(ind = 0; ind < numpixels; ind++)
    {
      intensity[ind] = (intensity[ind] - min) * 255.0 / (max - min);
    }
}

