package cn.yikuair.utils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlUtil {

	public static Document domObject(StringBuffer data) throws SAXException, IOException, ParserConfigurationException{
		Document xmlDoc = null;
		xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
		parse(new ByteArrayInputStream(data.toString().getBytes()));
		return xmlDoc;
	}
	public static Document noPrefixDomObjcet(StringBuffer data) throws SAXException, IOException, ParserConfigurationException{
		Document xmlDoc = null;
		xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
		parse(new ByteArrayInputStream(data.toString().getBytes()));
		return xmlDoc;
	}
	public static Document domObject(File file) throws SAXException, IOException, ParserConfigurationException{
		Document xmlDoc = null;
		xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
		parse(file);
		return xmlDoc;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
