package main;

import imageutils.ImageUtils;

import java.io.IOException;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

import salamidetector.SalamiDetector;

class RunBasicThreshold implements Runnable {
	private String filename;
	private int binThreshold;
	private int contThreshold;
	private Composite parent;
	private ImageData imageData;

	
	public RunBasicThreshold(String filename, int bThresh, int contThresh, Composite parent) {
		// TODO Auto-generated constructor stub
		this.filename = filename;
		this.binThreshold = bThresh;
		this.contThreshold = contThresh;
		this.parent = parent;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SalamiDetector sd = new SalamiDetector();		
		
		try {
			imageData = ImageUtils.matToImageData(sd.contourFinder(sd.prepareImage(this.filename, this.binThreshold),this.contThreshold));						
			ImageUtils.drawImageIn(parent, imageData);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
	public ImageData getImageData(){
		return imageData;
	}
	
}
class RunHsvThreshold implements Runnable {
	private String filename;
	private int hHigh = 179;
	private int hLow = 0;
	private int sHigh = 255;
	private int sLow = 0;
	private int vHigh = 255;
	private int vLow = 0;
	private ImageData imageData;
	private Composite parent;
	
	public RunHsvThreshold(String filename, int hh, int hl, int sh, int sl, int vh, int vl, Composite parent) {
		this.filename = filename;
		this.hHigh = hh;
		this.hLow = hl;
		this.vHigh = vh;
		this.vLow = vl;
		this.sHigh = sh;
		this.sLow = sl;
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		SalamiDetector sd = new SalamiDetector();
		try {
			imageData = ImageUtils.matToImageData(sd.hsvDetection(filename, hHigh, hLow, sHigh, sLow, vHigh, vLow));
			ImageUtils.drawImageIn(parent, imageData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ImageData getImageData() {
		return imageData;
	}
	
}