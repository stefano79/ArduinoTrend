package com.wordpress.blogste.ArduinoTrend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jssc.SerialPortException;

import com.wordpress.blogste.ArduinoTrend.ComunicationAbstract.EVENT;
import com.wordpress.blogste.Protocollo_Seriale_Ardunio.Protocollo;
import com.wordpress.blogste.SerialCom.Serial;
import com.wordpress.blogste.trend.Pen;
import com.wordpress.blogste.trend.PenModel;
import com.wordpress.blogste.trend.TrendModel;
import com.wordpress.blogste.trend.tools.ViewDialogPen;
import com.wordpress.blogste.trend.tools.ViewDialogPreference;

public class Controller implements Observer {

	private View view;
	private Pen[] pens;
	private Project project;
	private Comunication comunication;
	private Protocollo protocollo;
	private Serial serial;
	private ModelListPens modelListPens;

	private boolean fileEdited = false;
	private File file;

	@SuppressWarnings("unchecked")
	public Controller(final View view) {
		this.view = view;

		try {
			PreferenceData.init();
		} catch (InvalidPropertiesFormatException e2) {
			JOptionPane.showMessageDialog(view, "Errore - " + e2.getMessage(),
					"Errore", JOptionPane.ERROR_MESSAGE);
			e2.printStackTrace();
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(view, "Errore - " + e2.getMessage(),
					"Errore", JOptionPane.ERROR_MESSAGE);
			e2.printStackTrace();
		}

		// Inizializzazione progetto vuoto
		project = new Project("Senza Titolo");
		view.setTitle(project.getTitle());

		// Inizializzazione array delle penne
		pens = new Pen[Project.PENS_MAX];

		// Inizializzazione model della view lista penne
		modelListPens = new ModelListPens(pens);
		view.setModelListPens(modelListPens);
		// Inizializzazione model delle penne
		inizializePens(project.getPenModels());

		// Settaggi di default della classe Trend
		view.trend.addPens(pens);
		view.trend.setModel(new TrendModel(5, 5, 20000, 0, 100,
				TrendModel.TYPE_SCALE_AXIS_Y.AUTOMATIC));

		// Gestione chiusura applicazione
		view.addWindowListener(new ListenerCloseApplication());

		// Inizializzazione porta seriale
		serial = new Serial(Serial.DATA_RATE.DATARATE_57600,
				Serial.DATA_BITS.DATABITS_8, Serial.STOP_BITS.STOPBITS_1,
				Serial.PARITY.NONE);

		// Inizializzazione protocollo di comunicazione
		protocollo = new Protocollo(serial, Protocollo.TYPE_ARDUINO.DUEMILANOVE);
		protocollo.addObserver(modelListPens);

		// Inizializzazione comunicazione
		comunication = new Comunication(protocollo, serial, pens);
		comunication.addObserver(this);

		// Setting dei listener del menu della view principale
		view.addListenerMenuComunicazione(new ListenerMenuComunicazione());
		view.addListenerAtConnectionCommand(new ListenerConnection());
		view.addListenerAtInfo(new ListenerInfo());
		view.addListenerAddPen(new ListenerAddPen());
		view.addListenerAtListPens(new ListenerSelectedPen());
		view.addListenerMntmEditListPens(new ListenerEditPen());
		view.addListenerMntmRemoveListPens(new ListenerRemovePen());
		view.addListenerAtSettings(new ListenerSettingsTrend());
		view.addListenerAtNew(new ListenerNewProject());
		view.addListenerAtOpen(new ListenerOpenProject());
		view.addListendrAtSave(new ListenerSaveProject());
		view.addListendrAtSaveWithName(new ListenerSaveWithNameProject());
		view.addListenerAtImport(new ListenerImportData());
		view.addListenerAtExport(new ListenerExportData());

		// Popolamento lista datarate e settaggio del datarate di default
		Serial.DATA_RATE[] arrDataRate = Serial.DATA_RATE.values();
		String[] arrStringDataRate = new String[arrDataRate.length];
		for (int i = 0; i < arrStringDataRate.length; i++) {
			arrStringDataRate[i] = Integer.toString(arrDataRate[i].getValue());
		}
		view.setListDataRate(arrStringDataRate,
				PreferenceData.getValue(PreferenceData.SERIAL_BAUD_RATE));

	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg0.equals(comunication)) {
			EVENT status = (EVENT) arg1;
			switch (status) {
			case CLOSED:
				view.setLblStatoConnessione("Offline");
				view.setOnLine(false);
				break;
			case OPEN:
				view.setLblStatoConnessione("Online");
				view.setOnLine(true);
				break;
			case TIMEOUT:
				view.setLblStatoConnessione("TIMEOUT");
				view.setOnLine(false);
				break;
			}
		}
	}

	/*
	 * *********** Metodi privati *************
	 */

	private void inizializePens(PenModel[] penModels) {
		removeAllPen();
		if (penModels != null)
			for (int i = 0; i < penModels.length; i++) {
				if (penModels[i] != null) {
					pens[i] = new Pen(penModels[i]);
				}
			}

	}

	private void addNewPen(Pen pen) {
		for (int i = 0; i < pens.length; i++) {
			if (pens[i] == null) {
				pens[i] = pen;
				if (view.trend.isPlay()){
					pens[i].startReg();
				}
				if (i == 0
						&& view.trend.getTypeScaleAxisY() == TrendModel.TYPE_SCALE_AXIS_Y.PENS) {
					view.trend.setScaleAxisY(pen.getMin(), pen.getMax());
				}
				if (i == pens.length - 1)
					view.setEnabledAddPen(false);
				break;
			}
		}
		modelListPens.update(null, null);

		fileEdited = true;

	}

	private void removePen(int index) {

		for (int i = index; i < pens.length; i++) {

			if (i < pens.length - 1) {
				pens[i] = pens[i + 1];
			} else {
				pens[i] = null;
			}
		}
		modelListPens.update(null, null);

		view.setEnabledAddPen(true);

		fileEdited = true;
	}

	private void removeAllPen() {
		for (int i = 0; i < pens.length; i++) {
			pens[i] = null;
		}
		modelListPens.update(null, null);
		view.setEnabledAddPen(true);

	}

	private void updatePen(Pen pen, int index) {
		pens[index].setName(pen.getName());
		pens[index].setDescription(pen.getDescription());
		pens[index].setUnits(pen.getUnits());
		pens[index].setColor(pen.getColor());
		pens[index].setLimit(pen.getMin(), pen.getMax());
		pens[index].setScale(pen.isScale());
		pens[index].setScaleValue(pen.getScaleInMin(), pen.getScaleInMax(), pen.getScaleOutMin(), pen.getScaleOutMax());

		fileEdited = true;
	}

	private void refreshNameSerialPort() {
		view.setListSerialPort(Serial.getListCommPort(),
				PreferenceData.getValue(PreferenceData.SERIAL_COM_PORT));
	}

	private void newProject() {
		project = new Project("Senza Titolo");
		file = null;
		view.setTitle(project.getTitle());
		inizializePens(project.getPenModels());
		modelListPens.update(null, null);
		view.setTitle(project.getTitle());
		view.trend.setModel(project.getTrendModel());
	}

	private void openProject() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arduino Trend Progetto", "atp");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		int n = fileChooser.showOpenDialog(view);

		if (n == JFileChooser.APPROVE_OPTION) {

			try {
				project.load(fileChooser.getSelectedFile());
				file = fileChooser.getSelectedFile();
				inizializePens(project.getPenModels());
				modelListPens.update(null, null);
				view.setTitle(project.getTitle());
				view.trend.setModel(project.getTrendModel());
			} catch (IOException e) {
				view.popupErrorMessage(e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				view.popupErrorMessage("File non trovato");
				e.printStackTrace();
			}
		}

	}

	private void saveProject() {
		if (file == null) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Arduino Trend Progetto", "atp");
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setFileFilter(filter);
			int n = fileChooser.showSaveDialog(view);

			if (n == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				String nameFile = file.getName();
				FileFilter currentFilter = fileChooser.getFileFilter();
				if (currentFilter instanceof FileNameExtensionFilter) {
					String[] ext = ((FileNameExtensionFilter) currentFilter)
							.getExtensions();
					if (file.getName().endsWith(ext[0]) == false) {
						try {
							file = new File(file.getCanonicalPath() + "." + ext[0]);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				int a = JOptionPane.OK_OPTION;
				if (file.exists()) {
					a = JOptionPane.showConfirmDialog(view,
							"Vuoi sovrascire il file?");
				}
				switch (a) {
				case JOptionPane.OK_OPTION:

					try {
						project.setTitle(nameFile);
						view.setTitle(nameFile);
						project.save(file, pens, view.trend.getModel());
						fileEdited = false;
					} catch (IOException e) {
						view.popupErrorMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
				case JOptionPane.NO_OPTION:
					saveProject();
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}
			}
		} else {
			try {
				project.save(file, pens, view.trend.getModel());
				fileEdited = false;
			} catch (IOException e) {
				view.popupErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void importProjectAndData() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Importa");
		fileChooser.setDialogTitle("Importa progetto e  dati");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arduino Trend Data", "atd");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		int n = fileChooser.showOpenDialog(view);

		if (n == JFileChooser.APPROVE_OPTION) {

			try {
				project.importProjectAndData(fileChooser.getSelectedFile());
				file = null;
				inizializePens(project.getPenModels());
				modelListPens.update(null, null);
				view.setTitle(project.getTitle());
				view.trend.setModel(project.getTrendModel());
				view.trend.setMillsAtFirstRecord();
			} catch (IOException e) {
				view.popupErrorMessage(e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				view.popupErrorMessage("File non trovato");
				e.printStackTrace();
			}

		}
	}

	private void exportProjectAndData() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Esporta");
		fileChooser.setDialogTitle("Esporta progetto e dati");		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arduino Trend Data", "atd");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		//filter = new FileNameExtensionFilter("File generico csv", "csv");
		//fileChooser.addChoosableFileFilter(filter);

		int n = fileChooser.showSaveDialog(view);

		if (n == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			FileFilter currentFilter = fileChooser.getFileFilter();
			if (currentFilter instanceof FileNameExtensionFilter) {
				String[] ext = ((FileNameExtensionFilter) currentFilter).getExtensions();
				if (file.getName().endsWith(ext[0]) == false) {
					try {
						file = new File(file.getCanonicalPath() + "." + ext[0]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			int a = JOptionPane.OK_OPTION;
			if (file.exists()) {
				a = JOptionPane.showConfirmDialog(view,
						"Vuoi sovrascire il file?");
			}
			switch (a) {
			case JOptionPane.OK_OPTION:

				try {
					project.exportProject(file, pens, view.trend.getModel());
				} catch (IOException e) {
					view.popupErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
			case JOptionPane.NO_OPTION:
				exportProjectAndData();
				break;
			case JOptionPane.CANCEL_OPTION:
				System.out.println("CANCEL");
				break;
			}
		}
	}

	/*
	 * *********** Classi interne Listener per view principale *************
	 */

	private class ListenerCloseApplication implements WindowListener {
		@Override
		public void windowClosing(WindowEvent e) {
			int n = JOptionPane.NO_OPTION;
			if (fileEdited) {
				n = JOptionPane
						.showConfirmDialog(
								view,
								"Il progetto "
										+ project.getTitle()
										+ " è stato modificato. Salvare il File prima di uscire?");
			}

			switch (n) {
			case JOptionPane.OK_OPTION:
				saveProject();
			case JOptionPane.NO_OPTION:
				try {
					comunication.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				view.trend.close();
				e.getWindow().dispose();
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	private class ListenerMenuComunicazione implements MenuListener {

		@Override
		public void menuCanceled(MenuEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void menuDeselected(MenuEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void menuSelected(MenuEvent arg0) {
			refreshNameSerialPort();

		}

	}

	private class ListenerNewProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int n = JOptionPane.NO_OPTION;
			if (fileEdited) {
				n = JOptionPane
						.showConfirmDialog(
								view,
								"Il progetto "
										+ project.getTitle()
										+ " è stato modificato. Salvare il File prima di creare un nuovo progetto?");
			}

			switch (n) {
			case JOptionPane.OK_OPTION:
				saveProject();
				newProject();
				break;
			case JOptionPane.NO_OPTION:
				newProject();
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}

		}

	}

	private class ListenerOpenProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int n = JOptionPane.NO_OPTION;
			if (fileEdited) {
				n = JOptionPane
						.showConfirmDialog(
								view,
								"Il progetto "
										+ project.getTitle()
										+ " è stato modificato. Salvare il File prima di aprire un nuovo progetto?");
			}

			switch (n) {
			case JOptionPane.OK_OPTION:
				saveProject();
				openProject();
				break;
			case JOptionPane.NO_OPTION:
				openProject();
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		}

	}

	private class ListenerSaveProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			saveProject();

		}
	}

	private class ListenerSaveWithNameProject implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			file = null;
			saveProject();

		}
	}

	private class ListenerImportData implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			int n = JOptionPane.NO_OPTION;
			if (fileEdited) {
				n = JOptionPane
						.showConfirmDialog(
								view,
								"Il progetto "
										+ project.getTitle()
										+ " è stato modificato. Salvare il File prima di importare un nuovo progetto?");
			}

			switch (n) {
			case JOptionPane.OK_OPTION:
				saveProject();
				importProjectAndData();
				break;
			case JOptionPane.NO_OPTION:
				importProjectAndData();
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}

		}
	}

	private class ListenerExportData implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			exportProjectAndData();
		}

	}

	private class ListenerConnection implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (comunication.isConnected()) {

				try {
					comunication.close();
				} catch (IOException e1) {
					view.popupErrorMessage(e1.getMessage());
					e1.printStackTrace();
				}

			} else {

				Serial.DATA_RATE[] arrDataRate = Serial.DATA_RATE.values();
				try {
					serial.setDataRate(arrDataRate[view.getItemDataRate()]);
				} catch (SerialPortException e1) {
					view.popupErrorMessage(e1.getMessage());
					e1.printStackTrace();
				}
				try {
					refreshNameSerialPort();
					String namePort = view.getNameSerialPortSelected();
					comunication.open(namePort);
				} catch (SerialPortException e1) {
					if (e1.getExceptionType().equals(
							SerialPortException.TYPE_NULL_NOT_PERMITTED)) {
						view.popupErrorMessage("Porta seriale non selezionata");
					} else if (e1.getExceptionType().equals(
							SerialPortException.TYPE_PORT_NOT_FOUND)) {
						view.popupErrorMessage("Porta seriale "
								+ e1.getPortName() + " non trovata");
					} else if (e1.getExceptionType().equals(
							SerialPortException.TYPE_PORT_ALREADY_OPENED)) {
						view.popupErrorMessage("Porta seriale "
								+ e1.getPortName() + " � gi� in uso.");
					} else {
						view.popupErrorMessage(e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
		}

	}

	private class ListenerSettingsTrend implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			final ViewDialogPreference dialog = new ViewDialogPreference(view,
					"Impostazioni Trend", view.trend);
			dialog.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					if (dialog.option == ViewDialogPreference.OPTION.OK) {
						fileEdited = true;
					}
				}
			});
		}

	}

	private class ListenerInfo implements ActionListener {

		@SuppressWarnings("unused")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ViewDialogInfo info = new ViewDialogInfo(view);
		}

	}

	private class ListenerAddPen implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String[] addressArray = protocollo.getAddressMemory();
			final ViewDialogPen dialog = new ViewDialogPen(view, "Nuova penna",
					addressArray);
			dialog.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					if (dialog.option == ViewDialogPen.OPTION.OK) {
						Controller.this.addNewPen(dialog.getPen());
					}
				}
			});
		}

	}

	private class ListenerEditPen implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] addressArray = protocollo.getAddressMemory();
			final int indexPen = view.getIndexSelectedListPens();

			final ViewDialogPen dialog = new ViewDialogPen(view,
					"Modifica penna", addressArray, pens[indexPen]);

			dialog.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					if (dialog.option == ViewDialogPen.OPTION.OK) {
						Controller.this.updatePen(dialog.getPen(), indexPen);
					}
				}
			});
		}

	}

	private class ListenerRemovePen implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int n = JOptionPane.showConfirmDialog(view,
					"Sei sicuro di voler eliminare la penna?");
			if (n == 0) {
				final int indexPen = view.getIndexSelectedListPens();
				Controller.this.removePen(indexPen);
			}

		}

	}

	private class ListenerSelectedPen implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			final int indexPen = view.getIndexSelectedListPens();
			if (view.trend.getTypeScaleAxisY() == TrendModel.TYPE_SCALE_AXIS_Y.PENS) {
				view.trend.setScaleAxisY(pens[indexPen].getMin(),
						pens[indexPen].getMax());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
