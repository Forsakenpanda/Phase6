package TestSuits;

import static org.junit.Assert.*;
import org.junit.Test;
import Default.Account;

public class Account_getPlan_Test {
		
	@Test
	public void getPlan() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		assertEquals('S', a.getPlan());
	}
}