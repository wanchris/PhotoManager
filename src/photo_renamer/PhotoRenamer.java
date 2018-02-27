package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Creates the GUI for the photo renaming application.
 */
public class PhotoRenamer{
	/** This PhotoRenamer's String representation of the directory selected by the user.*/
	private String originalImageDirectory;
	/** This PhotoRenamer's String representation of the selected image from the JList listImages.*/
	private String key;
	/** This PhotoRenamer's String representation of the directory of the selected image from the JList listImages.*/
	private String directory;
	/** This PhotoRenamer's String representation of the name of the file containing the list of tags.*/
	final static String FILENAME = "tagsList.txt";
	/** This PhotoRenamer's JFrame that contains all the panels and scroll panes used for the GUI.*/
	private final JFrame jFrame;
	/** This PhotoRenamer's JButton that is used to exit the application.*/
	private final JButton exitButton;
	/** This PhotoRenamer's JButton that is used to pick the directory that contains the images the user wants to 
	 * rename.*/
	private final JButton pickDirectory;
	/** This PhotoRenamer's JButton that is used to display the selected image from the JList text onto the JLabel 
	 * image.*/
	private final JButton selectImage;
	/** This PhotoRenamer's JButton that is used to add tags onto an image or remove existing tags from an image.*/
	private final JButton addRemove;
	/** This PhotoRenamer's JButton that is used to swap between displaying the list of images or the name history
	 *  of a selected image from the JList listImages*/
	private final JButton directoryHistory;
	/** This PhotoRenamer's JButton that is used to revert the name of a selected image to a selected name it 
	 * previously had.*/
	private final JButton revert;
	/** This PhotoRenamer's JPanel that is used to hold all of its JButtons except for the JButton pickDirectory.*/
	private final JPanel buttonPanel;
	/** This PhotoRenamer's JPanel that is used to hold the JList listTags and the textField TextField that is 
	 * placed on the EAST side of the border layout of jFrame.*/
	private final JPanel eastPanel;
	/** This PhotoRenamer's JPanel that is used to hold the JList listImages and JButton directoryHistory that is placed
	 * on the WEST side of the border layout of jFrame.*/
	private final JPanel westPanel;
	/** This PhotoRenamer's JList<Object> that is used to hold the list of objects where each represent an image 
	 * file that can be selected by the user.*/
	private final JList<Object> listImages;
	/** This PhotoRenamer's JList<Object> that is used to hold the list of objects where each represent a tag that
	 * has previously been added to an image by the user.*/
	private final JList<Object> listTags;
	/** This PhotoRenamer's JScrollPane that is used to contain and display the JList listImages and allow the user
	 * to scroll through the list as well as select one of the images.*/
	private final JScrollPane scroll;
	/** This PhotoRenamer's JScrollPane that is used to contain and display the JList listTags and allow the user
	 * to scroll through the list as well as select one of the tags.*/
	private final JScrollPane tagScroll;
	/** This PhotoRenamer's JTextField that is used to accept user input, namely for new tags they wish add.*/
	private final JTextField textField;
	/** This PhotoRenamer's JLabel that is used to display the user selected image from JList listImages.*/
	private final JLabel image;
	/** This PhotoRenamer's ImageManager that is used to implement the add, remove and revert features, as well
	 * as store the history of image names.*/
	private final ImageManager imageManager;

