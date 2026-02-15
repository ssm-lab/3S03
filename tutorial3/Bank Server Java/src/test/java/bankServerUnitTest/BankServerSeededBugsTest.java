package bankServerUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bankServerExercise.BankException;
import bankServerExercise.BankServer;
import bankServerExercise.BankServerInterface;
import bankServerExercise.Transaction;
import bankServerExercise.Transaction.TransactionType;

/**
 * This class contains tests to that check that all the seeded bugs all still "work"
 * 
 * @author rda
 *
 */
public class BankServerSeededBugsTest {
	BankServerInterface server;
	
	@Before
	public void setUp() throws Exception {
		server = new BankServer();
		server.createAccount("123");
	}

	@Test
	public void testOverdrawnDebit()
	{
		//seeded bug - the overdraft check is made on the initial value of the account, not on the value after the debit
		
		assertTrue(server.debit("123", 50));
		assertEquals(-50, server.getAccountBalance("123"));
		assertEquals(-50, server.getNetValue());
		
		//second attempt should fail, because it _starts_ overdrawn
		assertFalse(server.debit("123", 50));
		assertEquals(-50, server.getAccountBalance("123"));
		assertEquals(-50, server.getNetValue());
	}
	
	@Test
	public void testCloseLastAccount()
	{
		//seeded bug - you can close the last account when it's overdrawn (and in doing so you can lose money, which will make the net value
		//of the bank inconsistent with its transaction history)
		
		server.debit("123", 100);
		server.closeAccount("123");
		assertEquals(0, server.getNoOfAccounts());
		
		List<Transaction> tList = server.getTransactionHistory();
		int transBalance = 0;
		for(Transaction t : tList)
		{
			if(t.getType() == TransactionType.CREDIT)
			{
				transBalance += t.getAmount();
			}
			else if(t.getType() == TransactionType.DEBIT)
			{
				transBalance -= t.getAmount();
			}
		}
		
		//balance of bank accounts will be zero because there are no accounts right now, but the balance calculated from the
		//transactions will include the owed money lost when we closed the last account
		assertEquals(-100, transBalance);  
	}
	
	@Test
	public void testZeroSavingsTarget()
	{
		//zero savings target not supposed to be allowed, but it is
		
		server.setSavingsTarget("123", 100);
		try
		{
			server.setSavingsTarget("123", -1);
			fail();
		}
		catch(BankException b) {}
		
		server.setSavingsTarget("123", 0); //should throw, but doesn't 
	}
	
	@Test
	public void testSavingsProgressReturnsWrongThing()
	{
		//savings progress supposed to return -1 if no target set, but returns 100
		
		assertEquals(100, server.getSavingsPercentage("123"));		
	}
	
	@Test
	public void testZeroBalanceSavingsProgress()
	{
		//If a savings target is set (nonzero) and balance is zero then we can get a divide-by-zero 
		
		server.getSavingsPercentage("123");
		
		try {
			server.setSavingsTarget("123", 100);
			server.getSavingsPercentage("123");
			fail();
		} catch (ArithmeticException e) {}
	}

	@Test
	public void testGetSavingsProgressForNonexistentAccount()
	{
		//unlike most of the other public methods, getSavingsPercentage() doesn't check if an account exists before doing things to it
		
		try
		{
			server.getSavingsPercentage("no such account)");
			fail();
		}
		catch (NullPointerException e)
		{
		}
	}
	
	@Test
	public void testCorrectMessageOnDebitNonexistent()
	{
		//an error that can happen on debit or credit always gives a message about debit
		
		try
		{
			server.credit("456", 500); //invalid because no such account
		}
		catch(BankException e)
		{
			assertEquals("debit", e.getMessage().substring(0,5));
		}
	}
	
//	Tutorial Answers
	@Test
	public void testServerBecomesInactiveAfterClosingLastAccount()
	{
	    // bug - closing the last account deactivates the server,
	    // even if the account is overdrawn

	    assertTrue(server.getActive());

	    server.debit("123", 10);   // overdraw the only account
	    server.closeAccount("123"); // allowed due to bug

	    assertFalse(server.getActive()); // server is now permanently inactive
	}

	
	
	
}
