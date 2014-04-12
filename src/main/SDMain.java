package main;

import org.opencv.core.Core;

import salamidetector.AppInterface;


public class SDMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		AppInterface sd = new AppInterface();
		sd.open();
	}

}
