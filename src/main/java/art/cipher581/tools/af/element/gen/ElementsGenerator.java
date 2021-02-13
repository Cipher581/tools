package art.cipher581.tools.af.element.gen;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.IElement;
import art.cipher581.tools.af.util.RandomUtilities;


public class ElementsGenerator implements IElementsGenerator {

	private List<IRandomElementGenerator<?>> elementGenerators = new ArrayList<IRandomElementGenerator<?>>();

	private IConfiguration configuration;
	
	private int totalProbability;
	
	private int[] probabilities;
	

	public ElementsGenerator(IConfiguration configuration) {
		super();

		this.configuration = configuration;
	}


	public ElementsGenerator(IConfiguration configuration, List<IRandomElementGenerator<?>> elementGenerators) {
		super();

		this.configuration = configuration;
		this.elementGenerators = elementGenerators;
		
		initProbabilityValues();
	}


	public synchronized void addElementGenerator(IRandomElementGenerator<?> elementGenerator) {
		this.elementGenerators.add(elementGenerator);
		
		initProbabilityValues();
	}


	@Override
	public synchronized List<IElement> generate(int imageWidth, int imageHeight) {
		int minElementsCount = configuration.getValueAsInt("minElementsCount", 20);
		int maxElementsCount = configuration.getValueAsInt("maxElementsCount", 60);

		int count = RandomUtilities.nextInt(minElementsCount, maxElementsCount);

		List<IElement> elements = new LinkedList<IElement>();

		for (int i = 0; i < count; i++) {
			IRandomElementGenerator<?> elementGenerator = getElementGenerator();

			IElement element = elementGenerator.generate(imageWidth, imageHeight);

			elements.add(element);
		}

		return elements;
	}


	private void initProbabilityValues() {
		this.totalProbability = 0;
		this.probabilities = new int[elementGenerators.size()];

		int i = 0;
		for (IRandomElementGenerator<?> elementGenerator : elementGenerators) {
			String name = elementGenerator.getName();
			
			String configKey = name + "Probability";
			
			int probability = configuration.getValueAsInt(configKey, 20);
			this.totalProbability += probability;
			this.probabilities[i] = probability;
			
			i++;
		}
	}


	private IRandomElementGenerator<?> getElementGenerator() {
		int r = RandomUtilities.nextInt(totalProbability);

		int i = 0;
		
		int probabilitySum = 0;
		for (int probability : probabilities) {
			int newProbabilitySum = probabilitySum + probability;
			
			if (r >= probabilitySum && r < newProbabilitySum) {
				IRandomElementGenerator<?> elementGenerator = elementGenerators.get(i);
				
				return elementGenerator;
			}
			
			probabilitySum = newProbabilitySum;
			i++;
		}
		
		return null;
	}

}
