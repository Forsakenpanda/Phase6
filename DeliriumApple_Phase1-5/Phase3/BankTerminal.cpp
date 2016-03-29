// BankTerminal.cpp : Defines the entry point for the console application.	


#include "BankAccount.h"
#include <fstream>
#include <iostream>
#include <string>
#include <string.h>
#include <regex>
#include <vector>
#include <iomanip>
using namespace std;
// To see if a user is logged in
bool loggedin = false;
// To see if a user is loggin in as an admin
bool isAdmin = false;
// Max number of accounts
const int maxAccount = 99999;
// This keeps track of the number of accounts
int accountNumbers = 0;
// This keeps track of the number of newly created accounts
int newAccountNumbers = 0;
// String to keep all of the users input
string input;
// This keeps track of the current user being used
string currentUser;
// This keeps track of the current user logged in
string loggedinUser;
// Bankaccount array for currently available bank accounts
BankAccount* accounts = new BankAccount[maxAccount];
// Bankaccount array for newly created bank accounts
BankAccount* newAccounts = new BankAccount[maxAccount];
// THis keeps track of all the transactions done.
vector<string> transactions(0);
// This is to keep track of limits
double limit [maxAccount][5];
// file for transaction
string transactionpath;
//
ofstream trans;

// Method to search if a user exists
// takes in the user you are looking for
// returns a true if found and false if not
bool searchUser(string accountuser) {
	for (int i = 0; i < accountNumbers; i++) {
		if (accountuser.compare(accounts[i].getName()) == 0) {
			return true;
		}
	}

	// Checks for new accounts made this session.
	for (int i = 0; i < newAccountNumbers; i++) {
		if (accountuser.compare(newAccounts[i].getName()) == 0) {
			cout << "This account is not available for use yet" << endl;
		}
	}
	return false;
}

// Method to search for account numbers
// takes in the account number and searches 
// through the accounts for the account number
// if found it will return the location in the array
// if not found it will return the max number of accounts
// plus 100
int searchAccounts(string accountnum) {
	for (int i = 0; i < accountNumbers; i++) {
		if (stoi(accountnum) == accounts[i].getAccNumber()) {
			return i;
		}
	}
	// Checks for new accounts made this session.
	for (int i = 0; i < newAccountNumbers; i++) {
		if (stoi(accountnum) == newAccounts[i].getAccNumber()) {
			cout << "This account is not available for use yet" << endl;
		}
	}
	return maxAccount+100;
}

bool limitcheck (string check, int accnum, double amount) {
	if(check.compare("w") == 0) {
		if(limit[accnum][0] + amount <= 500) {
			limit[accnum][0] += amount;
			return true;
		}
	} else if (check.compare("t") == 0) {
		if(limit[accnum][1] + amount <= 2000) {
			limit[accnum][1] += amount;
			return true;
		}
	} else if (check.compare("EC") == 0) {
		if(limit[accnum][2] + amount <= 2000) {
			limit[accnum][2] += amount;
			return true;
		}
	} else if (check.compare("TV") == 0) {
		if(limit[accnum][3] + amount <= 2000) {
			limit[accnum][3] += amount;
			return true;
		}
	} else if (check.compare("CQ") == 0) {
		if(limit[accnum][4] + amount <= 2000) {
			limit[accnum][4] += amount;
			return true;
		}
	}
	return false;
}
// Method to search for account numbers that are with a specific user
// takes in the account number and user as parameters. If found
// returns the location in the array it is found otherwise 
// returns the max number of accounts plus 100.
int searchUserAccounts(string accountnum , string user) {
	for (int i = 0; i < accountNumbers; i++) {
		if (user.compare(accounts[i].getName()) == 0) {
			if (stoi(accountnum) == accounts[i].getAccNumber()) {
				return i;
			}
		}
	}
	for (int i = 0; i < newAccountNumbers; i++)	{
		cout << stoi(accountnum) << " " << newAccounts[i].getAccNumber();
		if (stoi(accountnum) == newAccounts[i].getAccNumber()) {
			cout << "This account is not available for use yet" << endl;
		}
	}
	return maxAccount + 100;
}

