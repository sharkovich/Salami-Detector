package libloader;

import imageutils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class LibraryLoad {
	public void loadLibrary() {
	    try {

	        File fileOut = null;
	        String osName = System.getProperty("os.name");
	        URL url = null;
	        
	        if(osName.startsWith("Windows")){
	            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
	            if(bitness == 32){
	            	System.out.printf("Loading 32bit OpenCV native library\n");
	            	url = ImageUtils.class.getResource("/opencv/x86/opencv_java248.dll");
	          //      in = ImageUtils.class.getResourceAsStream("opencv/x86/opencv_java248.dll");
	                fileOut = File.createTempFile("lib", ".dll");
	            }
	            else if (bitness == 64){
	            	
	            	
	            	System.out.printf("Loading 64bit OpenCV native library\n");
	            	//System.out.println(ImageUtils.class.getResource("/opencv/x86/opencv_java248.dll").getFile());
	            	url = ImageUtils.class.getResource("/opencv/x64/opencv_java248.dll");
	               // in = ImageUtils.class.getClassLoader().getResourceAsStream("/opencv/x64/opencv_java248.dll");
	                //opencv/x64/opencv_java248.dll

	                fileOut = File.createTempFile("lib_opencv", ".dll");
	            }
	            else{
	            	System.out.printf("Loading 32bit OpenCV native library\n");
	            	url = ImageUtils.class.getResource("/opencv/x86/opencv_java248.dll");
	                //in = ImageUtils.class.getResourceAsStream("opencv/x86/opencv_java248.dll");
	                fileOut = File.createTempFile("lib_opencv", ".dll");
	            }
	        }
	        InputStream in = url.openStream();
	        OutputStream out = new FileOutputStream(fileOut);

	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = in.read(buffer)) > 0) {
	            out.write(buffer, 0, length);
	        }

	        fileOut.deleteOnExit();
	        out.close();
	        in.close();
	        //writer.close();
	        System.load(fileOut.toString());
	        System.out.printf("Loaded file:  %s\n", fileOut.toString());
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to load opencv native library", e);

	    }
	}


}
