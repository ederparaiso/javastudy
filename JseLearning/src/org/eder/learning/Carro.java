package org.eder.learning;

import java.util.ArrayList;
import java.util.List;
//import static java.lang.Math.PI;

public class Carro extends Veiculo{
	private int anoFab;
	public MarcaVeiculo m;
	public class Carroca {
		int nroCavalos;
	};
	enum Modelo {GOL, VECTRA};
	@Override
	void desc(){
		System.out.print("Carro: ");
		super.desc();
	}
	
	void desc(String outraDesc){
		System.out.println("teste");
	}
	
	public Carro(int ano, String marca){
		super();
		anoFab = ano;
		super.setMarca(marca);
	}
	
	public Carro(){super();}
	
	public boolean igual(Object o)
	{
		if (o == this) return true;
		if (!(o instanceof Carro))
			return false;
		Carro p = (Carro) o;
		return p.anoFab == anoFab;
	}
	
	static void copyList1(List<? extends String> src, List<? super String> dest) {
		for (int i = 0; i < src.size(); i++)
			dest.add(src.get(i));
	}

	static <T> void copyList(List<T> src, List<T> dest) {
		for (int i = 0; i < src.size(); i++)
			dest.add(src.get(i));
		//System.out.println(PI);
		
	}
	
	public static void main(String[] args){
		Carro c = new Carro(2010, "Vectra");
		c.desc();
		System.out.println(c);
		System.out.println(Integer.MIN_VALUE);
		List<Number> ln = new ArrayList<>();
		ln.add(new Byte("1"));
		ln.add(new Integer(12));
		for(Number n : ln){
			System.out.println(n);
		}
	}
}
