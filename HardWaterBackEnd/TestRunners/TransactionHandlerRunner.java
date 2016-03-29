package TestRunners;
import TestSuits.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TransactionHandlerRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(TransactionHandler_changePlan_Test.class, TransactionHandler_create_Test.class,
    		  TransactionHandler_delete_Test.class, TransactionHandler_deposit_Test.class, TransactionHandler_disableEnable_Test.class,
    		  TransactionHandler_findAccount_Test.class, TransactionHandler_login_Test.class, TransactionHandler_paybill_Test.class, 
    		  TransactionHandler_transfer_Test.class, TransactionHandler_withdrawal_Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
   }
}  
