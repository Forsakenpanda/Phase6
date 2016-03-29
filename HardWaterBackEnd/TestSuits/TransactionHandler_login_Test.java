package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_login_Test {
	
	
	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
	}
	
	@Test
	public void standardLogin() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(handler.findAccount(1), "S ");
		assertEquals(handler.getStandardSession(), true);
	}
	
	@Test
	public void adminLogin() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(-1, "A ");
		assertEquals(handler.getStandardSession(), false);
	}
}
