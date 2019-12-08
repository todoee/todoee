package io.meedo.finder.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.meedo.finder.configuration.jaxb.ElfinderConfiguration;

/**
 * Elfinder Configuration Wrapper class.
 *
 * @author James.zhang
 */
public class ElfinderConfigurationWrapper {

    private static final Logger log = LoggerFactory.getLogger(ElfinderConfigurationWrapper.class);

    public static final String XML_FILE = "elfinder-configuration.xml";
//    public static final String CONF_DIR = System.getProperty("user.home") + File.separator + "elfinder" + File.separator;
//    public static final String XML_PATH = CONF_DIR + XML_FILE;
    public static final String XML_PATH = XML_FILE;

//    public static final String XML_PATH = System.getenv("ELFINDER_CONFIGURATION_XML");

    private ElfinderConfiguration elfinderConfiguration;

    public ElfinderConfigurationWrapper() {
        loadElfinderConfiguration();
    }

    private void loadElfinderConfiguration() {
        if (XML_PATH == null) {
            throw new AssertionError(
                    "Could not load Elfinder Configuration XML file. Please verify if the ELFINDER_CONFIGURATION_XML enviroment variable is setup.");
        }

        try (InputStream resourceAsStream = Files.newInputStream(
                Paths.get(ElfinderConfigurationUtils.toURI(XML_PATH)))) {

            this.elfinderConfiguration = ElfinderConfigurationJaxb.unmarshal(resourceAsStream);

            if (log.isDebugEnabled()) {
                log.debug("Thumbnail Width: " + getThumbnailWidth());
                for (ElfinderConfiguration.Volume volume : getVolumes()) {
                    log.debug("Source  : " + volume.getSource());
                    log.debug("Alias   : " + volume.getAlias());
                    log.debug("Path    :\t " + volume.getPath());
                    log.debug("Default :\t " + volume.isDefault());
                    log.debug("Locale  :\t " + volume.getLocale());
                    log.debug("Locked  :\t\t " + volume.getConstraint().isLocked());
                    log.debug("Readable:\t\t " + volume.getConstraint().isReadable());
                    log.debug("Writable:\t\t " + volume.getConstraint().isWritable());
                }
            }

        } catch (IOException e) {
            log.warn("Could not close elfinder configuration input stream and releases any system resources associated with the stream", e);
//          throw new ElfinderConfigurationException("Could not close elfinder configuration input stream and releases any system resources associated with the stream", e);
        }
    }

    public Integer getThumbnailWidth() {
        int thumbnailWidth = 80; //default value
        if (elfinderConfiguration.getThumbnail() != null) {
            thumbnailWidth = elfinderConfiguration.getThumbnail().getWidth().intValue();
        }
        return thumbnailWidth;
    }

    public List<ElfinderConfiguration.Volume> getVolumes() {
        return Collections.unmodifiableList(elfinderConfiguration.getVolume());
    }
}
