package TestSuits;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;

import org.junit.Before;
import org.junit.Rule;
import Default.TransactionReader;
import org.junit.rules.TemporaryFolder;

public class TransactionReader_peek_Test {
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
	public void getsFirst() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals("22 Jason Runzer         00000 00000.00 S ",tr.peek());
	}
	
	@Test
	public void doesNotConsume() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals("22 Jason Runzer         00000 00000.00 S ",tr.peek());
		assertEquals("22 Jason Runzer         00000 00000.00 S ",tr.peek());
		assertEquals("22 Jason Runzer         00000 00000.00 S ",tr.getNext());
	}
	
	@Test
	public void peekNoContent() throws IOException {
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals(null, tr.peek());
	}
	@Test
	public void noContentpeekAfterConsume() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		tr.getNext();
		assertEquals(null, tr.peek());
	}
	
	@Test
	public void peekSecond() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.newLine();
		bw.write("testing 123");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		tr.getNext();
		assertEquals("testing 123", tr.peek());
	}
}