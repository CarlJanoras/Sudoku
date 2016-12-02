#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

void printInt(int n) {
  if (n > 9) printInt(n / 10);
  putchar_unlocked(n % 10 + '0');
}

void printString(char *s) {
  while (*s != '\0') {
    putchar_unlocked(*(s++));
  }
}

typedef struct node{
  int** grid;
  int subgridSize;
}puzzle;

void initialize(int** grid, int GridSize, int noOfStacks, int stackOptions[noOfStacks][GridSize], int* topOfStacks, int* firstStack) {
  int row, col, currentStack, x, gridSize = GridSize-1;
  for(row = 0; row < gridSize; row++){
    for(col = 0; col < gridSize; col++){
      currentStack = (row*gridSize)+col;
      stackOptions[currentStack][1] = grid[row][col];
      if (grid[row][col] != 0) {
        topOfStacks[currentStack]++;
      }
      if (grid[row][col] == 0 && *firstStack == -1) {
        *firstStack = currentStack;
      }
    }
  }
}

int inRow(int candidate, int noOfStacks, int GridSize, int stackOptions[noOfStacks][GridSize], int topOfStacks[noOfStacks], int currentRow) {
  int col, gridValue, currentStack, gridSize = GridSize-1;
  for (col = 0; col < gridSize; col++) {
    currentStack = (currentRow*gridSize)+col;
    gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
    if (candidate == gridValue) {
      return 1;
    }
  }
  return 0;
}

int inCol(int candidate, int noOfStacks, int GridSize, int stackOptions[noOfStacks][GridSize], int topOfStacks[noOfStacks], int currentCol) {
  int row, gridValue, currentStack, gridSize = GridSize-1;
  for (row = 0; row < gridSize; row++) {
    currentStack = (row*gridSize)+currentCol;
    gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
    if (candidate == gridValue) {
      return 1;
    }
  }
  return 0;
}

int inSubgrid(int candidate, int noOfStacks, int GridSize, int subgridSize, int stackOptions[noOfStacks][GridSize], int topOfStacks[noOfStacks], int startingCol, int startingRow) {
  int row, col, gridValue, currentStack, gridSize = GridSize-1;
  for (row = 0; row < subgridSize; row++) {
    for (col = 0; col < subgridSize; col++) {
      currentStack = ((startingRow+row)*gridSize)+(startingCol+col);
      gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
      if (candidate == gridValue) {
        return 1;
      }
    }
  }
  return 0;
}

int inX() {

}

int inY() {

}

int inXY() {

}

