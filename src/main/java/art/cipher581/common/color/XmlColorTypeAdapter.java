package art.cipher581.common.color;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.NotImplementedException;

public class XmlColorTypeAdapter extends XmlAdapter<String, java.awt.Color> {

	@Override
	public Color unmarshal(String v) throws Exception {
		return toColor(v);
	}

	@Override
	public String marshal(Color v) throws Exception {
		throw new NotImplementedException();
	}

	
	public static Color toColor(String v) {
		v = v.replace("#", "");
		
		int r = Integer.parseInt(v.substring(0, 2), 16);
		int g = Integer.parseInt(v.substring(2, 4), 16);
		int b = Integer.parseInt(v.substring(4, 6), 16);
		
		return new java.awt.Color(r, g, b);
	}
}
