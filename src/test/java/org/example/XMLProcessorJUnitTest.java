package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.example.model.Order;
import org.example.model.Orders;
import org.example.utils.XMLProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XMLProcessorJUnitTest {

    private XMLProcessor xmlProcessor;
    private String outputFolder;
    private String inputFolder;

    @BeforeEach
    void setXmlProcessor() throws ConfigurationException {

        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load("application.properties");
        inputFolder = config.getString("INPUT_DIRECTORY");
        outputFolder = config.getString("OUTPUT_DIRECTORY");
    }

    @Test
    void testIsValidFileName() throws IOException {

        xmlProcessor = new XMLProcessor(Path.of("orders01.xml"), outputFolder, inputFolder);
        assertTrue(xmlProcessor.isValidFilename());

        xmlProcessor = new XMLProcessor(Path.of("orders101.xml"), outputFolder, inputFolder);
        assertTrue(xmlProcessor.isValidFilename());

        xmlProcessor = new XMLProcessor(Path.of("orders01a.xml"), outputFolder, inputFolder);
        assertFalse(xmlProcessor.isValidFilename());

        xmlProcessor = new XMLProcessor(Path.of("order01.xml"), outputFolder, inputFolder);
        assertFalse(xmlProcessor.isValidFilename());
    }

    @Test
    void isContentValidTest() throws IOException {

        xmlProcessor = new XMLProcessor(Path.of("order01.xml"), outputFolder, inputFolder);
        assertTrue(xmlProcessor.isContentValid());

        xmlProcessor = new XMLProcessor(Path.of("order01.txt"), outputFolder, inputFolder);
        assertTrue(xmlProcessor.isContentValid());
    }

//    @Test
//    void processContentTest() {
//        Order order = new Order();
//        Order order1= new Order();
//        Orders orders = new Orders(List.of(order1, order));
//    }

}
