package com.personal.stockmarketsimulator.gui;

public class LimitExceededException extends Exception {

	public LimitExceededException() {

	}

	public LimitExceededException(String message) {
		super(message);
	}

}
