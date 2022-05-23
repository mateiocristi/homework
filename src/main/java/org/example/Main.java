package org.example;

import org.apache.commons.configuration.ConfigurationException;
import org.example.service.Watcher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ConfigurationException {

        Watcher watcher = new Watcher();
        watcher.start();
    }
}
