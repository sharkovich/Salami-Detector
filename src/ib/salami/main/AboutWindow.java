package ib.salami.main;

import ib.salami.imageutils.ImageUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutWindow extends Dialog {

	protected Object result;
	protected Shell shlAboutSalamiDetector;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AboutWindow(Shell parent, int style) {
		super(parent, style);
		setText("About");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAboutSalamiDetector.open();
		shlAboutSalamiDetector.layout();
		Display display = getParent().getDisplay();
		while (!shlAboutSalamiDetector.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAboutSalamiDetector = new Shell(getParent(), getStyle());

		shlAboutSalamiDetector.setSize(450, 300);
		shlAboutSalamiDetector.setText("About Salami Detector");
		shlAboutSalamiDetector.setLayout(null);
		
		Label lblNewLabel_1 = new Label(shlAboutSalamiDetector, SWT.NONE);
		lblNewLabel_1.setText("This computer software is distributed under GNU\r\nGeneral Public Licence.\r\n\r\n\r\nhttps://www.gnu.org/licenses/gpl.html");
		lblNewLabel_1.setBounds(20, 180, 266, 82);
		
		Button btnClose = new Button(shlAboutSalamiDetector, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlAboutSalamiDetector.dispose();
			}
		});
		btnClose.setBounds(339, 216, 95, 46);
		btnClose.setText("Close");
		
		Label lblNewLabel = new Label(shlAboutSalamiDetector, SWT.NONE);
		lblNewLabel.setBounds(267, 84, 167, 34);
		lblNewLabel.setText("Author: Igor Boczkaja\r\ne-mail: boczkaja@gmail.com");
		
		final Composite composite = new Composite(shlAboutSalamiDetector, SWT.NONE);
		composite.setBounds(20, 48, 200, 99);
		
		ImageData logo = new ImageData(AboutWindow.class.getResourceAsStream("/res/gpl_logo.png"));

		ImageUtils.drawImageIn(composite, logo);
			
	}
}
