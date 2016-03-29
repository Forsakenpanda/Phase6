package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_withdrawal_Test {

	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
	}
	
	@Test
	public void succesfullAdminNormalPlanWithdrawal() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		double withdrawalFunds = 10.00;
		int accountIndex = 0;
		handler.withdrawal(accountIndex, withdrawalFunds);
		double remainingFunds = 100 - withdrawalFunds;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(),0);
	}
	
	@Test
	public void successfullStandardNormalPlanWithdrawal() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double withdrawalFunds = 10.00;
		int accountIndex = 0;
		handler.withdrawal(accountIndex, withdrawalFunds);
		double remainingFunds = 100 - withdrawalFunds - 0.10;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(), 0);
	}
	
	@Test
	public void successfullStandardStudentPlanWithdrawal() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double withdrawalFunds = 10.00;
		int accountIndex = 1;
		handler.withdrawal(accountIndex, withdrawalFunds);
		double remainingFunds = 100 - withdrawalFunds - 0.05;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(), 0);
	}
	
	
	
	
	@Test
	public void zeroWithdrawalStandardStudent() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double withdrawalFunds = 0;
		int accountIndex = 1;
		handler.withdrawal(accountIndex, withdrawalFunds);
		double remainingFunds = 100;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(), 0);
	}
	
	@Test
	public void zeroWithdrawalStandardNormal() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double withdrawalFunds = 0;
		int accountIndex = 0;
		handler.withdrawal(accountIndex, withdrawalFunds);
		double remainingFunds = 100;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(), 0);
	}	
}
