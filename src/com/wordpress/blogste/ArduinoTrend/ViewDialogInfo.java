package com.wordpress.blogste.ArduinoTrend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class ViewDialogInfo extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private static final int MAJOR_VERSION = 0;
	private static final int MINOR_VERSION = 1;
	
	private static final String version = "Versione: " + MAJOR_VERSION + "." + MINOR_VERSION;

	
	private final JPanel contentPanel = new JPanel();
	
	

	public ViewDialogInfo(Frame frame) {
		super(frame);
		setResizable(false);
		inizialize();
	}

	/**
	 * Create the dialog.
	 */
	public void inizialize() {
		setTitle("Informazioni di Arduino Trend");
		setModal(true);
		setBounds(100, 100, 450, 248);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.info);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JLabel lblArduinoTrend = new JLabel("Arduino Trend");
			contentPanel.add(lblArduinoTrend);
		}
		{
			JLabel lblVersion = new JLabel(version);
			contentPanel.add(lblVersion);
		}
		{
			JLabel lblByStefanoAniballi = new JLabel("Developer: Stefano Aniballi");
			contentPanel.add(lblByStefanoAniballi);
		}
		{
			JLabel lblMail = new JLabel("e-mail: stefano.aniballi@gmail.com");
			contentPanel.add(lblMail);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		setVisible(true);
	}
	

}
