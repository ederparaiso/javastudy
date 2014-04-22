package org.arpit.javapostsforlearning.webservice;

public class HelloWorldWSClient {

	public static void main(String[] args) {
		HelloWorldImplService connection = new HelloWorldImplService();
		HelloWorld client = connection.getHelloWorldImplPort();
		System.out.println(client.helloWorld("Jose"));
	}

}
