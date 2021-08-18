package accounts;

import java.util.HashMap;

import com.personal.stockmarketsimulator.stocks.Stock;

public class StockPortfolio {

	private double stockValue = 0;
	private int stockAmount = 0;
	private HashMap<Stock, Integer> stockList;
	private BankAccount bankAccount;
	
	public StockPortfolio(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	private void purchaseStock(Stock stock, int amount) {
		
		if(bankAccount.getAccountBalance() >= stock.getPrice()*amount) {
			stockList.put(stock, amount);
			bankAccount.stockPurchase(stock.getPrice()*amount);
			stockAmount+=amount;
			calculateStockValue();
		}
		
	}
	
	private void calculateStockValue() {
		
		for(Stock key : stockList.keySet())
			stockValue += (key.getPrice()*stockList.get(key));
		
	}
	
}