// Method to padspaces in account holder name
// takes in the actual length you want the string and 
// the text you want to pad spaces with and returns
// the results string with spaces
string padSpace(int actuallength, string actualtext) {
	int padnum = actuallength - actualtext.length();
	string newtext = actualtext;
	for (int i = 0; i < padnum; i++) {
		newtext = newtext + " ";
	}
	return newtext;
}

// Method to pad zero for amounts and account numbers
// takes in the length you want the value to be and the string
// returns a new text with the padded zeroes
string padZero(int actuallength, string interger) {
	int padnum = actuallength - interger.length();
	string newinterger = interger;
	for (int i = 0; i < padnum; i++) {
		newinterger = "0" + newinterger;
	}
	return newinterger;
}

// Creates a new account takes in account holders name and 
// the amount. It checks for available accounts and then makes
// sure a newly created account doesn't have that account number
// returns the account number the new account is assigned.
string makeNewAccount(string holdername, string amount) {
	int freenum = 00001;
	bool checkfreenum = true;
	while (checkfreenum) {
		for (int i = 0; i < accountNumbers; i++) {
			if (freenum == accounts[i].getAccNumber())	{
				i = 0;
				freenum++;
			}
		}
		checkfreenum = false;
		for (int i = 0; i < newAccountNumbers; i++) {
			if (freenum == newAccounts[i].getAccNumber()) {
				i = maxAccount;
				freenum++;
				checkfreenum == true;
			}
		}
	}
	string sfreenum = padZero(5, to_string(freenum));
	newAccounts[newAccountNumbers] ={ sfreenum, holdername, "E", amount, "NP" };;
	newAccountNumbers++;
	return sfreenum;
}

// Simple method to just add a transaction to the transaction line
// takes in the transaction code, account holder, account number,
// amount and miscellaneous
void transactionsLine(string code, string account, string accounnum, string amount, string mm) {
	string transactionline = code + " " + padSpace(20, account) + " " + padZero(5, accounnum) + " " + padZero(8, amount) + " " + mm;
	transactions.push_back(transactionline);
}

// Method to delete accounts using the accounts
// postion in the array 
void deleteAccount(int accountnum) {
	for (int i = accountnum; i < accountNumbers-1; i++) {
		accounts[i] = accounts[i + 1];
		limit[i][0] = limit[i+1][0];
		limit[i][1] = limit[i+1][1];
		limit[i][2] = limit[i+1][2];
	}
	accountNumbers--;
}

// Method to fill the accounts array with bank accounts 
void fillaccounts() {
	ifstream myReadFile;
	myReadFile.open("BankAccount.txt");
	string line;
	string accountnum;
	string accountname;
	string accountstatus;
	string accountbalance;
	string accounttype;
	int lastspace;
	while (!myReadFile.eof()) {
		getline(myReadFile, line);
		accountnum = line.substr(0, 5);
		accountname = line.substr(6, 20);
		lastspace = accountname.size() - 1;
		// This is to get rid of all the empty space for the account holder name
		while (lastspace >= 0 && accountname[lastspace] == ' ') {
			--lastspace;
		}
		accountname = accountname.substr(0, lastspace + 1);
		accountstatus = line.substr(27, 1);
		accountbalance = line.substr(29, 8);
		accounttype = line.substr(38, 1);
		accounts[accountNumbers] = { accountnum, accountname, accountstatus, accountbalance, accounttype };
		accountNumbers++;
		limit[accountNumbers][0] = 0.0;
		limit[accountNumbers][1] = 0.0;
	}
}

