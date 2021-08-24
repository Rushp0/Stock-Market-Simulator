package com.personal.stockmarketsimulatortesting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class testingNewAPI {

	public static void main(String[] args) throws IOException {
		
		Map<String, String> data = new HashMap<String, String>();
		
		URL url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=AAPl&apikey=");
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
		
		
		System.out.println(data);
			
		

		
		


	}

}
