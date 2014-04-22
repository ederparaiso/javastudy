package org.eder.learning;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

//to perform custom serialization, create read/writeObject methods from Objectin/outputStream
class Employee implements Comparable<Employee>, Serializable {
	private static final long serialVersionUID = -6911922628704747612L;

	private String name;
	private int age;
	private transient String pwd;//not serializable field

	Employee(String name, int age) {
		pwd = (Math.random()*50)+"";
		this.age = age;
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public int compareTo(Employee e) {
		return name.compareTo(e.name);
	}

	@Override
	public String toString() {
		return name + " " + age + " "+(pwd==null?"***":pwd);
	}

	public static void main(String[] args) {
		testHashCode();

		// handling mutable keys in hashMap
		// testHashMutableKeys();
	}

	private static void testHashCode() {
		SortedSet<Employee> sse = new TreeSet<>();
		sse.add(new Employee("Sally Doe", 20));
		sse.add(new Employee("Bob Doe", 25));
		Employee e1 = new Employee("John Doe", 23);
		Employee e2 = new Employee("John Doe", 23);
		sse.add(e1);
		sse.add(e2);
		System.out.println(sse);
		System.out.println(e1.equals(e2));
	}

	private static void testHashMutableKeys() {
		Map<Employee, String> map1 = new HashMap<>();
		Employee e11 = new Employee("John Doe", 28);
		map1.put(e11, "SALES");
		System.out.println(map1);
		System.out.println("map1 contains key e1 = " + map1.containsKey(e11));
		map1.remove(e11);
		e11.setAge(29);
		map1.put(e11, "SALES");
		System.out.println(map1);
		System.out.println("map1 contains key e1 = " + map1.containsKey(e11));
		System.out.println(map1.keySet().size());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
		if (age != other.age) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}

class Employee2 implements Externalizable {
	private String name;
	private int age;
	private String pwd;//not serializable field

	public Employee2(){};
	
	Employee2(String name, int age) {
		pwd = (Math.random()*50)+"";
		this.age = age;
		this.name = name;
	}

	@Override
	public String toString() {
		return name + " " + age + " "+(pwd==null?"***":pwd); 
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		name = in.readUTF();
		age = in.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
		out.writeInt(age);
	}
}