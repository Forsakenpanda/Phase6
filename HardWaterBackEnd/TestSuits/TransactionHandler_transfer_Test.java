package TestSuits;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Default.Account;
import Default.TransactionHandler;

public class TransactionHandler_transfer_Test {

	public ArrayList<Account> accounts = new ArrayList<Account>();
	
	@Before
	public void addAccounts() {
		accounts.add(new Account(1, "Jason", true, 100.00, 'N', 0));
		accounts.add(new Account(2, "Stuart", true, 100.00, 'S', 0));
	}
	
	@Test
	public void successfulStudentTransfer() {
		String nextTransaction = "02 Jason Runzer         00001 00010.00 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 10.00;
		double fee = 0.05;
		assertEquals(true, handler.transfer(1, funds, "F ", nextTransaction));
		assertEquals(100.0 - funds - fee, handler.getAccounts().get(1).getFunds(), 0);
		assertEquals(100.0 + funds, handler.getAccounts().get(0).getFunds(), 0);
	}
	
	@Test
	public void successfulNormalTransfer() {
		String nextTransaction = "02 Stuart               00002 00010.00 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 10.00;
		double fee = 0.10;
		assertEquals(true, handler.transfer(0, funds, "F ", nextTransaction));
		assertEquals(100.0 - funds - fee, handler.getAccounts().get(0).getFunds(), 0);
		assertEquals(100.0 + funds, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void successfulAdminTransfer() {
		String nextTransaction = "02 Jason Runzer         00001 00010.00 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		double funds = 10.00;
		double fee = 0.00;
		assertEquals(true, handler.transfer(1, funds, "F ", nextTransaction));
		assertEquals(100.0 - funds - fee, handler.getAccounts().get(1).getFunds(), 0);
		assertEquals(100.0 + funds, handler.getAccounts().get(0).getFunds(), 0);
	}
	
	@Test
	public void doubleToTransfer() {
		String nextTransaction = "02 Stuart               00002 00010.00 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 10.00;
		assertEquals(false, handler.transfer(0, funds, "T ", nextTransaction));
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
		assertEquals(100.0, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void doubleFromTransfer() {
		String nextTransaction = "02 Stuart               00002 00010.00 F ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 10.00;
		assertEquals(false, handler.transfer(0, funds, "F ", nextTransaction));
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
		assertEquals(100.0, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void overMaxTransfer() {
		accounts.add(new Account(3, "Max", true, 99999.99, 'N', 0));
		String nextTransaction = "02 Jason Runzer         00001 99899.01 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 99900.0;
		boolean success =  handler.transfer(2, funds, "F ", nextTransaction);
		assertEquals(false, success);
		assertEquals(99999.99, handler.getAccounts().get(2).getFunds(), 0);
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
	}
	
	@Test
	public void notEnoughFunds() {
		String nextTransaction = "02 Stuart               00002 00100.01 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		double funds = 100.01;
		boolean success =  handler.transfer(0, funds, "F ", nextTransaction);
		assertEquals(false, success);
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
		System.out.println(handler.getAccounts().get(1).getFunds());
		assertEquals(100.0, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void secondAccountDoesntExist() {
		String nextTransaction = "02 Lololo               00005 00100.01 T ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		double funds = 10;
		boolean success =  handler.transfer(0, funds, "F ", nextTransaction);
		assertEquals(false, success);
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
	}
	
	@Test
	public void sourceSecond() {
		String nextTransaction = "02 Jason Runzer         00001 00010.00 F ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 10.00;
		double fee = 0.10;
		assertEquals(true, handler.transfer(1, funds, "T ", nextTransaction));
		assertEquals(100.0 - funds - fee, handler.getAccounts().get(0).getFunds(), 0);
		assertEquals(100.0 + funds, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void sourceSecondnotEnoughFunds() {
		String nextTransaction = "02 Stuart               00002 00100.01 F ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "A ");
		double funds = 100.01;
		boolean success =  handler.transfer(0, funds, "T ", nextTransaction);
		assertEquals(false, success);
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
		assertEquals(100.0, handler.getAccounts().get(1).getFunds(), 0);
	}
	
	@Test
	public void sourceSecondOverMaxTransfer () {
		accounts.add(new Account(3, "Max", true, 99999.99, 'N', 0));
		String nextTransaction = "02 Jason Runzer         00001 99899.01 F ";
		TransactionHandler handler = new TransactionHandler(accounts);
		handler.login(0, "S ");
		double funds = 99900.0;
		boolean success =  handler.transfer(2, funds, "T ", nextTransaction);
		assertEquals(false, success);
		assertEquals(99999.99, handler.getAccounts().get(2).getFunds(), 0);
		assertEquals(100.0, handler.getAccounts().get(0).getFunds(), 0);
	}
}
