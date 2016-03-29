package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_delete_Test {

	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
		accounts.add(new Account(3, "Hon", false, 100.00, 'S', 0));
	}
	
	@Test
	public void deleteAccount() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(true, handler.delete(0));
		assertEquals(accounts.size(), 2);
		assertEquals(2, accounts.get(0).getAccountNum());
	}
	
	@Test
	public void deleteAllAccounts() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(true, handler.delete(0));
		assertEquals(true, handler.delete(0));
		assertEquals(true, handler.delete(0));
		assertEquals(accounts.size(), 0);
	}
	
	@Test
	public void failDelete() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		assertEquals(false, handler.delete(0));
		assertEquals(accounts.size(),3);
		assertEquals(1, accounts.get(0).getAccountNum());
	}
}
