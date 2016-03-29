
public class BankAccount implements Comparable<BankAccount> {
  
  private int number;
  private String name;
  private float balance;
  private boolean active;
  private char type;
  private int transactions;

  /**
  *Constructs a BankAccount object with given parameters
  *@param number: the account number
  *@param name: the name on the account
  *@param active: whether the account is enabled or disabled
  *@param balance: the current balance of the account
  *@param type: Student or Non-student
  *@pram transactions: total number of transactions the account is involved in
  */
  
  public BankAccount(int number, String name, boolean active, float balance, char type, int transactions) {
    this.number = number;
    this.name = name;
    this.active = active;
    this.balance = balance;
    this.type = type;
    this.transactions = transactions;

  }
  
  // Prints basic information about the account to help with debugging
  public String toString() {
    return "Name: " + this.name + "\nNumber:" + this.number + "\nBalance: " + this.balance + "\n\n\n";
  }
  
  /**
  *Adds an amount to the balance of the user
  *@param amount: the amount to add to the balance
  */
  public void setBalance(float amount) {
    this.balance += amount;
  }
  
  /** 
  *Enables account. Returns true if account enabled else returns false.
  */
  public boolean enable() {
    if(!this.active) {
      this.active = true;
      return true;
    } else {
      return false;
    }
  }
  
  /** 
  *Disables account. Returns true if account disabled else return false.
  */
  public boolean disable() {
    if(this.active) {
      this.active = false;
      return true;
    } else {
      return false;
    }
  }
  
  /** 
  *Toggles the account type. Returns the new type of account.
  */
  public char changeType() {
    if(this.type == 'S') {
      this.type = 'N';
    } else {
      this.type = 'S';
    }
    return this.type;
  }
  
  /**
  *returns the account number
  */
  public int getNumber() {
    return this.number;
  }
  
  /**
  *returns the name associated with the account
  */
  public String getName() {
    return this.name;
  }
  
  /**
  *returns the balance on the account
  */
  public float getBalance() {
    return this.balance;
  }


  /**
  *returns the type of account
  */
  public char getType() {
    return this.type;
  }
  
  /**
  *Returns the status of the account
  */
  public boolean getStatus() {
    return this.active;
  }
  
  /**
  *returns the transaction fee associated with the account type
  */
  public float getTransactionFee() {
    if(this.type == 'S') {
      return 0.05f;
    } else {
      return 0.10f;
    }
  }

  /**
  *gets the total amount of transactions the account has been involved with
  */
  public int getTransactions(){
    return transactions;
  }

  /**
  *Increments the account's current transaction caount
  */
  public void addTransactions(){
    transactions++; 
  }

  /**
  *Compares two account numbers. Used for sorting
  */
  @Override
  public int compareTo(BankAccount otherAccount) {
    if(this.number > otherAccount.number){
      return 1;
    } else {
      return -1;
    }
  }
  
}
