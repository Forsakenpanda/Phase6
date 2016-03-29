package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_findAccount_Test {
	
	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'N', 0));
	}
	
	@Test
	public void findsCorrectAccount() {
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(1, handler.findAccount(2));
	}
	
	@Test
	public void zeroAccounts() {
		accounts.removeAll(accounts);
		assertEquals(accounts.size(),0);
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(handler.findAccount(1),-1);
	}
	
	@Test
	public void oneAccounts() {
		accounts.remove(1);
		assertEquals(accounts.size(),1);
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(handler.findAccount(1),0);	
	}
	
	@Test 
	public void twoAccounts() {
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(handler.findAccount(1),0);	
		assertEquals(handler.findAccount(2),1);	
	}
	
	@Test
	public void manyAccounts() {
		accounts.add(new Account(3, "Account3", true, 100.00, 'N', 0));
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(handler.findAccount(1),0);	
		assertEquals(handler.findAccount(2),1);	
		assertEquals(handler.findAccount(3), 2);
	}
	
	@Test
	public void accountDoesNotExist() {
		TransactionHandler handler = new TransactionHandler(accounts);
		assertEquals(handler.findAccount(4),-1);
	}
}
