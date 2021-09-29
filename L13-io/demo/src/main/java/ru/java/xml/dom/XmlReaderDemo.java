package ru.java.xml.dom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XmlReaderDemo {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        var file = new File(ClassLoader.getSystemResource("data.xml").getFile());

        var dbFactory = DocumentBuilderFactory.newInstance();
        var dBuilder = dbFactory.newDocumentBuilder();
        var xmlDocument = dBuilder.parse(file);

        //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        xmlDocument.getDocumentElement().normalize();

        List<Share> shareList = new ArrayList<>();
        System.out.println("Root element: " + xmlDocument.getDocumentElement().getNodeName()); // Root element: shares

        NodeList nList = xmlDocument.getElementsByTagName("share");
        for (var idx = 0; idx < nList.getLength(); idx++) {
            var nNode = nList.item(idx);
            System.out.println("Element: " + nNode.getNodeName()); // Element: share
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                var eElement = (Element) nNode;

                System.out.println("ticker: " + eElement.getElementsByTagName("ticker").item(0).getTextContent());
                System.out.println("last: " + eElement.getElementsByTagName("last").item(0).getTextContent());
                System.out.println("date : " + eElement.getElementsByTagName("date").item(0).getTextContent());
                System.out.println();

                shareList.add(
                        new Share(
                                eElement.getElementsByTagName("ticker").item(0).getTextContent(),
                            Double.parseDouble(eElement.getElementsByTagName("last").item(0).getTextContent()),
                            eElement.getElementsByTagName("date").item(0).getTextContent()
                        )
                );
            }
        }
        /*
            Element: share
            ticker: HYDR
            last: 0.6297
            date : 20180924

            Element: share
            ticker: GAZP
            last: 159
            date : 20180924
         */

        System.out.println(shareList);
        // [Share{ticker='LKOH', last=4780.0, date='20180924'}, Share{ticker='HYDR', last=0.6297, date='20180924'}, Share{ticker='GAZP', last=159.0, date='20180924'}]

        //XPath
        var xPath = XPathFactory.newInstance().newXPath();
        var expression = "/shares/share[@id='1']";
        var nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        for (var idx = 0; idx < nodeList.getLength(); idx++) {
            var nNode = nodeList.item(idx);
            System.out.println("\nXPath Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                var eElement = (Element) nNode;
                System.out.println("ticker: " + eElement.getElementsByTagName("ticker").item(0).getTextContent());
                System.out.println("last: " + eElement.getElementsByTagName("last").item(0).getTextContent());
                System.out.println("date: " + eElement.getElementsByTagName("date").item(0).getTextContent());
            }
        }
        /*
            XPath Element :share
            ticker: LKOH
            last: 4780
            date: 20180924
         */
    }
}
