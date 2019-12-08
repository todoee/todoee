package io.meedo.finder.command;

public interface ElfinderCommandFactory {

    ElfinderCommand get(String commandName);

}