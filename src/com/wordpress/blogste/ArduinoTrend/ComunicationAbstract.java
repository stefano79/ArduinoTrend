package com.wordpress.blogste.ArduinoTrend;

import java.io.IOException;
import java.util.Observable;

import jssc.SerialPortException;

public abstract class ComunicationAbstract extends Observable{
	
	/**
	 * 
	 * Enumeratore con la lista degli eventi per l' observer
	 *
	 */
	public enum EVENT {
		TIMEOUT, OPEN, CLOSED
	};
	
	public abstract void open(String namePort) throws SerialPortException;
	
	public abstract void close() throws IOException;
	
	public abstract boolean isConnected();
	
}
