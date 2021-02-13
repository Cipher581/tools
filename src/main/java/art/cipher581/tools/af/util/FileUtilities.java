package art.cipher581.tools.af.util;


import java.io.File;
import java.io.IOException;


public class FileUtilities {

	public static File getHomeDir() {
		return new File(System.getProperty("user.dir")); //$NON-NLS-1$
	}


	public static File getExportDir() {
		File homeDir = getHomeDir();

		return new File(homeDir, "ArtyFarty/export"); //$NON-NLS-1$
	}
	
	
	public static File createExportDir() throws IOException {
		File exportDir = getExportDir();
		
		if (!exportDir.exists()) {
			boolean success = exportDir.mkdirs();
			
			if (!success) {
				throw new IOException("error while creating directory " + exportDir);
			}
		}
		
		return exportDir;
	}


	public static File getUniqueFile(File dir, String name, String suffix) {
		File file = new File(dir, name + "." + suffix); //$NON-NLS-1$
		
		if (!file.exists()) {
			return file;
		}

		for (int i = 0; i < 1000000; i++) {
			file = new File(dir, name + "-" + i + "." + suffix); //$NON-NLS-1$
			
			if (!file.exists()) {
				return file;
			}
		}
		
		return null;
	}

}
