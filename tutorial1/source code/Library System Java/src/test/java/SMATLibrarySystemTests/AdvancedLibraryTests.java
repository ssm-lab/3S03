package SMATLibrarySystemTests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Parameterized;

import SMATLibrarySystem.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AdvancedLibraryTests.BasicTests.class,
    AdvancedLibraryTests.DueDateBoundaryTest.class
})
public class AdvancedLibraryTests {

    public static class BasicTests {

        @Test
        public void testGetBooksHeldThrowsWhenNotRegistered() {
            Customer c = new Customer(); // unregistered customer

            try {
                c.getBooksHeld();
                fail("Expected LibraryException, but the method returned normally. ");
            } catch (LibraryException e) {
                // Correct behavior
                return;
            } catch (Exception e) {
                fail("Expected LibraryException, but got: " + e.getClass().getSimpleName());
            }
        }


        @Test
        public void testBookCannotBeTakenOutTwiceBySameCustomer() {
            Library lib = new Library();
            LibraryBook b = new LibraryBook("Book", 2022, "050.1");
            Customer c = new Customer();
            lib.addBook(b);
            lib.register(c);

            // First checkout
            lib.takeOutBook(c, b);

            assertFalse("Book should be unavailable after first checkout", b.isAvailable());
            assertEquals("Customer should have exactly one book after first checkout",
                    1, c.getBooksHeld().size());

            // Second checkout (should NOT be allowed)
            try {
                lib.takeOutBook(c, b);
                fail("Expected an exception or rejection when checking out the same book twice.");
            } catch (LibraryException e) {
                // expected behavior
                return;
            } catch (Exception e) {
                fail("Expected LibraryException, but got: " + e.getClass().getSimpleName());
            }
        }

        
        @Test
        public void testToStringDoesNotCrashWhenNoCustomersButHoldingsExist() {
            Library lib = new Library();

            LibraryBook b = new LibraryBook("A", 1999, "123.4");
            lib.addBook(b);

            Customer c = new Customer();
            lib.register(c);
            lib.takeOutBook(c, b);

            // remove the only customer (bug is triggered here)
            lib.deregister(c);

            try {
                String output = lib.toString();
                // correct behaviour, toString() should return SOME string
                assertNotNull("toString() should return a non-null String", output);

            } catch (ArithmeticException e) {
                fail("toString() should NOT throw ArithmeticException when customers list is empty. "
                   + "reveals the division-by-zero bug.");
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getClass().getSimpleName());
            }
        }

    }


    //    Parameterized test
    @RunWith(Parameterized.class)
    public static class DueDateBoundaryTest {

        @Parameterized.Parameters(name = "{index}: start({0},{1}) → expectedMonth={2}, yearDelta={3}")
        public static Object[][] data() {
            return new Object[][] {
                { Calendar.NOVEMBER,  10, Calendar.DECEMBER, 0 },
                { Calendar.DECEMBER,  15, Calendar.JANUARY,  1 },
                { Calendar.JANUARY,   20, Calendar.FEBRUARY, 0 },
                { Calendar.FEBRUARY,   5, Calendar.MARCH,    0 }
            };
        }

        private final int startMonth;
        private final int startDay;
        private final int expectedMonth;
        private final int yearDelta;

        public DueDateBoundaryTest(int startMonth, int startDay, int expectedMonth, int yearDelta) {
            this.startMonth = startMonth;
            this.startDay = startDay;
            this.expectedMonth = expectedMonth;
            this.yearDelta = yearDelta;
        }

        @Test
        public void testDueDateMonthAndYearBehaviour() {
            CustomerType type = new StandardCustomerType();

            Calendar start = Calendar.getInstance();
            int baseYear = start.get(Calendar.YEAR);

            start.set(Calendar.MONTH, startMonth);
            start.set(Calendar.DAY_OF_MONTH, startDay);

            Calendar expected = (Calendar) start.clone();
            expected.add(Calendar.MONTH, 1);

            Calendar actual = type.getDueDate(start);

            assertEquals("Incorrect month for due date",
                    expectedMonth, actual.get(Calendar.MONTH));

            assertEquals("Incorrect year for due date",
                    baseYear + yearDelta, actual.get(Calendar.YEAR));
        }
    }
}
