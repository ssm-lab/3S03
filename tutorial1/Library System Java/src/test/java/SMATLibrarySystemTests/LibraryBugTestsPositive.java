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
 * This is the POSITIVE version of these tests - the tests pass if the bug is present, fail if its not. This is how I wrote them
 * originally - I wanted to make buggy code for the practical, so the buggy behaviour (wrt to the spec) was my intended
 * behaviour while writing it.
 * 
 */
public class LibraryBugTestsPositive {

	private Library lib;

	@Before
	public void setUp() throws Exception {
		lib = new Library();
	}

	@Test
	public void toStringNoBooks() {
		assertEquals(0, lib.getBookCount());
		assertEquals("Library with 0 book", lib.toString()); //"book" should be plural, but is singular
	}
	
	@Test
	public void acceptsThreePartDewey() {
		lib.addBook(new LibraryBook("A Book", 1981, "123.56789.4")); //should be rejected, but is accepted
	}

	@Test
	public void throwsWrongExceptionOnDeweyWithNoBookNumber()
	{
		try
		{
			lib.addBook(new LibraryBook("A Book", 1981, "123")); //will throw ArrayOutOfBounds when it's supposed to throw a LibraryException
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {} 
	}
	
	@Test
	public void cantGetByDeweyIfZeroClassAndDivision()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "003.56789"); //will get filed under "300.56789"
		lib.addBook(b);
		LibraryBook b2 = lib.getByDewey("003.56789"); //NB there is no (relevant) fault in anything called from here - the error happened two lines ago
		assertNull(b2);
	}
	
	@Test
	public void testWeakHashCode()
	{
		//the hashCode() implementation in LibraryBook is weak - it meets Java's hashCode() contract (e.g. books that are 
		//equal() will have the same hash code) but it doesn't discriminate between books published in the same year, 
		//even thought there's lots of other information that could be used for hashing
		
		//It's hard, though to test for this. There's no one right hashCode() for a given class. 
		//Most likely way to find its weakness by testing would be to do a performance test on a hash table where the 
		//key was a library book perhaps combined with the use of a profiler (which might show it spending a lot of time in hash table lookups)
		
		//Note, however, that in the code I gave you LibraryBook.hashCode() is never used - the only hashing that is done is of the Dewey number
		//which is the key for the HashMap. That will use String.hashCode().
	}
	
	@Test
	public void testWrongExceptionWhenTakeOutWithoutLibrary()
	{	
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		Customer c = new Customer();
		
		try
		{
			c.getBooksHeld(); //supposed to throw a LibraryException, but will throw NullPointerException instead 
			fail();
		} catch (NullPointerException e)
		{
		}			
	}
	
	@Test
	public void testCustomersKeepDeletedBooks()
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
		assertEquals(2, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) );
		assertTrue( c.getBooksHeld().contains(b2) );
	}
	
	@Test
	public void testMultipleCopiesNotAdded()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.45678");
		lib.addBook(b);
		lib.addBook(b); //second copy
		assertEquals(1, lib.getBookCount()); //but there's still only one book in lib
	}
	
	@Test
	public void testDivByZeroWhenDeleteLastCustomerIfHasHoldings()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		lib.addBook(b);
		
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		
		lib.deregister(c); //will reduce customer list (to empty), but not change holdings 
		
		try {
			lib.toString(); //will dry to divide # books out by the # of customers - latter is zero
			fail();
		} catch ( ArithmeticException e)
		{			
		}
		
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
	
	
//	@Test
//	public void testIgnoresCustomerTypeDueDateRules ()
//	{
//		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
//		lib.addBook(b);
//		
//		Customer c = new Customer("Gerald", Calendar.getInstance(), new LongLoanCustomerType());		
//		Calendar now = Calendar.getInstance();
//		lib.takeOutBook(c, b);
//		
//		Calendar due = lib.getDueDate(c,b); //will be due in the standard 1 month, not the 3 that it should be
//		
//		assertEquals(now.get(Calendar.MONTH) + 1, due.get(Calendar.MONTH) ); //bug: this test will spuriously fail in December!
//	}
}


