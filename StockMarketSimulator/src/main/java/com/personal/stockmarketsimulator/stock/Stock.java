package com.personal.stockmarketsimulator.stock;

import java.io.IOException;
import java.text.NumberFormat;
import java.net.URI;
import java.net.http.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.*;

public class Stock {
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
	private Dictionary<String,String> data = new Hashtable<String, String>();
	
	/**
	 * @param symbol: symbol for stock
	 */
	public Stock(String symbol){
		
		this.symbol = symbol;
		
		summary = retrieveStockData().body();
		try {
			convertToDictionary();
			this.currentPrice = Double.parseDouble(data.get("price"));
			this.previousClose = Double.parseDouble(data.get("previousClose"));
			
		} catch(InvalidStockSymbolException e) {
			System.err.println("The stock symbol entered is invalid");
			
		}

	}
	
	private HttpResponse<String> retrieveStockData(){
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://stockexchangeapi.p.rapidapi.com/price/"+symbol))
				.header("x-rapidapi-key", "b6b518db45msh86b1facf43da1e2p10a17ejsn51bd4968a13c")
				.header("x-rapidapi-host", "stockexchangeapi.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		@SuppressWarnings("unchecked")
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
	
	private void convertToDictionary() throws InvalidStockSymbolException {
		
		String[] splitResponse = summary.split("\",");
		
		if(splitResponse[0].isBlank()) {
			throw new InvalidStockSymbolException();
		}
		
		for(String i: splitResponse) 
			data.put((i.substring(2, i.indexOf(":")-1)).replace("\"","").strip(),  i.substring(i.indexOf(":")+2));
	}
	
	public Dictionary<String, String> getStockSummary(){
		return data;
	}
	
	public double getPrice() {
		return currentPrice;
	}
	
	public double getPreviousClose() {
		return previousClose;
	}
	public String getSymbol() {
		return symbol;
	}
	public String getPriceFormatted() {
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(currentPrice);
	}
	
	
	
	public static void main(String[] args) {
		
		Stock s = new Stock("TSLA");
		System.out.println(s.getSymbol()+": "+s.getPrice());
		
	}
	
}
