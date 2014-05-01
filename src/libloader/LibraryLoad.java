package libloader;

import imageutils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class LibraryLoad implements Runnable {
	private void loadOpenCVLibrary() {
	    try {

	        File fileOut = null;
	        String osName = System.getProperty("os.name");
	        URL url = null;
	        
	        if(osName.startsWith("Windows")){
	            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
	            if(bitness == 32){
	            	System.out.printf("Loading 32bit OpenCV native library\n");
	            	url = ImageUtils.class.getResource("/opencv/x86/opencv_java248.dll");

	                fileOut = File.createTempFile("lib", ".dll");
	            }
	            else if (bitness == 64){
	            	
	            	
	            	System.out.printf("Loading 64bit OpenCV native library\n");

	            	url = ImageUtils.class.getResource("/opencv/x64/opencv_java248.dll");


	                fileOut = File.createTempFile("lib_opencv", ".dll");
	            }
	            else{
	            	System.out.printf("Loading 32bit OpenCV native library\n");
	            	url = ImageUtils.class.getResource("/opencv/x86/opencv_java248.dll");

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
	        System.load(fileOut.toString());
	        System.out.printf("Loaded file:  %s\n", fileOut.toString());
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to load opencv native library", e);

	    }
	}
	
	private void loadSwtJar() {
		String swtFileName = null;
	    try {
	        String osName = System.getProperty("os.name").toLowerCase();
	        String osArch = System.getProperty("os.arch").toLowerCase();
	        URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();
	        Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
	        addUrlMethod.setAccessible(true);

	        String swtFileNameOsPart = 
	            osName.contains("win") ? "win32" :
	            osName.contains("mac") ? "macosx" :
	            osName.contains("linux") || osName.contains("nix") ? "linux_gtk" :
	            ""; // 

	        String swtFileNameArchPart = osArch.contains("64") ? "x64" : "x86";
	        swtFileName = "swt_"+swtFileNameOsPart+"_"+swtFileNameArchPart+".jar";

	        
	        URL swtFileUrl = new URL("rsrc:"+swtFileName); 
	        
	        System.out.println(swtFileUrl.getFile());
	        addUrlMethod.invoke(classLoader, swtFileUrl);
	    }
	    catch(Exception e) {
	        System.out.println("Unable to add the swt jar to the class path: " + swtFileName);
	        e.printStackTrace();
	    }
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadOpenCVLibrary();
		loadSwtJar();
		
	}


}