// Method for the withdraw action of the bank checks
// if user is an admin or not and then proceeds with 
// associated outputs and input checking. Applys the changes
// to current bank accounts and the records it in the transactions vector.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction.
void withdraw() {
	string accnum;
	string amount;
	if (isAdmin == true) {
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
	}
	cout << "Please enter account number:" << endl;
	getline(cin, input);
	accnum = input;
	// A bunch of if statments to make sure use is entering valid strings using regexes
	if (regex_match(accnum, regex("(\\d{5})"))) {
		int accountnum = searchUserAccounts(accnum, currentUser);
		if (accountnum <= maxAccount) {
			// A check to see if the account is enabled
			if (accounts[accountnum].getActivity()) {
				cout << "Please choose amount to withdraw:" << endl;
				getline(cin, input);
				amount = input;
				if (regex_match(amount, regex("(\\d+)(\.)(\\d{2})"))) {
					// Checks if the amount to pay is payable in biils
					int amountpay = stoi(input);
					if ((amountpay % 5) == 0) {
						// Checking if the account has enough money in their account 
						// if it is admin the it will not include the transaction fee 
						// other wise checks including the transaction fee.
						if (isAdmin == true) {
							if ((accounts[accountnum].getBalance() - stod(amount)) > 0) {
								accounts[accountnum].updateBalance(stod(amount) * -1);
								cout << "Your balance is " <<setprecision(2)<<fixed<< accounts[accountnum].getBalance() << endl;
							} else {
								cout << "Insufficient funds." << endl;
								return;
							}
						} else {
							if ((accounts[accountnum].getBalance() - stod(amount) - accounts[accountnum].transactionfee()) > 0) {
								if(limitcheck("w",accountnum,stod(amount))) {
									accounts[accountnum].updateBalance((stod(amount) + accounts[accountnum].transactionfee()) * -1);
									cout << "Your balance is " <<setprecision(2)<<fixed<<  accounts[accountnum].getBalance() << endl;
								} else {
									cout << "Exceeded withdrawal limit of $500" << endl;
									return;
								}
							} else {
								cout << "Insufficient funds." << endl;
								return;
							}
						}
					} else {
						cout << "Invalid amount chosen for transfer. Amount is not payable in bills." << endl;
						return;
					}
				} else {
					cout << "Invalid amount format." << endl;
					return;
				}
			} else {
				cout << "Account disabled." << endl;
				return;
			}
		} else {
			cout << "Name and Account number are not associated with each other" << endl;
			return;
		}
	}
	else {
		cout << "Invalid account number." << endl;
		return;
	}
	transactionsLine("01", currentUser, accnum, amount, "  ");
}

// Method for the deposit action of the bank checks
// if user is an admin or not and then proceeds with 
// associated outputs and input checking. Applys the changes
// to current bank accounts and the records it in the transactions vector
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void deposit() {
	string accnum;
	string amount;
	if (isAdmin == true) {
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
	}
	cout << "Please enter account number:" << endl;
	getline(cin, input);
	accnum = input;
	// A bunch of if statments to make sure use is entering valid strings using regexe
	if (regex_match(accnum, regex("(\\d{5})"))) {
		int accountnum = searchUserAccounts(accnum, currentUser);
		if (accountnum <= maxAccount) {
			// A check to see if the account is enabled
			if (accounts[accountnum].getActivity()) {
				cout << "Please enter amount to deposit:" << endl;
				getline(cin, input);
				amount = input;
				if (regex_match(amount, regex("(\\d+)(\.)(\\d{2})"))) {
					if (isAdmin == true) {
						// Checking if the accoount balance will exceed the max amount allowed
						if ((accounts[accountnum].getBalance() + stod(amount)) <= 99999.99) {
							accounts[accountnum].updateBalance(stod(amount));
							cout << "Your balance is " <<setprecision(2)<<fixed<< accounts[accountnum].getBalance() << endl;
						} else {
							cout << "Invalid amount: balance would exceed 99999.99 limit" << endl;
							return;
						}
					} else {
						// Checking if the account has enough money in their account 
						// other wise checks including the transaction fee.
						if ((accounts[accountnum].getBalance() - accounts[accountnum].transactionfee()) > 0) {
							// Checking if the accoount balance will exceed the max amount allowed
							if ((accounts[accountnum].getBalance() + stod(amount) - accounts[accountnum].transactionfee()) < 99999.99) {
								accounts[accountnum].updateBalance(stod(amount) - accounts[accountnum].transactionfee());
								cout << "Your balance is " <<setprecision(2)<<fixed<<  accounts[accountnum].getBalance() << endl;
							} else {
								cout << "Invalid amount: balance would exceed 99999.99 limit" << endl;
								return;
							}
						} else {
							cout << "Invalid amount chosen for deposit. Balance will be negative" << endl;
							return;
					 	}
					}
				} else {
					cout << "Invalid amount of chosen for deposit. Amount does not include cents." << endl;
					return;
				}
			} else {
				cout << "Account disabled" << endl;
				return;
			}
		} else {
			cout << "Name and Account number are not associated with each other" << endl;
			return;
		}
	} else {
		cout << "Invalid Account number" << endl;
		return;
	}
	transactionsLine("04", currentUser, accnum, amount, "  ");
}

