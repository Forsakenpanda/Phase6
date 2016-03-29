package TestSuits;

import Default.Account;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.*;

public class Account_isEnabled_Test {
	public Account account;
	final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
	final PrintStream store = System.out;
	
	@Test
	public void enableAccount() {
		account = new Account(1, "Stuart", false, 100.00, 'N', 0);
		assertEquals(account.modifyEnable(true), true);
		assertEquals(account.isEnabled(), true);
	}
	
	@Test
	public void disableAccount() {
		account = new Account(1, "Stuart", true, 100.00, 'N', 0);
		assertEquals(account.modifyEnable(false), true);
		assertEquals(account.isEnabled(), false);
	}
	
	@Test
	public void failedChange() {
		account = new Account(1, "Stuart", true, 100.00, 'N', 0);
		assertEquals(account.modifyEnable(true), false);
		assertEquals(account.isEnabled(), true);
	}
	
}
