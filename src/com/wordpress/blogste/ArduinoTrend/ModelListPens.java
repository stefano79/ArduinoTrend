package com.wordpress.blogste.ArduinoTrend;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import com.wordpress.blogste.trend.Pen;

public class ModelListPens extends AbstractListModel implements Observer {

	private static final long serialVersionUID = 1L;
	
	Pen[] pens;

	public ModelListPens(Pen[] pens) {
		super();
		this.pens = pens;
	}

	@Override
	public Object getElementAt(int index) {
		if (pens[index] != null) {
			return pens[index];
		} else {
			return null;
		}
	}

	@Override
	public int getSize() {
		int value = 0;
		for (Pen pen : pens)
			if (pen != null)
				value++;
		return value;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.fireContentsChanged(this, 0, getSize());
	}

	/**
	 * @param pens the pens to set
	 */
	public void setPens(Pen[] pens) {
		this.pens = pens;
		update(null, null);
	}
	
	

}
