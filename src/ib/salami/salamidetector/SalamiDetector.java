package ib.salami.salamidetector;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class SalamiDetector {

	/**
	 * Finds contour of image using OpenCV's Canny.
	 * @param src
	 * @param thresholdValue
	 * @return
	 */
	public Mat contourFinder(Mat src, int thresholdValue) {
			
		Mat edges = new Mat();
		Mat hierarchy = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();	
		
		Imgproc.Canny(src, edges, thresholdValue, thresholdValue*2);

		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE , Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
		
		System.out.printf("Detecting edges...\n");
		
		Mat contourDrawing = Mat.zeros(edges.size(), CvType.CV_8U);
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(contourDrawing, contours, i, new Scalar(255, 255, 255));
		}
		System.out.printf("Contours found: %d\n", contours.size());
		return contourDrawing;
	}

	/**
	 * Prepares an image for further contour detecting. 
	 * Converts image to grayscale, equalizes it's histogram then
	 * uses binary threshold and gaussian blur.
	 * @param filename
	 * @param thresholdValue
	 * @return
	 */
	public Mat prepareImage(String filename, int thresholdValue) {
		
		Mat src = Highgui.imread(filename);
		Mat src_gray = new Mat(src.size(), src.type());
		Mat dst = new Mat(src.size(), src.type());

		
		//Konwersja obrazu na skale szarości.
		System.out.printf("Preparing image...\n");
		if (src.empty()) {}
		else if (src.channels()>1) {
		    Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);				
		} else {
			src_gray = src;
		}


		Imgproc.equalizeHist(src_gray, dst);
		Imgproc.threshold(dst, dst, thresholdValue, 255, Imgproc.THRESH_BINARY);
		Imgproc.erode(dst, dst, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		Imgproc.dilate(dst, dst, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		
		Imgproc.GaussianBlur(dst, dst, new Size(11, 11), 0);
		
		return dst;
	}

	public Mat hsvDetection(String filename, int hHigh, int hLow, int sHigh, int sLow, int vHigh, int vLow) {
		Mat src = Highgui.imread(filename);
		Mat src_hsv = new Mat(src.size(), src.type());
		if (src.empty()) {}
		else {
		    Imgproc.cvtColor(src, src_hsv, Imgproc.COLOR_BGR2HSV);
		} 
		
		Mat thresholded = new Mat(src.size(), CvType.CV_8U);
		Core.inRange(src_hsv, new Scalar(hLow, sLow, vLow), new Scalar(hHigh, sHigh, vHigh), thresholded);
		Imgproc.threshold(thresholded, thresholded, 100, 255, Imgproc.THRESH_BINARY);
		Imgproc.erode(thresholded, thresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10)));
		Imgproc.dilate(thresholded, thresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10)));
		
		return thresholded;
		
	}

}

