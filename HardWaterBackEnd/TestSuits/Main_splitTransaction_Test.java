package TestSuits;
import org.junit.Test;
import Default.Main;
import static org.junit.Assert.*;

public class Main_splitTransaction_Test {

	@Test
	public void validSplit() {
		String split = "10 Jason Runzer         00000 00000.00 S ";
		String[] array = {"10", "Jason Runzer         ", "00000", "00000.00", "S "};
		assertArrayEquals(Main.splitTransaction(split), array);
	}
	
	@Test
	public void shortString() {
		assertArrayEquals(Main.splitTransaction("hell0"), null);
	}
	
	@Test
	public void longString() {
		assertArrayEquals(Main.splitTransaction("10 Jason Runzer         00000 00000.00 S l"), null);
	}
}
