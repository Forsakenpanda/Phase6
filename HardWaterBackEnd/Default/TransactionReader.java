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
		// referenced for listing files: http://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
		folder = new File(location);
		fileList = folder.listFiles();
		try {
			// used from https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
			File mergedTF = new File(location + "mergedTransactionFile.txt");
			// delete the old merged file
			mergedTF.delete();
			FileWriter fileWriter = new FileWriter(location + "mergedTransactionFile.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			// merge all the transaction files in the directory
			for (int i = 0; i < fileList.length; i++) {
				// check if the file is not the one to merge too
				if(fileList[i].isFile() && !fileList[i].getName().equals("mergedTransactionFile.txt")) {
					String name = fileList[i].getName();
					try {
						String line;
						// make a new file reader for the new file in the list
						FileReader fileReader = new FileReader(location + name);
						BufferedReader bufferedReader = new BufferedReader(fileReader);
						// append the entire file to the mergedFile
						while((line = bufferedReader.readLine()) != null) {
							bufferedWriter.write(line);
							// add a new line if there is more to go
							bufferedWriter.newLine();
						}
						bufferedReader.close();
					} catch (Exception e) {
						Main.reportError("The files could not be read");
					}
					// delete file somewhere here
					// fileList[i].delete();
				}
			}
			bufferedWriter.close();
		} catch (Exception e) {
			Main.reportError("Something went wrong with the merged file");
		}
		// try to open up the fileReader and get the first two lines of the buffer
		try {		
			transactionFileReader = new FileReader(location + "mergedTransactionFile.txt");
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