	/**
	 * Initializes the instance variables and sets up the GUI by adding the JButtons to JPanels, JLists and TextFileds
	 * to JScrollPanes, and JLabel, JScrollPanes and JPanels to the JFrame. Also configures the ActionListener,
	 * ListSelectionListener and KeyListeners for the JButtons, the JLists and the TextField respectively.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	PhotoRenamer() throws ClassNotFoundException, IOException{
		this.jFrame = new JFrame("Photo Renamer");

		Dimension dJF = new Dimension(600, 500); //JFrame dimension
		Dimension dSP = new Dimension(100,390); //dir scroll dimension/
		Dimension dWP = new Dimension (110, 500); //west panel
		Dimension dB = new Dimension(100,25); //button dimension
		Dimension dEP = new Dimension(110,500); //east pane dimension
		Dimension r = new Dimension(380, 420); //Image dimension
		Dimension t = new Dimension (100, 20); //Text dimension

		String currentDirectory = System.getProperty("user.dir");
		String path = currentDirectory + "\\" + FILENAME;

		Tag.loadTags(path);

		buttonPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		exitButton = new JButton("Exit");
		pickDirectory = new JButton("Select Folder");
		selectImage = new JButton("Select Image");
		addRemove = new JButton ("Add/Remove Tag");
		directoryHistory = new JButton ("Names");
		revert = new JButton("Revert");
		listImages = new JList<Object>();
		listTags = new JList<Object>();
		scroll = new JScrollPane(listImages);
		tagScroll = new JScrollPane(listTags);
		textField = new JTextField();
		image = new JLabel();
		imageManager = ImageManager.getInstance();

		buttonPanel.add(exitButton);
		buttonPanel.add(pickDirectory);
		buttonPanel.add(selectImage);
		buttonPanel.add(addRemove);
		buttonPanel.add(revert);
		jFrame.add(buttonPanel, BorderLayout.SOUTH);
		eastPanel.add(tagScroll, BorderLayout.CENTER);
		westPanel.add(scroll, BorderLayout.CENTER);
		westPanel.add(directoryHistory, BorderLayout.SOUTH);
		jFrame.add(image,BorderLayout.CENTER);
		eastPanel.add(textField, BorderLayout.SOUTH);
		jFrame.add(eastPanel,BorderLayout.EAST);
		jFrame.add(westPanel, BorderLayout.WEST);

		listImages.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listTags.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listImages.setLayoutOrientation(JList.VERTICAL);
		listTags.setLayoutOrientation(JList.VERTICAL);

		jFrame.setPreferredSize(dJF);
		//this.jFrame.setSize(600, 500);
		scroll.setPreferredSize(dSP);
		tagScroll.setPreferredSize(dSP);
		//jk.add(scroll, BorderLayout.WEST);
		//jk.add(tagScroll, BorderLayout.EAST);
		eastPanel.setPreferredSize(dEP);
		westPanel.setPreferredSize(dWP);
		directoryHistory.setPreferredSize(dB);
		textField.setPreferredSize(t);
		eastPanel.setBorder(BorderFactory.createEmptyBorder(0, -10, 0,0));
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,0));

		setTagsList(path);

		directoryHistory.setEnabled(false);
		selectImage.setEnabled(false);
		addRemove.setEnabled(false);
		revert.setEnabled(false);

		exitButton.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JButton exitButton and closes the application when the
			 * JButton is clicked.
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}			
		});

		pickDirectory.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JButton pickDirectory and calls the fileChooser method when
			 * the JButton is clicked. The user is then allowed to select a directory for which all of its images
			 * will then be listed in JList listImages.
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser();
			}
		});
		
		selectImage.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JButton selectImage and calls the setDirectory and setImage
			 * method when the JButton is clicked. The selected image from JList listImages will then be displayed
			 * on the JLabel image.
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setDirectory();
				setImage(r);
			}
		});

		addRemove.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JButton addRemove and calls the setDirectory, addRemoveTags,
			 * setDirList and setTagsList methods when the JButton is clicked. The tag selected in JList listTags
			 * and/or the user inputed in TextField textField will then either be added or removed from the image
			 * selected in JList listImages. The tag will be added if the image does not contain the tag and removed
			 * if it already contains the tag.
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setDirectory();
				try {
					addRemoveTags(path);
					setDirectoryList();
					setTagsList(path);
				} catch (ClassNotFoundException er) {
					// TODO Auto-generated catch block
					er.printStackTrace();
				} catch (IOException er) {
					// TODO Auto-generated catch block
					er.printStackTrace();
				}
			}
		});

		directoryHistory.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JButton directoryHistory and calls the setDirectory, and
			 * directoryHistoryDisplay methods when the JButton is clicked. JList listImages will toggle between
			 * displaying the list of images found in the directory selected by the user and the list of historical
			 * names of the image selected by the user in the JList.
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setDirectory();
				try {
					directoryHistoryDisplay();
				} catch (ClassNotFoundException er) {
					// TODO Auto-generated catch block
					er.printStackTrace();
				} catch (IOException er) {
					// TODO Auto-generated catch block
					er.printStackTrace();
				}
			}
		});

		revert.addActionListener(new ActionListener(){
			@Override
			/**
			 * Acts as an observer of the JButton revert and calls the revertImage, and setDirectoryList methods
			 * and changes the text of JButton directoryHistory to the String "Names" when the JButton is clicked. 
			 * The user selected image will have its name reverted to a previous name selected by the user in
			 * JList listImages. 
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					revertImage();
					setDirectoryList();
					directoryHistory.setText("Names");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		listImages.addListSelectionListener(new ListSelectionListener(){
			@Override
			/**
			 * Acts as an observer of the JList listImages and enables and disables buttons when an Object of the
			 * JList is first selected or when a different Object is selected. The JButton selectImage is enabled
			 * when an image is selected and disabled when a historical name of an image is selected from 
			 * listImages. JButton revert is enabled when a historical name is selected from listImages and disabled 
			 * otherwise. JButton addRemove is enabled when an image is selected from listImages and either 
			 * a tag is selected from JList listTags or the user inputed a new tag in TextField textField, and
			 * is disabled otherwise. JButton directoryHistory is enabled when an image is selected from listImages
			 * or when the text of directoryHistory is the String "Images" and disabled otherwise.
			 * 
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if(listImages.isSelectionEmpty()){
					selectImage.setEnabled(false);
					revert.setEnabled(false);
				}
				else{
					selectImage.setEnabled(true);
					directoryHistory.setEnabled(true);
					if((!textField.getText().isEmpty() || !listTags.isSelectionEmpty()) && directoryHistory.getText().equals("Names"))
						addRemove.setEnabled(true);
					else if(directoryHistory.getText().equals("Images")){
						revert.setEnabled(true);
						selectImage.setEnabled(false);
					}
				}
			}
		});

		textField.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0){};
			@Override
			public void keyTyped(KeyEvent arg0){}
			@Override
			/**
			 * Acts as an observer of the observable TextField textField and enables and disables JButton addRemove
			 * when the user inputs key commands into the textField. addRemove is disabled when no image is selected
			 * from JList listImages and neither textField contains text nor a tag from JList listTags is selected.
			 * addRemove is otherwise enabled. 
			 */
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(textField.getText().isEmpty() && listTags.isSelectionEmpty() || listImages.isSelectionEmpty() || directoryHistory.getText().equals("Images"))
					addRemove.setEnabled(false);
				else
					addRemove.setEnabled(true);
			}
		});

		listTags.addListSelectionListener(new ListSelectionListener(){
			@Override
			/**
			 * Acts as an observer of the observable JList listTags and enables and disables JButton addRemove
			 * when a tag is selected from listTags. addRemove is disabled when no image is selected
			 * from JList listImages and neither textField contains text nor a tag from JList listTags is selected.
			 * addRemove is otherwise enabled. 
			 */
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (listTags.isSelectionEmpty() && textField.getText().isEmpty() || listImages.isSelectionEmpty()|| directoryHistory.getText().equals("Images"))
					addRemove.setEnabled(false);
				else
					addRemove.setEnabled(true);
			}
		});
	}
	
	/**
	 * Allows the user to select a directory and sets the value of JList listImages to the list of images
	 * found in that directory.
	 */
	private void fileChooser(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			List<String> imageFiles = ImageDirectory.listImages(file);
			listImages.setListData(imageFiles.toArray());
		}
	}
	
	/**
	 * Sets the value of this key to the String value of the user selected Object from JList listImages 
	 * and this directory to the String value of the directory path of the selected image.
	 */
	private void setDirectory(){
		this.key = (String) listImages.getSelectedValue();
		this.directory = ImageDirectory.location.get(key);
	}

	
	/**
	 * Sets the value of JList listImages to the list of images found in the directory selected by the user. 
	 */
	private void setDirectoryList(){
		File file = new File(this.directory);
		List<String> imageFiles = ImageDirectory.getListImages(file.getParentFile());
		listImages.setListData(imageFiles.toArray());
	}
	
	/**
	 * Sets the image selected by the user from JList listImages to the JLabel image, displaying it in the GUI.
	 * @param r the dimensions of the preferred size of the JLabel image.
	 */
	private void setImage(Dimension r){
		BufferedImage img = null;
		Image rimg;
		ImageIcon icon;
		try {
			img = ImageIO.read(new File(this.directory));
		} catch (IOException e) {
			System.out.println("?");
		} catch (NullPointerException e){
			System.out.println(this.directory);
		}
		rimg = img.getScaledInstance(r.width, r.height, img.SCALE_SMOOTH);
		icon = new ImageIcon(rimg);
		image.setPreferredSize(r);
		image.setIcon(icon);
	}

	/**
	 * Sets the JList listTags to the list of all unique tags added by the user.
	 * @param path the String value of the path directory to the text file containing the list of all unique tags 
	 * added by the user.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void setTagsList (String path) throws ClassNotFoundException, IOException{
		//Tag.loadTags(path);
		if (!Tag.getTags().isEmpty())
			listTags.setListData(Tag.getTags().toArray());		
	}

	/**
	 * Renames the selected image to one of its historical names, selected by the user in JList listImages.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void revertImage() throws ClassNotFoundException, IOException{
		String key = (String) listImages.getSelectedValue();
		File x = new File(originalImageDirectory).getParentFile();
		this.directory = x.getAbsolutePath() + "\\" + key;

		try {
			imageManager.revert(key, originalImageDirectory);
		} catch (IOException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		}
	}

	/**
	 * Adds or removes the tags selected in JList listTags and typed in TextField textField to a selected image
	 * selected by the user in JList listImages. Tags are added if the image is not already tagged by the same 
	 * tag and removes the tag if it is.
	 * @param path the String value of the directory path to the text file containing the list of all unique tags
	 * added by the user.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void addRemoveTags(String path) throws ClassNotFoundException, IOException{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ArrayList<Tag> tagsRemove = new ArrayList<Tag>();

		String textFieldValue = textField.getText();
		String listTagsValue = (String) listTags.getSelectedValue();
		ArrayList<String> tagHistory = imageManager.getTagHistory(this.directory);

		if (!textFieldValue.isEmpty() && !tagHistory.contains(textFieldValue))
			tags.add(new Tag(textFieldValue));
		else if (!textFieldValue.isEmpty())
			tagsRemove.add(new Tag(textFieldValue));

		if (!listTags.isSelectionEmpty() && !textFieldValue.equals(listTagsValue)&& !tagHistory.contains(listTagsValue))
			tags.add(new Tag(listTagsValue));
		else if (!listTags.isSelectionEmpty())
			tagsRemove.add(new Tag((String) listTags.getSelectedValue()));

		if (!tags.isEmpty())
			imageManager.rename(tags, this.directory);
		else
			imageManager.remove(tagsRemove, this.directory);
		addRemove.setEnabled(false);
		revert.setEnabled(false);
	}

	/**
	 * Sets the value of JList listImages to either the list of all images in the user selected directory or to the
	 * list of historical names of the user selected image in listImages. The list is set to the list of all images 
	 * if the text of the JButton directoryHistory is the String "Images" and set to the list of all historical 
	 * names if the text of the JButton is "Names".  
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void directoryHistoryDisplay() throws ClassNotFoundException, IOException{
		if (directoryHistory.getText().equals("Names")){
			originalImageDirectory = this.directory;
			try{
				listImages.setListData(imageManager.getNameHistory(this.directory).toArray());
			}
			catch(NullPointerException e){
				listImages.setListData(new Object[]{});
			}
			directoryHistory.setText("Images");
			addRemove.setEnabled(false);
		}
		else{
			List<String> imageFiles = ImageDirectory.listImages((new File(originalImageDirectory)).getParentFile());
			listImages.setListData(imageFiles.toArray());
			directoryHistory.setText("Names");
			directoryHistory.setEnabled(false);
		}
	}

	/**
	 * Sets the JFrame jFrame to visible so that the user may interact with the application. The application is 
	 * also set to close completely when the user clicks on the "x" button located on the top right of the 
	 * application.
	 */
	private void show(){
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		PhotoRenamer pR = new PhotoRenamer();
		pR.show();
	}
}