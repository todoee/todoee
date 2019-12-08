package io.meedo.finder.command;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;

public class PutCommand extends AbstractJsonCommand implements ElfinderCommand {

    public static final String ENCODING = "utf-8";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        VolumeHandler file = findTarget(elfinderStorage, target);
        OutputStream os = file.openOutputStream();
        IOUtils.write(request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_CONTENT), os, ENCODING);
        os.close();
        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_CHANGED, new Object[]{getTargetInfo(request, file)});
    }
}
