package com.emiza.main;

import java.text.ParseException;
import com.emiza.util.MessageEngine;

public class CreateMessage {

	public static void main(String[] args) throws ParseException {
		try {
			MessageEngine meng = new MessageEngine(Integer.parseInt(args[0]), args[1], args[2],args[3]);
			meng.startProcessing();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
