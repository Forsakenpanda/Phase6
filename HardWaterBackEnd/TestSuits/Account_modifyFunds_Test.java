package TestSuits;

import Default.Account;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import java.io.*;

public class Account_modifyFunds_Test {
	public Account account;
	final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
	final PrintStream store = System.out;
	
	
	@Before
	public void addAccounts() {
		account = new Account(1, "Jason", true, 100.00, 'N', 0);
		System.setOut(new PrintStream(myOut));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(store);
	}
	
	@Test
	public void correctlyAddFundsNoFee() {
		double funds = 10.00;
		double fee = 0;
		double result = funds - fee + 100;
		assertEquals(true, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void correctlyAddFundsFee() {
		double funds = 10.00;
		double fee = 0.05;
		double result = funds - fee + 100;
		assertEquals(true, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void boundaryAddOverLimitNoFee() {
		double funds = 99900.00;
		double fee = 0.00;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
		assertEquals(myOut.toString().trim(), "ERROR: transaction would exceed maximum funds");
	}
	
	@Test
	public void boundaryAddOverLimitFee() {
		double funds = 99900.10;
		double fee = 0.10;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
		assertEquals(myOut.toString().trim(), "ERROR: transaction would exceed maximum funds");
	}
	
	@Test
	public void boundaryAddnegative() {
		double funds = -1.00;
		double fee = 0.10;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void boundaryAddFeeEqualsDeposit() {
		double funds = 0.10;
		double fee = 0.10;
		double result = 100;
		assertEquals(true, account.modifyFunds(funds, fee, true));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void correctlyWithdrawalnoFee() {
		double funds = 10.00;
		double fee = 0.00;
		double result = 100 - funds - fee;
		assertEquals(true, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void correctlyWithdrawalFee() {
		double funds = 10.00;
		double fee = 0.10;
		double result = 100 - funds - fee;
		assertEquals(true, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void boundaryWithdrawalOverFundsFee() {
		double funds = 99.91;
		double fee = 0.10;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
		assertEquals(myOut.toString().trim(), "ERROR: not enough funds");
	}
	
	@Test
	public void boundaryWithdrawalOverFundsnoFee() {
		double funds = 100.01;
		double fee = 0.00;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
		assertEquals(myOut.toString().trim(), "ERROR: not enough funds");
	}
	
	@Test
	public void withdrawalNegative() {
		double funds = -1.00;
		double fee = 0.00;
		double result = 100;
		assertEquals(false, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
	}
	
	@Test
	public void withdrawalDisabled() {
		double funds = 10.00;
		double fee = 0.10;
		double result = 100;
		account.modifyEnable(false);
		assertEquals(false, account.modifyFunds(funds, fee, false));
		assertEquals(result, account.getFunds(), 0);
	}
}
