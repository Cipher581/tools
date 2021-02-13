package art.cipher581.tools.af.element.gen;


import java.awt.Color;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.IElement;
import art.cipher581.tools.af.util.ColorGenerator;


public abstract class AbstractRandomElementGenerator<E extends IElement> implements IRandomElementGenerator<E> {

	protected ColorGenerator colorGenerator = new ColorGenerator();

	private IConfiguration configuration;

	
	public AbstractRandomElementGenerator() {
		super();
	}
	

	public AbstractRandomElementGenerator(IConfiguration configuration) {
		super();

		this.configuration = configuration;
	}

	protected final Color getRandomColor() {
		return colorGenerator.generateRandomColor();
	}
	
	protected int getConfigValueAsInt (String key, int defaultValue) {
		if (configuration != null) {
			return configuration.getValueAsInt(key, defaultValue);
		} else {
			return defaultValue;
		}
	}
	
	
	public IConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(IConfiguration configuration) {
		this.configuration = configuration;
	}

}
