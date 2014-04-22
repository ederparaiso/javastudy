package org.eder.learning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class TestProperties {

	public static void main(String[] args) {
		Properties defaultProperties = new Properties();
		
		try(FileOutputStream out = new FileOutputStream("default.properties")){
			defaultProperties.setProperty("author", "eder");
			defaultProperties.setProperty("lastExecution", new SimpleDateFormat("dd-MM-YY:HH:mm:ssss").format(new Date(System.currentTimeMillis())));
			defaultProperties.store(out, "app execution properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		defaultProperties = null;
		defaultProperties = new Properties();
		try(FileInputStream in = new FileInputStream("default.properties")){
			defaultProperties.load(in);
			System.out.println("File has: "+defaultProperties.size()+" properties.");
			if(!defaultProperties.containsKey("nonexistprop")){
				System.out.println("nonexistprop: "+defaultProperties.getProperty("nonexistprop", "default Value for non existing properties"));
				defaultProperties.setProperty("nonexistprop", "now has a value!");
				System.out.println("File has: "+defaultProperties.size()+" properties.");
				System.out.println("nonexistprop: "+defaultProperties.getProperty("nonexistprop"));
				defaultProperties.remove("nonexistprop");
				System.out.println("File has: "+defaultProperties.size()+" properties.");
				System.out.println("nonexistprop: "+defaultProperties.getProperty("nonexistprop"));
			}
			System.out.println("Listing all properties: ");
			defaultProperties.list(new PrintStream(System.out, true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