// Method for the transfer action of the bank checks
// if user is an admin or not and then proceeds with 
// associated outputs and input checking. Applys the changes
// to current bank accounts and the records it in the transactions vector
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void transfer() {
	string accto;
	string accfrom;
	string amount;
	if (isAdmin == true) {
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
	}
	cout << "Please enter account number to transfer from:" << endl;
	getline(cin, input);
	accfrom = input;
	// A bunch of if statments to make sure use is entering valid strings using regexes
	if (regex_match(accfrom, regex("(\\d{5})"))) {
		int accountfrom = searchUserAccounts(accfrom, currentUser);
		if (accountfrom <= maxAccount) {
			// A check to see if the account is enabled
			if (accounts[accountfrom].getActivity()) {
				cout << "Please enter account number to transfer to:" << endl;
				getline(cin, input);
				accto = input;
				if (regex_match(accto, regex("(\\d{5})"))) {
					int accountto = searchAccounts(accto);
					if (accountto <= maxAccount) {
						// A check to see if the account is enabled
						if (accounts[accountto].getActivity()) {
							cout << "Please enter amount to transfer:" << endl;
							getline(cin, input);
							amount = input;
							if (regex_match(amount, regex("(\\d+)(\.)(\\d{2})"))) {
								// Checking if the recieving account will be above the max amount in an account
								if ((accounts[accountto].getBalance() + stod(amount)) < 99999.99) {
									// Checking if the account has enough money in their account 
									// if it is admin the it will not include the transaction fee 
									// other wise checks including the transaction fee.
									if (isAdmin == true) {
										if (accounts[accountfrom].getBalance() > stod(amount)) {
											accounts[accountfrom].updateBalance(stod(amount)* -1);
											accounts[accountto].updateBalance(stod(amount));
											cout << "Transfer successful from account " << accfrom  << " to account " << accto << endl;
										} else {
											cout << "Insufficient funds" << endl;
											return;
										}
									} else {
										if (accounts[accountfrom].getBalance() - accounts[accountfrom].transactionfee()> stod(amount)) {
											if(limitcheck("t",accountfrom,stod(amount))) {
												accounts[accountfrom].updateBalance((stod(amount) + accounts[accountfrom].transactionfee()) * -1);
												accounts[accountto].updateBalance(stod(amount));
												cout << "Transfer successful from account " << accfrom  << " to account " << accto << endl;
											} else {
												cout << "Limit exceeded" << endl;
												return;
											}
										} else {
											cout << "Insufficient funds" << endl;
											return;
										}
									}
								} else {
									cout << "Invalid amount; balance would exceed 99999.99 limit" << endl;
									return;
								}
							} else {
								cout << "Not a valid amout" << endl;
								return;
							}
						} else {
							cout << "Account disabled" << endl;
							return;
						}
					} else {
						cout << "Account number does not exist" << endl;
						return;
					}
				} else {
					cout << "Invalid account number" << endl;
					return;
				}
			} else {
				cout << "Account is disabled" << endl;
				return;
			}
		} else {
			cout << "Name and Account number are not associated with each other" << endl;
			return;
		}
	} else {
		cout << "Invalid acount number" << endl;
		return;
	}
	transactionsLine("08", currentUser, accfrom, amount, "  ");
	transactionsLine("08", "", accto, amount, "  ");
}

