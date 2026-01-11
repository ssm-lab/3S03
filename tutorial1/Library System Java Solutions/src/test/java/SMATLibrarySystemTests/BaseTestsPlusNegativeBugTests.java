package SMATLibrarySystemTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** This suite approximates what I expect students to have at the end of Exercise 2 - some tests that pass because they test behaviour that
 * works correctly, and some tests that fail because of bugs.*/
@RunWith(Suite.class)
@SuiteClasses({ LibraryBugTestsNegative.class, LibraryTest.class })
public class BaseTestsPlusNegativeBugTests {

}
