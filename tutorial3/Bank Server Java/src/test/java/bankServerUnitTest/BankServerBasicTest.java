package bankServerUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bankServerExercise.BankException;
import bankServerExercise.BankServer;
import bankServerExercise.BankServerInterface;
import bankServerExercise.Transaction;

/** 
 * This class tests the normal, correctly-implemented behaviour of the bank server
 * 
 * @author rda
 *
 */
public class BankServerBasicTest {
	BankServerInterface server;

	@Before
	public void setUp() throws Exception {
		server = new BankServer();
		server.createAccount("123");
	}

	@Test
	public void testEmptyWhenCreated()
	{
		BankServer server2 = new BankServer(); //need an empty server
		assertEquals(0, server2.getNetValue());
	}
	
	@Test
	public void testToString()
	{
		assertEquals("BankServer - 1 accts worth �0", server.toString());
		
		assertTrue(server.credit("123", 100));
		server.createAccount("456");
		assertEquals("BankServer - 2 accts worth �100", server.toString());
	}
	
	@Test
	public void testCreateAccount() {
		assertEquals(1, server.getNoOfAccounts());
		
		server.createAccount("456");
		
		assertEquals(2, server.getNoOfAccounts());
		assertEquals(0, server.getNetValue());
		assertEquals(0, server.getAccountBalance("123"));
	}
	
	@Test
	public void testCredit()
	{		
		assertTrue(server.credit("123", 100));		
		assertEquals(100, server.getAccountBalance("123"));
		assertEquals(100, server.getNetValue());
	}

	@Test
	public void testValidDebit()
	{
		server.credit("123", 100);
		assertTrue(server.debit("123", 50));
		assertEquals(50, server.getAccountBalance("123"));
		assertEquals(50, server.getNetValue());
	}
	
	@Test
	public void testCloseAccount()
	{
		server.createAccount("456");
		server.credit("456", 97);
		
		server.closeAccount("456");
		assertEquals(1, server.getNoOfAccounts()); //just the default one we added
		assertEquals(0, server.getNetValue());
	}
	
	@Test
	public void testCantCloseOverdrawnAccount()
	{
		server.createAccount("456");
		server.debit("456", 45);
		
		try {
			server.closeAccount("456");
			fail();
		}
		catch (BankException e)
		{
		}
	}

	@Test
	public void testTranslog()
	{
		server.createAccount("456");
		server.debit("456", 45);
		server.credit("456", 45);
		server.closeAccount("456");
		
		List<Transaction> tList = server.getTransactionHistory();
//		for(Transaction t : tList)
//		{
//			System.out.println(t.getType());
//		}
		assertEquals(5, tList.size()); //should be an extra CREATE_ACCOUNT in index 0 of log from setUp() of this test class
		assertEquals(Transaction.TransactionType.DEBIT, tList.get(2).getType());
	}
	
	
//	Tutorial Answers
	@Test
	public void testCreditThenDebitReturnsToZero()
	{
	    assertTrue(server.credit("123", 50));
	    assertTrue(server.debit("123", 50));

	    assertEquals(0, server.getAccountBalance("123"));
	    assertEquals(0, server.getNetValue());
	}
	
	
	@Test
	public void testNetValueAcrossMultipleAccounts()
	{
	    server.createAccount("456");

	    server.credit("123", 40);
	    server.credit("456", 60);

	    assertEquals(2, server.getNoOfAccounts());
	    assertEquals(100, server.getNetValue());
	}


	
	
	
}
