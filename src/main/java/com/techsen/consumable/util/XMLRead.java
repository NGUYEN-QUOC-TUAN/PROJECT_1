package com.techsen.consumable.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class XMLRead {

	// Dom4j解释Xml文档  
    public String readXML(String xmlName,String id,String field) {  
    	
    	String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); 
		if(path.indexOf("WEB-INF")> 0)
            path = path.substring(1,path.indexOf("WEB-INF"));
		
		xmlName = path + "xml/" + xmlName;
		String value = ""; 
    	
        File inputXml = new File(xmlName);  
        SAXReader saxReader = new SAXReader();  
        try {  
            Document document = saxReader.read(inputXml); 
            
            Element root = document.getRootElement();
            for (Iterator<?> i = root.elementIterator("grid"); i.hasNext();) {
                Element node = (Element) i.next();
                if (node.attributeValue("id").equals(id)) {
                	value = node.elementText(field);
                    break;
                }
            }
            
            value = value.trim();
			return value;
            
        } catch (DocumentException e) {  
            System.out.println(e.getMessage()); 
            return "";
        } finally {
        	saxReader = null;
        	inputXml = null;
        }
    }  

}
