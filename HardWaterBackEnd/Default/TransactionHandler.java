package Default;
import java.util.ArrayList;
import java.util.Collections;

public class TransactionHandler {
	ArrayList<Account> accounts;
	private boolean standardSession;
	public TransactionHandler(ArrayList<Account> accounts) {
		this.accounts = accounts;
		standardSession = false;
	}
	
	/**
	 * True if in a standard session.
	 * @return false if not in a standard session.
	 */
	public boolean getStandardSession(){
		return standardSession;
	}
	
	/**
	 * Checks which session logged in a Standard session or Admin.
	 * @param accountIndex the index of the account number in the list.
	 * @param misc lets us know which type of session the user logged in as either S or A
	 * @return
	 */
	public boolean login(int accountIndex, String misc) {
		if(misc.charAt(0) == 'S') {
			standardSession = true;
		} else {
			standardSession = false;
		}
		return true;
	}
	
	/**
	 * Finds the account index to use.
	 * @param accountNumber the account number for the transaction
	 * @return the accountIndex in the accounts array, -1 if it doesn't exist
	 */
	public int findAccount(int accountNumber) {
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).getAccountNum() == accountNumber)
				return i;
		}
		return -1;
	}
	
	/**
	 * Withdrawal money from the account
	 * @param accountIndex the index of the account number in the list
	 * @param funds the amount of money that is being withdrawn 
	 * @return true if there is more money in the account then there is being withdrawn
	 */
	public  boolean withdrawal(int accountIndex, double funds) {
		double fee;
		//Passes in the statues of whether it is an admin transaction or standard transaction
		fee = accounts.get(accountIndex).getFee(standardSession);
    if (standardSession && accounts.get(accountIndex).getWithdrawalLimit() + funds * 100 > 50000)
      return false;
		if (accounts.get(accountIndex).modifyFunds(funds, fee, false)) {
      if (standardSession)
        accounts.get(accountIndex).addWithdrawalLimit((int) funds * 100);
      return true;
    }
    return false;
	}

	/**
	 * Deposit money into an account
	 * @param accountIndex the index of the account number in the list
	 * @param funds the amount of money that is being deposited into the account
	 * @return true if the money they are depositing is above the transaction fee and false if it is not
	 */
	public  boolean deposit(int accountIndex, double funds) {
		//Getting the fee that the transaction will be charged
		double fee = accounts.get(accountIndex).getFee(standardSession);
		if (!accounts.get(accountIndex).modifyFunds(funds, fee, true)) {
			Main.reportError("Not enough funds added");
		}
    if (standardSession) {
      accounts.get(accountIndex).addDepositLimit((int) funds * 100);
    }
		return true;
	}

	/**
	 * Enables or disables an account
	 * @param accountIndex the index of the account number in the list
	 * @param check the variable checks to see if the account is either currently disabled or enabled
	 * @return true after the disable or enable happens
	 */
	public  boolean disableEnable(int accountIndex, boolean check) {
		return accounts.get(accountIndex).modifyEnable(check);
	}

	/**
	 * Transfer transaction.
	 * <p>
	 * Transfers money from one account to the next account. Checks if the second
	 * transaction in the file is another transfer and validates the second account.
	 * <p>
	 * @param accountIndex the first transfer transaction account
	 * @param funds the funds involved in the transfer
	 * @param misc "T " if the account is receiving. "F " if the account is sending. 
	 * @return true if the transfer was successful.
	 */
	public  boolean transfer(int accountIndex, double funds, String misc, String nextTransaction) {
		double fee;

		// get the next transfer part
		String[] splitTransaction = Main.splitTransaction(nextTransaction);
		// the second index of the account
		int secondIndex = findAccount(Integer.parseInt(splitTransaction[2]));
		if (secondIndex == -1) {
			Main.reportError("second transfer account does not exist");
			return false;
		}
		// see if the transaction misc information is right and then transfer money
		if (splitTransaction[4].equals("TO") && misc.equals("FR")){
			fee = accounts.get(accountIndex).getFee(standardSession);
      if (standardSession && accounts.get(accountIndex).getTransferLimit() + funds * 100 > 100000) {
        Main.reportError("exceeded transfer limit");
        return false;
      }
			if(accounts.get(secondIndex).modifyFunds(funds, 0, true)) {
				if(!accounts.get(accountIndex).modifyFunds(funds, fee, false)){
					// restore the funds to the old balance
					accounts.get(secondIndex).modifyFunds(funds, 0, false);
					Main.reportError("transfer failed");
					return false;
				}
        if (standardSession)
          accounts.get(accountIndex).addTransferLimit((int) funds * 100);
			} else {
				Main.reportError("transfer failed");
				return false;
			}
		} else if (splitTransaction[4].equals("FR") && misc.equals("TO")) {
			fee = accounts.get(secondIndex).getFee(standardSession);
       if (standardSession && accounts.get(secondIndex).getTransferLimit() + funds * 100 > 100000) {
        Main.reportError("exceeded transfer limit");
        return false;
       }
			if(accounts.get(accountIndex).modifyFunds(funds, 0, true)) {
				if(!accounts.get(secondIndex).modifyFunds(funds, fee, false)){
					// restore the funds to the old balance
					accounts.get(accountIndex).modifyFunds(funds, 0, false);
					Main.reportError("transfer failed");
					return false;
				}
        if (standardSession)
           accounts.get(secondIndex).addTransferLimit((int) funds * 100);
			} else {
				Main.reportError("transfer failed");
				return false;
			}
		} else {
			Main.reportError("unknown destination and source for transfer");
			return false;
		}
		return true;
	}

	/**
	 * Creates transaction
	 * <p>
	 * Creates an account with a name, a number, some money and a plan
	 * It checks to make sure that the account number does not match any other account number
	 * <p>
	 * @param name
	 * @param accountNumber
	 * @param funds
	 * @param misc
	 * @return
	 */
	public  boolean create(String name, int accountNumber, double funds, String misc) {
		//Checks to make sure no other account has the same account number.
    Collections.sort(accounts);
    int newAccountNum = 1;
    for (int i = 0; i < accounts.size(); i++) { 
      if (accounts.get(i).getAccountNum() != newAccountNum)
        break;
      newAccountNum++;
    }
		if(standardSession) {
			Main.reportError("Must be an admin to create an account");
			return false;
		}
		accounts.add(new Account(newAccountNum, name, true, funds, 'N', 0));
		return true;
	}

	/**
	 * Deletes an account from the array list.
	 * @param accountNumber the account number to delete
	 * @param funds
	 * @param misc
	 * @return true if successful
	 */
	public boolean delete(int accountIndex) {
		if(!standardSession) {
			accounts.remove(accountIndex);
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Changes the plan of the account
	 * @param accountIndex the index of the account.
	 */
	public boolean changePlan(int accountIndex) {
		if(accounts.get(accountIndex).changePlan()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The paybill transaction.
	 * @param accountIndex the index of the account to pay the bill
	 * @param funds the amount to pay
	 * @param misc the company name
	 * @return true if the paybll is successful
	 */
	public boolean paybill(int accountIndex, double funds, String misc) {
		if(!misc.equals("EC") && !misc.equals("CQ") && !misc.equals("TV")){
			Main.reportError("company does not exist");
			return false;
		}
		double fee = accounts.get(accountIndex).getFee(standardSession);
    if (standardSession && accounts.get(accountIndex).getCompanyLimit(misc) + funds * 100 > 200000) {
    	Main.reportError("exceeded company paybill limit to " + misc);
    	return false;
    } 
    if (accounts.get(accountIndex).modifyFunds(funds, fee, false)) {
      if (standardSession) {
      	accounts.get(accountIndex).addCompanyLimit((int) funds * 100, misc);
      	return true;
      }
        
    }
    return false;
	}

	/**
	 * Gets the accounts.
	 * @return account list
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
}
