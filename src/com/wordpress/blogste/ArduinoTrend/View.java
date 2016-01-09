package com.wordpress.blogste.ArduinoTrend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.wordpress.blogste.trend.Pen;
import com.wordpress.blogste.trend.Trend;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.event.MenuListener;
import javax.swing.UIManager;

import java.awt.SystemColor;

import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;


public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String APP_NAME = "Arduino trend";
	private JPanel contentPane;
	public Trend trend;
	private JLabel lblStatoConnessione;
	private JMenuBar menuBar;
	private JMenu mnComunicazione;
	private JMenu mnDataRate;
	private JMenu mnSerialPort;
	private JMenuItem mntmOnLine;
	private JMenu mnTipoArduino;
	private JSeparator separator;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JSeparator separator_1;
	private JMenuItem mntmImport;
	private JMenuItem mntmExport;
	private JMenuItem mntmOffLine;
	private JPanel panelWest;
	private JButton btnOpen;
	private JLabel lblStatoConnessione_1;
	private JPanel panelPens;
	private JLabel lblPenne;
	private JButton btnAddPen;
	private JToolBar toolBar_1;
	private JButton btnSave;
	private JPanel panelNorth;
	private JToolBar toolBar_2;
	private JButton btnImport;
	private JButton btnExport;
	private JToolBar toolBar_3;
	private JButton btnOpenCOM;
	private JMenu mnHelp;
	private JMenuItem mntmInfo;
	private JPanel panelSouth;
	private JList listPens;
	private JMenuItem mntmEdit;
	private JMenuItem mntmRemove;
	private JPopupMenu popupMenuListPens;
	private JButton btnNew;
	private JToolBar toolBar_4;
	private JButton btnSettings;
	private JMenuItem mntmNew;
	private JPanel panel;
	private JMenuItem mntmSaveWithName;
	private JSeparator separator_2;
	private JMenuItem mntmExit;

	/**
	 * Create the frame.
	 */
	public View() {
		//setTitle("Arduino Trend");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 575, 413);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("Nuovo");
		mntmNew.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/New_1.png")));
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem("Apri");
		mntmOpen.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Open_Folder_1.png")));
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Salva");
		mntmSave.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Save_1.png")));
		mnFile.add(mntmSave);
		
		mntmSaveWithName = new JMenuItem("Salva con nome");
		mnFile.add(mntmSaveWithName);

		separator_1 = new JSeparator();
		mnFile.add(separator_1);

		mntmImport = new JMenuItem("Importa");
		mntmImport.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Import_1.png")));
		mnFile.add(mntmImport);

		mntmExport = new JMenuItem("Esporta");
		mntmExport.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Export_1.png")));
		mnFile.add(mntmExport);
		
		separator_2 = new JSeparator();
		mnFile.add(separator_2);
		
		mntmExit = new JMenuItem("Esci");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WindowEvent close = new WindowEvent(View.this, WindowEvent.WINDOW_CLOSING);
				View.this.dispatchEvent(close);
			}
		});
		mnFile.add(mntmExit);

		mnComunicazione = new JMenu("Comunicazione");
		menuBar.add(mnComunicazione);

		mntmOnLine = new JMenuItem("On Line");
		mntmOnLine.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/stock_draw-curved-connector-with-arrows.png")));
		mnComunicazione.add(mntmOnLine);

		mntmOffLine = new JMenuItem("Off Line");
		mntmOffLine.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/stock_draw-curved-connector-with-circles-2.png")));
		mntmOffLine.setEnabled(false);
		mnComunicazione.add(mntmOffLine);

		separator = new JSeparator();
		mnComunicazione.add(separator);

		mnTipoArduino = new JMenu("Tipo di Arduino");
		mnComunicazione.add(mnTipoArduino);

		mnSerialPort = new JMenu("Porta Seriale");
		mnComunicazione.add(mnSerialPort);

		mnDataRate = new JMenu("Baudrate");
		mnComunicazione.add(mnDataRate);

		mnHelp = new JMenu("Aiuto");
		menuBar.add(mnHelp);

		mntmInfo = new JMenuItem("Info");
		mnHelp.add(mntmInfo);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		trend = new Trend("");
		trend.setBackground(new Color(192, 192, 192));
		trend.setBorder(new EmptyBorder(5, 5, 0, 5));
		trend.setPreferredSize(new Dimension(800, 600));
		getContentPane().add(trend, BorderLayout.CENTER);

		panelWest = new JPanel();
		panelWest.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelWest.setBackground(UIManager.getColor("Panel.background"));
		panelWest.setPreferredSize(new Dimension(150, 10));
		contentPane.add(panelWest, BorderLayout.WEST);
		panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS));

		panelPens = new JPanel();
		panelPens.setPreferredSize(new Dimension(150, 10));
		panelPens.setBackground(UIManager.getColor("Panel.background"));
		panelWest.add(panelPens);
		panelPens.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panelPens.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
				lblPenne = new JLabel("PENNE");
				panel.add(lblPenne);
				lblPenne.setBorder(new EmptyBorder(0, 5, 0, 0));
				

					btnAddPen = new JButton("");
					btnAddPen.setBorderPainted(false);
					btnAddPen.setContentAreaFilled(false);
					btnAddPen.setToolTipText("Aggiungi penna");
					btnAddPen.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Add_7_light.png")));
					panel.add(btnAddPen);
					btnAddPen.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Add_7.png")));
					btnAddPen.setPreferredSize(new Dimension(180, 29));
					btnAddPen.setBorder(new EmptyBorder(0, 0, 0, 5));
					btnAddPen.setHorizontalAlignment(SwingConstants.RIGHT);

		listPens = new JList();
		listPens.setAlignmentX(Component.LEFT_ALIGNMENT);
		listPens.setPreferredSize(new Dimension(150, 0));
		panelPens.add(listPens);
		listPens.setAlignmentY(Component.TOP_ALIGNMENT);
		listPens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPens.setBackground(UIManager.getColor("Panel.background"));
		listPens.setCellRenderer(new ListPensCellRenderer());
		
		popupMenuListPens = new JPopupMenu();
		addPopup(listPens, popupMenuListPens);
		
		mntmEdit = new JMenuItem("Modifica");
		popupMenuListPens.add(mntmEdit);
		
		mntmRemove = new JMenuItem("Rimuovi");
		popupMenuListPens.add(mntmRemove);

		panelNorth = new JPanel();
		panelNorth.setBackground(new Color(211, 211, 211));
		panelNorth.setPreferredSize(new Dimension(10, 30));
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.X_AXIS));

		toolBar_1 = new JToolBar();
		toolBar_1.setBackground(new Color(211, 211, 211));
		toolBar_1.setRollover(true);
		toolBar_1.setFloatable(false);
		panelNorth.add(toolBar_1);
		
		btnNew = new JButton("");
		btnNew.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/New_1_light.png")));
		btnNew.setOpaque(true);
		btnNew.setBorderPainted(false);
		btnNew.setContentAreaFilled(false);
		btnNew.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/New_1.png")));
		toolBar_1.add(btnNew);
		btnNew.setToolTipText("Nuovo progetto");

		btnOpen = new JButton("");
		btnOpen.setContentAreaFilled(false);
		btnOpen.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Open_Folder_1_light.png")));
		btnOpen.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Open_Folder_1.png")));
		toolBar_1.add(btnOpen);
		btnOpen.setToolTipText("Apri progetto");
		btnOpen.setBorderPainted(false);

		btnSave = new JButton("");
		btnSave.setContentAreaFilled(false);
		btnSave.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Save_1_light.png")));
		btnSave.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Save_1.png")));
		btnSave.setToolTipText("Salva progetto");
		btnSave.setBorderPainted(false);
		toolBar_1.add(btnSave);

		toolBar_2 = new JToolBar();
		toolBar_2.setBackground(new Color(211, 211, 211));
		toolBar_2.setFloatable(false);
		panelNorth.add(toolBar_2);

		btnImport = new JButton("");
		btnImport.setContentAreaFilled(false);
		btnImport.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Import_1_light.png")));
		btnImport.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Import_1.png")));
		btnImport.setToolTipText("Importa dati");
		btnImport.setBorderPainted(false);
		toolBar_2.add(btnImport);

		btnExport = new JButton("");
		btnExport.setContentAreaFilled(false);
		btnExport.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Export_1_light.png")));
		btnExport.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Export_1.png")));
		btnExport.setToolTipText("Esporta dati");
		btnExport.setBorderPainted(false);
		toolBar_2.add(btnExport);

		toolBar_3 = new JToolBar();
		toolBar_3.setBackground(new Color(211, 211, 211));
		toolBar_3.setFloatable(false);
		panelNorth.add(toolBar_3);

		btnOpenCOM = new JButton("");
		btnOpenCOM.setContentAreaFilled(false);
		btnOpenCOM.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/stock_draw-curved-connector-with-circles-2_light.png")));
		btnOpenCOM.setSelectedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/stock_draw-curved-connector-with-arrows.png")));
		btnOpenCOM.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/stock_draw-curved-connector-with-circles-2.png")));
		btnOpenCOM.setToolTipText("OnLine");
		btnOpenCOM.setBorderPainted(false);
		toolBar_3.add(btnOpenCOM);
		
		toolBar_4 = new JToolBar();
		toolBar_4.setBackground(new Color(211, 211, 211));
		toolBar_4.setFloatable(false);
		panelNorth.add(toolBar_4);

		btnSettings = new JButton("");
		btnSettings.setContentAreaFilled(false);
		btnSettings.setPressedIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Settings_light.png")));
		btnSettings.setIcon(new ImageIcon(View.class.getResource("/com/wordpress/blogste/ArduinoTrend/Icons/Settings.png")));
		btnSettings.setToolTipText("Settaggi Trend");
		btnSettings.setBorderPainted(false);
		toolBar_4.add(btnSettings);

		panelSouth = new JPanel();
		panelSouth.setBackground(SystemColor.windowBorder);
		FlowLayout flowLayout = (FlowLayout) panelSouth.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panelSouth, BorderLayout.SOUTH);

		lblStatoConnessione_1 = new JLabel("Connessione");
		lblStatoConnessione_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelSouth.add(lblStatoConnessione_1);

		lblStatoConnessione = new JLabel("Offline");
		panelSouth.add(lblStatoConnessione);
		this.pack();
	}
	
	/*
	 * *********** Metodi privati *************
	 */
	
	private static void addPopup(final JList list, final JPopupMenu popup) {
		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					list.setSelectedIndex(list.locationToIndex(e.getPoint()));
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Frame#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		super.setTitle(APP_NAME + " - " + title);
	}
	

	/*
	 * *********** Metodi setter e getter *************
	 */

	

	public void setLblStatoConnessione(String text) {
		lblStatoConnessione.setText(text);
	}

	public void setOnLine(boolean status) {
		trend.setEnabledReg(status);
		btnOpenCOM.setSelected(status);
		btnNew.setEnabled(!status);
		mntmNew.setEnabled(!status);
		btnOpen.setEnabled(!status);
		mntmOpen.setEnabled(!status);
		btnImport.setEnabled(!status);
		mntmImport.setEnabled(!status);
		
		
		if (status) {
			mntmOnLine.setEnabled(false);
			mntmOffLine.setEnabled(true);
			mntmRemove.setEnabled(false);
			
		} else {
			mntmOnLine.setEnabled(true);
			mntmOffLine.setEnabled(false);
			mntmRemove.setEnabled(true);
		}
	}

	public void setListSerialPort(String namesPort[], String namePortSelected) {
		mnSerialPort.removeAll();
		JCheckBoxMenuItem chekBoxPortaSeriale;
		for (int i = 0; i < namesPort.length; i++) {
			chekBoxPortaSeriale = new JCheckBoxMenuItem(namesPort[i]);

			if (namesPort[i].equals(namePortSelected))
				chekBoxPortaSeriale.setState(true);

			chekBoxPortaSeriale.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String name = ((JCheckBoxMenuItem) e.getSource()).getText();
					for (int i = 0; i < mnSerialPort.getItemCount(); i++) {
						JCheckBoxMenuItem item = ((JCheckBoxMenuItem) mnSerialPort
								.getItem(i));
						if (item.getText().equals(name)) {
							item.setState(true);
							PreferenceData.setValue(PreferenceData.SERIAL_COM_PORT, name);
						} else {
							item.setState(false);
						}
					}
				}

			});
			mnSerialPort.add(chekBoxPortaSeriale);
		}
	}

	public String getNameSerialPortSelected() {
		String name = null;
		for (int i = 0; i < mnSerialPort.getItemCount(); i++) {
			JCheckBoxMenuItem item = ((JCheckBoxMenuItem) mnSerialPort
					.getItem(i));
			if (item.getState())
				name = item.getText();
		}
		return name;
	}

	public void setListDataRate(String[] arrDataRate, String DataRateSelected) {
		mnDataRate.removeAll();
		JCheckBoxMenuItem chekBoxBaudrate;
		for (int i = 0; i < arrDataRate.length; i++) {
			chekBoxBaudrate = new JCheckBoxMenuItem(arrDataRate[i]);
			if (arrDataRate[i].equals(DataRateSelected)) {
				chekBoxBaudrate.setState(true);
			}
			chekBoxBaudrate.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String name = ((JCheckBoxMenuItem) e.getSource()).getText();
					for (int i = 0; i < mnDataRate.getItemCount(); i++) {
						JCheckBoxMenuItem item = ((JCheckBoxMenuItem) mnDataRate
								.getItem(i));
						if (item.getText().equals(name)) {
							item.setState(true);
							PreferenceData.setValue(PreferenceData.SERIAL_BAUD_RATE, name);
						} else {
							item.setState(false);
						}
					}
				}

			});
			mnDataRate.add(chekBoxBaudrate);
		}
	}

	public int getItemDataRate() {
		for (int i = 0; i < mnDataRate.getItemCount(); i++) {
			JCheckBoxMenuItem item = ((JCheckBoxMenuItem) mnDataRate.getItem(i));
			if (item.isSelected()) {
				return i;
			}
		}
		return 0;
	}

	public void setModelListPens(ListModel model) {
		listPens.setModel(model);
	}
	
	public int getIndexSelectedListPens(){
		return listPens.getSelectedIndex();
	}
	
	public void setEnabledAddPen(boolean state){
		btnAddPen.setEnabled(state);
	}
	
	public Trend getTrend(){
		return trend;
	}

	/*
	 * *********** Metodi per assegnare i listener *************
	 */

	public void addListenerMenuComunicazione(MenuListener listener) {
		mnComunicazione.addMenuListener(listener);
	}
	
	public void addListenerAtNew(ActionListener listener){
		mntmNew.addActionListener(listener);
		btnNew.addActionListener(listener);
	}
	
	public void addListenerAtOpen(ActionListener listener){
		mntmOpen.addActionListener(listener);
		btnOpen.addActionListener(listener);
	}
	
	public void addListendrAtSave(ActionListener listener){
		mntmSave.addActionListener(listener);
		btnSave.addActionListener(listener);
	}
	
	public void addListendrAtSaveWithName(ActionListener listener){
		mntmSaveWithName.addActionListener(listener);
	}
	
	public void addListenerAtImport(ActionListener listener){
		mntmImport.addActionListener(listener);
		btnImport.addActionListener(listener);
	}
	
	public void addListenerAtExport(ActionListener listener){
		mntmExport.addActionListener(listener);
		btnExport.addActionListener(listener);
	}

	public void addListenerAtConnectionCommand(ActionListener listener) {
		mntmOnLine.addActionListener(listener);
		mntmOffLine.addActionListener(listener);
		btnOpenCOM.addActionListener(listener);
	}
	
	public void addListenerAtSettings(ActionListener listener){
		btnSettings.addActionListener(listener);
	}

	public void addListenerAtInfo(ActionListener listener) {
		mntmInfo.addActionListener(listener);
	}

	public void addListenerAddPen(ActionListener listener) {
		btnAddPen.addActionListener(listener);
	}
	
	public void addListenerMntmEditListPens (ActionListener listener){
		mntmEdit.addActionListener(listener);
	}
	
	public void addListenerMntmRemoveListPens (ActionListener listener){
		mntmRemove.addActionListener(listener);
	}
	
	public void addListenerAtListPens (MouseListener listener){
		listPens.addMouseListener(listener);
	}

	/*
	 * *********** Metodi pubblici *************
	 */

	public void popupMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public void popupErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Errore!", JOptionPane.ERROR_MESSAGE);
	}
	
	

	/*
	 * *********** Inner Class per render della lista penne *************
	 */

	private class ListPensCellRenderer extends JPanel implements
			ListCellRenderer {

		private static final long serialVersionUID = 1L;
		
		JLabel labelName;
		JLabel labelValue;

		public ListPensCellRenderer() {

			setMinimumSize(new Dimension(150, 20));
			setMaximumSize(new Dimension(150, 20));
			setPreferredSize(new Dimension(150, 20));
			setOpaque(true);

			// FlowLayout layout = new FlowLayout();
			GridLayout layout = new GridLayout();
			layout.setColumns(2);
			layout.setRows(1);
			this.setLayout(layout);
			this.setBorder(new EmptyBorder(0, 0, 0, 5));

			labelName = new JLabel("", JLabel.LEFT);
			labelValue = new JLabel("", JLabel.RIGHT);
			
		}

		@Override
		public Component getListCellRendererComponent(JList jlist,
				Object object, int index, boolean isSelected,
				boolean cellHasFocus) {

			Pen pen = (Pen) object;
			Color color = pen.getColor();
			
			this.setToolTipText(pen.getDescription());

			labelName.setText(pen.getName());
			IconColor icon = new IconColor(color);
			labelName.setIcon(icon);
			labelName.setIconTextGap(8);
			
			String value = String.format("%.2f", pen.getValue());
			labelValue.setText(value);

			if (isSelected) {
				setBackground(Color.gray);
			} else {
				setBackground(jlist.getBackground());
			}

			add(labelName);
			add(labelValue);

			return this;
		}

		private class IconColor implements Icon {

			Color color;
			static final int HEIGHT_RECT = 5;
			static final int WIDTH_RECT = 12;
			static final int V_GAP = -1;
			static final int H_GAP = 10;

			public IconColor(Color color) {
				super();
				this.color = color;
			}

			@Override
			public int getIconHeight() {
				return HEIGHT_RECT;
			}

			@Override
			public int getIconWidth() {
				return WIDTH_RECT + H_GAP;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {

				g.setColor(color);
				g.fillRoundRect(x + H_GAP, y + V_GAP, WIDTH_RECT, HEIGHT_RECT,
						0, 0);
			}

		}

	}

}