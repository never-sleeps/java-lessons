package ru.java.xml.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlReaderDemo {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        var file = new File(ClassLoader.getSystemResource("data.xml").getFile());

        List<Share> shareList = new XmlReaderDemo().parse(file);
        System.out.println(shareList);
    }

    private List<Share> parse(File file) throws ParserConfigurationException, SAXException, IOException {
        var factory = SAXParserFactory.newInstance();
        var saxParser = factory.newSAXParser();

        var handler = new XmlHandler();
        saxParser.parse(file, handler);
        return handler.getResult();
    }
}


