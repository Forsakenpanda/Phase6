package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_disableEnable_Test {
	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", false, 100.00, 'S', 0));
	}
	
	@Test
	public void enable() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(true,handler.disableEnable(1, true));
		assertEquals(true, handler.getAccounts().get(1).isEnabled());
	}
	
	@Test
	public void disable() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(true,handler.disableEnable(0, false));
		assertEquals(false, accounts.get(1).isEnabled());
	}
	
	@Test
	public void enableAlreadyEnabled() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(false ,handler.disableEnable(0, true));
		assertEquals(true, accounts.get(0).isEnabled());
		
	}
	
	@Test
	public void disableAlreadyDisabled() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false, handler.getStandardSession());
		assertEquals(false ,handler.disableEnable(1, false));
		assertEquals(false, accounts.get(1).isEnabled());
	}
}
