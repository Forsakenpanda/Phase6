package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;

public class Account_enableDisable_Test {
		
	@Test
	public void checkEnable() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		assertEquals(a.isEnabled(),true);
	}
	
	@Test
	public void checkDisable() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		a.modifyEnable(false);
		assertEquals(a.isEnabled(),false);
	}
}