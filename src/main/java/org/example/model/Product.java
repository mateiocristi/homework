package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@JacksonXmlRootElement(localName = "product")
public class Product {

    @JacksonXmlProperty(localName = "description")
    private String description;

    @JacksonXmlProperty(localName = "gtin")
    private long gtin;

    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;

    @JacksonXmlProperty(localName = "price")
    private double price;

    @JacksonXmlProperty(localName = "orderid")
    private long orderID;

    @JsonIgnore
    private String date;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JacksonXmlProperty(localName = "supplier")
    private String supplier;

    public long dateToTimestamp() {
        // 2012-07-12T15:29:33.000

        Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        try {
            date = format.parse(getDate().replace("T", " "));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date.getTime();
    }
}
