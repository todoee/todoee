package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.core.Target;
import io.meedo.finder.core.Volume;
import io.meedo.finder.service.ElfinderStorage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Defines how to execute the size command.
 *
 * @author James.zhang
 */
public class SizeCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String[] targets = request.getParameterValues(ElFinderConstants.ELFINDER_PARAMETER_TARGETS);

        List<Target> targetList = findTargets(elfinderStorage, targets);

        long size = 0;
        for (Target target : targetList) {
            Volume volume = target.getVolume();
            size += volume.getSize(target);
        }

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_SIZE, size);
    }
}
