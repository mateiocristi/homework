package org.example.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.example.utils.XMLProcessor;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class Watcher {

    private final WatchService watcher;
    private XMLProcessor xmlProcessor;
    private WatchKey key;
    private final String inputFolder;
    private final String outputFolder;

    private Logger log;


    public Watcher() throws IOException, ConfigurationException {

        this.watcher = FileSystems.getDefault().newWatchService();

        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load("application.properties");
        inputFolder = config.getString("INPUT_DIRECTORY");
        outputFolder = config.getString("OUTPUT_DIRECTORY");

    }

    public void start() throws IOException, InterruptedException {
        Path targetDirectory = Path.of(inputFolder);
        targetDirectory.register(watcher, ENTRY_CREATE);

        while ((key = watcher.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path file = ev.context();

                if (kind == OVERFLOW) {
                    continue;
                }

                xmlProcessor = new XMLProcessor(file, outputFolder, inputFolder);
                xmlProcessor.start();
            }

            key.reset();
        }
    }
}
