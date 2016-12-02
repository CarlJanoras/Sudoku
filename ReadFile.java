import java.io.*;
import javax.swing.*;
import java.util.*;

public class ReadFile{
   int nPuzzles;
   int [] subgrids;
   LinkedList <int [][]> initPuzzles = new LinkedList <int [][]>();
   int [][] puzzle;
   
   ReadFile() throws Exception{ //reading input from file
    BufferedReader br = new BufferedReader(new FileReader(new File("in.txt")));
    String line = null;
    int counter = 1;//for line number
    int size = 0;
    int x = 0;
    int i=0, j=0;
    
    while((line = br.readLine()) != null){
      if(counter == 1){//get number of input puzzles from first line
        nPuzzles = Integer.parseInt(line);
        subgrids = new int[nPuzzles];
        //System.out.println("number of Puzzles: " + subgrids.length);
      }else{
        StringTokenizer st = new StringTokenizer(line, " ");
        if(st.countTokens() ==1){ //if there is only one character in the current line
          int n = Integer.parseInt(st.nextToken());
          subgrids[x] = n;
          puzzle= new int[n*n][n*n];
          
          if(puzzle != null) initPuzzles.add(puzzle); //add initial puzzle
          
          
          x++;
          i=0;
        }else if(st.countTokens() ==0){ //if current line only contains whitespace         
        }else{ //get values for initial puzzle input
          while(st.hasMoreTokens()){
            int n = Integer.parseInt(st.nextToken());
            puzzle[i][j] = n;
            j++;
          }
          i++;
          j=0;
        }
      }
        
     counter++;
      //System.out.println(line);
    }
    br.close();
  }
  
  //input getters
  int getNPuzzles(){
    return nPuzzles;
  }
  int [] getSubgrids(){
    return subgrids; 
  }
  LinkedList <int [][]> getInitPuzzles(){
    return initPuzzles;
  }
  
}
