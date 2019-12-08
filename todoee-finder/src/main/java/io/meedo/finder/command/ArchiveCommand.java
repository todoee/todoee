package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.core.Target;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;
import io.meedo.finder.support.archiver.Archiver;
import io.meedo.finder.support.archiver.ArchiverType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Defines how to execute the archive command.
 *
 * @author James.zhang
 */
public class ArchiveCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);
        final String type = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TYPE);

        List<Target> targetList = findTargets(elfinderStorage, targets);

        try {
            Archiver archiver = ArchiverType.of(type).getStrategy();
            Target targetArchive = archiver.compress(targetList.toArray(new Target[targetList.size()]));

            Object[] archiveInfos = {getTargetInfo(request, new VolumeHandler(targetArchive, elfinderStorage))};
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, archiveInfos);

        } catch (Exception e) {
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, "Unable to create the archive! Error: " + e);
        }

    }
}
