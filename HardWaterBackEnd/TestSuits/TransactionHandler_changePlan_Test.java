package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_changePlan_Test {

	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
		accounts.add(new Account(3, "Hon", false, 100.00, 'S', 0));
	}
	
	@Test
	public void changeFromStudent() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(true, handler.changePlan(0));
		assertEquals('S', accounts.get(0).getPlan());
	}
	
	@Test
	public void changeFromNonStudent() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		assertEquals(true, handler.changePlan(1));
		assertEquals('N', accounts.get(1).getPlan());
	}
	
	@Test
	public void failureChange() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(false, handler.changePlan(2));
		assertEquals('S', accounts.get(2).getPlan());
	}
}
