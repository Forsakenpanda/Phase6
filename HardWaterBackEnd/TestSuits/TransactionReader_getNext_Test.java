package TestSuits;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;

import org.junit.Before;
import org.junit.Rule;
import Default.TransactionReader;
import org.junit.rules.TemporaryFolder;

public class TransactionReader_getNext_Test {
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
	public void getsFirstNext() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.newLine();
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.close();
		TransactionReader tr = new TransactionReader (testFolder.getRoot().getPath() + "/");
		assertEquals("22 Jason Runzer         00000 00000.00 S ",tr.getNext());
	}
	
	@Test
	public void getsTwo() throws IOException {
		bw.write("lol Jason Runzer         00000 00000.00 S ");
		bw.newLine();
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.newLine();
		bw.write("8234192842109428219034813409810942");
		bw.close();
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		assertEquals("lol Jason Runzer         00000 00000.00 S ", tr.getNext());
		assertEquals("02 Jason Runzer         00001 00100.00 F ", tr.getNext());
	}
		
	@Test
	public void getsMany() throws IOException {
		bw.write("lol Jason Runzer         00000 00000.00 S ");
		bw.newLine();
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.newLine();
		bw.write("8234192842109428219034813409810942");
		bw.newLine();
		bw.write("lasfasdf09810942");
		bw.close();
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		assertEquals("lol Jason Runzer         00000 00000.00 S ", tr.getNext());
		assertEquals("02 Jason Runzer         00001 00100.00 F ", tr.getNext());
		assertEquals("8234192842109428219034813409810942", tr.getNext());
	}
	@Test
	public void getsLast() throws IOException {
		bw.write("lol Jason Runzer         00000 00000.00 S ");
		bw.newLine();
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.close();
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		assertEquals("lol Jason Runzer         00000 00000.00 S ", tr.getNext());
		assertEquals("02 Jason Runzer         00001 00100.00 F ", tr.getNext());
	}
	
	@Test
	public void pastLast() throws IOException {
		bw.write("lol Jason Runzer         00000 00000.00 S ");
		bw.close();
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		tr.getNext();
		assertEquals(null,tr.getNext());
	}
}
