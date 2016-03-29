package TestRunners;
import TestSuits.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TransactionReaderRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(TransactionReader_Constructor_Test.class, TransactionReader_getNext_Test.class, 
    		  TransactionReader_hasNext_Test.class, TransactionReader_peek_Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  
