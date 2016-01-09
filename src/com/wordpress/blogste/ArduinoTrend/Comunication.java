package com.wordpress.blogste.ArduinoTrend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import jssc.SerialPortException;

import com.wordpress.blogste.Protocollo_Seriale_Ardunio.Protocollo;
import com.wordpress.blogste.SerialCom.Serial;
import com.wordpress.blogste.trend.Pen;

public class Comunication extends ComunicationAbstract implements Observer {

	private Protocollo protocollo;
	private Serial serial;
	private Pen[] pens;
	private int indexPens = 0;
	private Timer timerTrigger;
	private static final long TRIGGER_TIME = 10;
	private Trigger trigger;
	private javax.swing.Timer timerTimeOut;
	private static final int TIMEOUT = 50;
	private boolean busy = false;
	private boolean connected = false;
	private int delayFirstRunComunication = 0;
	private int repeatTimeout = 0;

	public Comunication(Protocollo protocollo, Serial serial, Pen[] pens) {

		this.protocollo = protocollo;
		protocollo.addObserver(this);

		this.serial = serial;
		this.pens = pens;

		inizializeTimeOut();
	}

	private void inizializeTimeOut() {
		timerTrigger = new Timer(true);
		trigger = new Trigger();
		timerTrigger.schedule(trigger, 0, TRIGGER_TIME);

		timerTimeOut = new javax.swing.Timer(TIMEOUT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repeatTimeout++;
				busy = false;
				if (repeatTimeout >= 5)
					try {
						close();
						setChanged();
						notifyObservers(EVENT.TIMEOUT);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0.equals(protocollo)) {
			for (Pen pen : pens)
				if (pen != null)
					pen.setValue(protocollo.memory.get(pen.getName()));

			timerTimeOut.restart();
			busy = false;
			repeatTimeout = 0;
		}
	}

	public void open(String namePort) throws SerialPortException {
		serial.openPort(namePort);
		trigger.start();
		busy = false;
		delayFirstRunComunication = 0;
		repeatTimeout = 0;
		connected = true;
		setChanged();
		notifyObservers(EVENT.OPEN);
	}

	public void close() throws IOException {
		trigger.stop();
		serial.closePort();
		timerTimeOut.stop();
		connected = false;
		setChanged();
		notifyObservers(EVENT.CLOSED);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/*
	 * ************* INNER CLASS *************
	 */

	/**
	 * @author stefanoaniballi
	 * 
	 *         Classe di estensione di TimerTask per creare un thread richiamato
	 *         da un java.util.timer
	 * 
	 */
	private class Trigger extends TimerTask {

		private boolean run;

		public Trigger() {
			super();
			run = false;
		}

		/*
		 * ************* METODI OVERRIDE *************
		 */

		@Override
		public void run() {
			// System.out.println("Trigger");
			if (delayFirstRunComunication > 200) {
				if (run == true && busy == false && pens[indexPens] != null)

					try {
						protocollo.read(pens[indexPens].getName());
						indexPens++;
						if (indexPens >= pens.length || pens[indexPens] == null)
							indexPens = 0;
						busy = true;
						timerTimeOut.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			delayFirstRunComunication++;
		}

		/*
		 * ************* METODI PUBBLICI *************
		 */

		@SuppressWarnings("unused")
		public boolean isRunning() {
			return run;
		}

		public void start() {
			run = true;
		}

		public void stop() {
			run = false;
		}
	}

}
