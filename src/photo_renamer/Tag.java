package photo_renamer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Tag {
	
	/** The name of the masterlist tag file */
	final static String FILENAME = "tagsList.txt";
	/** The master list of tags */ 
	public static List<String> tags = new ArrayList<String>();
	/** The name of the tag */
	private String name; 
	/** The path to follow to the masterlist of tags */
	private String path;
	
	/**
	 * Create a tag object, and add the tag to the list tags. 
	 * 
	 * @param name
	 *            the name of this tag.
	 * @param tags 
	 * 			  the list of tags to write into the file
	 * @throws IOException if there is an error creating, writing or reading files. 
	 * @throws ClassNotFoundException if no class with specified name can be found from file.
	 *             
	 */	
	public Tag (String name) throws ClassNotFoundException, IOException{
		this.setName(name); 
		initialize();
		if (!tags.contains(name))
			tags.add(name);	
		saveTags(this.path);	
		
	}
	
	/**
	 * Initialize the Tag class by loading the tags from file.
	 * 
	 * @throws IOException if there is an error creating, writing or reading files. 
	 * @throws ClassNotFoundException if no class with specified name can be found from file. 
	 *             
	 */	
	public void initialize() throws IOException, ClassNotFoundException{
		
		/** Current directory of the user */
		String currentDirectory = System.getProperty("user.dir");
		/** The path to follow to the masterlist of tags */
		String path = currentDirectory + "\\" + FILENAME;
		this.path = path;
		
		if (Tag.getTags().isEmpty()){
			try{
				loadTags(this.path);
			}catch(FileNotFoundException e){
				File f = new File(this.path);
				f.createNewFile();
			}
		}
	}
	
	/**
	 * Gets the list of tags
	 * 
	 * @return
	 * 			  the list of tags
	 *             
	 */	
	public static List<String> getTags() {
		return tags;
	}
	
	/**
	 * Gets the name of the tag
	 * 
	 * @return
	 * 			  the name of the tag
	 *             
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the tag
	 * 
	 * @param name
	 * 			  the name of the tag
	 *             
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Write a file based on a List of String type objects that are tags separated by a new line character. 
	 * 
	 * @param path
	 *            the path to the file we are writing to 
	 * @param tags 
	 * 			  the list of tags to write into the file
	 * @throws IOException if there is an error creating, writing or reading files. 
	 * @throws ClassNotFoundException if no class with specified name can be found from file. 
	 *             
	 */	
	public static void saveTags(String path) throws IOException, ClassNotFoundException{			
		
		/** The file to write to */
		File f = new File(path); 
		/** PrintWriter used to write to the file */
		PrintWriter writer = new PrintWriter(f); 
	    for (String tag: tags) {
	    	writer.println(tag); 
	    }
	    writer.close();
		
	}
		
	/**
	 * Remove all tags from the masterlist of tags variable.        
	 */	
	public static void removeTags() {
	    tags.clear(); 
	}
	
	/**
	 * Return a List of String type objects that are tags into the static tags variable. These tags are read from
	 * a file that is rooted at the given path parameter. 
	 * 
	 * @param path
	 *            the path to the file we are reading from 
	 */	
	public static void loadTags(String path) throws IOException, ClassNotFoundException {
		
		/** The file to read from */
		File f = new File(path);
		/** FileReader used to read the file */
		FileReader fileReader = new FileReader(f);
		/** Reads and buffers text in the file */
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		/** Store lines read from the file */
		String line = bufferedReader.readLine();
		while (line != null) {
			tags.add(line);
			line = bufferedReader.readLine(); 
		}
		fileReader.close();
	
	}
	
	public static void main(String[] args) {
		/*String path = "C:/Users/Windows/Desktop/Testing/Tags.txt";
		Tag one = new Tag("one"); 
		Tag two = new Tag("two"); 
		Tag three = new Tag("three"); 
		
		try {
			saveTags(path);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			loadTags(path);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		for(int i = 0; i < tags.size(); i++) {   
		    System.out.print(tags.get(i) + "\n");
		} 
		*/		
		
		
	}

}
