package TestSuits;
import Default.AccountWriter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class AccountWriter_Constructor_Test {

	public String fileLocation;

	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void createCurrent() {
		fileLocation = testFolder.getRoot().getPath()+"/";
		
		AccountWriter aw = new AccountWriter(fileLocation, true);
		boolean check = new File(fileLocation, "current-valid-accounts.txt").exists();
		assertEquals(check, true);
		
	}
	
	@Test
	public void createMaster() {
		fileLocation = testFolder.getRoot().getPath()+"/";
		
		AccountWriter aw = new AccountWriter(fileLocation, false);
		boolean check = new File(fileLocation, "master-valid-accounts.txt").exists();
		assertEquals(check, true);
		
	}

	
}
