package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.core.Target;
import io.meedo.finder.core.Volume;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;
import io.meedo.finder.support.archiver.Archiver;
import io.meedo.finder.support.archiver.ArchiverType;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines how to execute the extract command.
 *
 * @author Thiago Gutenberg Carvalho da Costa
 */
public class ExtractCommand extends AbstractJsonCommand implements ElfinderCommand {

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String targetHash = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        Target targetCompressed = elfinderStorage.fromHash(targetHash);
        Volume targetCompressedVolume = targetCompressed.getVolume();
        String mimeType = targetCompressedVolume.getMimeType(targetCompressed);

        try {
            Archiver archiver = ArchiverType.of(mimeType).getStrategy();
            Target decompressTarget = archiver.decompress(targetCompressed);

            Object[] archiveInfos = {getTargetInfo(request, new VolumeHandler(decompressTarget, elfinderStorage))};
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED, archiveInfos);

        } catch (Exception e) {
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, "Unable to extract the archive! Error: " + e);
        }

    }
}
