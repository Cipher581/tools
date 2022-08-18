package art.cipher581.common.color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Color")
@XmlAccessorType(XmlAccessType.FIELD)
public class Color implements Comparable<Color> {

	@XmlAttribute(name = "color")
	private String colorCode = null;

	@XmlAttribute
    private String product = null;

	@XmlAttribute
    private String code = null;

	@XmlAttribute
    private String name = null;
    
	@XmlAttribute
    private boolean active = true;

	public String getColor() {
		return colorCode;
	}
	
	public java.awt.Color getAwtColor() {
		return XmlColorTypeAdapter.toColor(colorCode);
	}

	public void setColor(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((colorCode == null) ? 0 : colorCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (colorCode == null) {
			if (other.colorCode != null)
				return false;
		} else if (!colorCode.equals(other.colorCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	public int compareTo(Color b) {
		int c = product.compareToIgnoreCase(b.product);
		
		if (c == 0) {
			c = code.compareToIgnoreCase(b.code);
		}
		
		return c;
	}

}
