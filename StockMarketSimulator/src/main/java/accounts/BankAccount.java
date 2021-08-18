package accounts;

public class BankAccount {

	private double accountBalance;
	
	public BankAccount() {
		
	}
	
	protected double getAccountBalance() {
		return accountBalance;
	}
	
	protected void deposit(double amount) {
		accountBalance+=amount;
	}
	
	protected void stockPurchase(double amount) {
		accountBalance-=amount;
	}
	
}
