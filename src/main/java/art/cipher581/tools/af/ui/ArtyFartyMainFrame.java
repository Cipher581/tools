package art.cipher581.tools.af.ui;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import art.cipher581.tools.af.Configuration;
import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.gen.CircleGenerator;
import art.cipher581.tools.af.element.gen.ElementsGenerator;
import art.cipher581.tools.af.element.gen.LineGenerator;
import art.cipher581.tools.af.element.gen.PolygonGenerator;
import art.cipher581.tools.af.element.gen.RectangleGenerator;
import art.cipher581.tools.af.img.ImageGenerator;
import art.cipher581.tools.af.util.FileUtilities;
import art.cipher581.tools.af.util.IFileProvider;


public class ArtyFartyMainFrame extends JFrame {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = -7732292483589621834L;

	private CanvasPanel canvasPanel = new CanvasPanel();

	private IConfiguration configuration;

	private ImageGenerator imageGenerator;


	public ArtyFartyMainFrame(IConfiguration configuration) {
		super();

		this.configuration = configuration;

		ElementsGenerator elementsGenerator = new ElementsGenerator(configuration);
		elementsGenerator.addElementGenerator(new CircleGenerator(configuration));
		elementsGenerator.addElementGenerator(new RectangleGenerator(configuration));
		elementsGenerator.addElementGenerator(new LineGenerator(configuration));
		elementsGenerator.addElementGenerator(new PolygonGenerator(configuration));

		imageGenerator = new ImageGenerator(elementsGenerator);
		imageGenerator.setWidth(configuration.getValueAsInt("imageWidth", 1920));
		imageGenerator.setHeight(configuration.getValueAsInt("imageHeight", 1080));

		int canvasWidth = configuration.getValueAsInt("canvasWidth", imageGenerator.getWidth());
		int canvasHeight = configuration.getValueAsInt("canvasHeight", imageGenerator.getHeight());
		canvasPanel.setSize(canvasWidth, canvasHeight);
		canvasPanel.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		canvasPanel.setMinimumSize(new Dimension(canvasWidth, canvasHeight));
		canvasPanel.setMaximumSize(new Dimension(canvasWidth, canvasHeight));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 800);
		setLayout(new GridLayout());
		setTitle("ArtyFarty"); //$NON-NLS-1$

		MouseAdapter mouseAdapter = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e);
			}

		};

		canvasPanel.addMouseListener(mouseAdapter);

		JScrollPane scrollPane = new JScrollPane(canvasPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		add(scrollPane);

		generateImage();
	}


	private void handleMouseClick(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			generateImage();
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
			showPopUpMenu(e);
		}
	}


	private void showPopUpMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem menuItemGenerate = new JMenuItem("Generate Image");
		menuItemGenerate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				generateImage();
			}
		});
		popupMenu.add(menuItemGenerate);

		JMenuItem menuItemSave = new JMenuItem("Save Image");
		menuItemSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveImage();
			}
		});
		popupMenu.add(menuItemSave);
		
		JMenuItem menuItemQuickSave = new JMenuItem("Quick Save Image");
		menuItemQuickSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quickSaveImage();
			}
		});
		popupMenu.add(menuItemQuickSave);

		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	
	private void quickSaveImage() {
		IFileProvider fileProvider = new IFileProvider() {
			
			@Override
			public File getFile() throws IOException {
				File exportDir = FileUtilities.createExportDir();
				File file = FileUtilities.getUniqueFile(exportDir, "export", "png");
				
				return file;
			}
		};

		saveImage(fileProvider);
	}


	private void saveImage() {
		IFileProvider fileProvider = new IFileProvider() {
			
			@Override
			public File getFile() throws IOException {
				return chooseExportFile();
			}
		};

		saveImage(fileProvider);
	}
	
	
	private void saveImage(IFileProvider fileProvider) {
		try {
			File file = fileProvider.getFile();

			if (file != null) {
				BufferedImage image = canvasPanel.getImage();

				ImageIO.write(image, "png", file);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			JOptionPane.showMessageDialog(this, "Error while saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private File chooseExportFile() throws IOException {
		File exportDir = FileUtilities.createExportDir();
		File defaultFile = FileUtilities.getUniqueFile(exportDir, "export", "png");

		JFileChooser fileChooser = new JFileChooser(defaultFile);

		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setApproveButtonText("Save");
		fileChooser.setSize(800, 600);
		fileChooser.setCurrentDirectory(exportDir);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int fcOption = fileChooser.showOpenDialog(this);
		
		if (fcOption != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		File file = fileChooser.getSelectedFile();

		if (file == null) {
			return null;
		}

		if (file.isDirectory()) {
			file = FileUtilities.getUniqueFile(file, "export", "png");
		}

		if (file.exists()) {
			int option = JOptionPane.showOptionDialog(this, "The file " + file + " already exists. Do you want to overwrite it?", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, JOptionPane.NO_OPTION);

			if (option == JOptionPane.CANCEL_OPTION) {
				return null;
			} else if (option == JOptionPane.NO_OPTION) {
				file = chooseExportFile();
			}
		}

		return file;
	}


	private void generateImage() {
		BufferedImage image = imageGenerator.generate();

		canvasPanel.setImage(image);
	}


	public static void main(String[] args) {
		try {
			Configuration configuration = new Configuration();
	
			ArtyFartyMainFrame mainFrame = new ArtyFartyMainFrame(configuration);
	
			mainFrame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			
			System.exit(8);
		}
	}

}
