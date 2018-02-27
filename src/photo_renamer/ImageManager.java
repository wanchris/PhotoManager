package photo_renamer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

/**
 * Manages all the functions of an image. 
 */
public class ImageManager {
	public static void main (String[] args) throws ClassNotFoundException, IOException, InterruptedException{
//		String pathImage = "C:\\Users\\Bryan\\Documents\\University Year Two\\kaneki.jpg";
//		String pathImageChanged = "C:\\Users\\Bryan\\Documents\\University Year Two\\kaneki @a @b.jpg";
//		ArrayList<Tag> tags = new ArrayList<Tag>();
//
//		tags.add(new Tag("a"));
//		tags.add(new Tag("b"));
//
//		imageManager i = new imageManager();
//		i.rename(tags, pathImage);
//		i.revert("kaneki.jpg", pathImageChanged);
//		i.rename(tags, pathImage);
//		i.revert("kaneki.jpg", pathImageChanged);
	}
	
	/** Name of the file to store name history */
	final static String FILENAME = "nameHistory.txt";
	/** Create a Map to map a file to it's name history */
	private Map<String, ArrayList<String>> nameHistory;
	/** String to store the path to read from or write to */
	private String path;
	/** Logger to log previous and new names */
    private static final Logger logger = Logger.getLogger(ImageManager.class.getName());
    /** Handler for logger to ensure log to file */
    private static Handler fileHandler; 
    
    /**
	 * Prevent additional instances of ImageManager to be created. 
	 */
	private ImageManager() {}
	
	/** Create an instance of the ImageManager class. */
	private static final ImageManager instance = new ImageManager();
    
	/**
	 * Return an instance of ImageManager 
	 * 
	 * @throws ClassNotFoundException if readFromFile can't find a class with a specified name from a file.
	 * @throws IOException if readFromFile has problems with file creation, reading or writing.
	 * 
	 * @return the instance of ImageManager to be used 
	 */	
    public static ImageManager getInstance() throws IOException, ClassNotFoundException{
    	/** Store the directory the user is in */
    	String currentDirectory = System.getProperty("user.dir");
    	/** Store the path to the name history file */
		String path = currentDirectory + "\\" + FILENAME;
		instance.path = path;
		instance.nameHistory = new HashMap<String, ArrayList<String>>();
		readFromFile(path);
		
		fileHandler = new FileHandler("log.txt",true); 
		fileHandler.setFormatter(new SimpleFormatter());
		logger.setLevel(Level.ALL);
		fileHandler.setLevel(Level.ALL);
		logger.addHandler(fileHandler);	
		
		return instance; 

    }

	/**
	 * Returns the name of the file being observed.
	 * 
	 * @return the String value of the name of the file.
	 */
	public ArrayList<String> getTagHistory(String pathImage){
		/** The file to read from */
		File file = new File(pathImage);
		/** List to store the tags to */
		ArrayList<String> tags = new ArrayList<String>();
		/** Store the tags in the file name (tags denoted by @) */
		String name = file.getName().substring(file.getName().indexOf("@") + 1, file.getName().lastIndexOf("."));
		
		for (String x: name.split("@")){
			tags.add(x.trim());
		}
		return tags;
	}
	
	/**
	 * Returns the name history of a file.
	 * 
	 * @param pathImage
	 * 			the filepath of the image.
	 * @return 
	 * 			an ArrayList of type String containing the name history
	 */
	public ArrayList<String> getNameHistory(String pathImage){
		/** The file to read from */
		File file = new File(pathImage);
		/** Name of the file to read from */
		String name = file.getName();
		return instance.nameHistory.get(name);
	}
	
	/**
	 * Renames the image being observed to include tags with a leading '@' and calls the addRename 
	 * function to update nameHistory with the new name.
	 * 
	 * @param tags
	 * 			list of tags being added to the image name.
	 * @throws IOException if there is a writing error when adding a rename to an image's name history.
	 */
	public void rename(ArrayList<Tag> tags, String  pathImage) throws IOException{
		/** Store file following the path pathImage */
		File curDir = new File(pathImage);
		
		if (curDir.exists()){
			/** Store the file extension of curDir */
			String exten = pathImage.substring(pathImage.lastIndexOf("."));
			/** Create a new path for the file */
			String renamedPath= pathImage.substring(0, pathImage.lastIndexOf("."));

			for(Tag tag: tags){
				renamedPath += " @" + tag.getName();
			}
			renamedPath += exten;
			
			/** Store file following the path renamedPath */
			File  newDir = new File(renamedPath);

			curDir.renameTo(newDir);
			
			addRename(curDir.getName(), newDir.getName());
			
			logger.log(Level.FINE, "Old name: " + curDir.getName()+ " " + "New name: " + newDir.getName());			
		}
		else
			System.out.println("This image file does not exist!");
	}

