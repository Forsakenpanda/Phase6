package TestSuits;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import Default.Main;

public class Main_dispatch_Test {

	public File folder;
	public File[] fileList;
	public FileWriter fileWriter;
	public BufferedWriter bufferedWriter;
		
	@Test
	public void login() throws IOException {
		String transaction = "10 Jason Runzer         00000 00000.00 S ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(true, m.dispatch(transaction));
		
	}
	
	@Test
	public void withdrawal() throws IOException {
		String transaction = "01 Jason Runzer         00001 00020.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(true, m.dispatch(transaction));
	}
		
	@Test
	public void paybill() throws IOException {
		String transaction = "03 Jason Runzer         00001 00020.00 TV";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void deposit() throws IOException {
		String transaction = "04 Jason Runzer         00001 00020.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void create() throws IOException {
		String transaction = "05 Jason Runzer         00011 00020.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		m.dispatch("10                      00000 00000.00 A ");
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void delete() throws IOException {
		String transaction = "04 Jason Runzer         00001 00000.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		m.dispatch("10                      00000 00000.00 A ");
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void disable() throws IOException {
		String transaction = "07 Jason Runzer         00001 00000.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		m.dispatch("10                      00000 00000.00 A ");
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void changePlan() throws IOException {
		String transaction = "08 Jason Runzer         00001 00000.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		m.dispatch("10                      00000 00000.00 A ");
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void logout() throws IOException {
		String transaction = "00 Jason Runzer         00000 00000.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(true, m.dispatch(transaction));
	}
	
	@Test
	public void shortTransaction() throws IOException {
		String transaction = "00 Jason Runzer         00000 00000.00  ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(false, m.dispatch(transaction));
	}
	
	@Test
	public void longTransaction() throws IOException {
		String transaction = "00 Jason Runzer         00000 00000.00    ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(false, m.dispatch(transaction));
	}
	
	@Test
	public void wrongCode() throws IOException {
		String transaction = "20 Jason Runzer         00000 00000.00   ";
		String[] args = {""};
		Main m = new Main();
		m.main(args);
		assertEquals(false, m.dispatch(transaction));
	}


}
