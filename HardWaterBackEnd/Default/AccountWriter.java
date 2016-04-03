package Default;
/**
* Copyright 2016 Jason, Stuart, Muhammad copyright
*/
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class writes to the masterAccounts file, it also formats the output as described in the requirements.
 * @author HardWater
 *
 */
public class AccountWriter {

	/**
	 * Variables declared so both methods can use them.
	 */
	private BufferedWriter bw;
	private FileWriter fw;
	private File file;
	private boolean current;
	
	/**
	 * Constructor that initializes the file that is it going to write to.
	 * @param fileName the file it will write to.
	 */
	public AccountWriter (String location, boolean current) {
		this.current = current;
    //Initialize the file variable to write to the proper txt file.
    try {
  	file = new File(location);
	  file.createNewFile();			
    } catch (IOException e) {
      Main.reportError("cannot delete account file");
    }
	}
	
	/**
	 * Writes all the strings in the ArrayList to the proper file.
	 * @param accounts holds all the Accounts in the system.
	 */
	public void write(ArrayList<Account> accounts) {
		Collections.sort(accounts);
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			//Runs for the number of accounts.
			for(int i=0; i <accounts.size(); i++) {
				// Stores the current account into holder.
				Account holder = accounts.get(i);
				// Stores the formated string in accountLine.
				String accountLine = "";
				// Checks whether the account is enabled or disabled.
				char isEnable;
				if(holder.isEnabled()) {
					isEnable = 'A';
				}
				else {
					isEnable = 'D';
				}				
				// Calls the method formatTFString and stores the result in accountLine.
				accountLine = formatTFString(holder.getAccountNum(), holder.getName(), isEnable, holder.getFunds(), holder.getCounter(), holder.getPlan(), current);	
				// Writes the formatted string into the txt file.
				bw.write(accountLine);
				bw.write("\n");
			}
			//After it is done writing all the accounts to the file it writes the END_OF_FILE account to the files
			if (!current) {
				bw.write("00000 END_OF_FILE          D 00000.00 0000 N\n");
			} else {
				bw.write("00000 END_OF_FILE          D 00000.00 N\n");
			}
			
			bw.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Formats the string as required in the requirements.
	 * @param accountN the account number.
	 * @param name the account holder name.
	 * @param status the accounts status (Active or Disabled).
	 * @param funds the accounts funds.
	 * @param numTransactions the number of transactions this account has been involved in.
	 * @return a string that mergers all the above variables into one long string.
	 */
	public String formatTFString(int accountN, String name, char status, double funds, int numTransactions, char plan, boolean current) {
		// String that will be added onto and put into the transaction file.
		String tfString = "";  
	    int nameLength = 0;
	    
	    for(int i =0; i < (5-(String.valueOf(accountN).length())); i++) {
	    	tfString += "0";
	    }
	    tfString += accountN;  
	    tfString += ' ';  
	    if(name.length() > 20) {
	    	tfString += name.substring(0, 20);
	    } else {
	    	tfString += name;
	    	nameLength = name.length(); 
	    	
		    for (int i = 0; i < (20-nameLength); i++) {
		        // Fills in the remaining spots left for the name section with space.
		        tfString += ' ';
		    }
	    }
	      
	    // Store the length of the name into the variable nameLength.
	    
	    // Will run only if the name of the person is less then 20 and if the name is less then 20 it will run for 20 minus the persons name times.

	    // Adds a space after the name section.
	    tfString += ' ';  
	    tfString += status;
	    tfString += ' ';  	    
	    double value = funds;
	    String s = String.format("%.2f", value);
	    // Will run only if the funds is less then 8 digits including a . for the separation between the decimal and the int.
	    for (int i = 0; i< (8-s.length()); i++) {
	    tfString += "0";
	    }
	    // Adds on the funds to the 0's added before.
	    tfString += s;  
	    // Adds a space after funds.
	    tfString += ' ';  
	    
	    if(!current) {
		    for(int i = 0; i < (4-(String.valueOf(numTransactions).length())); i++) {
		    	tfString += "0";
		    }
		    // Adds the number of transactions information to the last to characters in the transaction file.
		    tfString += numTransactions; 
		    tfString += " ";
		    tfString += plan;
		    return tfString;
	    } else {
	    	tfString += plan;
	    	return tfString;
	    }

	}
	
}
