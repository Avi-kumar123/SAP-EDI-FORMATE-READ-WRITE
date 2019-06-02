package com.emiza.util;

import java.io.IOException;
import java.io.InputStream;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import com.emiza.constant.Constant;

public class Util {

	public Ini getIni() throws InvalidFileFormatException, IOException {
		InputStream stream = this.getClass().getResourceAsStream(Constant.CONFIG);
		return new Ini(stream);
	}

}
