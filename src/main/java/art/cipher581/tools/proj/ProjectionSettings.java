package art.cipher581.tools.proj;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ProjectionSettings {

	private File imageDir;

	private List<File> imageFiles;

	private File imageFile;

	private int width;

	private int height;

	private boolean keepRatio = true;

	private int posX;

	private int posY;

	private List<IChangeListener> changeListeners = new LinkedList<>();

	private Color selectedColor;

	private double colorDistance = 0.1;

	private boolean showSelectedColorOnly;

	private boolean invertColors;

	public File getImageDir() {
		return imageDir;
	}

	public void setImageDir(File imageDir) {
		this.imageDir = imageDir;
	}

	public List<File> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<File> imageFiles) {
		this.imageFiles = imageFiles;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;

		notifyImageChange();
	}

	public void setImageFile(String imageFileName) {
		setImageFile(new File(imageDir, imageFileName));
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		System.out.println("setWidth(" + width + ")");

		if (width != this.width) {
			if (keepRatio) {
				double ratio = ((double) this.height) / ((double) this.width);

				this.height = (int) (ratio * width);
			}

			this.width = width;

			notifySettingChange();
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		System.out.println("setHeight(" + height + ")");

		if (height != this.height) {
			if (keepRatio) {
				double ratio = ((double) this.width) / ((double) this.height);

				this.width = (int) (ratio * height);
			}

			this.height = height;

			notifySettingChange();
		}
	}

	public void setKeepRatio(boolean keepRatio) {
		this.keepRatio = keepRatio;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;

		notifySettingChange();
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;

		notifySettingChange();
	}

	public void addChangeListener(IChangeListener changeListener) {
		this.changeListeners.add(changeListener);
	}

	private void notifySettingChange() {
		if (!changeListeners.isEmpty()) {
			for (IChangeListener changeListener : changeListeners) {
				changeListener.settingsChanged();
			}
		}
	}

	private void notifyImageChange() {
		if (!changeListeners.isEmpty()) {
			for (IChangeListener changeListener : changeListeners) {
				changeListener.imageChanged();
			}
		}
	}

	public static interface IChangeListener {

		public void settingsChanged();

		public void imageChanged();

	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;

		notifySettingChange();
	}

	public double getColorDistance() {
		return colorDistance;
	}

	public void setColorDistance(double colorDistance) {
		this.colorDistance = colorDistance;

		notifySettingChange();
	}

	public boolean isShowSelectedColorOnly() {
		return showSelectedColorOnly;
	}

	public void setShowSelectedColorOnly(boolean showSelectedColorOnly) {
		this.showSelectedColorOnly = showSelectedColorOnly;

		notifySettingChange();
	}

	public boolean isInvertColors() {
		return invertColors;
	}

	public void setInvertColors(boolean invertColors) {
		this.invertColors = invertColors;

		notifySettingChange();
	}

}
