import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Sudoku_UI{
  JFrame frame = new JFrame("Sudoku"); //main frame
  Container c; //container of all panels to implement card layout
  CardLayout card; //main layout
  JPanel gameplay; //for actual game
  JPanel option; //for choosing size/dimension of puzzle
  JPanel difficultyMenu; //for difficulty level e.g. puzzle x, puzzle y, etc.
  JPanel board = new JPanel();
  JPanel optionMenu;
  LinkedList <int [][]> initPuzzles; //initial puzzle container
  int nPuzzles; //number of input puzzles
  int [] subgrids; //dimension of subgrids of puzzle
  JTextField [][] jtf;

  //other variables initialization
  int i;

  //JPanel solutionOption;
  JPanel solutionMenu;
  JPanel solutionBoard;
  JPanel solutionPanel;
  JPanel solutionOption;
  JTextField solutionField[][];
  JButton[] solutionButtons;
  int[][] solution;
  BoxLayout bLayout;


  Sudoku_UI(int nPuzzles,final int [] subgrids, final LinkedList <int [][]> initPuzzles) throws Exception{
    //assigning input data to variables
    this.nPuzzles = nPuzzles;
    this.subgrids = subgrids;
    this.initPuzzles = initPuzzles;

    /*DRAWING GUI*/
    frame.setContentPane(new ImagePanel());
    c = frame.getContentPane(); //setting the container as the content of JFrame
    card = new CardLayout(10,10); //initializing card layout with 10 x 10 space
    c.setLayout(card); // setting layout of container

    //initialization of GUI components
    JPanel mainMenu = new JPanel(null); //for main menu
    optionMenu = new JPanel();
    gameplay = new JPanel(new BorderLayout(10,10));
    option = new JPanel();
    JPanel solve = new JPanel(new GridLayout(1,4));
    JPanel checker = new JPanel(new GridLayout(1,5));
    JScrollPane optionScroll = new JScrollPane(option);
    JButton play = new JButton("START");
    JButton backToMain = new JButton("Back to Main Menu");
    JButton backToOption = new JButton("Back to Puzzle Options");
    JButton solveRegular = new JButton("Solve Regular Sudoku");
    JButton solveX = new JButton("Solve X Sudoku");
    JButton solveY = new JButton("Solve Y Sudoku");
    JButton solveXY = new JButton("Solve XY Sudoku");
    JButton checkRegular = new JButton("Check Regular Sudoku");
    JButton checkX = new JButton("Check X Sudoku");
    JButton checkY = new JButton("Check Y Sudoku");
    JButton checkXY = new JButton("Check XY Sudoku");
    JButton [] sizeButtonMenu = new JButton[subgrids.length];
    BoxLayout bl = new BoxLayout(option, BoxLayout.Y_AXIS);

    //setting layouts
    option.setLayout(bl);
    
    /*EDITING COMPONENTS*/
    play.setFont(new Font("Arial",Font.BOLD, 12));
    play.setBounds(350,200,200,100);
    backToMain.setFont(new Font("Arial",Font.BOLD, 12));
    backToOption.setFont(new Font("Arial",Font.BOLD, 12));
    optionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    optionScroll.setPreferredSize(new Dimension(600, 400));
    mainMenu.setOpaque(false);
    optionMenu.setOpaque(false);
    option.setOpaque(false);

    /*ADDING ACTION LISTENERS*/

    //Displaying input puzzles
    i=0;

    while(i < subgrids.length){
      String label = Integer.toString(subgrids[i]*subgrids[i]);
      sizeButtonMenu[i] = new JButton(label + " x " + label);
      sizeButtonMenu[i].putClientProperty("index", i);
      //adding action listener
      sizeButtonMenu[i].addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JButton jb = (JButton) e.getSource();
          int pos = (Integer) jb.getClientProperty("index");
          //System.out.println(pos);
          int [][] p = initPuzzles.get(pos);
          board.setLayout(new GridLayout(p.length, p.length));
          jtf = new JTextField[p.length][p.length];
          for(int j=0; j < p.length; j++){
            for(int k=0; k < p.length; k++){
              jtf[j][k] = new JTextField(3);
              jtf[j][k].setFont(new Font("Arial", Font.BOLD, 20));
              jtf[j][k].setHorizontalAlignment(JTextField.CENTER);
              if(p[j][k] != 0){
                jtf[j][k].setText(Integer.toString(p[j][k]));
                jtf[j][k].setEditable(false);
              }
              board.add(jtf[j][k]);
            }
          }
          gameplay.add(board, BorderLayout.CENTER);
          card.show(c, "gameplay");
        }
      });
      option.add(sizeButtonMenu[i]);
      i++;
    }

    //toggle size Options
    play.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        card.show(c,"optionMenu");
      }
    });

    //toggle main menu
    backToMain.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        board.removeAll();
        card.show(c, "mainMenu");
      }
    });

    //toggle Puzzle option menu
    backToOption.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        board.removeAll();
        card.show(c, "optionMenu");
      }
    });
    solveX.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        boolean error = false;
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(jtf[j][k].getText().equals("")){
              puzzle[j][k] = 0;
            }else if(jtf[j][k].getText().matches(".*[a-z].*")){
              JOptionPane.showMessageDialog(frame,"Invalid input!", "Error",    JOptionPane.PLAIN_MESSAGE);
              jtf[j][k].setBackground(Color.RED);
              error = true;
            }else{
              puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
              jtf[j][k].setBackground(Color.WHITE);
            }
          }
        }
        if(error == false)SolveX(puzzle);
      }
    });
    solveY.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        boolean error = false;
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(jtf[j][k].getText().equals("")){
              puzzle[j][k] = 0;
            }else if(jtf[j][k].getText().matches(".*[a-z].*")){
              JOptionPane.showMessageDialog(frame,"Invalid input!", "Error",    JOptionPane.PLAIN_MESSAGE);
              jtf[j][k].setBackground(Color.RED);
              error = true;
            }else{
              puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
              jtf[j][k].setBackground(Color.WHITE);
            }
          }
        }
        if(error == false)SolveY(puzzle);
      }
    });
    solveXY.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        boolean error = false;
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(jtf[j][k].getText().equals("")){
              puzzle[j][k] = 0;
            }else if(jtf[j][k].getText().matches(".*[a-z].*")){
              JOptionPane.showMessageDialog(frame,"Invalid input!", "Error",    JOptionPane.PLAIN_MESSAGE);
              jtf[j][k].setBackground(Color.RED);
              error = true;
            }else{
              puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
              jtf[j][k].setBackground(Color.WHITE);
            }
          }
        }
        if(error == false)SolveXY(puzzle);
      }
    });
    solveRegular.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        solutionMenu = new JPanel();
        boolean error = false;
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(jtf[j][k].getText().equals("")){
              puzzle[j][k] = 0;
            }else if(jtf[j][k].getText().matches(".*[a-z].*")){
              JOptionPane.showMessageDialog(frame,"Invalid input!", "Error",    JOptionPane.PLAIN_MESSAGE);
              jtf[j][k].setBackground(Color.RED);
              error = true;
            }else{
              puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
              jtf[j][k].setBackground(Color.WHITE);
            }
          }
        }
        if(error == false)SolveRegular(puzzle);
      }
    });

    /*ADDING COMPONENTS TO PANELS*/
    mainMenu.add(play);
    solve.add(solveRegular);
    solve.add(solveX);
    solve.add(solveY);
    solve.add(solveXY);
    checker.add(backToOption);
    checker.add(checkRegular);
    checker.add(checkX);
    checker.add(checkY);
    checker.add(checkXY);
    gameplay.add(checker,BorderLayout.PAGE_START);
    gameplay.add(solve, BorderLayout.PAGE_END);
    optionMenu.add(backToMain);
    optionMenu.add(optionScroll);

    /*ADDING PANELS TO CONTAINER AND ASSIGNING KEYWORD TO IMPLEMENT CARD LAYOUT*/
    c.add("mainMenu", mainMenu);
    c.add("gameplay", gameplay);
    c.add("optionMenu", optionMenu);

    frame.setSize(1000,500);
    //frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  List initialize(int[][] grid, int[][] stackOptions, int[] topOfStacks, int firstStack){
    List returnValues = new ArrayList(3); //create a list for returning stackOptions, topOfStacks, firstStack respectively
    int row, col, currentStack, x, gridSize = grid.length;
    for(row = 0; row < gridSize; row++){
      for(col = 0; col < gridSize; col++){
        currentStack = (row*gridSize)+col;
        stackOptions[currentStack][1] = grid[row][col];
        if(grid[row][col] != 0){
          topOfStacks[currentStack]++;
        }
        if(grid[row][col] == 0 && firstStack == -1){
          firstStack = currentStack;
        }
      }
    }
    /*returnValues.set(0, stackOptions);
    returnValues.set(1, topOfStacks);
    returnValues.set(2, firstStack);*/
    returnValues.add(stackOptions);
    returnValues.add(topOfStacks);
    returnValues.add(firstStack);
    return returnValues;
  }

  int inRow(int candidate, int gridSize, int[][] stackOptions, int[] topOfStacks, int currentRow){
    int col, gridValue, currentStack;
    for(col = 0; col < gridSize; col++){
      currentStack = (currentRow * gridSize) + col;
      gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
      if(candidate == gridValue){
        return 1;
      }
    }
    return 0;
  }

  int inCol(int candidate, int gridSize, int[][] stackOptions, int[] topOfStacks, int currentCol){
    int row, gridValue, currentStack;
    for(row = 0; row < gridSize; row++){
      currentStack = (row * gridSize) + currentCol;
      gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
      if(candidate == gridValue){
        return 1;
      }
    }
    return 0;
  }

  int inSubgrid(int candidate, int gridSize, int[][] stackOptions, int[] topOfStacks, int startingCol, int startingRow){
    int row, col, gridValue, currentStack;
    int subgridSize = (int) Math.sqrt((double) gridSize);
    for(row = 0; row < subgridSize; row++){
      for(col = 0; col < subgridSize; col++){
        currentStack = ((startingRow + row) * gridSize) + (startingCol + col);
        gridValue = stackOptions[currentStack][topOfStacks[currentStack]];
        if(candidate == gridValue){
          return 1;
        }
      }
    }
    return 0;
  }

  /*NOTE TO GROUPMATES: dito niyo po ilagay yung pang solve ng puzzle :D*/
  void SolveRegular(int [][] puzzle){
    //insert code for checking regular puzzle here
    int x, row, col, candidate, solutionNo = 0;
    int noOfStacks = puzzle.length*puzzle.length;
    int gridSize = puzzle.length;
    int gridSizePlus = gridSize+1;
    int firstStack = -1, currentStack, backtrack;
    int usedInRow, usedInCol, usedInSubgrid;
    int currentRow, currentCol, startingRow, startingCol;
    int topOfStacks[] = new int[noOfStacks]; //array of the current top of each stack
    int stackOptions[][] = new int[noOfStacks][gridSizePlus]; //array of possible values for each stack
    int permanentStacks[] = new int[noOfStacks]; //array of the current top of each permanent stack
    List stackEssentials = new ArrayList(3);
    int subgridSize = (int) Math.sqrt((double) gridSize);

    //solution UI initializations
    List solutionsList = new <int[][]> ArrayList();
    int[][] solution;
    int srow, scol;

    //initialize permanentStacks topOfStacks to zero to remove trash value
    for(x = 0; x < noOfStacks; x++){
      permanentStacks[x] = topOfStacks[x] = 0;
    }

    //initialize stackOptions to zero to remove trash value
    for(row = 0; row < noOfStacks; row++){
      for(col = 0; col < gridSizePlus; col++){
        stackOptions[row][col] = 0;
      }
    }
    //initialize the stackOptions, topOfStacks and firstStack based on the given puzzle
    stackEssentials = initialize(puzzle, stackOptions, topOfStacks, firstStack);
    stackOptions = (int[][]) stackEssentials.get(0);
    topOfStacks = (int[]) stackEssentials.get(1);
    firstStack = (int) stackEssentials.get(2);

    for(x = 0; x < noOfStacks; x++){
      permanentStacks[x] = topOfStacks[x];
    }

    currentStack = firstStack;
    row = col = backtrack = 0;

    //fill the first stack
    for(candidate = gridSize; candidate >= 0; candidate--){
      if(candidate == 0 && topOfStacks[currentStack] == 0){
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
      usedInRow = inRow(candidate, gridSize, stackOptions, topOfStacks, currentRow);
      //check col for duplicates
      usedInCol = inCol(candidate, gridSize, stackOptions, topOfStacks, currentCol);
      //check subgrid for duplicates
      usedInSubgrid = inSubgrid(candidate, gridSize, stackOptions, topOfStacks, startingCol, startingRow);
      if(backtrack != 1 && usedInRow != 1 && usedInCol != 1 && usedInSubgrid != 1 && candidate != 0){
        stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
      }
    }

    currentStack++;

    while(topOfStacks[firstStack ] > 0){
      //fill the stackOptions
      if(currentStack < noOfStacks && backtrack == 0){
        if(permanentStacks[currentStack] != 1){
          for(candidate = gridSize; candidate >= 0; candidate--){
            if(candidate == 0 && topOfStacks[currentStack] == 0){
              backtrack = 1;
              currentStack = currentStack - 2;
              break;
            }
            //check current candidate as possible stackOption of the current stack
            currentRow = currentStack/gridSize;
            currentCol = currentStack%gridSize;
            startingRow = currentRow - (currentRow % subgridSize);
            startingCol = currentCol - (currentCol % subgridSize);

            //check row for duplicates
            usedInRow = inRow(candidate, gridSize, stackOptions, topOfStacks, currentRow);
            //check col for duplicates
            usedInCol = inCol(candidate, gridSize, stackOptions, topOfStacks, currentCol);
            //check subgrid for duplicates
            usedInSubgrid = inSubgrid(candidate, gridSize, stackOptions, topOfStacks, startingCol, startingRow);
            if(backtrack != 1 && usedInRow != 1 && usedInCol != 1 && usedInSubgrid != 1 && candidate != 0){
              stackOptions[currentStack][++topOfStacks[currentStack]] = candidate;
            }
          }
        }
        currentStack++;
      }else{  //backtrack
        if(currentStack == noOfStacks){
          solutionNo++;
          //System.out.println("\n Solution number: " + solutionNo);


          //set/reset srow and scol
          solution = new int[gridSize][gridSize];
          srow = -1;
          scol = 0;

          //print the solution
          for(x = 0; x < noOfStacks; x++){
            if(x%gridSize == 0){
              //System.out.println();
              srow++; //move to the next row
              scol = 0; //reset scol
            }
            //System.out.printf(" %d", stackOptions[x][topOfStacks[x]]);
            solution[srow][scol] = stackOptions[x][topOfStacks[x]]; //put the valid value to the solution table
            scol++; //move to the next column
          }
          //System.out.println();
          solutionsList.add(solution);

          //test print
          for(int count1 = 0; count1 < gridSize; count1++){
            System.out.println();
            for(int count2 = 0; count2 < gridSize; count2++){
              System.out.print(" " + solution[count1][count2]);
            }
          }
          System.out.println();

          currentStack--;
          backtrack = 1;
        }
        if(backtrack == 1 && permanentStacks[currentStack] != 1){
          stackOptions[currentStack][topOfStacks[currentStack]] = 0;
          topOfStacks[currentStack]--;
          if(topOfStacks[currentStack] >= 1){
            backtrack = 0;
            currentStack = currentStack+2;
          }
        }
        currentStack--;
      }
    }
    if(solutionNo == 0){
      //System.out.println("\n No possible solution.");
      JOptionPane.showMessageDialog(frame,"No possible solution.", "No Solution",    JOptionPane.PLAIN_MESSAGE);
    }else{
      createSolutionUI(solutionsList, gridSize, solutionNo);
    }
  }

  void createSolutionUI(List solutionsList, final int gridSize, int solutionNo){
    solutionPanel = new JPanel(new BorderLayout(10,10));
    solutionOption = new JPanel();
    solutionField = new JTextField[gridSize][gridSize];
    solutionButtons = new JButton[solutionNo];
    bLayout = new BoxLayout(solutionOption, BoxLayout.Y_AXIS);
    final JButton backToSolutions = new JButton("Back to Solutions");
    JButton backToGame = new JButton("Back to Game");
    JScrollPane scroll = new JScrollPane(solutionOption);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.setPreferredSize(new Dimension(600, 400));

    solutionOption.setLayout(bLayout);
    solutionMenu.add(backToGame);

    //adding actionlistener to backToSolutions button
    backToSolutions.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        card.show(c, "solutionMenu");
      }
    });

    //adding actionlistener to backtogame button
    backToGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        card.show(c, "gameplay");
      }
    });

    c.add("solutionPanel", solutionPanel);
    c.add("solutionMenu", solutionMenu);

    for(int solCount = 0; solCount < solutionNo; solCount++){
      solutionBoard = new JPanel();
      solution = (int[][]) solutionsList.get(solCount);
      solutionButtons[solCount] = new JButton("Solution " + (solCount+1));
      solutionButtons[solCount].putClientProperty("solution", solution);
      //add action listener
      solutionButtons[solCount].addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          solutionBoard.removeAll();
          solutionBoard.setLayout(new GridLayout(gridSize, gridSize));
          JButton button = (JButton) e.getSource();
          int [][] sol = (int [][]) button.getClientProperty("solution");
          for(int count1 = 0; count1 < gridSize; count1++){
            for(int count2 = 0; count2 < gridSize; count2++){
              solutionField[count1][count2] = new JTextField(3);
              solutionField[count1][count2].setFont(new Font("Arial", Font.BOLD, 20));
              solutionField[count1][count2].setHorizontalAlignment(JTextField.CENTER);
              solutionField[count1][count2].setText(Integer.toString(sol[count1][count2]));
              solutionField[count1][count2].setEditable(false);
              solutionBoard.add(solutionField[count1][count2]);
            }
          }
          solutionPanel.add(solutionBoard, BorderLayout.CENTER);
          solutionPanel.add(backToSolutions, BorderLayout.PAGE_START);
          card.show(c, "solutionPanel");
        }
      });
      solutionOption.add(solutionButtons[solCount]);
    }
    solutionMenu.add(scroll);
    card.show(c, "solutionMenu");
  }

  void SolveX(int [][] puzzle){
    //insert code for checking X puzzle here
  }
  void SolveY(int [][] puzzle){
    //insert code for checking Y puzzle here
  }
  void SolveXY(int [][] puzzle){
    //insert code for checking XY puzzle here
  }
}
