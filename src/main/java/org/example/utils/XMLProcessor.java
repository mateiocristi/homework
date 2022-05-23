package org.example.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.example.model.Order;
import org.example.model.Orders;
import org.example.model.Product;
import org.example.model.Supplier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class XMLProcessor extends Thread{

    private final Path file;
    private final String outputFolder;
    private final String inputFolder;
    private final XmlMapper xmlMapper;
    private String orderNr;
    private Orders orders;
    private final List<Product> products;
    private Map<String, List<Product>> outputData;

    public XMLProcessor(Path file, String outputFolder, String inputFolder) {
        this.file = file;
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
        this.xmlMapper = new XmlMapper();
        this.products = new ArrayList<>();
    }

    @Override
    public void run() {
        try {

            if (!isValidFilename() || !isContentValid()) {
                System.err.println("invalid file");
                return;
            }

            readXML();
            deleteFile();
            processContent();
            writeXML();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public boolean isValidFilename() throws IOException {
        String name = file.toString().split("\\.")[0];
        if (name.matches("^orders\\d*$") && name.contains("orders")) {
            orderNr = name.replaceAll("\\D+","");
            return true;
        }
        return false;

    }

    public boolean isContentValid() throws IOException{

        if (!Files.probeContentType(file).equals("text/xml")) {
            System.err.format("New file '%s'" +
                    " is not an xml file.%n\n", file);
            return false;
        }

        return true;

    }

    public void readXML() throws IOException {

        String content = Files.readString(Path.of(inputFolder + "\\" + file));
        orders = xmlMapper.readValue(content, Orders.class);
    }

    public void deleteFile() {

        File f = new File(inputFolder + "\\" + file.toString());
        f.delete();
    }

    public void writeXML() {
        for (String key : outputData.keySet()) {
            Supplier supplier = new Supplier(outputData.get(key));
            try {
                xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
                xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
                xmlMapper.writeValue(new FileOutputStream(outputFolder + "/" + key + orderNr + ".xml"), supplier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void processContent() {

        for (Order order: orders.getOrders()) {
            order.getProducts().forEach(product -> product.setDate(order.getCreated()));
            products.addAll(order.getProducts());
        }

        Comparator<Product> productDateComparator = Comparator.comparingLong(Product::dateToTimestamp);
        products.sort(productDateComparator.reversed());
        outputData = products.stream().collect(Collectors.groupingBy(Product::getSupplier));

    }

}
