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

void initialize(int** grid, int stackSize, int noOfStacks, int stackOptions[noOfStacks][stackSize], int* topOfStacks, int* firstStack) {
  int row, col, currentStack, x, gridSize = stackSize-1;
  for(row = 0; row < gridSize; row++){
    for(col = 0; col < gridSize; col++){
      currentStack = (row*gridSize)+col; //computes the index of the currentStack
      stackOptions[currentStack][1] = grid[row][col]; //assigns the values of each cell to the stack of values/options
      if (grid[row][col] != 0) {
        topOfStacks[currentStack]++; //updates the indeces of the topOfStacks
      }
      if (grid[row][col] == 0 && *firstStack == -1) {
        *firstStack = currentStack; //assigns the index of the firstStack 
      }
    }
  }
}

int inRow(int candidate, int noOfStacks, int stackSize, int stackOptions[noOfStacks][stackSize], int topOfStacks[noOfStacks], int currentRow) {
  int col, gridValue, currentStack, gridSize = stackSize-1;
  for (col = 0; col < gridSize; col++) {
    currentStack = (currentRow*gridSize)+col; //computes the index of the currentStack
    gridValue = stackOptions[currentStack][topOfStacks[currentStack]]; //get the current value for the currentStack 
    if (candidate == gridValue) {
      return 1; //returns 1 if there is a duplicate in the row for the candidate
    }
  }
  return 0; //returns 0 if there is no duplicate for the candidate
}

int inCol(int candidate, int noOfStacks, int stackSize, int stackOptions[noOfStacks][stackSize], int topOfStacks[noOfStacks], int currentCol) {
  int row, gridValue, currentStack, gridSize = stackSize-1;
  for (row = 0; row < gridSize; row++) {
    currentStack = (row*gridSize)+currentCol; //computes the index of the currentStack
    gridValue = stackOptions[currentStack][topOfStacks[currentStack]]; //get the current value for the currentStack 
    if (candidate == gridValue) {
      return 1; //returns 1 if there is a duplicate in the column for the candidate
    }
  }
  return 0; //returns 0 if there is no duplicate for the candidate
}

int inSubgrid(int candidate, int noOfStacks, int stackSize, int subgridSize, int stackOptions[noOfStacks][stackSize], int topOfStacks[noOfStacks], int startingCol, int startingRow) {
  int row, col, gridValue, currentStack, gridSize = stackSize-1;
  for (row = 0; row < subgridSize; row++) {
    for (col = 0; col < subgridSize; col++) {
      currentStack = ((startingRow+row)*gridSize)+(startingCol+col); //computes the index of the currentStack
      gridValue = stackOptions[currentStack][topOfStacks[currentStack]]; //get the current value for the currentStack 
      if (candidate == gridValue) {
        return 1; //returns 1 if there is a duplicate in the subgrid for the candidate
      }
    }
  }
  return 0; //returns 0 if there is no duplicate for the candidate
}

int inX(int candidate, int noOfStacks, int stackSize, int stackOptions[noOfStacks][stackSize], int topOfStacks[noOfStacks], int currentRow, int currentCol) {
  int row, col, gridValue, currentStack, gridSize = stackSize-1;
  //check if the candidate's row and col is in the 'X' of the grid
  if (currentRow == currentCol || currentRow+currentCol == gridSize-1) {
    for (row = 0; row < gridSize; row++) {
      for (col = 0; col < gridSize; col++) {
        //row and col combinations in the 'X' of the grid are checked for duplicates
        if (row == col && currentRow == currentCol && row != currentRow && col != currentCol) {
          currentStack = (row*gridSize)+col; //computes the index of the currentStack
          gridValue = stackOptions[currentStack][topOfStacks[currentStack]]; //get the current value for the currentStack 
          if (candidate == gridValue) {
            return 1; //returns 1 if there is a duplicate of the candidate in the 'X' of the grid 
          }
        } else if (currentRow+currentCol == gridSize-1 && row+col == gridSize-1  && row != currentRow && col != currentCol) {
          currentStack = (row*gridSize)+col; //computes the index of the currentStack
          gridValue = stackOptions[currentStack][topOfStacks[currentStack]]; //get the current value for the currentStack 
          if (candidate == gridValue) {
            return 1; //returns 1 if there is a duplicate of the candidate in the 'X' of the grid 
          }
        }
      }  
    }
    return 0; //returns 0 if there is no duplicate for the candidate
  } else {
    return 0; //returns 0 if the candidate is not in the 'X' of the grid
  }
}

int inY() {

}

int inXY() {

}

