package ib.salami.main;

import ib.salami.libloader.LibraryLoad;

public class Run {
	public static void main(String[] args) {

		LibraryLoad ll = new LibraryLoad();
		ll.run();

		
		try {
			AppInterface window = new AppInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
