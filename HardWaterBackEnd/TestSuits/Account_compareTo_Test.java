package TestSuits;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Default.Account;

public class Account_compareTo_Test {
	public Account a,b;
	@Before
	public void addAccounts() {
		a = new Account(1, "Jason", true, 100.0, 'S', 0);
		b = new Account(2, "Stuart", true, 100.0, 'S', 0);
	}
	
	@Test
	public void compareAtoB() {
		assertEquals(-1, a.compareTo(b));
	}
	
	@Test
	public void compareBtoA() {
		assertEquals(1, b.compareTo(a));
	}
}
