import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;

public class Sudoku_UI{
  JFrame frame = new JFrame("Sudoku"); //main frame
  JFrame solutions = new JFrame("Solution");
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
  
  Sudoku_UI(int nPuzzles,final int [] subgrids, final LinkedList <int [][]> initPuzzles){
    //assigning input data to variables
    this.nPuzzles = nPuzzles;
    this.subgrids = subgrids;
    this.initPuzzles = initPuzzles;
    
    /*DRAWING GUI*/
    c = frame.getContentPane(); //setting the container as the content of JFrame
    card = new CardLayout(10,10); //initializing card layout with 10 x 10 space
    c.setLayout(card); // setting layout of container
    
    //initialization of GUI components
    JPanel mainMenu = new JPanel(); //for main menu
    optionMenu = new JPanel();
    gameplay = new JPanel(new BorderLayout(10,10));
    option = new JPanel();
    JPanel solve = new JPanel(new GridLayout(1,4));
    JButton play = new JButton("PLAY");
    JButton backToMain = new JButton("Back to Main Menu");
    JButton backToOption = new JButton("Back to Puzzle Options");
    JButton solveRegular = new JButton("Solve Regular Sudoku");
    JButton solveX = new JButton("Solve X Sudoku");
    JButton solveY = new JButton("Solve Y Sudoku");
    JButton solveXY = new JButton("Solve XY Sudoku");
    JButton [] sizeButtonMenu = new JButton[subgrids.length];
    BoxLayout bl = new BoxLayout(option, BoxLayout.Y_AXIS);
    
    /*EDITING COMPONENTS*/
    play.setFont(new Font("Arial",Font.BOLD, 12));
    play.setAlignmentY(Component.CENTER_ALIGNMENT);
    backToMain.setFont(new Font("Arial",Font.BOLD, 12));
    backToOption.setFont(new Font("Arial",Font.BOLD, 12));
    
    //setting layouts
    option.setLayout(bl);
    
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
          System.out.println(pos);
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
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(!(jtf[j][k].getText()).equals(""))
            	puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
          }
        }
        SolveX(puzzle);
      }
    });
    solveY.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(!(jtf[j][k].getText()).equals(""))
            	puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
          }
        }
        SolveY(puzzle);
      }
    });
    solveXY.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(!(jtf[j][k].getText()).equals(""))
            	puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
          }
        }
        SolveXY(puzzle);
      }
    });
    solveRegular.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        int [][] puzzle = new int [jtf.length][jtf.length];
        for(int j=0; j < puzzle.length; j++){
          for(int k=0; k < puzzle.length; k++){
            if(!(jtf[j][k].getText()).equals(""))
            	puzzle [j][k] = Integer.parseInt(jtf[j][k].getText());
          }
        }
        SolveRegular(puzzle);
      }
    });
    
    /*ADDING COMPONENTS TO PANELS*/
    mainMenu.add(play);
    optionMenu.add(option);
    solve.add(solveRegular);
    solve.add(solveX);
    solve.add(solveY);
    solve.add(solveXY);
    gameplay.add(backToOption, BorderLayout.PAGE_START);
    gameplay.add(solve, BorderLayout.PAGE_END);
    optionMenu.add(backToMain);
    
    /*ADDING PANELS TO CONTAINER AND ASSIGNING KEYWORD TO IMPLEMENT CARD LAYOUT*/
    c.add("mainMenu", mainMenu);
    c.add("gameplay", gameplay);
    c.add("optionMenu", optionMenu);
    
    frame.setSize(700,500);
    //frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  /*NOTE TO GROUPMATES: dito niyo po ilagay yung pang solve ng puzzle :D*/
  void SolveRegular(int [][] puzzle){
    //insert code for checking regular puzzle here
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
