#include <stdio.h>
#include <stdlib.h>
#include "answer08.h"

SparseNode * SparseNode_create(int index, int value)
{
  SparseNode * snode = NULL;
  snode = malloc(sizeof(SparseNode));
  if(snode == NULL)
  {
    return NULL;
  }
  snode-> index = index;
  snode-> value = value;
  snode-> left = NULL;
  snode-> right = NULL;
  return snode;
}

SparseNode * SparseArray_insert(SparseNode * array, int index, int value)
{
  if(value == 0)
  {
    return array;
  }
  if(array == NULL)
  {
    array = SparseNode_create(index, value);
    return array;
  }
  if (array -> index == index)
  {
    array -> value = value;
  }
  else if(array -> index > index)
  {
    array->left = SparseArray_insert(array->left,index,value);
  }
  else if(array -> index < index)
  {
    array->right = SparseArray_insert(array->right,index,value);
  }
  return array;
}

SparseNode * SparseArray_build(int * indices, int * values, int length)
{
  int ind;
  SparseNode * snode = NULL;
  for(ind = 0; ind < length; ind++)
  {
    snode = SparseArray_insert(snode, indices[ind], values[ind]);
  }
  return snode;
}

void SparseArray_destroy(SparseNode * array)
{
  if(array == NULL)
  {
    return;
  }
  SparseArray_destroy(array->left);
  SparseArray_destroy(array->right);
  free(array);
}

int SparseArray_getMin(SparseNode * array)
{
  if(array->left == NULL)
  {
    return array->index;
  }
  int min = SparseArray_getMin(array->left);
  return min;
}

int SparseArray_getMax(SparseNode * array)
{
  if(array->right == NULL)
  {
    return array->index;
  }
  int max = SparseArray_getMax(array->right);
  return max;
}

SparseNode * SparseArray_getNode(SparseNode * array, int index)
{
  if (array == NULL)
  {
    return NULL;
  }
  if (array -> index == index)
  {
    return array;
  }
  if(array -> index > index)
  {
   return SparseArray_getNode(array->left,index);
  }
  else 
  {
   return  SparseArray_getNode(array->right,index);
  }
}


SparseNode * SparseArray_remove(SparseNode * array, int index)
{
  if ( array == NULL ) { return NULL ; }
  if ( index < ( array -> index ))
  {
    array -> left = SparseArray_remove ( array -> left , index );
    return array ;
  }
  if ( index > ( array -> index ))
  {
    array -> right = SparseArray_remove ( array -> right , index );
    return array ;
  }
  if ((( array -> left ) == NULL ) && (( array -> right ) == NULL ))
  {	
  // r has no child
    free ( array );
    return NULL ;
  }
  if (( array -> left ) == NULL )
  {
    // tn -> right must not be NULL
    SparseNode * rc = array -> right;
    free ( array );
    return rc ;
  }
  if (( array -> right ) == NULL )
  {
  // tn -> left must not be NULL
    SparseNode * lc = array -> left ;
    free ( array );
    return lc ;
}
    SparseNode * snode = array -> right ; // su must not be NULL
    while (( snode -> left ) != NULL )
    {
      snode = snode -> left ;
    }
      array -> value = snode -> value ;
      array -> index = snode -> index ;
      snode -> index = index;
    array -> right = SparseArray_remove ( array -> right , index );
    return array;
}

SparseNode * SparseArray_copy(SparseNode * array)
{
  if (array == NULL)
  {
    return NULL;
  }
  SparseNode * snode = NULL; 
  snode = SparseNode_create(array-> index, array-> value);
  if (array-> left== NULL && array->right== NULL)
  {
    return snode;
  }
  if (array->left != NULL)
  {
    snode->left = SparseArray_copy(array->left);
  }
  if (array->right != NULL)
  {
    snode->right = SparseArray_copy(array->right);
  }
  return snode;
}

SparseNode * mergeoperator2(SparseNode* a,SparseNode *b)
{
  SparseNode* search = NULL;
   search = SparseArray_getNode(a, b->index);
  if(search == NULL)
  {
    a = SparseArray_insert(a,b->index,b->value);
  }
  else
  {
    search->value += b->value;
    if(search->value == 0)
    {
      SparseArray_remove(a, b-> index);
    }
  }
  return a;
}
SparseNode * mergeoperator1(SparseNode * a, SparseNode * b)
{
  if(b == NULL)
  {
    return a;
  }
  mergeoperator2(a,b);
  a = mergeoperator1(a, b->left);
  a = mergeoperator1(a, b->right);
  return a;
}

SparseNode * SparseArray_merge(SparseNode * array_1, SparseNode * array_2)
{
   SparseNode * copyof1 = NULL;
   copyof1 =  SparseArray_copy(array_1);
   copyof1 = mergeoperator1(copyof1, array_2);
   return copyof1;
}
/*int main(int argc, char * * argv)
{
   SparseNode * root = SparseNode_create(10, 100);
   root->left = SparseNode_create(5, 101);
   root->right = SparseNode_create(15, 102);
   root->left->right = SparseNode_create(8, 103);
  
   return 0;
}*/

