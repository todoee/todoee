package io.meedo.finder.command;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadCommand extends AbstractJsonCommand implements ElfinderCommand {

    @SuppressWarnings("unchecked")
	@Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {

        List<FileItemStream> files = (List<FileItemStream>) request.getAttribute(FileItemStream.class.getName());
        List<VolumeHandler> added = new ArrayList<>();

        String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
        VolumeHandler parentDir = findTarget(elfinderStorage, target);

        for (FileItemStream file : files) {
            String fileName = file.getName();
            VolumeHandler newFile = new VolumeHandler(parentDir, fileName);
            newFile.createFile();
            InputStream is = file.openStream();
            OutputStream os = newFile.openOutputStream();

            IOUtils.copy(is, os);
            os.close();
            is.close();

            added.add(newFile);
        }

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, buildJsonFilesArray(request, added));
    }
}
