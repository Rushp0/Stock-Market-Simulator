package com.personal.stockmarketsimulator.stock;

/**
 * @author Rushi
 */

public class InvalidStockSymbolException extends Exception {

/**
 * 	InvalidStockSymbolException
 * 
 * 	Exception for when user inputs invalid Stock symbol	
 */
	
	
	public InvalidStockSymbolException() {}
	
	public InvalidStockSymbolException(String message) {
		super(message);
	}
	
	
}
