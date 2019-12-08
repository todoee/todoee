package io.meedo.finder.web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.command.ElfinderCommand;
import io.meedo.finder.command.ElfinderCommandFactory;
import io.meedo.finder.core.ElfinderContext;
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

public class ConnectorController {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorController.class);

    public static final String OPEN_STREAM = "openStream";
    public static final String GET_PARAMETER = "getParameter";

    @Resource(name = "commandFactory")
    private ElfinderCommandFactory elfinderCommandFactory;

    @Resource(name = "elfinderStorageFactory")
    private ElfinderStorageFactory elfinderStorageFactory;

    public void setElfinderCommandFactory(ElfinderCommandFactory elfinderCommandFactory) {
		this.elfinderCommandFactory = elfinderCommandFactory;
	}

	public void setElfinderStorageFactory(ElfinderStorageFactory elfinderStorageFactory) {
		this.elfinderStorageFactory = elfinderStorageFactory;
	}

    public void connector(HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            request = processMultipartContent(request);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        String cmd = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_COMMAND);
        ElfinderCommand elfinderCommand = elfinderCommandFactory.get(cmd);

        try {
            final HttpServletRequest protectedRequest = request;
            elfinderCommand.execute(new ElfinderContext() {
                @Override
                public ElfinderStorageFactory getVolumeSourceFactory() {
//                    return elfinderStorageFactory;
                	return getElfinderStorageFactory(protectedRequest);
                }

                @Override
                public HttpServletRequest getRequest() {
                    return protectedRequest;
                }

                @Override
                public HttpServletResponse getResponse() {
                    return response;
                }
            });
        } catch (Exception e) {
            logger.error("Unknown error", e);
        }
    }

    public ElfinderStorageFactory getElfinderStorageFactory(HttpServletRequest request) {
    	DefaultElfinderStorageFactory elfinderStorageFactory;
    	if (request.getSession().getAttribute("DefaultElfinderStorageFactory") != null) {
    		elfinderStorageFactory = (DefaultElfinderStorageFactory)request.getSession().getAttribute("DefaultElfinderStorageFactory");
			return elfinderStorageFactory;
		}
        elfinderStorageFactory = new DefaultElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getElfinderStorage(request));
        
        request.getSession().setAttribute("DefaultElfinderStorageFactory", elfinderStorageFactory);
        
        return elfinderStorageFactory;
    }

    public ElfinderStorage getElfinderStorage(HttpServletRequest request) {
//        ElfinderConfigurationWrapper elfinderConfiguration = new ElfinderConfigurationWrapper();
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();

        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
//        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnailWidth());
        defaultThumbnailWidth.setThumbnailWidth(80);

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
//        List<ElfinderConfiguration.Volume> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        
        List<Volume> elfinderVolumes = new ArrayList<>(1);
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(1);
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(1);
//        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
//        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
//        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
//        for (ElfinderConfiguration.Volume elfinderConfigurationVolume : elfinderConfigurationVolumes) {

        	String username = (String)request.getSession().getAttribute("username");
    		
            final String alias = username;
            final String path = "/home/" + username;
//            final String alias = elfinderConfigurationVolume.getAlias();
//            final String path = elfinderConfigurationVolume.getPath();
            
            final String source = "filesystem";
            final String locale = "zh_CN";
            final boolean isLocked = false;
            final boolean isReadable = true;
            final boolean isWritable = true;
//            final String source = elfinderConfigurationVolume.getSource();
//            final String locale = elfinderConfigurationVolume.getLocale();
//            final boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
//            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
//            final boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

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
//            defaultVolumeId++;
//        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }
    
    private HttpServletRequest processMultipartContent(final HttpServletRequest request) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request))
            return request;

        final Map<String, String> requestParams = new HashMap<>();
        List<FileItemStream> listFiles = new ArrayList<>();

        ServletFileUpload servletFileUpload = new ServletFileUpload();
        String characterEncoding = request.getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = "UTF-8";
        }
        servletFileUpload.setHeaderEncoding(characterEncoding);
        FileItemIterator targets = servletFileUpload.getItemIterator(request);

        while (targets.hasNext()) {
            final FileItemStream item = targets.next();
            String name = item.getFieldName();
            InputStream stream = item.openStream();
            if (item.isFormField()) {
                requestParams.put(name, Streams.asString(stream, characterEncoding));
            } else {
                String fileName = item.getName();
                if (fileName != null && !fileName.trim().isEmpty()) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    IOUtils.copy(stream, os);
                    final byte[] bs = os.toByteArray();
                    stream.close();

                    listFiles.add((FileItemStream) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                            new Class[]{FileItemStream.class}, new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    if (OPEN_STREAM.equals(method.getName())) {
                                        return new ByteArrayInputStream(bs);
                                    }

                                    return method.invoke(item, args);
                                }
                            }));
                }
            }
        }

        request.setAttribute(FileItemStream.class.getName(), listFiles);
        return (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{HttpServletRequest.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
                        if (GET_PARAMETER.equals(arg1.getName())) {
                            return requestParams.get(arg2[0]);
                        }

                        return arg1.invoke(request, arg2);
                    }
                });
    }
}