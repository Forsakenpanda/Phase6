package TestSuits;

import static org.junit.Assert.*;
import org.junit.Test;
import Default.Account;

public class Account_getFee_Test {
		
	@Test
	public void getStudentFee() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		assertEquals(0.05, a.getFee(true), 0);
	}
	
	@Test
	public void getNormalFee() {
		Account a = new Account(1, "Jason", true, 100.0, 'N', 0);
		assertEquals(0.1, a.getFee(true), 0);
	}
	
	@Test
	public void getAdminNormalFee() {
		Account a = new Account(1, "Jason", true, 100.0, 'N', 0);
		assertEquals(0.0, a.getFee(false), 0);
	}
	
	@Test
	public void getAdminStudentFee() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		assertEquals(0.0, a.getFee(false), 0);
	}
}