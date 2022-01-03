package art.cipher581.tools.proj;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import art.cipher581.commons.gui.ImagePanel;
import art.cipher581.commons.util.FileComparatorCreation;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.commons.util.img.JavaImageScaler;
import art.cipher581.tools.proj.ProjectionSettings.IChangeListener;

public class ProjectionTool {

	public static void showTool() throws IOException {
		File imageDir = chooseDir();

		if (imageDir == null) {
			return;
		}

		System.out.println("Choosen directory: " + imageDir);

		ProjectionSettings settings = createSettings(imageDir);

		if (settings == null) {
			return;
		}

		createSettingsFrame(settings);

		JFrame imageFrame = createImageFrame(settings);

		settings.setChangeListener(new IChangeListener() {

			@Override
			public void settingsChanged() {
				applySettings(imageFrame, settings);
			}

			@Override
			public void imageChanged() {
				showImage(imageFrame, settings);
			}

		});
	}

	private static JFrame createImageFrame(ProjectionSettings settings) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new GridLayout());
		frame.setUndecorated(true);

		applySettings(frame, settings);

		showImage(frame, settings);

		frame.setVisible(true);

		return frame;
	}

	private static void showImage(JFrame frame, ProjectionSettings settings) {
		frame.getContentPane().removeAll();

		try {
			System.out.println("Display image: " + settings.getImageFile());

			BufferedImage image = ImageUtils.load(settings.getImageFile());

			ImagePanel imagePanel = new ImagePanel(image, new JavaImageScaler());

			frame.getContentPane().add(imagePanel);

			if (frame.isVisible()) {
				frame.revalidate();
			}
		} catch (Exception e) {
			System.err.println("Image can't be displayed");
			e.printStackTrace();
		}
	}

	private static void applySettings(JFrame frame, ProjectionSettings settings) {
		System.out.println("Applying settings");
		
		frame.setSize(settings.getWidth(), settings.getHeight());
		frame.setMaximumSize(new Dimension(settings.getWidth(), settings.getHeight()));
		frame.setMinimumSize(new Dimension(settings.getWidth(), settings.getHeight()));
		frame.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
		// frame.setBounds(settings.getPosX(), settings.getPosY(), settings.getWidth(), settings.getHeight());
		frame.setResizable(false);
		frame.setLocation(settings.getPosX(), settings.getPosY());
	}

	private static void createSettingsFrame(ProjectionSettings settings) {
		JFrame frame = new JFrame();

		frame.setSize(600, 600);
		frame.setLayout(new GridLayout());
		frame.setTitle("Settings");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());

		frame.add(contentPane);

		ComboBoxModel<String> selectImageModel = createImageSelectModel(settings);

		JComboBox<String> selectImage = new JComboBox<>(selectImageModel);
		selectImage.setPreferredSize(new Dimension(300, 22));
		selectImage.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selectedImage = (String) selectImage.getSelectedItem();

					settings.setImageFile(selectedImage);
			    }
			}
		});

		JTextField textFieldWidth = new JTextField(String.valueOf(settings.getWidth()));
		textFieldWidth.setName("Width");
		addTextListener(textFieldWidth, applyIntSetting(settings::setWidth));

		JTextField textFieldHeight = new JTextField(String.valueOf(settings.getHeight()));
		textFieldHeight.setName("Height");
		addTextListener(textFieldWidth, applyIntSetting(settings::setHeight));

		JTextField textFieldPosX = new JTextField(String.valueOf(settings.getPosX()));
		textFieldPosX.setName("Pos X");
		addTextListener(textFieldPosX, applyIntSetting(settings::setPosX));

		JTextField textFieldPosY = new JTextField(String.valueOf(settings.getPosY()));
		textFieldPosY.setName("Pos Y");
		addTextListener(textFieldPosY, applyIntSetting(settings::setPosY));

		int y = 0;

		contentPane.add(new JLabel("Image"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		contentPane.add(selectImage, new GridBagConstraints(1, y++, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		
		contentPane.add(new JLabel("Width"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		contentPane.add(textFieldWidth, new GridBagConstraints(1, y++, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		
		contentPane.add(new JLabel("Height"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		contentPane.add(textFieldHeight, new GridBagConstraints(1, y++, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		
		contentPane.add(new JLabel("Pos X"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		contentPane.add(textFieldPosX, new GridBagConstraints(1, y++, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		
		contentPane.add(new JLabel("Pos Y"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		contentPane.add(textFieldPosY, new GridBagConstraints(1, y++, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));
		
		contentPane.add(new JPanel(), new GridBagConstraints(0, y++, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));

		frame.setVisible(true);
	}

	private static Consumer<String> applyIntSetting(IntConsumer intConsumer) {
		return new Consumer<String>() {

			@Override
			public void accept(String t) {
				if (t != null && !t.isEmpty()) {
					try {
						int v = Integer.parseInt(t);

						System.out.println("Applying numeric value: " + v);
						
						intConsumer.accept(v);
					} catch (Exception e) {
						System.err.println("Inavlid Value: " + t);
					}
				} else {
					intConsumer.accept(0);
				}
			}

		};
	}

	private static void addTextListener(JTextField textField, Consumer<String> consumer) {
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}

			private void textChanged() {
				System.out.println("Text changed (" + textField.getName() + "): " + textField.getText());
				consumer.accept(textField.getText());
			};

		});
	}

	private static ComboBoxModel<String> createImageSelectModel(ProjectionSettings settings) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

		for (File file : settings.getImageFiles()) {
			model.addElement(file.getName());
		}

		model.setSelectedItem(settings.getImageFile().getName());

		return model;
	}

	private static ProjectionSettings createSettings(File imageDir) throws IOException {
		File imageFile = getDefaultFile(imageDir);

		if (imageFile == null) {
			return null;
		}
		
		ProjectionSettings settings = new ProjectionSettings();
		
		BufferedImage img = ImageUtils.load(imageFile);
		
		settings.setImageDir(imageDir);
		settings.setImageFile(imageFile);
		settings.setImageFiles(getImageFiles(imageDir));
		settings.setWidth(img.getWidth());
		settings.setHeight(img.getHeight());
		
		return settings;
	}


	private static List<File> getImageFiles(File imageDir) throws IOException {
		List<File> imageFiles = FileUtilities.getFiles(imageDir, Pattern.compile("(?i)^.+\\.(jpg|png)$"), false);

		return imageFiles;
	}

	private static File getDefaultFile(File imageDir) throws IOException {
		List<File> imageFiles = getImageFiles(imageDir);
		
		File file = imageFiles.stream().sorted(new FileComparatorCreation()).findFirst().get();
		
		System.out.println("Standard Bild: " + file);

		return file;
	}

	private static File chooseDir() {
		File homeDir = FileUtilities.getHomeDir("Desktop");

		System.out.println("homeDir: " + homeDir);

		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setApproveButtonText("Open");
		fileChooser.setSize(800, 600);

		if (homeDir.exists()) {
			fileChooser.setCurrentDirectory(homeDir);
		}

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int fcOption = fileChooser.showOpenDialog(null);

		if (fcOption != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		File file = fileChooser.getSelectedFile();

		return file;
	}
	
	public static void main(String[] args) {
		try {
			showTool();
		} catch (Exception e) {
			e.printStackTrace();

			System.exit(8);
		}
	}
	
}
