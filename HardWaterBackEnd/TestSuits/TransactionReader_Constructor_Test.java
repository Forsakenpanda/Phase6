package TestSuits;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import org.junit.Before;
import org.junit.Rule;
import Default.TransactionReader;
import org.junit.rules.TemporaryFolder;


public class TransactionReader_Constructor_Test {
	public File newFile;
	public FileWriter fw;
	public BufferedWriter bw;
	public File secondFile;
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

	@Before
	public void makeFile() throws IOException {
		newFile = testFolder.newFile("1");
		fw = new FileWriter(newFile);
		bw = new BufferedWriter(fw);
	}
	
	@Test
	public void mergeOneFile() throws IOException {
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		File newFile = testFolder.newFile("mergedTransactionFile.txt");
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		FileReader fr = new FileReader(newFile);
		BufferedReader br = new BufferedReader(fr);
		assert(br.readLine() != null);
	}
	
	@Test
	public void mergeTwoFiles() throws IOException {
		secondFile = testFolder.newFile("transaction2");
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		fw.close();
		fw = new FileWriter(secondFile);
		bw = new BufferedWriter(fw);
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.close();
		fw.close();
		File merged = testFolder.newFile("mergedTransactionFile.txt");
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		FileReader fr = new FileReader(merged);
		BufferedReader br = new BufferedReader(fr);
		assertEquals(br.readLine(),"22 Jason Runzer         00000 00000.00 S ");
		assertEquals(br.readLine(),"02 Jason Runzer         00001 00100.00 F ");
		br.close();
		fr.close();
	}
	
	@Test
	public void mergeManyFiles() throws IOException {
		secondFile = testFolder.newFile("transaction2");
		bw.write("22 Jason Runzer         00000 00000.00 S ");
		bw.close();
		fw.close();
		fw = new FileWriter(secondFile);
		bw = new BufferedWriter(fw);
		bw.write("02 Jason Runzer         00001 00100.00 F ");
		bw.close();
		fw.close();
		File thirdFile = testFolder.newFile("transaction3");
		fw = new FileWriter(thirdFile);
		bw = new BufferedWriter(fw);
		bw.write("00 Jason Runzer         00000 00000.00 S ");
		bw.close();
		fw.close();
		File merged = testFolder.newFile("mergedTransactionFile.txt");
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		FileReader fr = new FileReader(merged);
		BufferedReader br = new BufferedReader(fr);
		assertEquals(br.readLine(), "22 Jason Runzer         00000 00000.00 S ");
		assertEquals(br.readLine(), "02 Jason Runzer         00001 00100.00 F ");
		assertEquals(br.readLine(), "00 Jason Runzer         00000 00000.00 S ");
		br.close();
		fr.close();
	}
	
	@Test
	public void mergeNone() throws IOException {
		newFile.delete();
		File merged = testFolder.newFile("mergedTransactionFile.txt");
		TransactionReader tr = new TransactionReader(testFolder.getRoot().getPath() + "/");
		FileReader fr = new FileReader(merged);
		BufferedReader br = new BufferedReader(fr);
		assertEquals(br.readLine(), null);
		br.close();
		fr.close();
	}

}
