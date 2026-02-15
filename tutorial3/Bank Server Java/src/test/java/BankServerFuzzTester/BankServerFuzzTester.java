package BankServerFuzzTester;

import java.util.ArrayList;
import java.util.Random;

import bankServerExercise.BankException;
import bankServerExercise.BankServer;
import bankServerExercise.BankServerInterface;

public final class BankServerFuzzTester {
	private Random rng = new Random();
	private ArrayList<String> existingAccounts;
	
	public static void main(String[] args) {
		BankServerInterface server = new BankServer();		
		BankServerFuzzTester tester = new BankServerFuzzTester();
		tester.fuzz(server);
	}

	private void fuzz(BankServerInterface server) {
		existingAccounts = new ArrayList<String>();
		int actionCount = 0;
		
		while(true)
		{
			actionCount++;
			
			try
			{
				//doVeryDumbAction(server);
				doSlightlySmarterAction(server);
			}
			//change this catch in order to ignore expected exceptions
			catch (BankException | NullPointerException | ArithmeticException e)
			{
				//for some cases, may need to interrogate exception details to see if it _is_ a known problem, and if not then re-throw it 
				//(that's hard to do well, and exception may not give you the info you need, so you may have to add it at point of throw somehow)
				 System.out.println("Caught exception after " + actionCount + " actions: " + e);
			}
			
			System.out.println(actionCount);
		}
		
	}
	
	//This just does one of a few completely random calls
	private void doVeryDumbAction(BankServerInterface server)
	{
		int action = rng.nextInt(4);
		
		switch(action)
		{
		case 0: 
			server.createAccount("" + rng.nextInt(9999));
			break;
		case 1:
			server.credit("" + rng.nextInt(9999), rng.nextInt(1000));
			break;
		case 2:
			server.debit("" + rng.nextInt(9999), rng.nextInt(1000));
			break;
		case 3:
			server.closeAccount("" + rng.nextInt(9999));
			break;
		default:
			throw new RuntimeException("unknown random action");
		}
	}

	
	private void doSlightlySmarterAction(BankServerInterface server)
	{
		int action = rng.nextInt(6);
		
		if(existingAccounts.isEmpty())
		{
			String acctNo = "" + rng.nextInt(9999);
			server.createAccount(acctNo);
			existingAccounts.add(acctNo);	
		}
		
		switch(action)
		{
		case 0:
			String acctNo = "" + rng.nextInt(9999);
			server.createAccount(acctNo);
			existingAccounts.add(acctNo);			
			break;
		case 1:
			server.credit(pickExistingAccount(), rng.nextInt(1000));
			break;
		case 2:
			server.debit(pickExistingAccount(), rng.nextInt(1000));
			break;
		case 3:
			String accountToClose = pickExistingAccount();
			server.closeAccount(accountToClose);
			existingAccounts.remove(accountToClose);
			break;
		case 4:
			server.setSavingsTarget(pickExistingAccount(), rng.nextInt(10000));
//			Tutorial Answer - set balance to zero more often, likely to trigger arithmetic exception with division by 0 
//			server.setSavingsTarget(pickExistingAccount(), rng.nextInt(5));
			break;
		case 5:
			server.getSavingsPercentage(pickExistingAccount());
			break;			
		default:
			throw new RuntimeException("unknown random action");
		}
	}
	
	private String pickExistingAccount()
	{
			return existingAccounts.get(rng.nextInt(existingAccounts.size()));
	}
	
}
