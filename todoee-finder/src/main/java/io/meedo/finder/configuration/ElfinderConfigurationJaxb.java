package io.meedo.finder.configuration;

import org.xml.sax.SAXException;

import io.meedo.finder.configuration.jaxb.ElfinderConfiguration;
import io.meedo.finder.exception.ElfinderConfigurationException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * JAXB class to validate and read Elfinder Configuration XML File.
 *
 * @author Thiago Gutenberg Carvalho da Costa
 */
public class ElfinderConfigurationJaxb {

    public static final String SCHEMA_XML = "elfinder-configuration.xsd";

    private static final ElfinderConfigurationJaxb INSTANCE = new ElfinderConfigurationJaxb();

    private final Unmarshaller unmarshaller;

    private ElfinderConfigurationJaxb() throws ElfinderConfigurationException {
        try {
            // jaxb context
            JAXBContext context = JAXBContext.newInstance(io.meedo.finder.configuration.jaxb.ElfinderConfiguration.class.getPackage().getName());

            // creates unmarshaller
            this.unmarshaller = context.createUnmarshaller();

            // xml schema factory
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SCHEMA_XML);
            Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));

            this.unmarshaller.setSchema(schema);

        } catch (JAXBException ex) {
            throw new ElfinderConfigurationException("Could not create the elFinder configuration JAXBContext instance", ex);
        } catch (SAXException ex) {
            throw new ElfinderConfigurationException("Could not load elFinder configuration schema xml " + SCHEMA_XML, ex);
        }
    }

    /**
     * Gets elFinder Configuration
     *
     * @return possible object is {@link ElfinderConfiguration }
     * @throws ElfinderConfigurationException
     */
    public static ElfinderConfiguration unmarshal(InputStream is) throws ElfinderConfigurationException {
        if (null != is) {
            try {
                return (ElfinderConfiguration) ElfinderConfigurationJaxb.INSTANCE.unmarshaller.unmarshal(is);
            } catch (JAXBException e) {
                throw new ElfinderConfigurationException("Could not unmarshal elFinder configuration xml", e);
            }
        }
        throw new ElfinderConfigurationException("Could not find elFinder configuration xml from " + ElfinderConfigurationWrapper.XML_PATH);
    }
}
