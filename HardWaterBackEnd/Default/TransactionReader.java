package Default;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * The TransactionReader class provides access to the transaction file line by line.
 * It acts just like an iterator with the necessary methods implemented.
 * @author HardWater
 */
/**
* Copyright 2016 Jason, Stuart, Muhammad copyright
*/
public class TransactionReader {
	private int index;
	private File folder;
	private File[] fileList;
	private FileReader transactionFileReader;
	private BufferedReader transactionBufferedReader;
	private String line;
	private String nextLine;

	/**
	 * Constructs a Transaction reader.
	 * <p>
	 * Merges all transaction files in the location provided and opens a new file reader
	 * to read from the new file.
	 * </p>
	 * @param location the path to the transaction files.
	 * @throws IOException 
	 */
	public TransactionReader(String location) throws IOException {
		line = "";
		nextLine = "";
		index = -1;
		try {		
			transactionFileReader = new FileReader(location);
			transactionBufferedReader = new BufferedReader(transactionFileReader);
			line = transactionBufferedReader.readLine();
			nextLine = transactionBufferedReader.readLine();
		} catch (Exception e) {
			Main.reportError("Something went wrong with the merged file");
		}
	}
	
	/**
	 * Determines if there is another transaction to process.
	 * @return false if there is no more transactions in the file.
	 */
	public boolean hasNext() {
		return line != null;
	}
	
	/**
	 * Gets the next transaction.
	 * @return the next transaction in the file.
	 */
	public String getNext() {
		String tempLine = line;
		line = nextLine;
		try {
			nextLine = transactionBufferedReader.readLine();
		} catch (Exception e) {
			Main.reportError("something went wrong with the transaction reader");
		}
		return tempLine;
	}
	
	/**
	 * Gets the next transaction without consuming it.
	 * @return the next transaction.
	 */
	public String peek() {
		return line;
	}
}
