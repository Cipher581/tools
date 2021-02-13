package art.cipher581.tools.pixelart.util;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class FileUtilities extends art.cipher581.commons.util.FileUtilities {

	public static File getWorkingDir() throws IOException {
		File homeDir = new File(System.getProperty("user.home"));
		File workingDir = new File(homeDir, "pixelArt");

		if (!workingDir.exists()) {
			createDir(workingDir);
		}

		return workingDir;
	}

}
