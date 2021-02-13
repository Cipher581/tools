package art.cipher581.tools.af.element.gen;


import java.util.List;

import art.cipher581.tools.af.element.IElement;


public interface IElementsGenerator {

	public List<IElement> generate(int imageWidth, int imageHeight);

}
