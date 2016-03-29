package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;

public class Account_getName_Test {
		
	@Test
	public void getName() {
		Account a = new Account(1, "Jason", true, 100.0, 'S', 0);
		assertEquals(a.getName(),"Jason");
	}
}