// Method for the paybill action of the bank checks
// if user is an admin or not and then proceeds with 
// associated outputs and input checking. Applys the changes
// to current bank accounts and the records it in the transactions vector
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void paybill() {
	string accnum;
	string amount;
	string company;
	if (isAdmin == true) {
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
	}
	cout << "Please enter account to pay from:" << endl;
	getline(cin, input);
	accnum = input;
	// A bunch of if statments to make sure use is entering valid strings using regexes
	if (regex_match(input, regex("(\\d{5})"))) {
		int accountnum = searchUserAccounts(input, currentUser);
		if (accountnum <= maxAccount) {
			// Checks if the account is valid.
			if (accounts[accountnum].getActivity()) {
				cout << "Please enter the company to pay to:" << endl;
				getline(cin, input);
				company = input;
				if (company.compare("EC") == 0 || company.compare("CQ") == 0 || company.compare("TV") == 0) {
					cout << "Please enter the amount to pay:" << endl;
					getline(cin, input);
					amount = input;
					if (regex_match(amount, regex("(\\d+)(\.)(\\d{2})"))) {
						// Checking if the account has enough money in their account 
						// if it is admin the it will not include the transaction fee 
						// other wise checks including the transaction fee.
						if (accounts[accountnum].getBalance() > stod(amount)) {
							if (isAdmin == true) {
								accounts[accountnum].updateBalance(stod(amount)*-1);
								cout << "Bill successfully paid" << endl;
								
							} else {
									if (accounts[accountnum].getBalance() - stod(amount) - accounts[accountnum].transactionfee() > stod(amount)) {
										if(limitcheck(company,accountnum,stod(amount))){
											accounts[accountnum].updateBalance((stod(amount) + accounts[accountnum].transactionfee())*-1);
											cout << "Bill successfully paid" << endl;	
										} else { 
											cout << "Limit exceeded" << endl;
										}
									} else {
										cout << "Insufficient funds" << endl;
										return;
									}
							}
						} else {
							cout << "Insufficient funds" << endl;
							return;
						}
					} else {
						cout << "Not a valid amount" << endl;
						return;
					}
				} else {
					cout << "Invalid company" << endl;
					return;
				}
			} else {
				cout << "Account disabled" << endl;
				return;
			}
		} else {
			cout << "Name and Account number are not associated" << endl;
			return;
		}
	} else {
		cout << "Invalid account number" << endl;
		return;
	}
	transactionsLine("03", currentUser, accnum, amount, company);
}

// Method for the changeplan action of the bank checks
// if user is an admin or not. If they are an admin proceeds 
// with associated outputs and input checking and applys the changes
// to current bank accounts and the records it in the transactions vector.
// If user is not an admin then they are greeted with an error message.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void changeplan() {
	string accnum;
	if (isAdmin == true) {
		// A bunch of if statments to make sure use is entering valid strings using regexes
		cout << "Enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
		cout << "Enter account number:" << endl;
		getline(cin, input);
		accnum = input;
		if (regex_match(accnum, regex("(\\d{5})"))) {
			int accountnum = searchUserAccounts(accnum, currentUser);
			if (accountnum > maxAccount) {
				cout << "Invalid account number. This account does not belong to you." << endl;
				return;
			} else {
				if(accounts[accountnum].getActivity()) {
					accounts[accountnum].changeType();
					cout << "Plan has successfully changed." << endl;
				} else {
					cout << "Account disabled" << endl;
					return;
				}
			}
		} else {
			cout << "Invalid account number." << endl;
			return;
		}
	} else {
		cout << "Invalid command. This is an admin only command." << endl;
		return;
	}
	transactionsLine("08", currentUser, accnum, "00000.00", "  ");
}

// Method for the enable action of the bank checks
// if user is an admin or not. If they are an admin proceeds 
// with associated outputs and input checking and applys the changes
// to current bank accounts and the records it in the transactions vector.
// If user is not an admin then they are greeted with an error message.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void enable() {
	string accnum;
	if (isAdmin == true) {
		// A bunch of if statments to make sure use is entering valid strings using regexes
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
		cout << "Please enter your account number:" << endl;
		getline(cin, input);
		accnum = input;
		if (regex_match(accnum, regex("(\\d{5})"))) {
			int accountnum = searchUserAccounts(accnum, currentUser);
			if (accountnum <= maxAccount) {
        	                if (accounts[accountnum].getActivity()) {
        	                        cout << "Account is already enabled" <<endl;
        	                        return;
	                        }
				accounts[accountnum].enable();
				cout << "Account has been enabled" << endl;
			} else {
				cout << "Account not found" << endl;
				return;
			}
		} else {
			cout << "Account number is incorrect" << endl;
			return;
		}
	} else {
		cout << "Invalid command. This is an admin only command." << endl;
		return;
	}
	transactionsLine("09", currentUser, accnum, "00000.00", "  ");
}

