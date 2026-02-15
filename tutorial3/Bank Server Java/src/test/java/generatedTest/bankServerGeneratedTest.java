package generatedTest;

import bankServerExerciseWithCheckrep.BankServerWithACheckrep;
import org.junit.Assert;
import org.junit.Test;

public class bankServerGeneratedTest {
  @Test
  public void generatedTest_0() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A0");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A0", 450);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A0", 289);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A0", 157);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A0", 337);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A0", 453);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_1() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A1");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A1", 226);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A1", 65);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A1", 249);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A1", 85);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A1", 416);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_2() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A2");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A2", 337);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A2", 350);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A2", 353);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A2", 392);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A2", 244);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_3() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A3");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A3", 472);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A3", 33);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A3", 375);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A3", 334);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A3", 186);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_4() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A4");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A4", 342);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A4", 450);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A4", 236);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A4", 350);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A4", 390);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_5() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A5");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A5", 314);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A5", 54);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A5", 321);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A5", 198);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A5", 117);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }

  @Test
  public void generatedTest_6() {
    BankServerWithACheckrep server = new BankServerWithACheckrep();
    server.createAccount("A6");
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A6", 343);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A6", 300);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.credit("A6", 298);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A6", 327);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
    server.debit("A6", 90);
    Assert.assertTrue(server.checkHistoryMatchesCurrentBalance());
  }
}
