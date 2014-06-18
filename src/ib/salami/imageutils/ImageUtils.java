package ib.salami.imageutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImageUtils {
	
	/**
	 * Converts OpenCV's Mat array containing image data into SWT's ImageData type.
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static ImageData matToImageData (Mat image) throws IOException
	{
		System.out.print("Converting OpenCV's Mat to image...\n");
		
		Mat image_tmp = image;
		
		MatOfByte matOfByte = new MatOfByte();
		
	    Highgui.imencode(".jpg", image_tmp, matOfByte); 

	    byte[] byteArray = matOfByte.toArray();
	    

        InputStream in = new ByteArrayInputStream(byteArray);
        
	    ImageData id = new ImageData(in);
	    id.width = (int)image.size().width;
	    id.height = (int)image.size().height;

        return id;
	}
	
	/**
	 * Scales an image containing ImageData type to fit given dimensions
	 * without changing it's aspect ratio.
	 * @param imageData
	 * @param width
	 * @param height
	 * @return
	 */
	public static ImageData scaleImageTo (ImageData imageData, int width, int height)
	{

		int width_im = imageData.width;	
		int height_im = imageData.height;

		double factor, factorH;
		
		double aspectRatio = (double)width/(double)height;
		double imageAr = (double)width_im/(double)height_im;
		
		if (imageAr > 1){
			factor = (double)width/(double)width_im;
		} else if (imageAr < 1) {
			factor = (double)height/(double)height_im;
		} else {
			factor = 1;
			factor = 1;
		}
			
		/**
		if (width_im > height_im)
			factor = (double)width/(double)width_im;
		else if (width_im < height_im)
			factor = (double)height/(double)height_im;
		else 
			factor = 1;
		**/
		//if (factor > 1) factor = 1;
		System.out.printf("Scaling image (%dx%d) to %dx%d dimensions...\n", width_im, height_im, (int)(factor*width_im), (int)(factor*height_im));
		return imageData.scaledTo((int)(factor*width_im), (int)(factor*height_im));

	}
	
	/**
	 * Disposes all children in composite parent.
	 * Used for clearing images from frame.
	 * @param c composite to clear
	 */
	public static void disposeImages(Composite c) {
		if (c.getChildren().length != 0) {
			System.out.printf("Disposing previous image...\n");
			for (Control ctrl : c.getChildren()) {
				if (ctrl.getClass() == Canvas.class)
					ctrl.dispose();
			}
		}
	}
	
	/**
	 * Draws an image from ImageData type in a Composite parent.
	 * @param parent
	 * @param imageData
	 */
	public static void drawImageIn(Composite parent, ImageData imageData) {
		
		disposeImages(parent);
		int width = parent.getSize().x;
		int height = parent.getSize().y;
		
		final Image image = new Image(parent.getDisplay(), ImageUtils.scaleImageTo(imageData, width, height));
		if (image != null) {
			
			Canvas pictureFrame = new Canvas(parent, SWT.NONE);
			pictureFrame.setBounds(0, 0, width, height);
			pictureFrame.addPaintListener(new PaintListener() {
				
				public void paintControl(PaintEvent arg0) {
					arg0.gc.drawImage(image, 0, 0);							
				}
			});		
			for (Control ctrl : parent.getChildren()) {
				pictureFrame.moveAbove(ctrl);
			}

		}	
	}
	
	
	

}
	