void puzzleSolutions(int** grid, int gridSize, int noOfStacks, int subgridSize) {
  int x, row, col, candidate, GridSize = gridSize+1, solutionNo = 0;
  int firstStack = -1, currentStack, backtrack; 
  int UsedInRow, UsedInCol, UsedInSubgrid;
  int currentRow, currentCol, startingRow, startingCol;
  int topOfStacks[noOfStacks]; //array of the current top of each stack
  int stackOptions[noOfStacks][GridSize]; //array of possible values for each stack
  int permanentStacks[noOfStacks]; //array of the current top of each permanent stack

  //initialize permanentStacks topOfStacks to zero to remove trash value
  for (x = 0; x < noOfStacks; x++) {
    permanentStacks[x] = topOfStacks[x] = 0;
  }

  //initialize stackOptions to zero to remove trash value
  for (row = 0; row < noOfStacks; row++){
    for(col = 0; col < GridSize; col++){
      stackOptions[row][col] = 0;
    }
  }

  //initialize the stackOptions, topOfStacks and firstStack based on the given puzzle
  initialize(grid, GridSize, noOfStacks, stackOptions, topOfStacks, &firstStack);

  for (x = 0; x < noOfStacks; x++) {
    permanentStacks[x] = topOfStacks[x];
  }

  currentStack = firstStack; 
  row = col = backtrack = 0;

  //fill the first stack
  for (candidate = gridSize; candidate >= 0; candidate--) {
    if (candidate == 0 && topOfStacks[currentStack] == 0) {
      backtrack = 1;
      currentStack = currentStack-2;
      break;
    }
    //check current candidate as possible stackOption of the current stack
    currentRow = currentStack/gridSize;
    currentCol = currentStack%gridSize;
    startingRow = currentRow-(currentRow%subgridSize);
    startingCol = currentCol-(currentCol%subgridSize);

    //check row for duplicates
    UsedInRow = inRow(candidate, noOfStacks, GridSize, stackOptions, topOfStacks, currentRow);
    //check col for duplicates
    UsedInCol = inCol(candidate, noOfStacks, GridSize, stackOptions, topOfStacks, currentCol);
    //check subgrid for duplicates
    UsedInSubgrid = inSubgrid(candidate, noOfStacks, GridSize, subgridSize, stackOptions, topOfStacks, startingCol, startingRow);
    if (backtrack != 1 && UsedInRow != 1 && UsedInCol != 1 && UsedInSubgrid != 1 && candidate != 0) {
      stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
    }
  }

  currentStack++;
   
  while (topOfStacks[firstStack] > 0) {
    //fill the stackOptions
    
    if (currentStack < noOfStacks && backtrack == 0) {
      if (permanentStacks[currentStack] != 1) {
        for (candidate = gridSize; candidate >= 0; candidate--) {
          if (candidate == 0 && topOfStacks[currentStack] == 0) {
            backtrack = 1;
            currentStack = currentStack-2;
            break;
          }
          //check current candidate as possible stackOption of the current stack
          currentRow = currentStack/gridSize;
          currentCol = currentStack%gridSize;
          startingRow = currentRow-(currentRow%subgridSize);
          startingCol = currentCol-(currentCol%subgridSize);

          //check row for duplicates
          UsedInRow = inRow(candidate, noOfStacks, GridSize, stackOptions, topOfStacks, currentRow);
          //check col for duplicates
          UsedInCol = inCol(candidate, noOfStacks, GridSize, stackOptions, topOfStacks, currentCol);
          //check subgrid for duplicates
          UsedInSubgrid = inSubgrid(candidate, noOfStacks, GridSize, subgridSize, stackOptions, topOfStacks, startingCol, startingRow);
          if (backtrack != 1 && UsedInRow != 1 && UsedInCol != 1 && UsedInSubgrid != 1 && candidate != 0) {
            stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
          }   
        }
      }
      currentStack++;
    } else { //backtrack
      if (currentStack == noOfStacks) {
        solutionNo++;
        printString("\n Solution number: \0");
        printInt(solutionNo);
        //print the solution
        for (x = 0; x < noOfStacks; x++) {
          if (x%gridSize == 0) {
            putchar_unlocked('\n');
          }
          putchar_unlocked(' ');
          printInt(stackOptions[x][topOfStacks[x]]);
        }
        putchar_unlocked('\n');
        currentStack--;
        backtrack = 1;
      }
      
      if (backtrack == 1 && permanentStacks[currentStack] != 1){
        stackOptions[currentStack][topOfStacks[currentStack]] = 0;
        topOfStacks[currentStack]--;
        if (topOfStacks[currentStack] >= 1) {
          backtrack = 0;
          currentStack = currentStack+2;
        }
      }   
      currentStack--;
    }
  }
  if (solutionNo == 0) {
    printString("\n   No possible solution.\n");
  }
}

void print_puzzle(int** grid, int gridSize){
  int row, col;
  for(row = 0; row < gridSize; row++){
    for(col = 0; col < gridSize; col++){
      putchar_unlocked(' ');
      printInt(grid[row][col]);
    }
    putchar_unlocked('\n');
  }
}

void solvePuzzles(puzzle *p, int numberOfPuzzles) {
  int x, gridSize, noOfStacks;
  for (x = 0; x < numberOfPuzzles; x++) {
    gridSize = p[x].subgridSize*p[x].subgridSize;
    noOfStacks = gridSize*gridSize;
    printString("\nPuzzle Number: \0");
    printInt(x+1);
    putchar_unlocked('\n');
    print_puzzle(p[x].grid, gridSize);
    puzzleSolutions(p[x].grid, gridSize, noOfStacks, p[x].subgridSize); 
  } 
}

int main(){
  int numberOfPuzzles;
  puzzle *p;
  int x, row, col, gridSize;

  FILE *fp = fopen("puzzles.txt", "r");
  if (freopen("output.txt", "w", stdout)) {}
  
  if (fscanf(fp, "%d", &numberOfPuzzles)) {}
  
  p = (puzzle *) malloc(sizeof(puzzle)*numberOfPuzzles);
  
  for(x = 0; x < numberOfPuzzles; x++){
    if (fscanf(fp, "%d", &(p[x].subgridSize))) {}
    gridSize = p[x].subgridSize*p[x].subgridSize;
    p[x].grid = (int **) malloc(sizeof(int *)*gridSize);
    
    for(row = 0; row < gridSize; row++){
      p[x].grid[row] = (int *) malloc(sizeof(int)*gridSize);
    }
    
    for(row = 0; row < gridSize; row++) {
      for(col = 0; col < gridSize; col++) {
        if (fscanf(fp, "%d", &(p[x].grid[row][col]))) {}
      }
    }
  }
  
  solvePuzzles(p, numberOfPuzzles);
  
  fclose(fp);

  return 0;
}
