package TestSuits;

import Default.Account;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.*;

public class Account_changePlan_Test {

	public Account account;
	final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
	final PrintStream store = System.out;
	
	
	@Before
	public void addAccounts() {
		System.setOut(new PrintStream(myOut));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(store);
	}
	
	@Test
	public void changeToStudent() {
		account = new Account(1, "Stuart", true, 100.00, 'N', 0);
		account.changePlan();
		assertEquals(account.getPlan(), 'S');
	}
	
	@Test
	public void changeToNonStudent() {
		account = new Account(1, "Stuart", true, 100.00, 'S', 0);
		account.changePlan();
		assertEquals(account.getPlan(), 'N');
	}
	
	@Test
	public void changePlanDisable() {
		account = new Account(1, "Stuart", false, 100.00, 'S', 0);
		account.changePlan();
		assertEquals(myOut.toString().trim(), "ERROR: Account: 1 is disabled");
	}
}
