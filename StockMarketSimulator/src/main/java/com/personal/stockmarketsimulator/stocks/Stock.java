package com.personal.stockmarketsimulator.stocks;

import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Stock:
 * 
 * Contains information from API on the stock
 * 
 * @author Rushi
 */

public class Stock implements Serializable{
	
	private static final long serialVersionUID = 2015460279361181404L;
	
	private String symbol;
	private double currentPrice;
	private double percentChange;
	private double open;
	private double previousClose;
	private Map<String,String> data = new HashMap<String, String>();
	
	
	/**
	 * @param symbol: symbol for stock
	 */
	@SuppressWarnings("deprecation")
	public Stock(String symbol){
		
		this.symbol = symbol.toUpperCase();
		
		retrieveStockData();
		
		currentPrice = new Double(data.get("price"));
		percentChange = new Double(data.get("change percent").replace("%", ""));
		open = new Double(data.get("open"));
		previousClose = new Double(data.get("previous close"));
		this.symbol = data.get("symbol");

	}
	
	private void retrieveStockData(){

		URL url;
		try {
			url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey=");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			
			request.setRequestMethod("GET");
			request.connect();
			
			Scanner scn = new Scanner(request.getInputStream());
			
			String output = "";
			
			while(scn.hasNext())
				output+=scn.nextLine();

			String[] splitResponse = output.split(",");
			splitResponse[0]=splitResponse[0].substring(splitResponse[0].indexOf("01."));
			splitResponse[splitResponse.length-1] = splitResponse[splitResponse.length-1].replace("}", "").strip();
			
			for(String i: splitResponse)
				data.put(i.substring(i.indexOf(".")+2, i.indexOf(":")-1), i.substring(i.indexOf(":")+3, i.length()-1));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String, String> getStockSummary(){
		return data;
	}
	
	public double getPercentChange() {
		return percentChange;
	}
	
	public String getPercentChangeFormatted() {
		return percentChange+"%";
	}
	
	public double getOpen() {
		return open;
	} 
	
	public String getOpenFormatted() {
		return currencyFormat(open);
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
	
	public String getVolume() {
		return data.get("volume");
	}
	
	private String currencyFormat(double amount) {
		NumberFormat currencyFormatter =  NumberFormat.getCurrencyInstance();
		return currencyFormatter.format(amount); 
	}
	
	public boolean equals(Stock s) {
		return this.symbol.equals(s.getSymbol());
	}
}