// Method for the disable action of the bank checks
// if user is an admin or not. If they are an admin proceeds 
// with associated outputs and input checking and applys the changes
// to current bank accounts and the records it in the transactions vector.
// If user is not an admin then they are greeted with an error message.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void disable() {
	string accnum;
	if (isAdmin == true) {
		// A bunch of if statments to make sure use is entering valid strings using regexes
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
		cout << "Please enter account number:" << endl;
		getline(cin, input);
		accnum = input;
		if (regex_match(accnum, regex("(\\d{5})"))) {
			int accountnum = searchUserAccounts(accnum, currentUser);
			if (accountnum <= maxAccount) {
                                if (!accounts[accountnum].getActivity()) {
                                        cout << "Account is already disabled" <<endl;
                                        return;
                                }
				accounts[accountnum].disable();
				cout << "Account is disabled" << endl;
			} else {
				cout << "Account not found" << endl;
				return;
			}
		} else {
			cout << "Account number is incorrect" << endl;
			return;
		}
	} else {
		cout << "Invalid command. This is an admin only command." << endl;
		return;
	}
	transactionsLine("07", currentUser, accnum, "00000.00", "  ");
}

// Method for the create action of the bank checks
// if user is an admin or not. If they are an admin proceeds 
// with associated outputs and input checking and applys the changes
// to current bank accounts and the records it in the transactions vector.
// If user is not an admin then they are greeted with an error message.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void create() {
	string accnum = "";
	string amount = "";
	if (isAdmin == true) {
		// A bunch of if statments to make sure use is entering valid strings using regexes
		cout << "Please enter account holder:" << endl;
		getline(cin, input);
		currentUser = input;
		if (regex_match(currentUser, regex("(\[A-Za-z]*)||((\[A-Za-z]*)(\\s{1})(\[A-Za-z]*))"))) {
			if (input.length() >20) {
				currentUser.resize(20);
				cout << "Account holder name truncated to " << currentUser <<"."<< endl;
			}
				cout << "Please enter the initial amount:" << endl;
				getline(cin, input);
				amount = input;
				if (regex_match(amount, regex("(\\d+)(\.)(\\d{2})"))) {
					if (stod(amount) <= 99999.99) {
						accnum = makeNewAccount(currentUser, amount);
						cout << "Account created, account number is "  << accnum << " with balance " << amount << endl;
					} else {
						cout << "Too much money in account" << endl;
						return;
					}
				} else {
					cout << "Invalid amount." << endl;
					return;
				}
		} else {
			cout << "Invalid account holder name." << endl;
			return;
		}
	} else {
		cout << "Invalid command. This is an admin only command." << endl;
		return;
	}
	transactionsLine("05", currentUser, accnum, amount, "  ");
}

// Method for the delete action of the bank checks
// if user is an admin or not. If they are an admin proceeds 
// with associated outputs and input checking and applys the changes
// to current bank accounts and the records it in the transactions vector.
// If user is not an admin then they are greeted with an error message.
// If an input error is encountered then it will exit the method and not
// make any changes or record the transaction
void deletes() {
	string accnum = "";
	if (isAdmin == true) {
		// A bunch of if statments to make sure use is entering valid strings using regexes
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (!searchUser(input)) {
			cout << "Invalid account holder name." << endl;
			return;
		}
		currentUser = input;
		cout << "Please enter your account number:" << endl;
		getline(cin, input);
		accnum = input;
		if (regex_match(accnum, regex("(\\d{5})"))) {
			int accountnum = searchUserAccounts(accnum, currentUser);
			if (accountnum <= maxAccount) {
				deleteAccount(accountnum);
				cout << "Successfully deleted account " << accnum << endl;
			} else {
				cout << "Account not found" << endl;
				return;
			}
		} else {
			cout << "Invalid account number" << endl;
			return;
		}
	} else {
		cout << "Invalid command. This is an admin only command." << endl;
		return;
	}
	transactionsLine("06", currentUser, accnum, "00000.00", "  ");
}

