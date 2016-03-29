package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_deposit_Test {
	
	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
	}
	
	@Test
	public void correctStandardNormalFee() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double depositFunds = 10.00;
		int accountIndex = 0;
		assertEquals(true, handler.deposit(accountIndex, depositFunds));
		double remainingFunds = 100 + depositFunds - 0.1;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(),0);
	}
	
	@Test
	public void correctStandardStudentFee() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		double depositFunds = 10.00;
		int accountIndex = 1;
		assertEquals(true, handler.deposit(accountIndex, depositFunds));
		double remainingFunds = 100 + depositFunds - 0.05;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(),0);
	}
	
	@Test
	public void correctAdminNormalFee() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		double depositFunds = 10.00;
		int accountIndex = 0;
		assertEquals(true, handler.deposit(accountIndex, depositFunds));
		double remainingFunds = 100 + depositFunds;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(),0);
	}
	
	@Test
	public void correctAdminStudentFee() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		double depositFunds = 10.00;
		int accountIndex = 1;
		assertEquals(true, handler.deposit(accountIndex, depositFunds));
		double remainingFunds = 100 + depositFunds;
		assertEquals(remainingFunds, accounts.get(accountIndex).getFunds(),0);
	}
	
}
