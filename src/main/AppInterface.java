package main;


import imageutils.ImageUtils;

import java.io.File;
import java.io.IOException;

import libloader.LibraryLoad;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opencv.core.Mat;

import salamidetector.SalamiDetector;

public class AppInterface {
	
	protected Shell shlSalamiDetector;
	
	private Text textPT;
	private Text text;
	private int binaryThersholdValue = 100;
	private int contourThresholdvalue = 150;
	private File file;
	private Image image;
	private String[] extensions = {"*.png","*.jpg", "*.jpeg","*.bmp"};

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		LibraryLoad ll = new LibraryLoad();
		ll.loadLibrary();
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try {
			AppInterface window = new AppInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		Image icon = new Image(shlSalamiDetector.getDisplay(), AppInterface.class.getResourceAsStream("/res/icon.png"));
		//Image icon = new Image(shlSalamiDetector.getDisplay(), "res/icon.png");
		shlSalamiDetector.setImage(icon);
		
		shlSalamiDetector.open();
		shlSalamiDetector.layout();
		while (!shlSalamiDetector.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (image != null) image.dispose();
	}

	/**
	 * Opens file dialog for choosing a file to open.
	 * @return 
	 */
	public File getFilename()
	{
		FileDialog fd = new FileDialog(shlSalamiDetector);
		
	

		
		
		
		
		fd.setFilterExtensions(extensions);
		fd.setFilterPath("C:\\");
		
		String path = fd.open();
		
		if (path != null) {			
			System.out.printf("File found.\n");
			File file = new File(path);
			return file;
		} else {
			System.out.printf("File not found!\n");
			return null;
		}
		
	}


	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSalamiDetector = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		shlSalamiDetector.setSize(667, 768);
		shlSalamiDetector.setText("Salami Detector");



		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		shlSalamiDetector.setLayout(new FormLayout());
		
		final Composite composite = new Composite(shlSalamiDetector, SWT.BORDER);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.top = new FormAttachment(0, 129);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		

		
		
		
		Button btnOpen = new Button(shlSalamiDetector, SWT.NONE);
		FormData fd_btnOpen = new FormData();
		fd_btnOpen.bottom = new FormAttachment(0, 52);
		fd_btnOpen.right = new FormAttachment(0, 552);
		fd_btnOpen.top = new FormAttachment(0, 4);
		fd_btnOpen.left = new FormAttachment(0, 470);
		btnOpen.setLayoutData(fd_btnOpen);
		btnOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				file = getFilename();
				if (file!=null) {
					ImageData tmpData = new ImageData(file.getAbsolutePath());			
					ImageUtils.drawImageIn(composite, tmpData);
				}
			}
		});
		btnOpen.setText("Open");
		
		Button btnGenerate = new Button(shlSalamiDetector, SWT.NONE);
		fd_composite.right = new FormAttachment(btnGenerate, 0, SWT.RIGHT);
		fd_composite.left = new FormAttachment(btnGenerate, -642);
		FormData fd_btnGenerate = new FormData();
		fd_btnGenerate.left = new FormAttachment(0, 470);
		fd_btnGenerate.bottom = new FormAttachment(0, 106);
		fd_btnGenerate.right = new FormAttachment(0, 652);
		fd_btnGenerate.top = new FormAttachment(0, 58);
		btnGenerate.setLayoutData(fd_btnGenerate);
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (file != null) {
					
					SalamiDetector sd = new SalamiDetector();
					Mat imageMat = sd.contourFinder(sd.prepareImage(file.getAbsolutePath(), binaryThersholdValue),contourThresholdvalue);
					
					ImageData imageData;
					
					try {
						imageData = ImageUtils.matToImageData(imageMat);
						ImageUtils.drawImageIn(composite, imageData);
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					

					
				} else {
					MessageBox mb = new MessageBox(shlSalamiDetector);
					mb.setMessage("No file selected!");
					mb.open();
				}
			}
		});
		btnGenerate.setText("Generate");
		
		textPT = new Text(shlSalamiDetector, SWT.BORDER);
		FormData fd_textPT = new FormData();
		fd_textPT.top = new FormAttachment(0, 18);
		fd_textPT.left = new FormAttachment(0, 424);
		textPT.setLayoutData(fd_textPT);
		textPT.setEditable(false);
		textPT.setText("100");
		
		text = new Text(shlSalamiDetector, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 78);
		fd_text.left = new FormAttachment(0, 424);
		text.setLayoutData(fd_text);
		text.setEditable(false);
		text.setText("150");
		
		Label lblThreshold = new Label(shlSalamiDetector, SWT.NONE);
		FormData fd_lblThreshold = new FormData();
		fd_lblThreshold.right = new FormAttachment(0, 115);
		fd_lblThreshold.top = new FormAttachment(0, 23);
		fd_lblThreshold.left = new FormAttachment(0, 23);
		lblThreshold.setLayoutData(fd_lblThreshold);
		lblThreshold.setText("Binary Threshold");
		
		Label lblContourThreshold = new Label(shlSalamiDetector, SWT.NONE);
		FormData fd_lblContourThreshold = new FormData();
		fd_lblContourThreshold.right = new FormAttachment(0, 115);
		fd_lblContourThreshold.top = new FormAttachment(0, 81);
		fd_lblContourThreshold.left = new FormAttachment(0, 13);
		lblContourThreshold.setLayoutData(fd_lblContourThreshold);
		lblContourThreshold.setText("Contour Threshold");
		
		shlSalamiDetector.setDefaultButton(btnGenerate);
		
		final Scale scale = new Scale(shlSalamiDetector, SWT.NONE);
		FormData fd_scale = new FormData();
		fd_scale.right = new FormAttachment(0, 418);
		fd_scale.top = new FormAttachment(0, 10);
		fd_scale.left = new FormAttachment(0, 121);
		scale.setLayoutData(fd_scale);
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				binaryThersholdValue = scale.getSelection();
				textPT.setText(Integer.toString(binaryThersholdValue));
			}
		});

		
		scale.setMaximum(255);
		scale.setSelection(100);
		
		final Scale scale_1 = new Scale(shlSalamiDetector, SWT.NONE);
		FormData fd_scale_1 = new FormData();
		fd_scale_1.right = new FormAttachment(0, 418);
		fd_scale_1.top = new FormAttachment(0, 69);
		fd_scale_1.left = new FormAttachment(0, 121);
		scale_1.setLayoutData(fd_scale_1);
		scale_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				contourThresholdvalue = scale_1.getSelection();
				text.setText(Integer.toString(contourThresholdvalue));
			}
		});
		scale_1.setMaximum(255);
		scale_1.setSelection(150);
		
		Button btnClear = new Button(shlSalamiDetector, SWT.NONE);
		FormData fd_btnClear = new FormData();
		fd_btnClear.bottom = new FormAttachment(0, 52);
		fd_btnClear.right = new FormAttachment(0, 652);
		fd_btnClear.top = new FormAttachment(0, 4);
		fd_btnClear.left = new FormAttachment(0, 570);
		btnClear.setLayoutData(fd_btnClear);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImageUtils.disposeChildren(composite);
			}
		});
		btnClear.setText("Clear");
		
		Menu menu = new Menu(shlSalamiDetector, SWT.BAR);
		shlSalamiDetector.setMenuBar(menu);
		
		MenuItem mntmFile_1 = new MenuItem(menu, SWT.CASCADE);
		mntmFile_1.setText("File");
		
		Menu menu_1 = new Menu(mntmFile_1);
		mntmFile_1.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.setText("Open Image");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImageUtils.disposeChildren(composite);
				file = null;
			}
		});
		mntmNewItem_2.setText("Clear");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmNewItem_3 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlSalamiDetector.dispose();
			}
		});
		mntmNewItem_3.setText("Exit");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_2, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AboutWindow aw = new AboutWindow(shlSalamiDetector, SWT.SMOOTH & (~SWT.RESIZE));
				aw.open();
			}
		});
		mntmNewItem_1.setText("About");
		

		


	}
}

