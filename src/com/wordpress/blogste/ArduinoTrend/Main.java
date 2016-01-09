package com.wordpress.blogste.ArduinoTrend;

import javax.swing.UIManager;

public class Main {


	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
			System.setProperty("apple.laf.useScreenMenuBar", "true");					//Abilitazione il menu sulla barra di OSX
			System.setProperty("com.apple.mrj.application.apple.menu.about.name",
					"Arduino Trend");													//Settaggio nome applicazione sella barra di OSX
			
		} catch (Throwable e){
			e.printStackTrace();
		}
	
		try {			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());		//Abilitazione look oggetti come il sistema
			} catch (Throwable e) {
			e.printStackTrace();
		}
		
		View view = new View();
		Controller controller = new Controller(view);
		view.pack();
		view.setVisible(true);
		
	}

}
