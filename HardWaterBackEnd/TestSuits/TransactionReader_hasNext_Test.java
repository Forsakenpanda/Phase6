package TestSuits;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;

import org.junit.Before;
import org.junit.Rule;
import Default.TransactionReader;
import org.junit.rules.TemporaryFolder;

public class TransactionReader_hasNext_Test {
	public File newFile;
	public FileWriter fw;
	public BufferedWriter bw;
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Before
	public void makeFile() throws IOException {
		newFile = testFolder.newFile("transaction");
		fw = new FileWriter(newFile);
		bw = new BufferedWriter(fw);
	}
	
	@Test
	public void hasContent() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals(true,tr.hasNext());
	}
	@Test
	public void noContents() throws IOException {
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals(false,tr.hasNext());
	}
	
	@Test
	public void noContentsAfter() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		tr.getNext();
		assertEquals(false,tr.hasNext());
	}
}
