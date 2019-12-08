package io.meedo.finder.command;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

public class GetCommand extends AbstractJsonCommand implements ElfinderCommand {

    public static final String ENCODING = "utf-8";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
        final VolumeHandler vh = findTarget(elfinderStorage, target);
        final InputStream is = vh.openInputStream();
        final String content = IOUtils.toString(is, ENCODING);
        is.close();
        json.put(ElFinderConstants.ELFINDER_PARAMETER_CONTENT, content);
    }
}
