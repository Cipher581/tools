package art.cipher581.common.color;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import art.cipher581.commons.da.DataAccessException;

public class ColorXmlDao {

	public ColorSet loadColorSet(String resourceName) throws IOException, DataAccessException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ColorSet.class);
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		    InputStream is = ColorXmlDao.class.getResourceAsStream(resourceName);

		    if (is == null) {
		    	throw new DataAccessException("resource " + resourceName + " not found");
		    }

		    ColorSet colorSet = (ColorSet) jaxbUnmarshaller.unmarshal(is);
		    
		    return colorSet;
		} catch (JAXBException e) {
			throw new DataAccessException("Error while loading resource " + resourceName, e);
		}
	}

}
