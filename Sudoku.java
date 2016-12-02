import java.io.*;
import javax.swing.*;
import java.util.*;

public class Sudoku{
  static int nPuzzles; //number of puzzles
  static int [] subgrids; //array for size of subgrids of every puzzles
  static LinkedList <int[][]> initPuzzles = new LinkedList<int[][]>();
  
  public static void main(String [] args) throws Exception{
      //read input file and get data
      ReadFile rf = new ReadFile();
      nPuzzles = rf.getNPuzzles();
      subgrids = rf.getSubgrids();
      initPuzzles = rf.getInitPuzzles();
      
      //System.out.println("nPuzzles: " + nPuzzles + "\nsubgrids:" + subgrids.length);
      
    Sudoku_UI ui = new Sudoku_UI(nPuzzles,subgrids,initPuzzles); //initialize ui for gameplay
      
  }
  
}
