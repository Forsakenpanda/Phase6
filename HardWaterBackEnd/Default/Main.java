package Default;
/**
* Copyright 2016 Jason, Stuart, Muhammad copyright
*/
/**
* The backend of the Bank of Hard Water is compiled with the following command:
* 	javac Main.java
* This program can be run with the command:
*	java Main
* After running the program it will merge all the transactions files that have 
* been created on the front end and merge them together making one massive transaction file.
* Then it applies all the transactions to the current account file and master account file.
* This program is intended to be a robust banking backend that does not crash
* and provides useful feedback to the user.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The starting class for the back end.
 * <p>
 * This class delegates tasks to other objects, and starts the processing of the
 * transaction files.
 * <p>
 * 
 * @author HardWater
 *
 */
public class Main {
	private static ArrayList<Account> accounts;
	private static boolean masterAccountsFound;
	private static TransactionReader tr;
	private static TransactionHandler handler;

	/**
	 * Starts the back end by initializing the accounts and then reading the
	 * transactions.
	 * 
	 * @param args
	 *            no command line arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		accounts = new ArrayList<Account>();
		FileReader accountFileReader;
		boolean masterAccountsFileFound = false;
		// initialize accounts
		try {
			String line;
			// add the accounts into the system
			// try to read from the master
			File f = new File("../Application/Accounts/master-valid-accounts.txt");
			if(f.exists() && !f.isDirectory()) { 
			    // do something
				 accountFileReader = new FileReader ("../Application/Accounts/master-valid-accounts.txt");
				 masterAccountsFileFound = true;
			} else {
				accountFileReader = new FileReader("../Application/Accounts/current-valid-accounts.txt");
			}
			
			BufferedReader accountBufferedReader = new BufferedReader(accountFileReader);
			while ((line = accountBufferedReader.readLine()) != null) {
				// add the new account
				if(!line.substring(0,5).equals("00000")){
					if(!masterAccountsFileFound)
						accounts.add(new Account(Integer.parseInt(line.substring(0, 5)), line.substring(6, 27),
								line.charAt(27) == 'A', Double.valueOf(line.substring(29, 37)), line.charAt(38), 0));
					else {
						accounts.add(new Account(Integer.parseInt(line.substring(0, 5)), line.substring(6, 27),
								line.charAt(27) == 'A', Double.valueOf(line.substring(29, 37)), line.charAt(43), 
								Integer.valueOf(line.substring(39,42))));
					}
				}
			}
			accountBufferedReader.close();
		} catch (IOException e) {
			Main.reportError("something went wrong with the account reader");
		}

		tr = new TransactionReader("../Application/TF/");
		handler = new TransactionHandler(accounts);
		while(tr.hasNext()){
			dispatch(tr.getNext());
		}

		accounts = handler.getAccounts();
		AccountWriter aw = new AccountWriter("../Application/Accounts/", false);
		aw.write(accounts);
		AccountWriter ca = new AccountWriter("../Application/Accounts/", true);
		ca.write(accounts);
		//TO DO: delete all transaction files
	}

	

	/**
	 * Prints the error message.
	 * 
	 * @param msg
	 *            the error message.
	 */
	public static void reportError(String msg) {
		System.out.println("ERROR: " + msg);
	}

	/**
	 * Splits up the transaction into an array.
	 * <p>
	 * At each index: 0 - transaction code 1 - Name 2 - Account number 3 - Funds
	 * involved 4 - Misc information
	 * <p>
	 * 
	 * @param transaction
	 *            the transaction string to split.
	 * @return the split up transaction.
	 */
	public static String[] splitTransaction(String transaction) {
		if(transaction.length() != 41){
			Main.reportError("transaction string not correct length");
			return null;
		}
		String[] splitArray = new String[5];
		splitArray[0] = transaction.substring(0, 2);
		splitArray[1] = transaction.substring(3, 24);
		splitArray[2] = transaction.substring(24, 29);
		splitArray[3] = transaction.substring(30, 38);
		splitArray[4] = transaction.substring(39, 41);
		return splitArray;
	}

	

	/**
	 * Dispatches the transaction to the correct handler.
	 * @param s the transaction string.
	 */
	public static boolean dispatch(String s) {
		if (s.length() != 41) {
			System.out.println(s.length());
			reportError("transaction string not correct length");
			return false;
		}
		String[] splitTrans = splitTransaction(s);
		// the index of the account for the transaction
		int indexVal;
		int transCode = Integer.parseInt(splitTrans[0]);
		indexVal = handler.findAccount(Integer.parseInt(splitTrans[2]));
		// check if the account exists and bypass if logging in or out or when creating an account
		if (indexVal == -1 && transCode != 5 && transCode != 10 && transCode != 00) {
			reportError("acount does not exist");
			return false;
		}
		if (transCode == 10)
			return handler.login(indexVal, splitTrans[4]);
		else if (transCode == 1)
			return handler.withdrawal(indexVal, Double.parseDouble(splitTrans[3]));
		else if (transCode == 2) {
			if (!tr.hasNext() && !tr.peek().substring(0, 3).equals("02")) {
				Main.reportError("invalid transfer transactions");
				return false;
			}
			return handler.transfer(indexVal, Double.parseDouble(splitTrans[3]), splitTrans[4], tr.getNext());
		}
		else if (transCode == 3)
			return handler.paybill(indexVal, Double.parseDouble(splitTrans[3]), splitTrans[4]);
		else if (transCode == 4)
			return handler.deposit(indexVal, Double.parseDouble(splitTrans[3]));
		else if (transCode == 5)
			return handler.create(splitTrans[1] ,Integer.parseInt(splitTrans[2]), Double.parseDouble(splitTrans[3]), splitTrans[4]);
		else if (transCode == 6)
			return handler.delete(indexVal);
		else if (transCode == 7) 
			return handler.disableEnable(indexVal, false);
		else if (transCode == 8)
			return handler.changePlan(indexVal);
		else if (transCode == 9)
			return handler.disableEnable(indexVal, true);
		else if (transCode == 0)
			return true;
		else
			return false;
	}
	
	
}