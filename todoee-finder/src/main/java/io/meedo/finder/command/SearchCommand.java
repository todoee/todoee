package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.core.Target;
import io.meedo.finder.core.Volume;
import io.meedo.finder.core.VolumeSecurity;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines how to execute the search command.
 *
 * @author James.zhang
 */
public class SearchCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {

        final String query = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_SEARCH_QUERY);

        try {
            List<Object> objects = null;
            List<Volume> volumes = elfinderStorage.getVolumes();
            for (Volume volume : volumes) {

                // checks volume security
                Target volumeRoot = volume.getRoot();
                VolumeSecurity volumeSecurity = elfinderStorage.getVolumeSecurity(volumeRoot);

                // search only in volumes that are readable
                if (volumeSecurity.getSecurityConstraint().isReadable()) {

                    // search for targets
                    List<Target> targets = volume.search(query);

                    if (targets != null) {

                        // init object list
                        if (objects == null) {
                            objects = new ArrayList<>(targets.size());
                        }

                        // adds targets info in the return list
                        for (Target target : targets) {
                            objects.add(getTargetInfo(request, new VolumeHandler(target, elfinderStorage)));
                        }
                    }
                }
            }

            Object[] objectArray = objects != null ? objects.toArray() : new Object[0];
            json.put(ElFinderConstants.ELFINDER_PARAMETER_FILES, objectArray);

        } catch (Exception e) {
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, "Unable to search! Error: " + e);
        }
    }
}
