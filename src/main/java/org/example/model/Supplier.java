package org.example.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JacksonXmlRootElement(localName = "products")
public class Supplier {

    @JacksonXmlProperty(localName = "product")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Product> products;
}
