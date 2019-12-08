package io.meedo.finder.command;

import io.meedo.finder.core.ElfinderContext;

public interface ElfinderCommand {

    void execute(ElfinderContext context) throws Exception;

}
