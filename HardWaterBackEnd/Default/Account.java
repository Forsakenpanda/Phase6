package Default;
/**
* Copyright 2016 Jason, Stuart, Muhammad copyright
*/
import java.lang.Comparable;
import java.text.DecimalFormat;
/**
 * This class initializes an Account with all the information an account should have, as well as doing 
 * simple addition and subtraction to the funds, changing the status of the account and the account plan.
 * @author HardWater
 *
 */
public class Account implements Comparable<Account> {

	/**
	 * These variables are all aspects of what belongs in an account.
	 */
	private int accountNumber;
	private double funds;
	private boolean enabled;
	private String name;
	private char plan;
	private int counter;

	/**
	 * Constructor for Account.
	 * @param name the name of the account holder.
	 * @param funds the amount of funds in the user account.
	 * @param enabled the status of the account.
	 * @param plan shows what plan the account is.
	 * @param accountNumber the account number for the account.
	 * @param counter how many transactions have been made with this account.
	 */
	public Account(int accountNumber, String name, boolean enabled, double funds, char plan, int counter) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.funds = funds;
		this.enabled = enabled;
		this.plan = plan;
		this.counter = counter;
	}
	
	/**
	 * Returns the name of the account holder.
	 * @return the name of the account holder.
	 */
	public String getName () {
		return name;
	}
	
	/**
	 * Returns the amount of money in the account.
	 * @return the amount of money in the account.
	 */
	public double getFunds() {
		return funds;
	}
	
	/**
	 * Returns whether the account is disabled or not.
	 * @return true when the account is enabled and false when the account is disabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Returns the plan of the account.
	 * @return S for student or N for Normal
	 */
	public char getPlan() {
		return plan;
	}
	
	/**
	 * Returns the transaction fee for the account.
	 * @return the transaction fee for the account.
	 */
	public double getFee(boolean standard) {
		double fee = 0.0;
		if(standard) {
			if (plan == 'N')
				fee = 0.1;
			else
				fee = 0.05;
		}
		return fee;
	}
	
	/**
	 * Returns the number of the account.
	 * @return the account number.
	 */
	public int getAccountNum() {
		return accountNumber;
	}
	
	/**
	 * Returns the number of transactions.
	 * @return the number of transactions.
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Changes the amount of money in the account.
	 * @param funds the amount of money being add or taken away from the account.
	 * @param fee the transaction fee that will be deducted from the transaction.
	 * @return Whether the attempt to deposit/withdrawal from the account was successful.
	 */
	public boolean modifyFunds(double funds, double fee, boolean add) {
		DecimalFormat df = new DecimalFormat("####0.00");
		if(!enabled){
			Main.reportError(name + "account: " + accountNumber + " is disabled");
			return false;
		}
		//see if we want to add or subtract funds to the account
		if(!add){
			//check if the funds are negative
			if(funds <= 0) {
				Main.reportError("cannot withdrawal a negative or zero amount");
				return false;
			}
			funds = 0 - funds;
			// Checks whether the funds will be above zero after the transaction
			if(this.funds + funds - fee < 0){
				Main.reportError("not enough funds");
				return false;
			}
		}
		else {
			if (funds <= 0) {
				Main.reportError("cannot deposit a negative or zero amount");
				return false;
			}
			//see if we will exceed the maximum
			if(this.funds + funds - fee > 99999.99){
				Main.reportError("transaction would exceed maximum funds");
				return false;
			}
		}
		//perform the addition
		this.funds = Double.valueOf(df.format(this.funds)) + Double.valueOf(df.format(funds)) - Double.valueOf(fee);
		this.funds = Double.valueOf(df.format(this.funds));
		counter++;
		return true;
	}
	
	/**
	 * Changes the plan of the account.
	 */
	public boolean changePlan() {
		if(plan == 'S' && enabled) {
			plan = 'N';
			counter++;
			return true;
		}
		else if (plan == 'N' && enabled){
			plan = 'S';
			counter++;
			return true;
		}
		Main.reportError("Account: "+accountNumber+" is disabled");
		return false;
	}
	
	/**
	 * Changes the status of the account to disabled or enabled.
	 * @param check true if enabling, false if disabling.
	 */
	public boolean modifyEnable (boolean enable) {
		if(enabled && !enable) {
			enabled = false;
			counter++;
			return true;
		}
		else if(!enabled && enable) {
			enabled = true;
			counter++;
			return true;
		}
		return false;
	}
	
	@Override
	/**
	 * Compares two account numbers.
	 * @param account the account to compare to.
	 * @return positive number if account is less than current account, otherwise a negative number.
	 */
	public int compareTo(Account account) {
		if (accountNumber >= account.getAccountNum())
			return 1;
		else
			return -1;
	}
}

	