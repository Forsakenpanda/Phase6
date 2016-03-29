package TestSuits;
import Default.AccountWriter;
import Default.Account;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class AccountWriter_write_Test {
	public File newFile;
	public FileReader fr;
	public BufferedReader bw;
	public ArrayList<Account> accounts;
	public String fileLocation;
	public String comparing;
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	

	
	@Test
	public void writeToFileCurrent() throws IOException {
		accounts = new ArrayList<Account>();
		fileLocation = testFolder.getRoot().getPath()+"/";
		AccountWriter aw = new AccountWriter(fileLocation, true);
		accounts.add(new Account(20, "Stuart", true, 1000.00, 'S', 0));
		aw.write(accounts);
		fr = new FileReader(fileLocation+"current-valid-accounts.txt");
		bw = new BufferedReader(fr);
		comparing = bw.readLine();
		assertEquals(comparing, "00020 Stuart               A 01000.00 S");
		
	}
	
	@Test
	public void writeToFileMaster() throws IOException {
		accounts = new ArrayList<Account>();
		fileLocation = testFolder.getRoot().getPath()+"/";
		AccountWriter aw = new AccountWriter(fileLocation, false);
		accounts.add(new Account(20, "Stuart", true, 1000.00, 'S', 0));
		aw.write(accounts);
		fr = new FileReader(fileLocation+"master-valid-accounts.txt");
		bw = new BufferedReader(fr);
		comparing = bw.readLine();
		assertEquals(comparing, "00020 Stuart               A 01000.00 0000 S");
	}
	
	@Test
	public void endOfFileCheckMaster() throws IOException {
		accounts = new ArrayList<Account>();
		fileLocation = testFolder.getRoot().getPath()+"/";
		AccountWriter aw = new AccountWriter(fileLocation, false);
		accounts.add(new Account(20, "Stuart", true, 1000.00, 'S', 0));
		aw.write(accounts);
		fr = new FileReader(fileLocation+"master-valid-accounts.txt");
		bw = new BufferedReader(fr);
		comparing = bw.readLine();
		comparing = bw.readLine();
		assertEquals(comparing, "00000 END_OF_FILE          D 00000.00 0000 N");
	}
	
	@Test
	public void endOfFileCheckCurrent() throws IOException {
		accounts = new ArrayList<Account>();
		fileLocation = testFolder.getRoot().getPath()+"/";
		AccountWriter aw = new AccountWriter(fileLocation, true);
		accounts.add(new Account(20, "Stuart", true, 1000.00, 'S', 0));
		aw.write(accounts);
		fr = new FileReader(fileLocation+"current-valid-accounts.txt");
		bw = new BufferedReader(fr);
		comparing = bw.readLine();
		comparing = bw.readLine();
		assertEquals(comparing, "00000 END_OF_FILE          D 00000.00 N");
	}
	
	
}
