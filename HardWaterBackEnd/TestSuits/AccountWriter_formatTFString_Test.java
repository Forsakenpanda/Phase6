package TestSuits;
import Default.AccountWriter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AccountWriter_formatTFString_Test {

	@Test
	public void normalInputAdimn() {
		AccountWriter aw = new AccountWriter("hello.txt", true);
		assertEquals(aw.formatTFString(10, "Stuart", 'D', 1000.00, 1, 'S', false), "00010 Stuart               D 01000.00 0001 S");
	}
	
	@Test
	public void nomralInputStandard() {
		AccountWriter bw = new AccountWriter("hello.txt", false);
		assertEquals(bw.formatTFString(10, "Stuart", 'D', 1000.00, 1, 'S', true), "00010 Stuart               D 01000.00 S");
	}
	
	@Test
	public void longName() {
		AccountWriter cw = new AccountWriter("hello.txt", true);
		assertEquals(cw.formatTFString(10, "Stuart VERYLONGGGGGGGGGGGGGGG", 'D', 1000.00, 1, 'S', false), "00010 Stuart VERYLONGGGGGG D 01000.00 0001 S");
	}
	
	@Test
	public void doubleAsInt() {
		AccountWriter dw = new AccountWriter("hello.txt", true);
		assertEquals(dw.formatTFString(10, "Stuart", 'D', 1000, 1, 'S', false), "00010 Stuart               D 01000.00 0001 S");
	}
}
