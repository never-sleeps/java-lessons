package ru.java.xml.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/* for java 9,10
https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception-in-j
--add-modules java.xml.bind

removed from java 11
*/
public class XmlReaderDemo {
    public static void main(String[] args) throws JAXBException {
        ClassLoader classLoader = ru.java.xml.sax.XmlReaderDemo.class.getClassLoader();

        File fileShare = new File(classLoader.getResource("share.xml").getFile());

        JAXBContext jaxbContext = JAXBContext.newInstance(Share.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Share share = (Share) unmarshaller.unmarshal(fileShare);
        System.out.println(share); // Share{ticker='LKOH', last=4780.0, date='20180924'}

        File fileData = new File(classLoader.getResource("data.xml").getFile());

        JAXBContext jaxbContext2 = JAXBContext.newInstance(Shares.class);
        Unmarshaller unmarshaller2 = jaxbContext2.createUnmarshaller();
        Shares shareList = (Shares) unmarshaller2.unmarshal(fileData);
        System.out.println(shareList);
        // Shares{shares=[Share{ticker='LKOH', last=4780.0, date='20180924'}, Share{ticker='HYDR', last=0.6297, date='20180924'}, Share{ticker='GAZP', last=159.0, date='20180924'}]}
    }
}
