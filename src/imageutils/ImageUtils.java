package imageutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.ImageData;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImageUtils {
	public static ImageData matToImageData (Mat image) throws IOException
	{
		System.out.print("Converting OpenCV's Mat to image...\n");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
	
	public static ImageData scaleImageTo (ImageData imageData, int width, int height)
	{

		int width_im = imageData.width;
		int height_im = imageData.height;
		System.out.printf("Scaling image (%dx%d) to fit container of %dx%d dimensions...\n", width_im, height_im, width, height);
		double factor;
		
		if (width_im >= width) {
			factor = (double)width/(double)width_im;
			
		} else if (height_im >= height) {
			factor = (double)height/(double)height_im;
		} else {
			factor = 1;
		}
		return imageData.scaledTo((int)(factor*width_im), (int)(factor*height_im));

	}

}
	