package com.personal.stockmarketsimulator;

import java.util.Scanner;

public class TestingInput {

	public static void main(String[] args) {
		
		Scanner scn = new Scanner(System.in);
		
		System.out.println("Bal: ");
		Double accBal = scn.nextDouble();
		
		Main window = new Main();
		window.setAccountValue(accBal);
		window.setVisible(true);
		
	}
	
}
