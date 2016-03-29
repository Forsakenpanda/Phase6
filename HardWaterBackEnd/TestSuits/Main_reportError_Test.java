package TestSuits;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import Default.Main;

public class Main_reportError_Test {
	
	//redirect output taken from: http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
	final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
	final PrintStream store = System.out;
	
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(myOut));
	    //System.setErr(new PrintStream(errContent));
	}
	
	@Test
	public void correctFormat() {
		String error = "something went wrong";
		Main.reportError(error);
		assertEquals(myOut.toString().trim(), "ERROR: " + error);
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(store);
	}

}
