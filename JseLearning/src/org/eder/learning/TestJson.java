package org.eder.learning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestJson {

	public static void main(String[] args) {
		//gsonObjectModelApi();
		//gsonStreamingApi();
		//gsonmixedStreamingObjectModelAccess();
		//jacksonObjectModelApi();
		//jacksonStreamingApi();
		//jacksonMixedStreamingObjectModelAccess();
	}

	private static void jacksonMixedStreamingObjectModelAccess() {
		ArrayList<String> generes = new ArrayList<>();
		generes.add("Fantasy");
		List<Book1> books = new ArrayList<>();
		books.add(new Book1("J K Rowling", "Harry Potter", generes, 2002,15.00d));
		books.add(new Book1("J R Tolkien", "Lord of the Rings", generes, 2010,23.00d));
		books.add(new Book1("G R Martin", "Game of thrones", generes, 2012,33.00d));
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jf = new JsonFactory(mapper);
		try {
			JsonGenerator jg = jf.createGenerator(new File("c:\\temp\\jacksonmixed.json"), JsonEncoding.UTF8);
			//jg.setCodec(mapper); //passed through jf constructor 
			jg.writeStartArray();
			for (Book1 message : books) {
				//jg.writeObject(message); //older version
				mapper.writeValue(jg, message);
			}
			jg.writeEndArray();
			jg.flush();
			jg.close();
			
			JsonParser jp = jf.createParser(new File("c:\\temp\\jacksonmixed.json"));
			//jp.setCodec(mapper); //passed through jf constructor
			List<Book1> readedbooks = new ArrayList<>();
			Book1 readedbook;
			jp.nextToken();//read JsonToken.START_ARRAY
			while(jp.nextToken() == com.fasterxml.jackson.core.JsonToken.START_OBJECT){
				//readedbook = jp.readValueAs(Book1.class); //older version
				readedbook = mapper.readValue(jp, Book1.class);
				readedbooks.add(readedbook);
			}
			
			for(Book1 rb : readedbooks)
				System.out.println(rb);
			
			jp.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jacksonStreamingApi() {
		JsonFactory jf = new JsonFactory();
		try {
			JsonGenerator jg = jf.createGenerator(new File("c:\\temp\\jacksonstreaming.json"), JsonEncoding.UTF8);
			jg.writeStartObject();
			jg.writeStringField("author", "J K Rowling");
			jg.writeStringField("name", "Harry Potter");
			jg.writeFieldName("generes");
			jg.writeStartArray();
			jg.writeString("Fantasy");
			jg.writeEndArray();
			jg.writeNumberField("year", 2002);
			jg.writeNumberField("price", 15.0d);
			jg.writeEndObject();
			jg.flush();
			jg.close();
			
			JsonParser jp = jf.createParser(new File("c:\\temp\\jacksonstreaming.json"));
			Book1 b = new Book1();
			jp.nextToken();//read JsonToken.START_OBJECT
			while(jp.nextToken() != com.fasterxml.jackson.core.JsonToken.END_OBJECT){
				String field = jp.getCurrentName();
				if("author".equals(field))
					b.setAuthor(jp.getText());
				else if("name".equals(field))
					b.setName(jp.getText());
				else if("generes".equals(field)){
					jp.nextToken();
					ArrayList<String> generes = new ArrayList<>();
					while(jp.nextToken() != com.fasterxml.jackson.core.JsonToken.END_ARRAY){
						generes.add(jp.getText());
					}
					b.setGeneres(generes);
				}
				else if("year".equals(field) && jp.getCurrentToken()!=com.fasterxml.jackson.core.JsonToken.FIELD_NAME)
					b.setYear(jp.getValueAsInt());
				else if("price".equals(field)){//same effect of 'if' above
					jp.nextToken();
					b.setPrice(jp.getDoubleValue());
				}
			}
			System.out.println(b);
			jp.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jacksonObjectModelApi() {
		//Object model
		ArrayList<String> generes = new ArrayList<>();
		generes.add("Fantasy");
		Book1 b = new Book1("J K Rowling", "Harry Potter", generes, 2002,15.00d);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("c:\\temp\\jacksonbooks.json"), b);
			Book1 jsonBook = mapper.readValue(new File("c:\\temp\\jacksonbooks.json"), Book1.class);
			System.out.println(jsonBook.toString());
			
		} catch (JsonGenerationException | JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Generics
		Map<String, Object> bookmap = new HashMap<>();
		bookmap.put("author", "J K Rowling");
		bookmap.put("year", 2010);
		bookmap.put("topSales", true);
		
		try {
			mapper.writeValue(new File("c:\\temp\\jacksonmap.json"), bookmap);
			Map<String, Object> jsonreadmap = new HashMap<>();
			//jsonreadmap = mapper.readValue(new File("c:\\temp\\jacksonmap.json"), Map.class); //not type safe
			jsonreadmap = mapper.readValue(new File("c:\\temp\\jacksonmap.json"), new TypeReference<Map<String, Object>>() {});
			System.out.print(jsonreadmap.get("author")+" ");
			System.out.print(jsonreadmap.get("year")+" ");
			System.out.println(jsonreadmap.get("topSales")+" ");
		} catch (JsonGenerationException | JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void gsonmixedStreamingObjectModelAccess() {
		Gson parser = new Gson();
		ArrayList<String> generes = new ArrayList<>();
		generes.add("Fantasy");
		List<Book1> books = new ArrayList<>();
		books.add(new Book1("J K Rowling", "Harry Potter", generes, 2002,15.00d));
		books.add(new Book1("J R Tolkien", "Lord of the Rings", generes, 2010,23.00d));
		books.add(new Book1("G R Martin", "Game of thrones", generes, 2012,33.00d));
		try {
			JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream("c:\\temp\\gsonbooks-out.json"),"UTF-8"));
			writer.setIndent("  ");
			writer.beginArray();
			for (Book1 message : books) {
				parser.toJson(message, Book1.class, writer);
			}
			writer.endArray();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream("c:\\temp\\gsonbooks-out.json"),"UTF-8"));
			List<Book1> messages = new ArrayList<Book1>();
			reader.beginArray();
			while (reader.hasNext()) {
				Book1 message = parser.fromJson(reader, Book1.class);
				messages.add(message);
			}
			reader.endArray();
			reader.close();
			for (Book1 b : messages) {
				System.out.println(b.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream("c:\\temp\\gsonbooks-out.json"), "UTF-8"));
			 JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));){
			while (true) {
				JsonToken token = reader.peek();
				switch (token) {
				case BEGIN_ARRAY:
					reader.beginArray();
					writer.beginArray();
					break;
				case END_ARRAY:
					reader.endArray();
					writer.endArray();
					break;
				case BEGIN_OBJECT:
					reader.beginObject();
					writer.beginObject();
					break;
				case END_OBJECT:
					reader.endObject();
					writer.endObject();
					break;
				case NAME:
					String name = reader.nextName();
					writer.name(name);
					break;
				case STRING:
					String s = reader.nextString();
					writer.value(s);
					break;
				case NUMBER:
					String n = reader.nextString();
					writer.value(new BigDecimal(n));
					break;
				case BOOLEAN:
					boolean b = reader.nextBoolean();
					writer.value(b);
					break;
				case NULL:
					reader.nextNull();
					writer.nullValue();
					break;
				case END_DOCUMENT:
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	private static void gsonStreamingApi() {
		//streaming api
		/*
		 * {
			"name":"ann",
			"age":29,
			"messages":["msg 1","msg 2","msg 3"]
		   }
		 */
		
		OutputStreamWriter out = new OutputStreamWriter(System.out);
		JsonWriter writer;
		
		try {
			writer = new JsonWriter(out);
			//writer = new JsonWriter(new FileWriter("c:\\temp\\user.json"));
			
			//set indentation for pretty print
			writer.setIndent("  ");
			writer.beginObject();// {
			writer.name("name").value("mkyong"); // "name" : "ann"
			writer.name("age").value(29); // "age" : 29
			writer.name("messages"); // "messages" : 
			writer.beginArray(); // [
			writer.value("msg 1"); // "msg 1"
			writer.value("msg 2"); // "msg 2"
			writer.value("msg 3"); // "msg 3"
			writer.endArray(); // ]
			writer.endObject(); // }
			writer.flush();
			
			//close writer
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\temp\\user.json"));
			reader.beginObject();
		  	while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("name")) {
					System.out.println(reader.nextString());
				} else if (name.equals("age")) {
					System.out.println(reader.nextInt());
				} else if (name.equals("message")) {
					// read array
					reader.beginArray();
					while (reader.hasNext()) {
						System.out.println(reader.nextString());
					}
					reader.endArray();
				} else {
					reader.skipValue(); // avoid some unhandle events
				}
			}
			reader.endObject();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static void gsonObjectModelApi() {
		//object model api (like xml dom)
		//toJson
		ArrayList<String> g = new ArrayList<>();
		g.add("Adventure");
		g.add("Action");
		Book1 b = new Book1("Tolkien", "LoTR",g,1980,15.00d);
		ArrayList<Book1> books = new ArrayList<>();
		books.add(b);
		Bookstore bs = new Bookstore(books);
		Gson gsonParser = new Gson();
		String gstring = gsonParser.toJson(bs);
		System.out.println(gstring);
		
		//fromJson string
		Bookstore gbs = gsonParser.fromJson(gstring, Bookstore.class);
		System.out.println(gbs.books.get(0).toString());
		
		//generics
		// conversion to json
		System.out.println("Converting Generics to Json");
		List<String> listOfString = new ArrayList<>();
		listOfString.add("Elem 1");
		listOfString.add("Elem 2");
		Gson gson = new Gson();
		String jsonStr = gson.toJson(listOfString);
		System.out.println("json representation :" + jsonStr);
		Type collectionType2 = new TypeToken<List<String>>() {}.getType();
		List<String> listObj = gson.fromJson(jsonStr, collectionType2);
		System.out.println("converted object representation: " + listObj);
	}
}

class Book1{
	@SerializedName(value = "escritor")//used only for gson
	String author;
	String name;
	ArrayList<String> generes;
	int year;
	double price;
	transient int availableCount;//ignored by gson parser, for jackson, omit get/set method
	
	public Book1(){}//used by jackson
	public Book1(String a, String n, ArrayList<String>g, int y, double p){
		author = a;
		name = n;
		generes = g;
		year = y;
		price = p;
		availableCount = 5;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getGeneres() {
		return generes;
	}

	public void setGeneres(ArrayList<String> generes) {
		this.generes = generes;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	@Override
	public String toString(){
		StringBuilder g = new StringBuilder("[");
		for(String gn : generes)
			g.append(gn+" ");
		g.append("]");
		return author+", "+name+", "+g.toString()+", "+year+", "+price+", "+availableCount;
	}
}
class Bookstore{
	ArrayList<Book1> books;
	
	public Bookstore(ArrayList<Book1> b){
		books = b;
	}
}
