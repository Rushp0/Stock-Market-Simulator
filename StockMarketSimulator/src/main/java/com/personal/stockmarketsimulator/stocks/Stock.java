package com.personal.stockmarketsimulator.stocks;

import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.net.URI;
import java.net.http.*;
import java.util.Map;


import com.personal.stockmarketsimulator.gui.LimitExceededException;

public class Stock implements Serializable{
	/**
	 * Stock:
	 * 
	 * Contains information from API on the stock
	 * 
	 * @author Rushi
	 */
	
	private String symbol;
	private double currentPrice;
	private double percentChange;
	private double open;
	private double previousClose;
	private String summary;
	private Map<String,String> data;
	
	/**
	 * @param symbol: symbol for stock
	 */
	public Stock(String symbol){
		
		this.symbol = symbol;
		
		summary = retrieveStockData().body();
		
		try {
			if(summary.equals("{\"message\":\"Limit Exceeded\"}")) 
				throw new LimitExceededException("API limit has been reached.");
			
		}catch(LimitExceededException e) {
			System.err.println("LimitExceededException: " +e.getMessage());
		}
		
		try {
			convertToHashMap();
			this.currentPrice = Double.parseDouble(data.get("price"));
			this.previousClose = Double.parseDouble(data.get("previousClose"));
			
		} catch(InvalidStockSymbolException e) { // add exception catch for if stock symbol slips by. ex: APPL vs. AAPl
			System.err.println("The stock symbol entered is invalid");
			
		} catch(NullPointerException e) {
			System.err.println("NullPointerException: The failed to parse price from API response. Line 58 Stock.java");
		}

	}
	
	public Stock(String symbol, double price, double previousClose) {
		this.symbol = symbol;
		this.currentPrice = price;
		this.previousClose = previousClose;
	}
	
	private HttpResponse<String> retrieveStockData(){
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://stockexchangeapi.p.rapidapi.com/price/"+symbol))
				.header("x-rapidapi-key", "b6b518db45msh86b1facf43da1e2p10a17ejsn51bd4968a13c")
				.header("x-rapidapi-host", "stockexchangeapi.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		HttpResponse<String> response = null;
		
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOException");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("InterruptedException");
		}
		return response;
	}
	
	private void convertToHashMap() throws InvalidStockSymbolException {
		
		String[] splitResponse = summary.split("\","); // seperates data from API
		
		if(splitResponse[0].isBlank()) {
			throw new InvalidStockSymbolException(); // throws exception if search box is empty
		}
		
		for(String i: splitResponse) 
			data.put((i.substring(2, i.indexOf(":")-1)).replace("\"","").strip(),  i.substring(i.indexOf(":")+2));

	}
	
	public Map<String, String> getStockSummary(){
		return data;
	}
	
	public double getPrice() {
		return currentPrice;
	}
	
	public double getPreviousClose() {
		return previousClose;
	}
	
	public String getPreviousCloseFormatted() {
		return currencyFormat(previousClose);
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getPriceFormatted() {
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(currentPrice);
	}
	
	public String getCompany() {
		return symbol+"R"; // return data.get("symbolName");
	}
	
	private String currencyFormat(double amount) {
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(amount); 
	}
	
	public boolean equals(Stock s) {
		return this.symbol.equals(s.getSymbol());
	}
}
