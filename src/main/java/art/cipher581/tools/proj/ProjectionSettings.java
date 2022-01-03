package art.cipher581.tools.proj;

import java.io.File;
import java.util.List;

public class ProjectionSettings {

	private File imageDir;

	private List<File> imageFiles;

	private File imageFile;

	private int width;

	private int height;

	private int posX;

	private int posY;
	
	private IChangeListener changeListener;

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
		this.width = width;
		
		notifySettingChange();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		
		notifySettingChange();
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

	public void setChangeListener(IChangeListener changeListener) {
		this.changeListener = changeListener;
	}
	
	private void notifySettingChange() {
		if (changeListener != null) {
			changeListener.settingsChanged();
		}
	}
	
	private void notifyImageChange() {
		if (changeListener != null) {
			changeListener.imageChanged();
		}
	}
	
	
	public static interface IChangeListener {
		
		public void settingsChanged();
		
		public void imageChanged();

	}

}
