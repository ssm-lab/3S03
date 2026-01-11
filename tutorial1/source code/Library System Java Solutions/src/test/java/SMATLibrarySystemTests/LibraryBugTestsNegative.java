package SMATLibrarySystemTests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import SMATLibrarySystem.Customer;
import SMATLibrarySystem.CustomerType;
import SMATLibrarySystem.Library;
import SMATLibrarySystem.LibraryBook;
import SMATLibrarySystem.LibraryException;
import SMATLibrarySystem.StandardCustomerType;

/**
 * These are tests that check that the seeded bugs are still there
 * 
 * This is the NEGATIVE version of these tests - the tests fail if the bug is present. This is the way you'd normally write them,
 * if your goal was to check the code versus the spec (or, more generally, to produce code that works).
 * 
 * NB note how I've changed some of the test names from the positive version, to make it clear what intended behaviour they're testing
 * 
 */
public class LibraryBugTestsNegative {

	private Library lib;

	@Before
	public void setUp() throws Exception {
		lib = new Library();
	}

	@Test
	public void toStringNoBooks() {
		assertEquals(0, lib.getBookCount());
		assertEquals("Library with 0 books", lib.toString()); //"book" should be plural, but is singular
	}
	
	
	//"Input validation code that accepts some inputs it shouldn�t"
	@Test
	public void rejectsThreePartDewey() {
		try 
		{
			lib.addBook(new LibraryBook("A Book", 1981, "123.56789.4")); //should be rejected, but is accepted
			fail();
		}
		catch (LibraryException e)
		{}
	}

	
	@Test
	public void throwsExceptionOnDeweyWithNoBookNumber()
	{
		try
		{
			lib.addBook(new LibraryBook("A Book", 1981, "123")); //will throw ArrayOutOfBounds when it's supposed to throw a LibraryException
			fail();
		} catch (LibraryException e) {} 
	}
	
	
	@Test
	public void getByDeweyIfZeroClassAndDivision()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "003.56789"); //will get filed under "300.56789"
		lib.addBook(b);
		LibraryBook b2 = lib.getByDewey("003.56789"); //NB there is no (relevant) fault in anything called from here - the error happened two lines ago
		assertEquals(b,b2);
	}
	
	
	//"A performance issue where an overridden method meets the letter of its contract but not the spirit of its purpose (you may struggle to create an actual test for this, but you should at least think about how it could be done)"
	@Test
	public void testStrongHashCode()
	{
		fail();
		
		//This is a malformed test - it doesn't actually test anything! Why have I included it? Well:
		
		//The hashCode() implementation in LibraryBook is weak - it meets Java's hashCode() contract (e.g. books that are 
		//equal() will have the same hash code) but it doesn't discriminate between books published in the same year, 
		//even thought there's lots of other information that could be used for hashing
		
		//It's hard, though to test for this. There's no one right hashCode() for a given class. 
		//Most likely way to find its weakness by testing would be to do a performance test on a hash table where the 
		//key was a library book perhaps combined with the use of a profiler (which might show it spending a lot of time in hash table lookups)
		
		//Another thing you could do would be to create several instances of LibraryBook will distinctly different field values, and assert
		//that they can't all have the same hash code. In theory, you could just be unlucky, given a finite hash code size, but this is unlikely.
		
		//Note, however, that in the code I gave you LibraryBook.hashCode() is never used - the only hashing that is done is of the Dewey number
		//which is the key for the HashMap. That will use String.hashCode().
	}
	
	
	//"An exception is thrown when it should be, but it�s of the wrong type"
	@Test
	public void testExceptionWhenTakeOutWithoutLibrary()
	{	
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		Customer c = new Customer();
		
		try
		{
			c.getBooksHeld(); //supposed to throw a LibraryException, but will throw NullPointerException instead 
			fail();
		} catch (LibraryException e)
		{
		}			
	}
	
	
	//"A sequence of actions that can lead to a system state that doesn�t make sense"
	@Test
	public void testCustomersLoseDeletedBooks()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);		
		
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c, b2);
		
		assertEquals(2, lib.getBookCount());
		assertEquals(2, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) );
		assertTrue( c.getBooksHeld().contains(b2) );
				
		lib.deleteBook(b2); //book gets deleted from library's main book list, but not from its customer holdings list
		assertEquals(1, lib.getBookCount());
		assertEquals(1, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) ); //NB with the library code I gave you, test never gets to here because assert on previous line fails
		assertFalse( c.getBooksHeld().contains(b2) );
	}
	
	
	@Test
	public void testMultipleCopiesAdded()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.45678");
		lib.addBook(b);
		lib.addBook(b); //second copy
		assertEquals(2, lib.getBookCount()); //but there's still only one book in lib
	}
	
	
	//"A string formatting error that has a significant consequence (i.e. it does more than lead to a bad string being printed)"
	@Test
	public void testToStringAfterDeleteLastCustomerAndTheyHadHoldings()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		lib.addBook(b);
		
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		
		lib.deregister(c); //will reduce customer list (to empty), but not change holdings 
		
		assertEquals("Library with 1 book", lib.toString()); //will dry to divide # (books out on loan) by the # of customers - latter is zero
		
		//NB in order for this bug to cause an exception (rather than just an Inf result) I've had to write the code for it using integer divisions,
		//which is really a bug in itself because we'll often be in very small numbers where integer division will give misleading results
		//(e.g. lots of "average of 0 books out per customer" when it's really about 0.9).
	}
	
	
	private class LongLoanCustomerType implements CustomerType
	{

		@Override
		public Calendar getDueDate(Calendar startDate) {
			Calendar due = (Calendar) startDate.clone();
			due.add(Calendar.MONTH, 3);
			return due;
		}		
	}
	
	//"A bug that will be exhibited only when a new derived class is implemented that has a certain type of behaviour"
	@Test
	public void testAppliesCustomerTypeDueDateRules ()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		lib.addBook(b);
		
		Customer c = new Customer("Gerald", Calendar.getInstance(), new LongLoanCustomerType());		
		Calendar now = Calendar.getInstance();
		lib.takeOutBook(c, b);
		
		Calendar due = lib.getDueDate(c,b); //will be due in the standard 1 month, not the 3 that it should be
		
		assertEquals(now.get(Calendar.MONTH) + 3, due.get(Calendar.MONTH) ); //bug: this test will spuriously fail in Oct, Nov or Dec!
	}
}


