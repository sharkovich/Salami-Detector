package main;

import libloader.LibraryLoad;

public class Run {
	public static void main(String[] args) {

		LibraryLoad ll = new LibraryLoad();
		ll.loadSwtJar();
		ll.loadOpenCVLibrary();

		
		try {
			AppInterface window = new AppInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
