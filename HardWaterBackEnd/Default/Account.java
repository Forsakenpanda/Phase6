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
	private int funds;
	private boolean enabled;
	private String name;
	private char plan;
	private int counter;
  private int transferLimit;
  private int withdrawalLimit;
  private int depositLimit;
  private int ecLimit;
  private int cqLimit;
  private int tvLimit;


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
		this.funds = (int) (funds * 100);
		this.enabled = enabled;
		this.plan = plan;
		this.counter = counter;
    this.transferLimit = 0;
    this.withdrawalLimit = 0;
    this.depositLimit = 0;
    this.ecLimit = 0;
    this.cqLimit = 0;
    this.tvLimit = 0;
	}
	
	/**
	 * Returns the name of the account holder.
	 * @return the name of the account holder.
	 */
	public String getName () {
		return name;
	}
  
  /**
  *returns the current amount of money that has been paid to a single company in a standard session.
  * @param comp the company in question
  * @return the paybill fund to use.
  */
  public int getCompanyLimit(String comp) {
  	if (comp.equals("EC")) {
  		return ecLimit;
  	} else if (comp.equals("CQ")) {
  		return cqLimit;
  	} else {
  		return tvLimit;
  	} 
  }

  /**
   * Returns the current amount of money that has been transfered in 
   * a standard session.
   * @return the money that was transfered.
   */ 
  public int getTransferLimit() {
    return transferLimit;
  }

  /**
   * Returns the current amount of money that has been deposited in
   * a standard session.
   * @return the money that was deposited
   */ 
	

  public int getWithdrawalLimit() {
    return withdrawalLimit;
  }
  
  public void addDepositLimit(int money) {
    depositLimit += money;
  }
  /**
   * Increases the withdrawal limit.
   * @param money the money to add to the limit
   */ 
  public void addWithdrawalLimit(int money) {
    withdrawalLimit += money; 
  }
  
  /**
   * Increases the transfer limit.
   * @param money the money to add to the limit
   */
  public void addTransferLimit(int money) {
    transferLimit += money;
  }

  /**
  * Increases a specific company's paybill limit.
  * @param money: the money to add to the limit
  * @param comp: The company limit to add to
  */
  public void addCompanyLimit(int money, String comp) {
  	if (comp.equals("EC")) {
  		ecLimit += money;
  	} else if (comp.equals("CQ")) {
  		cqLimit += money;
  	} else {
  		tvLimit += money;
  	} 
  }

	/**
	 * Returns the amount of money in the account.
	 * @return the amount of money in the account.
	 */
	public double getFunds() {
		return funds / 100.0;
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
			// Checks whether the funds will be above zero after the transaction
			if(this.funds - (int) (100 * funds) - (int) (fee * 100) - depositLimit < 0){
        System.out.println("funds : " + (this.funds + funds - fee));
        System.out.println("fee: " + fee);
				Main.reportError("not enough funds");
				return false;
			}
      this.funds = this.funds - (int) (funds * 100) - (int) (fee * 100);
		}
		else {
			if (funds <= 0) {
				Main.reportError("cannot deposit a negative or zero amount");
				return false;
			}
			//see if we will exceed the maximum
			if(this.funds * 100 + (int) (funds * 100) - (int) (fee * 100) > (99999.99 * 100)){
				Main.reportError("transaction would exceed maximum funds");
				return false;
			}
		 this.funds = this.funds + (int) (funds * 100) - (int) (fee * 100);

		}
		//perform the addition
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

	
