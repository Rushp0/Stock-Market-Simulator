package com.personal.stockmarketsimulator.accounts;

import java.io.Serializable;
import java.text.NumberFormat;

public class BankAccount implements Serializable{

	private double accountBalance;
	
	public BankAccount() {
		accountBalance = 10000;
	}
	
	public double getAccountBalance() {
		return accountBalance;
	}
	
	public String getAccountBalanceFormatted() {
		return currencyFormat(accountBalance);
	}
	
	protected void deposit(double amount) {
		accountBalance+=amount;
	}
	
	protected void stockPurchase(double amount) {
		accountBalance-=amount;
	}
	
	private String currencyFormat(double amount) {
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(amount); 
	}
	
}