// Method for the login action of the bank. It checks 
// the input to see if they are correct and outputs questions. 
// It in the transactions vector If an input error is  
// encountered then it will exit the method and not
// make any changes or record the transaction
void login() {	
	fillaccounts();
	string acctype;
	cout << "Please specify account type:" << endl;
	getline(cin, input);
	if (input == "admin") {
		cout << "Login successful as admin user" << endl;	
		loggedin = true;
		isAdmin = true;
		loggedinUser = "";
		acctype = "A ";
	} else if (input == "standard") {
		
		cout << "Please enter account holder name:" << endl;
		getline(cin, input);
		if (searchUser(input) == true) {
			cout << "Login successful as standard user" << endl;
			currentUser = input;
			loggedin = true;
			loggedinUser = input;
			acctype = "S ";
		} else {
			cout << "Login failed; not a valid account holder" << endl;
			accounts = new BankAccount[maxAccount];
			return;
		}
	} else {
		cout << "Invalid account type; valid account types are standard and admin" << endl;
		return;
	}
	transactionsLine("10", loggedinUser, "00000", "00000.00", acctype);
}

// Method to complete logout action. Record the logout transaction and writes 
// all the transactions to the logout file. Also it emptys clears all the variable
// values.
void logout() {
	transactionsLine("00", loggedinUser, "00000", "00000.00", "  ");
	char* tmp ;
	for (int i = 0; i < transactions.size(); i++) {

		tmp = (char *)alloca(transactions.at(i).size() + 1);
		memcpy(tmp, transactions.at(i).c_str(), transactions.at(i).size() + 1);
		trans << tmp << endl;
	}
	// This is done to empty out the transaction vector
	transactions.clear();
	loggedin = false;
	isAdmin = false;
	currentUser = "";
}

int main(int argc, char* argv[])
{
	string inputfile;
	string inputpath;
	string directory;
	string outputpath;
	ifstream in;
	ofstream out;
	ifstream firstpass;
	if (argc > 2)
	{
		inputfile = string(argv[1]);
		directory = string(argv[2]);
		inputpath = "./INPUT/" + directory + "/"+ inputfile;
		outputpath = "./NEWOUTPUT/" + directory + "/" + inputfile;
		transactionpath = "./TRANSACTION/" + directory + "/" + inputfile;
		firstpass.open(inputpath);
		firstpass.close();
		in.open(inputpath);
		out.open(outputpath);
		trans.open(transactionpath);
		cin.rdbuf(in.rdbuf());
		cout.rdbuf(out.rdbuf());
	} else {
		transactionpath = "TransactionFile.txt";
	}
	// Terminate program on FINAL input 0

	while (true && !cin.eof()) {

		// If the input is a text file, we need to change the input streeam to read from it.
		// ASSUME THAT A TEXTFILE WILL BE LOADED INSTEAD OF TEXT COMMANDS
		// Read input from the user
		getline(cin, input);
		if (loggedin == true) {
			if (input == "withdrawal") {
				withdraw();
			} else if (input == "deposit") {
				deposit();
			} else if (input == "transfer") {
				transfer();
			} else if (input == "paybill") {
				paybill();
			} else if (input == "changeplan") {
				changeplan();
			} else if (input == "enable") {
				enable();
			} else if (input == "disable") {
				disable();
			} else if (input == "login") {
				cout << "Already logged in. Logout before logging with another account holder." << endl;
			} else if (input == "create") {
				create();
			} else if (input == "delete") {
				deletes();
			} else if (input == "logout") {
				logout();
				cout << "Logout successful." << endl;
			} else {
				cout << "Not a valid command." << endl;
			}
		} else {
			if (input == "login") {
				login();
			} else {
				cout << "Invalid Command." << endl;
			}
		}
	}
	in.close();
	out.close();
	trans.close();
    return 0;
}