void puzzleSolutions(int** grid, int gridSize, int noOfStacks, int subgridSize) {
  //noOfStacks is based on number of cells in the puzzle
  int row, col; //traverses the cells of the grid

  int solvable = 0; //serves as flag if the initial values of the grid does not violate the rules 

  int stackSize = gridSize+1; //size of each stack
 
  int solutionNo = 0; //indicates number of possible solutions
  
  int candidate; //assigns the possible values for each cells 
  
  int firstStack = -1; //points to the first cell that have a value zero (initialized to -1 for a possible firstStack w/ index zero) 

  int currentStack; //traverses which cell is currently filled with possible values (holds the index of the currentStack)  

  int backtrack; //serves as flag that tells if backtracking should be done
  
  int UsedInRow, UsedInCol, UsedInSubgrid, UsedInX, UsedInY; //holds the values (1[if used], 0[not used]) for the checking of candidates
  
  int currentRow, currentCol, startingRow, startingCol; //stores the indexes for checking the candidate
  
  int topOfStacks[noOfStacks]; //array of indeces of the top of each stack used to access the stackOptions

  int stackOptions[noOfStacks][stackSize]; //array of possible values for each stack (format: stackOptions[in currentStack][index of the top of the current stack(topOfStacks[currentStack])])

  int permanentStacks[noOfStacks]; //array of the indeces of the top of each permanent stack (this holds the index of the permanent values)
  
  //additional variable
  int x;

  //initialize permanentStacks topOfStacks to zero to remove trash value
  for (currentStack = 0; currentStack < noOfStacks; currentStack++) {
    permanentStacks[currentStack] = topOfStacks[currentStack] = 0;
  }

  //initialize stackOptions to zero to remove trash value
  for (row = 0; row < noOfStacks; row++){
    for(col = 0; col < stackSize; col++){
      stackOptions[row][col] = 0;
    }
  }

  //initialize the stackOptions, topOfStacks and firstStack based on the given puzzle or grid
  initialize(grid, stackSize, noOfStacks, stackOptions, topOfStacks, &firstStack);

  //initialize permanentStacks based on the initial topOfStacks
  for (currentStack = 0; currentStack < noOfStacks; currentStack++) {
    permanentStacks[currentStack] = topOfStacks[currentStack];
  }

  //check the given grid for possible violation of the initial values in the 'X' of the grid
  for (row = 0; row < gridSize; row++) {
    if (solvable == 0) {
      for (col = 0; col < gridSize; col ++) {
        if (row == col || row+col == gridSize-1) { //check the initial values in the 'X' of the grid
          currentStack = (row*gridSize)+col; //computes the index of the currentStack
          x = stackOptions[currentStack][topOfStacks[currentStack]]; //get the value for the currentStack 
          UsedInX = inX(x, noOfStacks, stackSize, stackOptions, topOfStacks, row, col);
          if (UsedInX == 1 && x != 0) {
            solvable = 1;
            break;
          }
        }
      }
    } else {
      break;
    }
  }

  if (solvable == 0) { //continue to solve for the possible solution if the initial grid is valid
    currentStack = firstStack; 
    row = col = backtrack = 0;

    //fill the first stack
    for (candidate = gridSize; candidate >= 0; candidate--) {
      if (candidate != 0 && backtrack != 1) { //check current candidate as possible stackOption of the current stack
        //initialize indeces for checking candidate
        currentRow = currentStack/gridSize;
        currentCol = currentStack%gridSize;
        startingRow = currentRow-(currentRow%subgridSize);
        startingCol = currentCol-(currentCol%subgridSize);
        
        //check row for duplicates
        UsedInRow = inRow(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentRow);
        if (UsedInRow !=1) {
          //check col for duplicates
          UsedInCol = inCol(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentCol);
          if (UsedInCol != 1) {
            //check subgrid for duplicates
            UsedInSubgrid = inSubgrid(candidate, noOfStacks, stackSize, subgridSize, stackOptions, topOfStacks, startingCol, startingRow);
            if (UsedInSubgrid != 1) {
              //check the 'X' of the grid for duplicates
              UsedInX = inX(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentRow, currentCol);
              if (UsedInX != 1) {
                stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
              }
            }
          }
        }
      } else if (candidate == 0 && topOfStacks[currentStack] == 0) { //if the candidate is zero and there is no assigned candidate for the currentStack do backtrack
        backtrack = 1; 
        currentStack = currentStack-2;
      }
    }

    currentStack++;
     
    while (topOfStacks[firstStack] > 0) {
      //fill the stackOptions
      
      if (currentStack < noOfStacks && backtrack == 0) {
        if (permanentStacks[currentStack] != 1) {
          for (candidate = gridSize; candidate >= 0; candidate--) {
            if (candidate != 0 && backtrack != 1) { //check current candidate as possible stackOption of the current stack
              //initialize indeces for checking candidate
              currentRow = currentStack/gridSize;
              currentCol = currentStack%gridSize;
              startingRow = currentRow-(currentRow%subgridSize);
              startingCol = currentCol-(currentCol%subgridSize);
              
              //check row for duplicates
              UsedInRow = inRow(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentRow);
              if (UsedInRow !=1) {
                //check col for duplicates
                UsedInCol = inCol(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentCol);
                if (UsedInCol != 1) {
                  //check subgrid for duplicates
                  UsedInSubgrid = inSubgrid(candidate, noOfStacks, stackSize, subgridSize, stackOptions, topOfStacks, startingCol, startingRow);
                  if (UsedInSubgrid != 1) {
                    //check the 'X' of the grid for duplicates
                    UsedInX = inX(candidate, noOfStacks, stackSize, stackOptions, topOfStacks, currentRow, currentCol);
                    if (UsedInX != 1) {
                      stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
                    }
                  }
                }
              }
            } else if (candidate == 0 && topOfStacks[currentStack] == 0) { //if the candidate is zero and there is no assigned candidate for the currentStack do backtrack
              backtrack = 1; 
              currentStack = currentStack-2;
            }
          }
        }
        currentStack++;
      } else {
        if (currentStack == noOfStacks) {  //print the solution if the last stack is reached
          solutionNo++; //increment solution number
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
          backtrack = 1; //set flag to do backtracking
        }
        
        //algotrithm to backtrack
        if (backtrack == 1 && permanentStacks[currentStack] != 1){
          stackOptions[currentStack][topOfStacks[currentStack]] = 0; //remove the candidate at the currentStack
          topOfStacks[currentStack]--; //reduce the index of the topOfStack 
          if (topOfStacks[currentStack] >= 1) { //if there is still candidates stop backtracking
            backtrack = 0;
            currentStack = currentStack+2;
          }
        }   
        currentStack--;
      }
    }
  }
  if (solutionNo == 0) { //if no solution is found
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
