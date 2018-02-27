package photo_renamer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Select, read, and print the contents of a directory.
 */

public class ImageDirectory {
	
	/**
	 * Prevent additional instances of ImageDirectory to be created. 
	 */
	private ImageDirectory() {}
	
	
	/**
	 * Returns an instance of ImageDirectory
	 * 
	 * @return
	 * 			an instance of ImageDirectory
	 */
	public static ImageDirectory getInstance() {
		return ImageDirectoryHolder.INSTANCE;
	}
	

	/**
	 * Create an instance of ImageDirectory. 
	 */
	private static class ImageDirectoryHolder {
		private static final ImageDirectory INSTANCE = new ImageDirectory();
	}
	
	/** List of image filenames */
	static List<String> imageList = new ArrayList<String>();
	/** Maps image filenames to their respective locations */
	static Map<String, String> location = new HashMap<String, String>();
	
	/**
	 * Return a List of String type objects that are file names. These file names should
	 * be of files that are image-type files (i.e. png or jpg).  
	 * 
	 * @param file 
	 *            the file to be read from  
	 * @return 
	 *            the list of image file names stored in the given file 
	 */	
	public static List<String> listImages(File file) {
		
		/** List of files contained in variable file */
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				listImages(files[i]);
			}
			else if (files[i].getName().endsWith(".png") || files[i].getName().endsWith(".jpg")) {
				if(!location.containsValue(files[i].getPath())){
					imageList.add(files[i].getName()); 
					location.put(files[i].getName(), files[i].getPath());
				}

			}
		}
		return imageList; 

	}
	
	/**
	 * Return a List of String type objects that are names of image files. 
	 * 
	 * @param file 
	 *            the file to be read from  
	 * @return 
	 *            the list of image file names stored in the given file 
	 */	
	public static List<String> getListImages(File file){
		imageList.clear();
		location.clear();
		return listImages(file);
	}

	public static void main(String[] args) {

		//File f = new File("C:\\Users\\Bryan\\Documents\\University Year Two"); 
		//List<String> fileList = listImages(f); 

		//for(int i = 0; i < fileList.size(); i++) {   
		//	System.out.print(fileList.get(i) + "\t" + location.get(fileList.get(i)) + "\n");
		//} 

	}
}

