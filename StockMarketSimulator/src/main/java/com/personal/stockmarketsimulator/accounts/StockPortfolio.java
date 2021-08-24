package com.personal.stockmarketsimulator.accounts;

import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.personal.stockmarketsimulator.stocks.Stock;

public class StockPortfolio implements Serializable {

	private static int FINISH_SERIALIZLING = 1;
	private static int FLUSH_SERIALIZLING = 0;

	private double stockValue;
	private int stockAmount;
	private Map<Stock, Integer> stockList = new HashMap<Stock, Integer>();
	private BankAccount bankAccount = new BankAccount();

	public StockPortfolio(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
		calculateStockValue();
	}

	public void purchaseStock(Stock stock, int amount) {

		if (bankAccount.getAccountBalance() >= stock.getPrice() * amount) {
			int inPortfolio = 0;
			for (Stock key : stockList.keySet()) {
				if (key.equals(stock)) {
					stockList.replace(key, stockList.get(key), stockList.get(key) + amount);
					break;
				}
				inPortfolio++;
			}

			if (inPortfolio == stockList.size())
				stockList.put(stock, amount);

			bankAccount.stockPurchase(stock.getPrice() * amount);
			stockAmount = stockList.size();
			calculateStockValue();
			serializablePortfolio(FLUSH_SERIALIZLING);

		}

	}

	public boolean sellStock(Stock stock, int amount) {

		for (Stock key : stockList.keySet()) {
			if (key.equals(stock) && stockList.get(key) >= amount) {

				bankAccount.deposit(amount * key.getPrice());
				stockList.replace(key, stockList.get(key), stockList.get(key) - amount);

				if (stockList.get(key).equals(0))
					stockList.remove(key);

				this.updateStockPortfolio();
				return true;

			} else {
				JOptionPane.showConfirmDialog(null, "Not enough stocks to sell.");
			}
		}
		return false;

	}

	public int getStockAmountOwned(Stock stock) {

		for (Stock key : stockList.keySet())
			if (key.equals(stock))
				return stockList.get(key);

		return 0;

	}

	private void calculateStockValue() {
		stockValue = 0;
		for (Stock key : stockList.keySet())
			stockValue += (key.getPrice() * stockList.get(key));

	}

	private void serializablePortfolio(int serializingValue) {

		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/StockPortfolio.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(stockList);
			out.writeObject(bankAccount);

			out.flush();
			fileOut.flush();

			if (serializingValue == FINISH_SERIALIZLING) {
				out.close();
				fileOut.close();
			}

		} catch (IOException e) {
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
			calculateStockValue();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void finishSeralizlingPortfolio() {
		serializablePortfolio(FINISH_SERIALIZLING);
	}

	public double getPortfolioValue() {
		calculateStockValue();
		return stockValue;
	}

	public int getPortfolioSize() {
		return stockList.size();
	}

	public String getPortfolioValueFormatted() {
		calculateStockValue();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(stockValue);
	}

	public Map<Stock, Integer> getPortfolio() {
		return stockList;
	}

	public String[] getDataArray(Stock s) {
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		return new String[] { s.getSymbol(), stockList.get(s).toString(),
				currencyFormatter.format(((Double) s.getPrice())).toString(),
				(currencyFormatter.format((Double) (stockList.get(s) * s.getPrice()))).toString() };
	}

	public void updateStockPortfolio() {
		calculateStockValue();
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}
}
