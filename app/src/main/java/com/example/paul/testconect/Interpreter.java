package com.example.paul.testconect;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Paul on 4/25/2016.
 */
public class Interpreter {

    public String parseXML(String xmlRecords) {

        String xmlOut ="";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlRecords));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("recipe");

            // iterate the employees
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                NodeList name = element.getElementsByTagName("name");
                Element line = (Element) name.item(0);
                xmlOut = xmlOut.concat("Name: " + getCharacterDataFromElement(line)+ "\n");

                NodeList description = element.getElementsByTagName("description");
                line = (Element) description.item(0);
                xmlOut = xmlOut.concat( "Description: " + getCharacterDataFromElement(line)+ "\n");

                NodeList link = element.getElementsByTagName("recipelink");
                line = (Element) link.item(0);
                xmlOut = xmlOut.concat("Link: " + getCharacterDataFromElement(line) + "\n"+ "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlOut;
    }
        public static String getCharacterDataFromElement(Element e) {
            Node child = e.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
            return "?";
        }
}
