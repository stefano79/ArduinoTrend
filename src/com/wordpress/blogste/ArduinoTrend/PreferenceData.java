package com.wordpress.blogste.ArduinoTrend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class PreferenceData {
	public static final String SERIAL_BAUD_RATE = "serial.baud_rate";
	public static final String SERIAL_COM_PORT = "serial.com_port";
	

	private static final String subPath = "lib";
	private static final String nameFile = "preference.xml";

	static Properties props = new Properties();

	public static void init() throws InvalidPropertiesFormatException,
			IOException {

		FileInputStream in = new FileInputStream(getPath());
		props.loadFromXML(in);
		save();

	}

	private static void save() throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream(getPath());
		try {
			props.storeToXML(out, "Parametri generali Arduino trend");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getPath() {
		String path = System.getProperty("user.dir");
		return path + File.separator + subPath + File.separator + nameFile;
	}

	public static String getValue(String key) {
		return (String) props.get(key);
	}
	
	public static void setValue(String key, String value){
		props.put(key, value);
		try {
			save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
