package TestRunners;
import TestSuits.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class AccountWriterRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(AccountWriter_Constructor_Test.class, AccountWriter_formatTFString_Test.class,
    		  AccountWriter_formatTFString_Test.class, AccountWriter_write_Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  
