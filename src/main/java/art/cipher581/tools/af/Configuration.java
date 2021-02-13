package art.cipher581.tools.af;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration implements IConfiguration {

	private File configFile = new File("./artyFarty.properties");

	private Properties properties = new Properties();

	
	public Configuration() throws IOException {
		super();
		
		readProperties();
	}

	
	public Configuration(File configFile) throws IOException {
		super();

		setConfigFile(configFile);
	}

	@Override
	public String getValue(String key) {
		return properties.getProperty(key);
	}

	public void setConfigFile(File configFile) throws IOException {
		this.configFile = configFile;

		readProperties();
	}

	private void readProperties() throws IOException {
		FileInputStream fis = new FileInputStream(configFile);

		properties.clear();
		properties.load(fis);
	}

	@Override
	public String getValue(String key, String defaultValue) {
		String value = getValue(key);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	@Override
	public int getValueAsInt(String key, int defaultValue) {
		String value = getValue(key);

		if (value == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}

}
