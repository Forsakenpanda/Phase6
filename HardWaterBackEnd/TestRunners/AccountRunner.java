package TestRunners;
import TestSuits.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class AccountRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(Account_changePlan_Test.class, Account_compareTo_Test.class,  
    		  Account_enableDisable_Test.class, Account_getAccountNum_Test.class, 
    		  Account_getCounter_Test.class, Account_getFee_Test.class,  Account_getFunds_Test.class, 
    		  Account_getName_Test.class, Account_getPlan_Test.class, 
    		  Account_isEnabled_Test.class, Account_modifyFunds_Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  
