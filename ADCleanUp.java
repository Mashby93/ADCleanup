import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.lang.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 *
 * @author Michael.Ashby
 */
public class ADCleanUp {
List<String> Ref= new ArrayList<String>();
PingThread t1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ADCleanUp inst = new ADCleanUp();
        inst.inputs();
        inst.Pingers();
    }
     public void inputs(){
      /**Read the output.txt file line by line, for each hostname.
        *Add each hostname to Ref (References) arraylist */
        File file = new File("Output.txt");
        boolean f = false;
        Path p = null;
        try {
      FileInputStream fis = new FileInputStream(file);
      DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
      while ((strLine = br.readLine()) != null) {
        Ref.add(strLine);
      }//end while
      fis.close();
    }//end try
        catch (IOException e) {
      e.printStackTrace();
    }//end catch
    
    try{file = file.getAbsoluteFile();
    p = file.toPath();
    Files.delete(p);
     }
    catch(Exception d){System.out.print(d);}
    }//end input

     public void Pingers(){
     for(int i=0;i<Ref.size();i++){
       t1 = new PingThread(Ref.get(i));
       t1.start();
       }
       try{t1.join();}
       catch(InterruptedException e){}
}//End Pingers
     
}//End ADCleanUp class

class PingThread extends Thread {
   List<String> Con = new ArrayList<String>();
    String threadname;
    public PingThread(String name){
    threadname=name;
    }

   public void run() {
   
   try{
   Runtime rt = Runtime.getRuntime();
   rt.exec("cmd /c ping "+threadname + " > " + threadname + ".tmp");
   }//end try
   catch(IOException c){}
   try{Thread.sleep(8000);}
   catch(Exception c){}
     readFile();
   }//end run
   
   public void readFile(){
   File file = new File(threadname+".tmp");
        try {
      FileInputStream fis = new FileInputStream(file);
      DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
      while ((strLine = br.readLine()) != null) {
        Con.add(strLine);
      }//end while
      fis.close();
    }//end try
        catch (IOException e) {
      e.printStackTrace();
    }//end catch
   readLN();
   }//End readFile
   
  public void readLN(){
  int i = 0;
  int found=0;
  //Check if Output file already exists, if not create it.
  File Complete = new File("Output.txt");
  Path filePath = FileSystems.getDefault().getPath("Output.txt");
  File tmp = new File(threadname+".tmp");
  Path p;
  if(!Complete.exists()){
  
  try{Files.createFile(filePath);}catch(IOException c){}
  }
  if(Con.size() >5){
  String read = Con.get(8);
  
  char[] charArray = read.toCharArray();
  found =  Character.getNumericValue(charArray[34]);
  }else{found =0;}
  if(found ==0){

  try(FileWriter fw = new FileWriter("Output.txt",true);
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter Writer = new PrintWriter(bw)){
     Writer.println(threadname);
 }
catch(IOException e){System.err.println(e);}

}//end if
try{
    tmp = tmp.getAbsoluteFile();
    p = tmp.toPath();
    Files.delete(p);
}//end try
catch(Exception d){System.out.print(d);}

  }//end readLN
  
}// End Thread class