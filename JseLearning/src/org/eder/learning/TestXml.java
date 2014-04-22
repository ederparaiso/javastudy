package org.eder.learning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;

import javax.naming.OperationNotSupportedException;
import javax.sql.rowset.spi.XmlReader;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Namespace;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestXml {
	// SAX API reads xml sequentially and performs only read operation
	// DOM API provides read and write operations and random access but the
	// documents size depends memory available
	// SAX and DOM are old apis, instead use stax or jaxb
	// SAX package: org.xml.sax.

	/*
		<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		<catalog xmlns="http://www.tutortutor.ca/" xmlns:f="http://www.w3schools.com/furniture">
		<!--Just main books available in book store-->
			<book id="bk101">
				<author>Gambardella, Matthew</author>
				<f:price>15.00</f:price>
				<origin f:publishedprevious="s" firstedition="n">EUA</origin>
				<isbn/>
			</book>
		</catalog>
	 */
	public static void main(String[] args) {
		//streamRead();
		//streamWrite();
		//eventRead();
		//eventWrite(); 
		testXpath();
		
	}

	private static void testXpath() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse("c:\\temp\\bookstore.xml");
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			XPathExpression xpe = xp.compile("/catalog/book[@id='bk101']/author/text()");
			Object result = xpe.evaluate(doc, XPathConstants.NODESET);
			NodeList nl = (NodeList)result;
			for(int i=0;i<nl.getLength();i++)
				System.out.println(nl.item(i).getNodeValue());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void eventWrite() throws FactoryConfigurationError {
		XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
		try {
			XMLEventWriter xmlew = xmlof.createXMLEventWriter(new FileWriter("c:\\temp\\bookstore-ew.xml",false));
			final XMLEventFactory event = XMLEventFactory.newFactory();
			
			xmlew.add(event.createStartDocument("UTF-8", "1.0", false));
			
			Iterator<Namespace> namespaces = new Iterator<Namespace>() {
				int index=0;
				Namespace[] ns;
				{
					ns = new Namespace[2];
					ns[0] = event.createNamespace("http://www.tutortutor.ca/");
					ns[1] = event.createNamespace("f","http://www.w3schools.com/furniture");
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Namespace next() {
					return ns[index++];
				}
				
				@Override
				public boolean hasNext() {
					return index!=2;//iterator has only 2 namespaces
				}
			};
			xmlew.add(event.createStartElement("", "http://www.tutortutor.ca/", "catalog", null, namespaces));
			xmlew.add(event.createComment("Just main books available in book store"));
			
			Iterator<Attribute> attributes = new Iterator<Attribute>() {
				Attribute[] att;
				int index=0;
				{
					att = new Attribute[1];
					att[0] = event.createAttribute("id", "bk101");
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Attribute next() {
					return att[index++];
				}
				
				@Override
				public boolean hasNext() {
					return index!=1;
				}
			};
			xmlew.add(event.createStartElement("", "", "book",attributes ,null));
			
			xmlew.add(event.createStartElement("", "", "author"));
			xmlew.add(event.createCharacters("Gambardella, Matthew"));
			xmlew.add(event.createEndElement("", "", "author"));
			
			xmlew.add(event.createStartElement("f", "http://www.w3schools.com/furniture", "price"));
			xmlew.add(event.createCharacters("15.00"));
			xmlew.add(event.createEndElement("f", "http://www.w3schools.com/furniture", "price"));
			
			attributes = new Iterator<Attribute>() {
				Attribute[] att;
				int index=0;
				{
					att = new Attribute[2];
					att[0] = event.createAttribute("f","http://www.w3schools.com/furniture","publishedprevious", "s");
					att[1] = event.createAttribute("firstedition", "n");
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
				
				@Override
				public Attribute next() {
					return att[index++];
				}
				
				@Override
				public boolean hasNext() {
					return index!=2;
				}
			};
			xmlew.add(event.createStartElement("", "", "origin",attributes,null));
			xmlew.add(event.createCharacters("EUA"));
			xmlew.add(event.createEndElement("", "", "origin"));
			
			xmlew.add(event.createStartElement("", "", "isbn"));
			xmlew.add(event.createEndElement("", "", "isbn"));
					
			xmlew.add(event.createEndElement("", "", "book"));
			xmlew.add(event.createEndElement("", "", "catalog"));
			xmlew.add(event.createEndDocument());
			
			xmlew.flush();
			xmlew.close();
			System.out.println("Finished");
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void eventRead() {
		try{
			XMLInputFactory xmlif = XMLInputFactory.newFactory();
			XMLEventReader xmler = xmlif.createXMLEventReader(new FileReader("c:\\temp\\bookstore.xml"));
			XMLEvent evt;
			StringBuilder sb;
			while (xmler.hasNext()) {
				evt = xmler.nextEvent();
				switch (evt.getEventType()) {
				case XMLEvent.START_DOCUMENT:
					//System.out.println(evt.toString());
					StartDocument sdevt =((StartDocument) evt); 
					String encoding2 = sdevt.getCharacterEncodingScheme();
					String xmlVer = sdevt.getVersion();
					boolean standAlone = sdevt.standaloneSet(); 
					if(xmlVer!=null || encoding2 !=null || standAlone){
						sb = new StringBuilder("<?xml");
						sb.append((xmlVer==null?"":" version=\""+xmlVer+"\""));
						sb.append((encoding2==null?"":" encoding=\""+encoding2+"\""));
						sb.append((standAlone?" standalone=\""+(sdevt.isStandalone()?"yes":"no")+"\"":""));
						sb.append("?>");
						System.out.println(sb.toString());
					}
					break;
				case XMLEvent.START_ELEMENT:
					StringBuilder nsb = new StringBuilder();
					StartElement seevt = ((StartElement)evt);
					Iterator<Namespace> namespaces = seevt.getNamespaces();
					boolean hasNamespaces = false;
					while(namespaces.hasNext()){
						hasNamespaces = true;
						nsb.append(namespaces.next()).append(" ");
					}
					sb=new StringBuilder("<");
					QName tag =seevt.getName(); 
					String prefix =tag.getPrefix(); 
					if(prefix!=null && prefix.length()>0)
						sb.append(tag.getPrefix()+":");
					sb.append(tag.getLocalPart()+" ");
					Iterator<Attribute> attributes = seevt.getAttributes();
					while(attributes.hasNext()){
						sb.append((Attribute)attributes.next()).append(" ");
					}
					if(hasNamespaces)
						sb.append(nsb.toString());
					sb.append(">");
					System.out.println(sb);
					break;
				case XMLEvent.END_ELEMENT:
					EndElement eevt = (EndElement)evt;
					sb=new StringBuilder("</");
					QName tag2 = eevt.getName();
					String prefix2 = tag2.getPrefix();
					if(prefix2!=null && prefix2.length()>0)
						sb.append(prefix2 + ":");
					sb.append(tag2.getLocalPart()).append(">");
					System.out.println(sb);
					break;
				case XMLEvent.CHARACTERS:
					Characters chevt = (Characters)evt;
					if (!chevt.isWhiteSpace())
						System.out.println(chevt);
					break;
				case XMLEvent.COMMENT:
					Comment cevt = (Comment)evt;
					System.out.println(cevt);
					break;
				}
			}
		}catch (FactoryConfigurationError fce) {
			System.err.println("FCE: " + fce);
		} catch (FileNotFoundException fnfe) {
			System.err.println("FNFE: " + fnfe);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void streamWrite() throws FactoryConfigurationError {
		XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
		try {
			
			//XMLStreamWriter xmlsw = xmlof.createXMLStreamWriter(new FileWriter("c:\\temp\\bookstore-sw.xml",false));
			XMLStreamWriter xmlsw = xmlof.createXMLStreamWriter(new FileOutputStream("c:\\temp\\bookstore-sw.xml",false),"UTF-8");
			xmlsw.writeStartDocument("UTF-8", "1.0");
			xmlsw.writeStartElement("catalog");
			xmlsw.writeDefaultNamespace("http://www.tutortutor.ca/");
			xmlsw.writeNamespace("f", "http://www.w3schools.com/furniture");
			xmlsw.writeComment("Just main books available in book store");

			xmlsw.writeStartElement("book");
			xmlsw.writeAttribute("id", "bk101");
		
			xmlsw.writeStartElement("author");
			xmlsw.writeCharacters("Gambardella, Matthew");
			xmlsw.writeEndElement();
			
			xmlsw.writeStartElement("http://www.w3schools.com/furniture","price");
			xmlsw.writeCharacters("15.00");
			xmlsw.writeEndElement();
			
			xmlsw.writeStartElement("origin");
			xmlsw.writeAttribute("http://www.w3schools.com/furniture","publishedprevious","s");
			xmlsw.writeAttribute("firstedition","n");
			xmlsw.writeCharacters("EUA");
			xmlsw.writeEndElement();
			
			xmlsw.writeEmptyElement("isbn");
			
			xmlsw.writeEndElement();
			
			xmlsw.writeEndElement();
			xmlsw.writeEndDocument();
			
			xmlsw.flush();
			xmlsw.close();
			System.out.println("Finished.");
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void streamRead() {
		try {
			XMLInputFactory xmlif = XMLInputFactory.newFactory();
			XMLStreamReader xmlsr;
			xmlsr = xmlif.createXMLStreamReader(new FileReader("c:\\temp\\bookstore.xml"));
			int evt = 0;
			String xmlVer = xmlsr.getVersion();
			String encoding = xmlsr.getCharacterEncodingScheme();
			boolean standAlone = xmlsr.standaloneSet(); 
			if(xmlVer!=null || encoding !=null || standAlone){
				StringBuilder sb = new StringBuilder("<?xml");
				sb.append((xmlVer==null?"":" version=\""+xmlVer+"\""));
				sb.append((encoding==null?"":" encoding=\""+encoding+"\""));
				sb.append((standAlone?" standalone=\""+(xmlsr.isStandalone()?"yes":"no")+"\"":""));
				sb.append("?>");
				System.out.println(sb.toString());
			}
			while (xmlsr.hasNext()) {
				switch (evt = xmlsr.next()) {
				case XMLStreamReader.START_ELEMENT:
					StringBuilder sb = new StringBuilder("<");

					String prefix = xmlsr.getPrefix();
					if (prefix != null && prefix.length() > 0)
						sb.append(xmlsr.getPrefix() + ":");
					sb.append(xmlsr.getLocalName()).append(" ");
					int nscnt = 0;
					if ((nscnt = xmlsr.getNamespaceCount()) > 0)
						for (int i = 0; i < nscnt; i++) {
							if (xmlsr.getNamespacePrefix(i) == null)
								sb.append(" xmlns=\""
										+ xmlsr.getNamespaceURI(i) + "\"");
							else
								sb.append(" xmlns:"
										+ xmlsr.getNamespacePrefix(i) + "=\""
										+ xmlsr.getNamespaceURI(i) + "\"");
						}

					int attcnt = 0;
					if ((attcnt = xmlsr.getAttributeCount()) > 0)
						for (int i = 0; i < attcnt; i++){
							sb.append(xmlsr.getAttributeLocalName(i))
									.append("='")
									.append(xmlsr.getAttributeValue(i))
									.append("' ");
							//more improvements to print prefix/namespaces of attributes 
							//System.out.println("======== "+xmlsr.getAttributeName(i).getLocalPart()+" | "+xmlsr.getAttributeName(i).getPrefix());
						}
					sb.append(">");
					System.out.println(sb.toString());
					break;
				case XMLStreamReader.END_ELEMENT:
					//System.out.println("======== "+xmlsr.getName().getLocalPart()+" | "+xmlsr.getName().getPrefix());
					StringBuilder sb2 = new StringBuilder("</");
					String prefix2 = xmlsr.getPrefix();
					if (prefix2 != null && prefix2.length() > 0)
						sb2.append(xmlsr.getPrefix() + ":");
					sb2.append(xmlsr.getLocalName() + ">");
					System.out.println(sb2.toString());
					break;
				case XMLStreamReader.CHARACTERS:
					if (!xmlsr.isWhiteSpace())
						System.out.println(xmlsr.getText());
					break;
				case XMLStreamReader.COMMENT:
					System.out.println("<!--" + xmlsr.getText() + "-->");
					break;
				}
			}
		} catch (FactoryConfigurationError fce) {
			System.err.println("FCE: " + fce);
		} catch (FileNotFoundException fnfe) {
			System.err.println("FNFE: " + fnfe);
		} catch (XMLStreamException xmlse) {
			System.err.println("XMLSE: " + xmlse);
		}
	}

}
