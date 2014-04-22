package org.eder.learning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

//Java Architecture for XML Binding
public class TestJAXB {

	public static void main(String[] args) {
		//jaxbBasic();
		jaxbstreaming();
	}

	private static void jaxbstreaming(){
		ArrayList<String> cats = new ArrayList<String>();
		cats.add("Fiction");
		cats.add("Adventure");

		Book b = new Book();
		b.setAuthor("Eduardo Sphor");
		b.setBookName("A Batalha do Apocalipse");
		b.setCategories(cats);
		
		Book b2 = new Book();
		b2.setAuthor("J R Tolkien");
		b2.setBookName("O Senhor dos Anéis");
		b2.setCategories(cats);
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Book.class);
			
			XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
			XMLStreamWriter xmlsw = xmlof.createXMLStreamWriter(new FileOutputStream("c:\\temp\\jaxbbookstreaming.xml",false),"UTF-8");
	        Marshaller m = jc.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        xmlsw.writeStartDocument("UTF-8", "1.0");
	        xmlsw.writeStartElement("Books");

	        m.marshal(b, xmlsw);
	        m.marshal(b2, xmlsw);

	        xmlsw.writeEndElement();
	        xmlsw.writeEndDocument();
	        xmlsw.flush();
	        xmlsw.close();
	        
	        XMLInputFactory xmlif = XMLInputFactory.newFactory();
			XMLStreamReader xmlsr;
			xmlsr = xmlif.createXMLStreamReader(new FileReader("c:\\temp\\jaxbbookstreaming.xml"));
			
			Unmarshaller um = jc.createUnmarshaller();

			while (xmlsr.hasNext()) {
				if(xmlsr.getEventType()==XMLStreamReader.START_ELEMENT && xmlsr.getLocalName().equalsIgnoreCase("book")){
					Book unMarshalledBook = (Book)um.unmarshal(xmlsr);
					System.out.println(unMarshalledBook.toString());
				}
				else if(xmlsr.getEventType()!=7){
					xmlsr.next();
				}
				else{
					xmlsr.nextTag();
				}
			}
			
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private static void jaxbBasic() {
		Book b = new Book();
		b.setAuthor("Eduardo Sphor");
		b.setBookName("A Batalha do Apocalipse");
		ArrayList<String> cats = new ArrayList<String>();
		cats.add("Fiction");
		cats.add("Adventure");
		b.setCategories(cats);
		
		try {
			JAXBContext context = JAXBContext.newInstance(Book.class);
			Marshaller m = context.createMarshaller();
			//optional, just for formating xml
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			m.marshal(b, System.out);
			m.marshal(b, new File("c:\\temp\\jaxbbook.xml"));
			
			Unmarshaller um = context.createUnmarshaller();
			Book unMarshalledBook = (Book)um.unmarshal(new File("c:\\temp\\jaxbbook.xml"));
			System.out.println(unMarshalledBook.toString());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}

@XmlRootElement//(name="Library")
@XmlType(propOrder = { "author", "bookName", "categories" })
class Book{
	private String bookName;
	private String author;
	private List<String> categories;
	
	@XmlElement(name="name")
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String title) {
		this.bookName = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	// XmLElementWrapper generates a wrapper element around XML representation
	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("{"+bookName+", "+author+", ");
		for(String s:categories){
			sb.append(s+", ");
		}
		sb.append("}");
		return sb.toString();
	}
}