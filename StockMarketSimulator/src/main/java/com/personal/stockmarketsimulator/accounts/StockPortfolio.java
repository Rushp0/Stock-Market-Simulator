package com.personal.stockmarketsimulator.accounts;

import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.personal.stockmarketsimulator.stocks.Stock;

public class StockPortfolio implements Serializable{

	private static int FINISH_SERIALIZLING = 1;
	private static int FLUSH_SERIALIZLING = 0;
	
	
	private double stockValue = 0;
	private int stockAmount = 0;
	private Map<Stock, Integer> stockList = new HashMap<Stock, Integer>();
//	private static Map<Stock, Integer> emptyHashMap = new HashMap<Stock, Integer>();
	private BankAccount bankAccount;
	
	public StockPortfolio(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	public void purchaseStock(Stock stock, int amount) {
		
		if(bankAccount.getAccountBalance() >= stock.getPrice()*amount) {
			int inPortfolio = 0;
			for(Stock key : stockList.keySet()) {
				if(key.equals(stock)) {
					stockList.replace(key, stockList.get(key), stockList.get(key)+amount);
					break;
				}
				inPortfolio++;
			}
			
			if(inPortfolio == stockList.size())
				stockList.put(stock, amount);

			bankAccount.stockPurchase(stock.getPrice()*amount);
			stockAmount = stockList.size();
			calculateStockValue();
			serializablePortfolio(FLUSH_SERIALIZLING);
			
			
		}
		
	}
	
	private void calculateStockValue() {
		
		for(Stock key : stockList.keySet())
			stockValue += (key.getPrice()*stockList.get(key));
		
	}
	
	private void serializablePortfolio(int serializingValue) {
		
		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/StockPortfolio.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			
			out.writeObject(stockList);
			out.writeObject(bankAccount);
			
			out.flush();
			fileOut.flush();
			
			if(serializingValue == FINISH_SERIALIZLING) {
				out.close();
				fileOut.close();
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void deserializePortfolio() {
		
		try {
			
			FileInputStream fileIn = new FileInputStream("/tmp/StockPortfolio.ser");
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			
			stockList = (HashMap<Stock, Integer>) objIn.readObject();
			bankAccount = (BankAccount) objIn.readObject();
			
			fileIn.close();
			objIn.close();
			
			stockAmount = stockList.size();
			
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void finishSeralizlingPortfolio() {
		serializablePortfolio(FINISH_SERIALIZLING);
	}
	
	public double getPortfolioValue() {
		return stockValue;
	}
	
	public int getPortfolioSize() {
		return stockList.size();
	}
	
	public String getPortfolioValueFormatted() {
		
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(stockValue);
	}
	
	public Map<Stock, Integer> getPortfolio(){
		return stockList;
	}

	public String[] getDataArray(Stock s) {
		return new String[] {s.getCompany(), s.getSymbol(), stockList.get(s).toString(), ((Double)(stockList.get(s)*s.getPrice())).toString()};
	}
	
//	public static void main(String[] args) {
//		
//		BankAccount b = new BankAccount();
//		StockPortfolio sp = new StockPortfolio(b);
//		
//		sp.serializablePortfolio(FLUSH_SERIALIZLING);
//		
//	}

	
	
}
