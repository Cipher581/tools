package art.cipher581.tools.proj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import art.cipher581.commons.gui.ImagePanel;
import art.cipher581.commons.util.FileComparatorCreation;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.IImageModifier;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.commons.util.img.InvertColors;
import art.cipher581.commons.util.img.JavaImageScaler;
import art.cipher581.commons.util.img.SelectedColorImageModifier;
import art.cipher581.tools.proj.ProjectionSettings.IChangeListener;

public class ProjectionTool {

	private static boolean SETTINGS_CHANGE_DISABLED = false;

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

		settings.addChangeListener(new IChangeListener() {

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
			createModifier(imagePanel, settings);

			imagePanel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						System.out.println("show image popup menu");
						showImagePopup(e, imagePanel, settings);
					}
				}

			});

			frame.getContentPane().add(imagePanel);

			if (frame.isVisible()) {
				frame.revalidate();
			}
		} catch (Exception e) {
			System.err.println("Image can't be displayed");
			e.printStackTrace();
		}
	}

	private static void createModifier(ImagePanel imagePanel, ProjectionSettings settings) {
		imagePanel.setImageModifier(new IImageModifier() {

			@Override
			public BufferedImage modify(BufferedImage imgPaint) {
				BufferedImage image = imgPaint;

				if (settings.isShowSelectedColorOnly()) {
					SelectedColorImageModifier imageModifier = new SelectedColorImageModifier(settings.getSelectedColor(), settings.getColorDistance());

					image = imageModifier.modify(imgPaint);
				}

				if (settings.isInvertColors()) {
					image = new InvertColors().modify(image);
				}

				return image;
			}

		});

	}

	protected static void showImagePopup(MouseEvent mouseEvent, ImagePanel imagePanel, ProjectionSettings projectionSettings) {
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem menuItemSelectColor = new JMenuItem("Select Color");
		menuItemSelectColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedImage img = imagePanel.getPaintedImage();

				Color color = ImageUtils.getColor(img, mouseEvent.getX(), mouseEvent.getY());

				projectionSettings.setSelectedColor(color);
			}

		});

		popupMenu.add(menuItemSelectColor);

		popupMenu.show(imagePanel, mouseEvent.getX(), mouseEvent.getY());
	}

	private static void applySettings(JFrame frame, ProjectionSettings settings) {
		System.out.println("Applying settings");

		frame.setSize(settings.getWidth(), settings.getHeight());
		frame.setMaximumSize(new Dimension(settings.getWidth(), settings.getHeight()));
		frame.setMinimumSize(new Dimension(settings.getWidth(), settings.getHeight()));
		frame.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
		// frame.setBounds(settings.getPosX(), settings.getPosY(), settings.getWidth(),
		// settings.getHeight());
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

		JCheckBox checkBoxKeepRatio = new JCheckBox();
		checkBoxKeepRatio.setSelected(true);
		checkBoxKeepRatio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setKeepRatio(checkBoxKeepRatio.isSelected());
			}

		});

		JTextField textFieldPosX = new JTextField(String.valueOf(settings.getPosX()));
		textFieldPosX.setName("Pos X");
		addTextListener(textFieldPosX, applyIntSetting(settings::setPosX));

		JTextField textFieldPosY = new JTextField(String.valueOf(settings.getPosY()));
		textFieldPosY.setName("Pos Y");
		addTextListener(textFieldPosY, applyIntSetting(settings::setPosY));

		JTextField textFieldColorDistance = new JTextField(String.valueOf(settings.getColorDistance()));
		textFieldColorDistance.setName("Farbdistanz");
		addTextListener(textFieldColorDistance, applyDoubleSetting(settings::setColorDistance));

		JButton buttonSubWidth10 = createAddButton(-10, "Width-10", settings::getWidth, settings::setWidth);
		JButton buttonSubWidth1 = createAddButton(-1, "Width-1", settings::getWidth, settings::setWidth);
		JButton buttonAddWidth1 = createAddButton(1, "Width+1", settings::getWidth, settings::setWidth);
		JButton buttonAddWidth10 = createAddButton(10, "Width+10", settings::getWidth, settings::setWidth);

		JButton buttonSubHeight10 = createAddButton(-10, "Height-10", settings::getHeight, settings::setHeight);
		JButton buttonSubHeight1 = createAddButton(-1, "Height-1", settings::getHeight, settings::setHeight);
		JButton buttonAddHeight1 = createAddButton(1, "Height+1", settings::getHeight, settings::setHeight);
		JButton buttonAddHeight10 = createAddButton(10, "Height+10", settings::getHeight, settings::setHeight);

		JButton buttonSubPosX10 = createAddButton(-10, "PosX-10", settings::getPosX, settings::setPosX);
		JButton buttonSubPosX1 = createAddButton(-1, "PosX-1", settings::getPosX, settings::setPosX);
		JButton buttonAddPosX1 = createAddButton(1, "PosX+1", settings::getPosX, settings::setPosX);
		JButton buttonAddPosX10 = createAddButton(10, "PosX+10", settings::getPosX, settings::setPosX);

		JButton buttonSubPosY10 = createAddButton(-10, "PosY-10", settings::getPosY, settings::setPosY);
		JButton buttonSubPosY1 = createAddButton(-1, "PosY-1", settings::getPosY, settings::setPosY);
		JButton buttonAddPosY1 = createAddButton(1, "PosY+1", settings::getPosY, settings::setPosY);
		JButton buttonAddPosY10 = createAddButton(10, "PosY+10", settings::getPosY, settings::setPosY);

		JPanel panelSelectedColor = new JPanel();
		updateColor(panelSelectedColor, settings.getSelectedColor());
		panelSelectedColor.setBorder(new LineBorder(Color.BLACK, 1));
		panelSelectedColor.setPreferredSize(new Dimension(50, 25));

		JCheckBox checkboxSelectedColorOnly = new JCheckBox();
		checkboxSelectedColorOnly.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setShowSelectedColorOnly(checkboxSelectedColorOnly.isSelected());
			}

		});
		
		JCheckBox checkboxInvertColors = new JCheckBox();
		checkboxInvertColors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setInvertColors(checkboxInvertColors.isSelected());
			}

		});

		int y = 0;

		contentPane.add(new JLabel("Image"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(selectImage, new GridBagConstraints(1, y++, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Width"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubWidth10, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubWidth1, new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(textFieldWidth, new GridBagConstraints(3, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddWidth1, new GridBagConstraints(4, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddWidth10, new GridBagConstraints(5, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Height"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubHeight10, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubHeight1, new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(textFieldHeight, new GridBagConstraints(3, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddHeight1, new GridBagConstraints(4, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddHeight10, new GridBagConstraints(5, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Keep ratio"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(checkBoxKeepRatio, new GridBagConstraints(1, y++, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Pos X"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubPosX10, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubPosX1, new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(textFieldPosX, new GridBagConstraints(3, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddPosX1, new GridBagConstraints(4, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddPosX10, new GridBagConstraints(5, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Pos Y"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubPosY10, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonSubPosY1, new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(textFieldPosY, new GridBagConstraints(3, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddPosY1, new GridBagConstraints(4, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(buttonAddPosY10, new GridBagConstraints(5, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Farbe"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(panelSelectedColor, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Farbdistanz"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(textFieldColorDistance, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Nur Farbe anzeigen"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(checkboxSelectedColorOnly, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JLabel("Farben invertieren"), new GridBagConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		contentPane.add(checkboxInvertColors, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(new JPanel(), new GridBagConstraints(0, y++, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		settings.addChangeListener(new IChangeListener() {

			@Override
			public void settingsChanged() {
				System.out.println("Updating form values");

				SwingUtilities.invokeLater(() -> {
					SETTINGS_CHANGE_DISABLED = true;

					updateText(textFieldWidth, String.valueOf(settings.getWidth()));
					updateText(textFieldHeight, String.valueOf(settings.getHeight()));
					updateText(textFieldPosX, String.valueOf(settings.getPosX()));
					updateText(textFieldPosY, String.valueOf(settings.getPosY()));

					updateColor(panelSelectedColor, settings.getSelectedColor());

					SETTINGS_CHANGE_DISABLED = false;
				});
			}

			@Override
			public void imageChanged() {
			}

		});

		frame.setVisible(true);
	}

	private static void updateColor(JPanel p, Color c) {
		if (c != null) {
			p.setBackground(c);
			p.setOpaque(true);
		} else {
			p.setOpaque(false);
		}
	}

	private static void updateText(JTextField t, String val) {
		if (!t.getText().equals(val)) {
			System.out.println("Updating textfield " + t.getName() + ": " + t.getText() + " => " + val);
			t.setText(val);
		}
	}

	private static JButton createAddButton(int add, String name, IntSupplier supplier, IntConsumer consumer) {
		JButton b = new JButton(String.valueOf(add));
		b.setName(name);

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button " + b.getName() + " clicked");
				int v = supplier.getAsInt();

				v += add;

				consumer.accept(v);
			}

		});

		return b;
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

	private static Consumer<String> applyDoubleSetting(DoubleConsumer doubleConsumer) {
		return new Consumer<String>() {

			@Override
			public void accept(String t) {
				if (t != null && !t.isEmpty()) {
					try {
						double v = Double.parseDouble(t);

						System.out.println("Applying numeric value: " + v);

						doubleConsumer.accept(v);
					} catch (Exception e) {
						System.err.println("Inavlid Value: " + t);
					}
				} else {
					doubleConsumer.accept(0);
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
				if (!SETTINGS_CHANGE_DISABLED) {
					System.out.println("Text changed (" + textField.getName() + "): " + textField.getText());
					consumer.accept(textField.getText());
				}
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
		settings.setSize(img.getWidth(), img.getHeight());

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
