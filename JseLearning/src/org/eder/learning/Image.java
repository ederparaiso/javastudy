package org.eder.learning;

class Image {
	private byte[] image;
	private String name;
	private Image(String name) {
		this.name = name;
		image = new byte[1024 * 1024 * 100];
	}

	static Image getImage(String name) {
		return new Image(name);
	}
	
	String getName(){
		return name;
	}
	
}