	/**
	 * Reads file from path to update nameHistory with changes made prior to current iteration of program.
	 * 
	 * @param path 
	 * 				the directory path to the file containing past nameHistory values
	 * @throws ClassNotFoundException if no class with specified name can be found from file.
	 * @throws IOException if there is an error creating, writing or reading files. 
	 */
	public static void readFromFile(String path)throws ClassNotFoundException, IOException{
		try{
			/** read from input stream (file) */ 
			InputStream file = new FileInputStream(path);
			/** buffer to read ahead */ 
			InputStream buffer = new BufferedInputStream(file);
			/** ObjectInputStream to read java objects */ 
			ObjectInput input = new ObjectInputStream(buffer);

			//deserialize the Map
			instance.nameHistory = (Map<String, ArrayList<String>>) input.readObject();
			input.close();
		}catch(FileNotFoundException e){
			File f = new File(path);
			f.createNewFile();
			writeToFile(path);
			readFromFile(path);
		}
	}

	/**
	 * Writes the value of nameHistory to the text file location at directory path.
	 * 
	 * @param path 
	 * 				the path of the text file to be written in.
	 * @throws IOException if there is an error writing to a file.
	 */
	public static void writeToFile(String path) throws IOException{
		/** write to output stream (file) */ 
		OutputStream file = new FileOutputStream(path);
		/** buffer to speed up process */ 
		OutputStream buffer = new BufferedOutputStream(file);
		/** ObjectOuputStream to write java objects */
		ObjectOutput output = new ObjectOutputStream(buffer);

		output.writeObject(instance.nameHistory);
		output.close();
	}

	/**
	 * Adds the new name of an image to nameHistory, recording its change over time.
	 * 
	 * @param prevName the name of an image prior to its renaming.
	 * @param newName the new name of an image.
	 * @throws IOException if there is an error in writing the history of a name's changes into the path text file.
	 */
	public void addRename(String prevName, String newName) throws IOException{
		if (nameHistory.containsKey(prevName)){
			nameHistory.put(newName, nameHistory.get(prevName));
			if (!nameHistory.get(newName).contains(newName))
				nameHistory.get(newName).add(newName);
			nameHistory.remove(prevName);
		}
		else {
			/** List to store previous and current names. */
			ArrayList<String> add = new ArrayList<String>();
			add.add(prevName);
			add.add(newName);

			instance.nameHistory.put(newName, add);
		}
		
		writeToFile(instance.path);
	}

	/**
	 * Reverts the name of image to one that it previously had.
	 * 
	 * @param oldName 
	 * 				a previous name of an image.
	 * @throws IOException if there is a writing error when adding a rename to an image's name history.
	 */
	public void revert(String oldName, String pathImage) throws IOException{
		/** Store file following the path pathImage */
		File curDir = new File(pathImage);
		/** Store name of the file following pathImage */
		String curName = curDir.getName();
		if (instance.nameHistory.containsKey(curName) && instance.nameHistory.get(curName).contains(oldName)){
			
			curDir.renameTo(new File (curDir.getParent() + "\\" + oldName));
			addRename(curName, oldName);
			
			logger.log(Level.FINE, "Old name: " + curDir.getName()+ " " + "New name: " + oldName);
			
		}
		else
			System.out.println("This was not an old name or image was not found!");
	}
	
	/**
	 * Removes tags from the name
	 * 
	 * @param tags 
	 * 			the tags to remove
	 * @param pathImage
	 * 			the path of the file to be renamed 
	 * @throws IOException if there is a writing error when adding a rename to an image's name history.
	 */
	public void remove(ArrayList<Tag> tags, String pathImage) throws IOException{
		/** Store file following the path pathImage */
		File curDir = new File(pathImage);
		/** Store name of the file following pathImage */
		String curName = curDir.getName();
		/** Store the new name of the file following pathImage */
		String newName = curName;
		for (Tag t: tags){
			newName = newName.replace(" @" + t.getName(), "");
		}
		curDir.renameTo(new File(curDir.getParent() + "\\" + newName));
		addRename(curName, newName);
		logger.log(Level.FINE, "Old name: " + curDir.getName()+ " " + "New name: " + newName);
	}

}
