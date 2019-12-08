package io.meedo.finder.support.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.command.CommandFactory;
import io.meedo.finder.configuration.ElfinderConfigurationWrapper;
import io.meedo.finder.configuration.jaxb.ElfinderConfiguration;
import io.meedo.finder.core.Volume;
import io.meedo.finder.core.VolumeSecurity;
import io.meedo.finder.core.impl.DefaultVolumeSecurity;
import io.meedo.finder.core.impl.SecurityConstraint;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.ElfinderStorageFactory;
import io.meedo.finder.service.VolumeSources;
import io.meedo.finder.service.impl.DefaultElfinderStorage;
import io.meedo.finder.service.impl.DefaultElfinderStorageFactory;
import io.meedo.finder.service.impl.DefaultThumbnailWidth;
import io.meedo.finder.support.locale.LocaleUtils;

/**
 * Elfinder Root Configuration.
 *
 * @author James.zhang
 */
public class ElfinderRootConfig {

    public CommandFactory getCommandFactory() {
        CommandFactory commandFactory = new CommandFactory();
        commandFactory.setClassNamePattern("io.meedo.finder.command.%sCommand");
        return commandFactory;
    }

    public ElfinderConfigurationWrapper getElfinderConfigurationWrapper() {
        return new ElfinderConfigurationWrapper();
    }

    public ElfinderStorageFactory getElfinderStorageFactory() {
        DefaultElfinderStorageFactory elfinderStorageFactory = new DefaultElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getElfinderStorage());
        return elfinderStorageFactory;
    }

    public ElfinderStorage getElfinderStorage() {
        ElfinderConfigurationWrapper elfinderConfiguration = getElfinderConfigurationWrapper();
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();

        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnailWidth());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
        List<ElfinderConfiguration.Volume> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
        for (ElfinderConfiguration.Volume elfinderConfigurationVolume : elfinderConfigurationVolumes) {

            final String alias = elfinderConfigurationVolume.getAlias();
            final String path = elfinderConfigurationVolume.getPath();
            final String source = elfinderConfigurationVolume.getSource();
            final String locale = elfinderConfigurationVolume.getLocale();
            final boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            final boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

            // creates new volume
            Volume volume = VolumeSources.of(source).newInstance(alias, path);

            elfinderVolumes.add(volume);
            elfinderVolumeIds.put(volume, Character.toString(defaultVolumeId));
            elfinderVolumeLocales.put(volume, LocaleUtils.toLocale(locale));

            // creates security constraint
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setLocked(isLocked);
            securityConstraint.setReadable(isReadable);
            securityConstraint.setWritable(isWritable);

            // creates volume pattern and volume security
            final String volumePattern = Character.toString(defaultVolumeId) + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
            elfinderVolumeSecurities.add(new DefaultVolumeSecurity(volumePattern, securityConstraint));

            // prepare next volumeId character
            defaultVolumeId++;
        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }

}
