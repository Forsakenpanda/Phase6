package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_paybill_Test {

	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
	}
	
	@Test
	public void checkEC() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(handler.paybill(0, 50, "EC"), true);
		assertEquals(50, accounts.get(0).getFunds(),0);
	}
	
	@Test
	public void checkCP() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(true, handler.getStandardSession());
		assertEquals(handler.paybill(0, 50, "TV"), true);
		assertEquals(49.90, accounts.get(0).getFunds(),0);
	}
	
	@Test
	public void checkTV() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(handler.paybill(0, 50, "TV"), true);
		assertEquals(50, accounts.get(0).getFunds(),0);
	}
	
	@Test
	public void notEnoughMoney() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(handler.paybill(0, 110, "EC"), false);
		assertEquals(100, accounts.get(0).getFunds(),0);
	}
	
	@Test
	public void comanyFalse() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(handler.paybill(0, 50, "AS"), false);
		assertEquals(100, accounts.get(0).getFunds(),0);
	}
}
