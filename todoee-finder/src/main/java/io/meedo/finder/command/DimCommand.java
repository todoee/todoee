package io.meedo.finder.command;

import org.json.JSONObject;

import io.meedo.finder.ElFinderConstants;
import io.meedo.finder.service.ElfinderStorage;
import io.meedo.finder.service.VolumeHandler;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

public class DimCommand extends AbstractJsonCommand implements ElfinderCommand {
    public static final String SEPARATOR = "x";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);

        BufferedImage image;
        VolumeHandler volumeHandler = findTarget(elfinderStorage, target);
        image = ImageIO.read(volumeHandler.openInputStream());

        json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_DIM, image.getWidth() + SEPARATOR + image.getHeight());
    }
}
