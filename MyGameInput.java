package yahtzee;
import java.io.Serializable;

public class MyGameInput  implements Serializable
{
    private static final long serialVersionUID = 1L;
// command options
   static final int NO_COMMAND=-1;
   static final int JOIN_GAME=1;
   static final int ROLL_DICE = 2;
   static final int KEEP_DIE = 3;
   static final int RELEASE_DIE = 4;
   static final int CHECK_SCORE = 5;
   static final int PASS_SCORE = 6;
   static final int DISCONNECTING=7;
   static final int RESETTING=8;
   
   int command=NO_COMMAND;
   
   String myName;
   int index = -1;
   
   
   public void setName(String name)
   {
       myName=name;
   }
}