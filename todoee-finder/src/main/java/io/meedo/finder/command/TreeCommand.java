package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class TreeCommand extends AbstractJsonCommand implements ElfinderCommand {
    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Map<String, VolumeHandler> files = new HashMap<>();
        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
        addSubFolders(files, volumeHandler);

        json.put(ElFinderConstants.ELFINDER_PARAMETER_TREE, buildJsonFilesArray(request, files.values()));
    }
}
