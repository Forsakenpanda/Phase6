package TestSuits;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;
public class TransactionHandler_create_Test {
	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Test
	public void correctlyAddsAccount () {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(true,handler.create("Jason1", 3, 100, "  "));
		accounts = handler.getAccounts();
		assertEquals("Jason1", accounts.get(0).getName());
		assertEquals(3, accounts.get(0).getAccountNum());
		assertEquals(100, accounts.get(0).getFunds(),0);
		assertEquals('N', accounts.get(0).getPlan());
	}
	
	@Test
	public void doesntCreateExistingAccount() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		assertEquals(false,handler.create("Jason1", 1, 100, "  "));
		assertEquals(1,handler.getAccounts().size());
	}
	
	@Test
	public void doesntCreateForStandard() {
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		assertEquals(false,handler.create("Jason1", 1, 100, "  "));
		assertEquals(0,handler.getAccounts().size());
	}
	
}
