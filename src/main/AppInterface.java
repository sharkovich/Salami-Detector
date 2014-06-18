package main;


import imageutils.ImageUtils;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AppInterface {
	
	protected Shell shlSalamiDetector;
	
	private Text textBT;
	private Text textCT;
	private int binaryThersholdValue = 100;
	private int contourThresholdvalue = 150;
	private File file;
	private Image image;
	private boolean isGenerated = false;
	private ImageData generatedImageData = null;
	private ImageData vanillaImageData = null;
	
	private int openedTab = 0;
	
	private int hHigh = 179;
	private int hLow = 0;
	private int sHigh = 255;
	private int sLow = 0;
	private int vHigh = 255;
	private int vLow = 0;

	private static final String[] FILTER_NAMES = {
		"Image files (*.bmp;*.png;*.jpg;*.jpeg)",
		"JPEG (*.jpg;*.jpeg)",
	    "BMP (*.bmp)",
	    "PNG (*.png)",
	    "All Files (*.*)"};

	  private static final String[] FILTER_EXTS = {
		"*.bmp;*.png;*.jpg;*.jpeg",
		"*.jpg;*.jpeg",
	  	"*.bmp",
	  	"*.png",
	  	"*.*"};
	  private Text tSHigh;
	  private Text tHLow;
	  private Text tHHigh;
	  private Text tSLow;
	  private Text tVHigh;
	  private Text tVLow;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		Image icon = new Image(shlSalamiDetector.getDisplay(), AppInterface.class.getResourceAsStream("/res/icon.png"));

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
	private File getFilename()
	{
		FileDialog fd = new FileDialog(shlSalamiDetector);
		
		fd.setFilterNames(FILTER_NAMES);
		fd.setFilterExtensions(FILTER_EXTS);
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
	private void clearImage(Composite parent) {
		
		isGenerated = false;
		ImageUtils.disposeImages(parent);
		file = null;
		generatedImageData = null;
		vanillaImageData = null;
	}


	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlSalamiDetector = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shlSalamiDetector.setSize(674, 768);
		shlSalamiDetector.setText("Salami Detector");
		shlSalamiDetector.setLayout(new FormLayout());
		
		
		final CTabFolder tabFolder = new CTabFolder(shlSalamiDetector, SWT.FLAT);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.right = new FormAttachment(0, 665);
		fd_tabFolder.top = new FormAttachment(0, 3);
		fd_tabFolder.left = new FormAttachment(0, 3);
		tabFolder.setLayoutData(fd_tabFolder);

		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmBasicThreshold = new CTabItem(tabFolder, SWT.NONE);
		tbtmBasicThreshold.setText("Binary Threshold");
		
		Composite bThreshComposite = new Composite(tabFolder, SWT.NONE);
		tbtmBasicThreshold.setControl(bThreshComposite);
				GridLayout gl_bThreshComposite = new GridLayout(3, false);
				gl_bThreshComposite.marginRight = 5;
				gl_bThreshComposite.marginTop = 10;
				gl_bThreshComposite.marginLeft = 10;
				bThreshComposite.setLayout(gl_bThreshComposite);
				
				Label lblContourThreshold = new Label(bThreshComposite, SWT.NONE);
				lblContourThreshold.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
				lblContourThreshold.setText("Contour\r\nThreshold");
		
		
				
				final Scale scaleContThresh = new Scale(bThreshComposite, SWT.NONE);
				GridData gd_scaleContThresh = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
				gd_scaleContThresh.widthHint = 500;
				scaleContThresh.setLayoutData(gd_scaleContThresh);
				scaleContThresh.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						contourThresholdvalue = scaleContThresh.getSelection();
						textCT.setText(Integer.toString(contourThresholdvalue));
					}
				});
				scaleContThresh.setMaximum(255);
				scaleContThresh.setSelection(150);
				
				textCT = new Text(bThreshComposite, SWT.BORDER);
				textCT.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
				textCT.setEditable(false);
				textCT.setText("150");
						
						Label lblThreshold = new Label(bThreshComposite, SWT.NONE);
						lblThreshold.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
						lblThreshold.setText("Binary\r\nThreshold");
						
						final Scale scaleBinThresh = new Scale(bThreshComposite, SWT.NONE);
						GridData gd_scaleBinThresh = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
						gd_scaleBinThresh.widthHint = 500;
						scaleBinThresh.setLayoutData(gd_scaleBinThresh);
						scaleBinThresh.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								binaryThersholdValue = scaleBinThresh.getSelection();
								textBT.setText(Integer.toString(binaryThersholdValue));
							}
						});
						
								
								scaleBinThresh.setMaximum(255);
								scaleBinThresh.setSelection(100);
						
						textBT = new Text(bThreshComposite, SWT.BORDER);
						textBT.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
						textBT.setEditable(false);
						textBT.setText("100");
						

		Composite mainComposite = new Composite(shlSalamiDetector, SWT.NONE);
		fd_tabFolder.bottom = new FormAttachment(mainComposite, -6);
		mainComposite.setLayout(new GridLayout(3, false));
		FormData fd_mainComposite = new FormData();
		fd_mainComposite.top = new FormAttachment(0, 155);
		fd_mainComposite.bottom = new FormAttachment(100);
		fd_mainComposite.left = new FormAttachment(0, 3);
		fd_mainComposite.right = new FormAttachment(100, -3);
		
		CTabItem tbtmHsvThreshold = new CTabItem(tabFolder, SWT.NONE);
		tbtmHsvThreshold.setText("HSV Threshold");
		
		Composite hsvThreshComposite = new Composite(tabFolder, SWT.NONE);
			tbtmHsvThreshold.setControl(hsvThreshComposite);
			GridLayout gl_hsvThreshComposite = new GridLayout(9, false);
			gl_hsvThreshComposite.horizontalSpacing = 10;
			gl_hsvThreshComposite.marginLeft = 5;
			gl_hsvThreshComposite.marginRight = 5;
			hsvThreshComposite.setLayout(gl_hsvThreshComposite);
			new Label(hsvThreshComposite, SWT.NONE);
			
			Label lblHue = new Label(hsvThreshComposite, SWT.NONE);
			lblHue.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			lblHue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblHue.setAlignment(SWT.CENTER);
			lblHue.setText("Hue");
			new Label(hsvThreshComposite, SWT.NONE);
			new Label(hsvThreshComposite, SWT.NONE);
			
			Label lblSaturation = new Label(hsvThreshComposite, SWT.NONE);
			lblSaturation.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblSaturation.setText("Saturation");
			lblSaturation.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			lblSaturation.setAlignment(SWT.CENTER);
			new Label(hsvThreshComposite, SWT.NONE);
			new Label(hsvThreshComposite, SWT.NONE);
			
			Label lblValue = new Label(hsvThreshComposite, SWT.NONE);
			lblValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblValue.setText("Value");
			lblValue.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			lblValue.setAlignment(SWT.CENTER);
			new Label(hsvThreshComposite, SWT.NONE);
			
			Label label = new Label(hsvThreshComposite, SWT.NONE);
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			label.setText("High");
			
			final Scale scaleHHigh = new Scale(hsvThreshComposite, SWT.NONE);
			scaleHHigh.setMaximum(179);
			scaleHHigh.setSelection(179);
			scaleHHigh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tHHigh.setText(Integer.toString(scaleHHigh.getSelection()));
					hHigh = scaleHHigh.getSelection();
				}
			});
			GridData gd_scaleHHigh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleHHigh.widthHint = 130;
			scaleHHigh.setLayoutData(gd_scaleHHigh);
			
			tHHigh = new Text(hsvThreshComposite, SWT.BORDER | SWT.CENTER);
			tHHigh.setEditable(false);
			tHHigh.setText("179");
			GridData gd_tHHigh = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_tHHigh.widthHint = 17;
			tHHigh.setLayoutData(gd_tHHigh);
			
			Label lblHigh = new Label(hsvThreshComposite, SWT.NONE);
			lblHigh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblHigh.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			lblHigh.setText("High");
			
			final Scale scaleSHigh = new Scale(hsvThreshComposite, SWT.NONE);
			scaleSHigh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tSHigh.setText(Integer.toString(scaleSHigh.getSelection()));
					sHigh = scaleSHigh.getSelection();
				}
			});
			scaleSHigh.setMaximum(255);
			scaleSHigh.setSelection(255);
			GridData gd_scaleSHigh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleSHigh.widthHint = 130;
			scaleSHigh.setLayoutData(gd_scaleSHigh);
			
			tSHigh = new Text(hsvThreshComposite, SWT.BORDER | SWT.CENTER);
			tSHigh.setText("255");
			tSHigh.setEditable(false);
			GridData gd_tSHigh = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_tSHigh.widthHint = 17;
			tSHigh.setLayoutData(gd_tSHigh);
			
			Label label_3 = new Label(hsvThreshComposite, SWT.NONE);
			label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_3.setText("High");
			label_3.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			
			final Scale scaleVHigh = new Scale(hsvThreshComposite, SWT.NONE);
			scaleVHigh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tVHigh.setText(Integer.toString(scaleVHigh.getSelection()));
					vHigh = scaleVHigh.getSelection();
				}
			});
			scaleVHigh.setMaximum(255);
			scaleVHigh.setSelection(255);
			GridData gd_scaleVHigh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleVHigh.widthHint = 130;
			scaleVHigh.setLayoutData(gd_scaleVHigh);
			
			tVHigh = new Text(hsvThreshComposite, SWT.BORDER | SWT.CENTER);
			tVHigh.setText("255");
			tVHigh.setEditable(false);
			GridData gd_tVHigh = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_tVHigh.widthHint = 17;
			tVHigh.setLayoutData(gd_tVHigh);
			
			Label lblLow = new Label(hsvThreshComposite, SWT.NONE);
			lblLow.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblLow.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			lblLow.setText("Low");
			
			final Scale scaleHLow = new Scale(hsvThreshComposite, SWT.NONE);
			scaleHLow.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tHLow.setText(Integer.toString(scaleHLow.getSelection()));
					hLow = scaleHLow.getSelection();
				}
			});
			scaleHLow.setMaximum(179);
			GridData gd_scaleHLow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleHLow.widthHint = 130;
			scaleHLow.setLayoutData(gd_scaleHLow);
			
			tHLow = new Text(hsvThreshComposite, SWT.BORDER | SWT.CENTER);
			tHLow.setText("0");
			tHLow.setEditable(false);
			GridData gd_tHLow = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gd_tHLow.widthHint = 17;
			tHLow.setLayoutData(gd_tHLow);
			
			Label label_1 = new Label(hsvThreshComposite, SWT.NONE);
			label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_1.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			label_1.setText("Low");
			
			final Scale scaleSLow = new Scale(hsvThreshComposite, SWT.NONE);
			scaleSLow.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tSLow.setText(Integer.toString(scaleSLow.getSelection()));
					sLow = scaleSLow.getSelection();
				}
			});
			scaleSLow.setMaximum(255);
			GridData gd_scaleSLow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleSLow.widthHint = 130;
			scaleSLow.setLayoutData(gd_scaleSLow);
			
			tSLow = new Text(hsvThreshComposite, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
			tSLow.setText("0");
			tSLow.setEditable(false);
			GridData gd_tSLow = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_tSLow.widthHint = 17;
			tSLow.setLayoutData(gd_tSLow);
			
			Label label_2 = new Label(hsvThreshComposite, SWT.NONE);
			label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_2.setText("Low");
			label_2.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
			
			final Scale scaleVLow = new Scale(hsvThreshComposite, SWT.NONE);
			scaleVLow.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					tVLow.setText(Integer.toString(scaleVLow.getSelection()));
					vLow = scaleVLow.getSelection();
				}
			});
			scaleVLow.setMaximum(255);
			GridData gd_scaleVLow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_scaleVLow.widthHint = 130;
			scaleVLow.setLayoutData(gd_scaleVLow);
			
			tVLow = new Text(hsvThreshComposite, SWT.BORDER | SWT.CENTER);
			tVLow.setText("0");
			tVLow.setEditable(false);
			GridData gd_tVLow = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_tVLow.widthHint = 17;
			tVLow.setLayoutData(gd_tVLow);
			mainComposite.setLayoutData(fd_mainComposite);
		
			
			
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				System.out.println(tabFolder.getSelectionIndex());
				openedTab = tabFolder.getSelectionIndex();
			}
		});	
			
		final Composite imageFrame = new Composite(mainComposite, SWT.BORDER);
		GridData gd_imageFrame = new GridData(SWT.LEFT, SWT.CENTER, true, true, 3, 2);
		gd_imageFrame.heightHint = 562;
		gd_imageFrame.widthHint = 653;
		imageFrame.setLayoutData(gd_imageFrame);
		imageFrame.setToolTipText("Click on image to view original");
		imageFrame.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnOpen = new Button(mainComposite, SWT.CENTER);
		GridData gd_btnOpen = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOpen.widthHint = 135;
		btnOpen.setLayoutData(gd_btnOpen);
		btnOpen.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		btnOpen.addSelectionListener(new SelectionAdapter() {
			//Open image
			@Override	
			public void widgetSelected(SelectionEvent e) {			
				clearImage(imageFrame);
				file = getFilename();
				if (file!=null) {
					
					vanillaImageData = new ImageData(file.getAbsolutePath());			
					ImageUtils.drawImageIn(imageFrame, vanillaImageData);
				}
			}
		});
		btnOpen.setText("Open");
		
		Button btnClear = new Button(mainComposite, SWT.NONE);
		GridData gd_btnClear = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnClear.widthHint = 135;
		btnClear.setLayoutData(gd_btnClear);
		btnClear.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearImage(imageFrame);
			}
		});
		btnClear.setText("Clear");
		

		Button btnGenerate = new Button(mainComposite, SWT.NONE);
		GridData gd_btnGenerate = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnGenerate.widthHint = 210;
		btnGenerate.setLayoutData(gd_btnGenerate);
		btnGenerate.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.BOLD));
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (file != null) {
					switch (openedTab) {
					case 0:
						RunBasicThreshold startBT = new RunBasicThreshold(file.getAbsolutePath(), binaryThersholdValue, contourThresholdvalue, imageFrame);
						startBT.run();
						generatedImageData = startBT.getImageData();
						isGenerated = true;
						break;
					case 1:
						RunHsvThreshold startHSV = new RunHsvThreshold(file.getAbsolutePath(), hHigh, hLow, sHigh, sLow, vHigh, vLow, imageFrame);
						startHSV.run();
						generatedImageData = startHSV.getImageData();
						isGenerated = true;
						break;

					default:
						break;
					}

					
				} else {
					MessageBox mb = new MessageBox(shlSalamiDetector);
					mb.setMessage("No file selected!");
					mb.open();
				}
			}
		});
		btnGenerate.setText("Generate");
		
		shlSalamiDetector.setDefaultButton(btnGenerate);


		
		final Label infoText1 = new Label(imageFrame, SWT.CENTER);
		infoText1.setFont(SWTResourceManager.getFont("Calibri", 9, SWT.NORMAL));
		infoText1.setAlignment(SWT.CENTER);
		infoText1.setText("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\nClick on generated image to view original");

		
		//Menus start here
		
		Menu menu = new Menu(shlSalamiDetector, SWT.BAR);
		shlSalamiDetector.setMenuBar(menu);
		
		MenuItem mntmFile_1 = new MenuItem(menu, SWT.CASCADE);
		mntmFile_1.setText("File");
		
		Menu menu_1 = new Menu(mntmFile_1);
		mntmFile_1.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				file = getFilename();
				if (file!=null) {
					ImageData tmpData = new ImageData(file.getAbsolutePath());			
					ImageUtils.drawImageIn(imageFrame, tmpData);
					
				}
			}
		});
		mntmNewItem.setText("Open Image");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearImage(imageFrame);
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
		shlSalamiDetector.getDisplay().addFilter(SWT.MouseDown, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	        	if (event.widget == infoText1)
	        		System.out.println("Click");
	        	if (event.widget.getClass() == Canvas.class && isGenerated) {

		        	ImageUtils.disposeImages(imageFrame);
		        	ImageUtils.drawImageIn(imageFrame, vanillaImageData);
	        	}
	        }
	    });
		shlSalamiDetector.getDisplay().addFilter(SWT.MouseUp, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	        	if (event.widget.getClass() == Canvas.class && isGenerated) {
		        	ImageUtils.disposeImages(imageFrame);
		        	ImageUtils.drawImageIn(imageFrame, generatedImageData);
	        	}
	        }
	    });


		



	}
}

