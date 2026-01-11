package SMATLibrarySystemTests;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 * These are tests that check those system features that are meant to work correctly
 * 
 */
public class LibraryTest {
	private Library lib;

	@Before
	public void setUp() throws Exception {
		lib = new Library();
	}

	@Test
	public void testAddBook() {
		assertEquals(0,lib.getBookCount());
		lib.addBook(new LibraryBook());
		assertEquals(1,lib.getBookCount());
	}
	
	@Test
	public void testDeleteBook() {
		assertEquals(0,lib.getBookCount());
		LibraryBook b = new LibraryBook();
		lib.addBook(b);
		assertEquals(1,lib.getBookCount());
		
		lib.deleteBook(b);
		assertEquals(0,lib.getBookCount());
	}
	
	@Test
	public void testToString() {
		assertEquals(0,lib.getBookCount());
		lib.addBook(new LibraryBook());
		assertEquals("Library with 1 book", lib.toString());
	}
	
	// Coverage results for the following test (coverage of the test case code itself) illustrate a weakness in EclEmma related to code with exceptions
	@Test
	public void rejectsFourDigitClassDivisionSection()
	{
		try
		{
			lib.addBook(new LibraryBook("A Book", 1981, "1234.56789"));
			fail();
		} catch (LibraryException e) {}	
	}

	@Test
	public void testGetByDewey()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		lib.addBook(b);
		LibraryBook b2 = lib.getByDewey("123.56789");
		assertEquals(b, b2);
	}
		
	@Test
	public void testTakeOutBook()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		lib.addBook(b);
		assertTrue(b.isAvailable());
		Customer c = new Customer();
		lib.takeOutBook(c, b);
		assertFalse(b.isAvailable());
	}
	
	@Test
	public void testGetCustomerBookList()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);
				 
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c, b2);
		
		assertEquals(2, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) );
		assertTrue( c.getBooksHeld().contains(b2) );		
	}
	
//	Bugged test examples--------------------------------------
//	@Test
//	public void testDueDateForStandardCustomerType()
//	{
//		CustomerType ct = new StandardCustomerType();
//		Calendar now = Calendar.getInstance();
//		Calendar due = ct.getDueDate(now);
//		
//		assertEquals(now.get(Calendar.MONTH) + 1, due.get(Calendar.MONTH) ); //bug: this test will spuriously fail in December!
//	}
	
//	@Test
//	public void testDueDateForStandardCustomerTypeInLibrary()
//	{
//		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
//		lib.addBook(b);
//		
//		Customer c = new Customer("Gerald", Calendar.getInstance(), new StandardCustomerType());		
//		Calendar now = Calendar.getInstance();
//		lib.takeOutBook(c, b);
//		
//		Calendar due = lib.getDueDate(c,b);
//		
//		assertEquals(now.get(Calendar.MONTH) + 1, due.get(Calendar.MONTH) ); //bug: this test will spuriously fail in December!
//	}
	
	@Test
	public void testReturnBook()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);
				 
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c, b2);
		lib.returnBook(c, b);
		assertEquals(1, c.getBooksHeld().size());
		assertFalse( c.getBooksHeld().contains(b) );
		assertTrue( c.getBooksHeld().contains(b2) );
	}
	
	
	/* This is a "coverage motivated" test - I added this after I saw something wasn't covered - to see this, 
	 * comment this test out then run coverage again. 
	 * 
	 * Also, maybe think about what potential errors might slip through undetected if this was test wasn't included, 
	 * and therefore what the potential benefit of checking coverage has been. */ 
	@Test
	public void returnsNullOnDueDateForBookNotTakenOut()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		
		Customer c = new Customer("Gerald", Calendar.getInstance(), new StandardCustomerType());
		lib.takeOutBook(c, b2);
		
		//next few lines are necessary if this test is actually going to add the coverage we want. Why?
		LibraryBook b3 = new LibraryBook("Another Book 2", 1981, "123.13");
		Customer c2 = new Customer();
		lib.register(c2);		
		lib.takeOutBook(c2, b3);						
				
		assertNull(lib.getDueDate(c,b));				
	}
	

	
	//coverage motivated 
	@Test
	public void getCustomerBookListWhenMultipleCustomersHaveBooksOut()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);
				 
		Customer c = new Customer();
		Customer c2 = new Customer();
		lib.register(c);
		lib.register(c2);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c2, b2);
		
		assertEquals(1, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) );
		assertFalse( c.getBooksHeld().contains(b2) ); //spurious test if we trust ArrayList.size() and ArrayList.contains(), but gets across the point of the test
	}
	
	
	//coverage motivated
	//this exploits knowledge of ArrayList's ordering behaviour
	@Test
	public void testReturnBookWhenBookWasntTheFirstOneTakenOut()
	{
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);
				 
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c, b2);
		lib.returnBook(c, b2);
		assertEquals(1, c.getBooksHeld().size());
		assertTrue( c.getBooksHeld().contains(b) );
		assertFalse( c.getBooksHeld().contains(b2) );
	}
	
	
	//coverage motivated
	@Test
	public void testToStringWithMultipleBooksAndOneOnLoan() {
		LibraryBook b = new LibraryBook("A Book", 1981, "123.56789");
		LibraryBook b2 = new LibraryBook("Another Book", 1981, "123.1");
		lib.addBook(b);
		lib.addBook(b2);
				 
		Customer c = new Customer();
		lib.register(c);
		lib.takeOutBook(c, b);
		lib.takeOutBook(c, b2);
		
		assertEquals("Library with 2 books. Average of 2 books held per customer.", lib.toString());
	}
	
	
	//a few more tests would be needed here so that LibraryBook is fully covered
}
