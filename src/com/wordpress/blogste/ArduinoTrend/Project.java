package com.wordpress.blogste.ArduinoTrend;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.wordpress.blogste.trend.Pen;
import com.wordpress.blogste.trend.PenModel;
import com.wordpress.blogste.trend.Trend;
import com.wordpress.blogste.trend.TrendModel;

@SuppressWarnings("unused")
public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1593862289059985704L;
	private String title;
	private PenModel penModels[];
	private TrendModel trendModel;
	public static final int PENS_MAX = 8;

	public Project(String title) {
		this.title = title;
		trendModel = new TrendModel(5, 5, 20000, 0, 1024,
				TrendModel.TYPE_SCALE_AXIS_Y.AUTOMATIC);
	}

	public void save(File file, Pen[] pens, TrendModel trendModel)
			throws IOException {

		penModels = new PenModel[pens.length];

		for (int i = 0; i < pens.length; i++) {
			if (pens[i] != null) {
				penModels[i] = new PenModel(pens[i].getModel().getName(),
						pens[i].getModel().getDescription(), pens[i].getModel()
								.getUnits(), pens[i].getModel().getColor(),
						pens[i].getModel().getMin(), pens[i].getModel()
								.getMax());
			}

		}

		this.trendModel = trendModel;

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		fileOutputStream.close();
	}

	@SuppressWarnings("resource")
	public void load(File file) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(
				fileInputStream);
		Project project = (Project) objectInputStream.readObject();
		this.setTitle(project.getTitle());
		this.setPenModels(project.getPenModels());
		this.setTrendModel(project.getTrendModel());

	}

	public void exportProject(File file, Pen[] pens, TrendModel trendModel)
			throws IOException {

		penModels = new PenModel[pens.length];

		for (int i = 0; i < pens.length; i++) {
			if (pens[i] != null) {
				penModels[i] = pens[i].getModel();
			}

		}

		this.trendModel = trendModel;

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		fileOutputStream.close();

	}

	public void exportCSV(File file, Pen[] pens) {

	}

	public void importProjectAndData(File file) throws IOException,
			ClassNotFoundException {
		load(file);
	}

	/**
	 * @return the name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the name to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the dataPens
	 */
	public PenModel[] getPenModels() {
		return penModels;
	}

	/**
	 * @param penModels
	 *            the dataPens to set
	 */
	public void setPenModels(PenModel[] penModels) {
		this.penModels = penModels;
	}

	/**
	 * @return the trendModel
	 */
	public TrendModel getTrendModel() {
		return trendModel;
	}

	/**
	 * @param trendModel
	 *            the trendModel to set
	 */
	public void setTrendModel(TrendModel trendModel) {
		this.trendModel = trendModel;
	}

}
