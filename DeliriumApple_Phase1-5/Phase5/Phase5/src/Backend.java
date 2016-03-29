/*
*Backend.java, BankAccount.java, and Validation.java created by Matthew McCormick, Nick Gregorio, and Janahan Mathanamohan starting on March 11th, 2016
*The following code is the backend for a bank terminal
*It will take inputs MasterAccountFile.txt and MergedTransactionFile.txt, and create outputs CurrentAccountsFile.txt and also update files MasterAccountFile.txt and MergedTransactionFile.txt 
*(assumed that Transactions merged on backend, not front end)
*Backend.java and related files are currently run on their own.
*Program should be compiled via javac Backend.java BankAccount.java Validation.java, and run via the command java Backend MasterAccountFile.txt MergedTransactionFile.txt
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.PrintWriter;
import java.io.File;
import java.io.FilenameFilter;


public class Backend {

  /**
  * Merges transaction files in the form xxxxTransaction.txt, where xxxx is a number from 0001 to 9999
  */
  public static void mergeTransactions() {
    try {

      FileInputStream fis;
      BufferedReader br;
      PrintWriter writer = new PrintWriter("MergedTransactionFile.txt", "UTF-8");

      //Grabbing the current directory via ./ allows dynamic usage.
      File dir = new File("./");
      File[] match = dir.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith("Transaction.txt");
        }
      });

      //
      for (File file : match) {
        System.out.println(file.getName());
        fis = new FileInputStream(file.getName());
        br = new BufferedReader(new InputStreamReader(fis));
        String line;
        while((line = br.readLine()) != null) {
          writer.println(line);
        }
      }

      //Appending an extra, empty 00 line allows subsequent methods to know which line should not compute transactions.
      writer.print(padSpace(41, "00"));
      writer.close();


    } catch (IOException e) {
      System.out.println("ERROR: Issue reading transaction files. \n" + e);
      return;
    }
  }
  

  /**
  *Takes the inputted MasterAccountsFile, and stores this data in a map.
  *@param file: The inputted file equivalent to MasterAccountFile.txt
  */
  public static Map<Integer, BankAccount> importAccountsFile(String file) {
    try {
      Map<Integer, BankAccount> accounts = new HashMap<Integer, BankAccount>();
      
      FileInputStream fis = new FileInputStream(file);

      //Construct BufferedReader from InputStreamReader
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));

      String line;
      while ((line = br.readLine()) != null) {
        //Relevant info must be grabbed from each line in the file, to store it relative to a account.

        String accountNumberString = line.substring(0, 5);
        int accountNumber = Integer.parseInt(accountNumberString);
        
        String accountName = line.substring(6, 26);
        
        char statusChar = line.charAt(27);
        boolean status = statusChar == 'A';
        
        String balanceString = line.substring(29 , 37);
        float balance = Float.parseFloat(balanceString);
        
        String transactionString = line.substring(38,42);
        int transactions = Integer.parseInt(transactionString);


        char accountType = line.charAt(43);


        
        BankAccount account = new BankAccount(accountNumber, accountName, status, balance, accountType, transactions);
        
        accounts.put(accountNumber, account);
      }
      
      br.close();
      
      return accounts;

    } catch (IOException e) {
      System.out.println("Error: Issue when reading accounts file \n" + e);
      return null;
    }
  }

  /*
   * Parameter(s): Filename of transaction file.
   * Reads the transaction file and runs the transactions.
   */
  public static void importTransactionFile(String file, Map<Integer, BankAccount> accounts) {
    try {
      FileInputStream fis = new FileInputStream(file);

      //Construct BufferedReader from InputStreamReader
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));
      //Hold the results of check
      String results = " ";
      //Hold the second user of results 
      String results2 = " ";  
      String line;
      while (!(line = br.readLine()).equals("00                                       ") && !(results.equals("Fatal"))) {
        // TODO: Debugging info, remove
        System.out.println(line);
        
        
        // Gets the transaction id number and converts it to an integer
        String transactionString = line.substring(0, 2);
        int transaction = Integer.parseInt(transactionString);
        
        // Gets the account name and removes the trailing spaces
        String accountName = line.substring(3, 23);
        accountName = accountName.replaceAll("\\s+$", "");
        
        // Gets the account number and converts it to an integer
        String accountNumString = line.substring(24, 29);
        int accountNum = Integer.parseInt(accountNumString);
        
        // Gets the account balance and converts it to a float
        String balanceString = line.substring(30, 38);
        float balance = Float.parseFloat(balanceString);
        
        // Gets the misc field
        // TODO: Find out what this is used for?
        String misc = line.substring(39, 42);
        // Creates a new Validator object
        Validator valid = new Validator();
        // Creates a tmp bankaccount to hold accounts
        BankAccount tmp;
        //Here, distribute tasks depending on what the transaction code is. (FOR BALANCE CHANGES, WE SHOULD MAKE USE OF A NEW OBJECT IF POSSIBLE)
        switch(transaction) {
        case 0: case 10: //End of session, unsure if anything needed
          System.out.println("Logged in/out");  
          break;
        case 1: //withdrawal
          System.out.println("withdrawal");
          System.out.println(accountNum);
          tmp = accounts.get(accountNum);
          results = valid.withdrawal(tmp.getBalance(),tmp.getType(),balance,accountNum,"Withdrawal");
          if(results.equals("Pass")){
            System.out.println("Withdrawal" + " successful.");
            accounts.get(accountNum).setBalance(-(balance+tmp.getTransactionFee()));
            System.out.println("Withdrawal" + tmp.getBalance());
          } 
          break;
        case 2: //transfer
          // Creates a tmp variable to store second accounts results is validation
          // gets the line for second user
          line = br.readLine();
          // gets the account number for the second user
          String accountNumString2 = line.substring(24, 29);
          // changes the account number to an int
          int accountNum2 = Integer.parseInt(accountNumString2);
          // Creates a tmp second account for transfter
          BankAccount tmp2 = accounts.get(accountNum2);
          tmp = accounts.get(accountNum);
          results = valid.withdrawal(tmp.getBalance(),tmp.getType(),balance,accountNum,"Transfer1");
          results2 = valid.deposit(tmp2.getBalance(),tmp.getType(),balance,accountNum2,"Transfer2");
          if(results.equals("Pass") && results2.equals("Pass")){
            System.out.println("Transfer1" + " successful.");
            accounts.get(accountNum).setBalance(-(balance+tmp.getTransactionFee()));
            System.out.println("Transfer1" + tmp.getBalance());
            System.out.println("Transfer2" + " successful.");
            accounts.get(accountNum2).setBalance((balance+tmp2.getTransactionFee()));
            System.out.println("Transfer2" + tmp2.getBalance());
          } 
          break;
        case 3: //paybill
          System.out.println("Paybill");
          System.out.println(accountNum);
          tmp = accounts.get(accountNum);
          results = valid.paybill(tmp.getBalance(),tmp.getType(),balance,accountNum,misc);
          if(results.equals("Pass")){
            System.out.println("Paybill" + " successful.");
            accounts.get(accountNum).setBalance(-(balance+tmp.getTransactionFee()));
            System.out.println("Paybill" + tmp.getBalance());
          }
          break;
        case 4: //deposit
          System.out.println("deposit");
          System.out.println(accountNum);
          tmp = accounts.get(accountNum);
          results = valid.deposit(tmp.getBalance(),tmp.getType(),balance,accountNum,"Deposit");
          if(results.equals("Pass")){
            System.out.println("Deposit" + " successful.");
            accounts.get(accountNum).setBalance((balance+tmp.getTransactionFee()));
            System.out.println("Deposit" + tmp.getBalance());
          } 
          break;
        case 5: //create
          System.out.println("Create");
          System.out.println(accountNum);
          results = valid.create(accounts,accountNum,balance);
          if(results.equals("Pass")) {
            BankAccount account = new BankAccount(accountNum, accountName,true,balance,'N',0);
            accounts.put(accountNum, account);                      
          }
          break;
        case 6: //delete
          System.out.println("delete");
          results = valid.verifyUser( accounts,accountNum,accountName, "delete");
          if(results.equals("Pass")) {
            accounts.remove(accountNum);                      
          }
          break;
        case 7: //disable
          System.out.println("disable");
          results = valid.verifyUser( accounts,accountNum,accountName, "disable");
          if(results.equals("Pass")) {
            accounts.get(accountNum).disable();                      
          }
          break;
        case 8: //changeplan
          System.out.println("changeplan");
          results = valid.verifyUser( accounts,accountNum,accountName, "changeplan");
          if(results.equals("Pass")) {
            accounts.get(accountNum).changeType();                      
          }
          break;
        case 9: //enable
          System.out.println("enable");
          results = valid.verifyUser( accounts,accountNum,accountName, "enable");
          if(results.equals("Pass")) {
            accounts.get(accountNum).enable();                      
          }
          break;
        default:
          System.out.println("Error: Command not found!");
          // TODO: Give an error and halt execution
          break;
        }
      }

      br.close();

    } catch (IOException e) {
      System.out.println("Error: Issue when reading transaction file \n" + e);
      return;
    }
  }

  /**
  *Writes the Master and current account files in a sorted fashion
  *@param accounts: A list of all accounts, in sorted order.
  *@param isMaster: distinction between writing to master file or current file.
  */
  public static void writeFile(List<BankAccount> accounts, boolean isMaster){
    String active;
    Float preBalance;
    String balance;
    PrintWriter writer;


    try {
      //To remove unneeded repetition, writer must be implemented in different ways.
      if (isMaster) {
        writer = new PrintWriter("MasterAccountFile.txt", "UTF-8");
      } else {
        writer = new PrintWriter("CurrentAccountFile.txt", "UTF-8");
      } 
      

      for (int i = 0; i < accounts.size(); i++){
        if (accounts.get(i).getStatus()) {
          active = "A";
        } else {
          active = "D";
        }

        preBalance = accounts.get(i).getBalance();
        balance = String.format("%.2f", preBalance);

        writer.print(padZero(5, String.valueOf(accounts.get(i).getNumber())) + " " + padSpace(20, accounts.get(i).getName()) + " " + active + " " + padZero(8, balance) + " ");
        //This statement is only printed if printing to the master account file.
        if(isMaster) {
          writer.print(padZero(4, String.valueOf(accounts.get(i).getTransactions())) + " ");
        }
        writer.print(accounts.get(i).getType());

        
        if (accounts.size() - i != 1) {
          writer.print("\n");
        }
      }

      writer.close();
    } catch (IOException e) {
      System.out.println("ERROR: Issue writing master account file. \n" + e);
      return;
    }
    
    
  }

  /**
  * For transaction info containing strings, this method will pad spaces to fit the character requirements
  *@param totalLength: The character requirement of the string
  *@param text: The actual contents of the string before padding.
  */
  public static String padSpace(int totalLength, String text){
    int padNum = totalLength - text.length();
    for (int i = 0; i<padNum; i++){
      text = text + " ";
    }
    return text;
  }

  /**
  * For transaction info containing numerical data, this method will pad zeroes to fit the character requirements
  *@param totalLength: the character requirement of the numerical data
  *@param value: the actual value of the integer, in String form.
  */
  public static String padZero(int totalLength, String value) {
    int padNum = totalLength - value.length();
    for (int i = 0; i<padNum; i++) {
      value = "0" + value;
    }
    return value;
  }

  /*
   * Takes the merged transaction file and master bank accounts file as parameters
   * Creates a current bank accounts file and new master bank accounts file 
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      System.out.println("Error: Provide arguments Master Accounts File and Merged Transaction File.");
      return;
    }

    //Merge the transactions initially, in case any changes have been made.
    mergeTransactions();

    Map<Integer, BankAccount> accounts = importAccountsFile(args[0]);
    
    if(accounts == null) {
      System.out.println("Error: No accounts found!");
      return;
    }
    
    
    
    

    // Makes a List from the Hashmap of accounts
    List<BankAccount> accountsList = new ArrayList<BankAccount>(accounts.values());
    // Sorts the List
    Collections.sort(accountsList);
    // Prints the list out
    System.out.println(accountsList);


    
    
 
    
    // TODO: Just handle the transactions as standard input with the same code as in this function
    importTransactionFile(args[1], accounts);
    
    //Write to the master account file
    writeFile(accountsList, true);
    //Write to the current account file.
    writeFile(accountsList, false);

  }

}
