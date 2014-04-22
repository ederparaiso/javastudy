package org.eder.learning;

public class Veiculo {
	private String marca;
	public void setMarca(String m){
		marca = m;
	}
	public String getMarca(){
		return marca;
	}
	
	void desc(){
		System.out.println("Marca = "+marca);
	}
}
