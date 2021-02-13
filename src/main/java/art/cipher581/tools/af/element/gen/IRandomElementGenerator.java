package art.cipher581.tools.af.element.gen;


import art.cipher581.tools.af.element.IElement;


public interface IRandomElementGenerator<E extends IElement> {

	public E generate(int imageWidth, int imageHeight);
	
	public String getName();
	
}
