package org.eder.learning;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.util.Date;

public class FileTest {
	public static void main(String[] args) {
		//testBasicFile();
		//testRandomAccessFile();
		//testFileStreamClasses();
		//testFilterOutputStream();
		//testDataStream();
		//testSerialization();
		//testExternalizable();
		testCharReaderWriters();
	}

	private static void testCharReaderWriters() {
		long starttime, endtime;
		// using outputstreamwriter
		/*try (OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream("\\temp\\charfile.txt"))) {
			osw.write("Hello world!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		starttime = System.currentTimeMillis();
		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"\\temp\\charfile.txt"))) {
			isr.skip(6);
			char[] cbuf = new char[6];
			while (isr.read(cbuf) != -1) {
		//		System.out.println(new String(cbuf));
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time test 1: "+(endtime-starttime));
		
		// equivalente code with fileWriter
		/*try (FileWriter fw = new FileWriter("\\temp\\charfile2.txt")) {
			fw.write("Hello world!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		starttime = System.currentTimeMillis();
		try (FileReader fr = new FileReader("\\temp\\charfile2.txt")) {
			fr.skip(6);
			char[] cbuf = new char[6];
			while (fr.read(cbuf) != -1) {
			//	System.out.println(new String(cbuf));
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time test 2: "+(endtime-starttime));
		
		// Buffer structure
		/*try (BufferedWriter bw = new BufferedWriter(new FileWriter(
				"\\temp\\charfile3.txt"))) {
			bw.write("Hello world!");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		starttime = System.currentTimeMillis();
		try (BufferedReader bf = new BufferedReader(new FileReader(
				"\\temp\\charfile3.txt"))) {
			bf.skip(6);
			char[] cbuf = new char[6];
			while (bf.read(cbuf) != -1) {
			//	System.out.println(new String(cbuf));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time test 3: "+(endtime-starttime));
		/*
		 * Results, file size 20kb	
		    Elapsed time test 1: 13
		 	Elapsed time test 2: 6
		 	Elapsed time test 3: 2
		 * */
	}

	private static void testExternalizable() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("\\temp\\Employee2.txt"))){
			oos.writeObject(new Employee2("Jose", 23));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("\\temp\\Employee2.txt"))){
			Employee2 e = (Employee2)ois.readObject();
			System.out.println(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void testSerialization() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("\\temp\\Employee.txt"))){
			oos.writeObject(new Employee("Jose", 23));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("\\temp\\Employee.txt"))){
			Employee e = (Employee)ois.readObject();
			System.out.println(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void testDataStream() {
		try (FileOutputStream fos = new FileOutputStream("\\temp\\daos.txt",false);
				DataOutputStream dos = new DataOutputStream(fos)){
			dos.writeUTF("Batata");
			dos.writeInt(50);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileInputStream fis = new FileInputStream("\\temp\\daos.txt");
				DataInputStream dis = new DataInputStream(fis)){
			System.out.println(dis.readUTF());
			System.out.println(dis.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testFilterOutputStream() {
		try(FileOutputStream fos = new FileOutputStream("\\temp\\filteroutputstream.txt")){
			LogFilterOutputStream lfos = new LogFilterOutputStream(fos);
			for(int i = 0; i < 10; i++){
				lfos.write(i);
				Thread.sleep(1000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void testFileStreamClasses() {
		testRandomAccessFile();
		
		long starttime, endtime;
		starttime = System.currentTimeMillis();
		try (FileInputStream fis = new FileInputStream("\\temp\\uselessfile.txt");
				FileOutputStream fos = new FileOutputStream("\\temp\\backupfile.txt",false)){
			int b;
			while((b = fis.read()) != -1)
				fos.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time reading byte-to-byte: "+(endtime-starttime));
		
		starttime = System.currentTimeMillis();
		try (FileInputStream fis = new FileInputStream("\\temp\\uselessfile.txt");
				FileOutputStream fos = new FileOutputStream("\\temp\\backupfile.txt",false)){
			byte[] b = new byte[4];
			while((fis.read(b,0,4)) != -1)
				fos.write(new String(b).getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time reading 4 bytes per time: "+(endtime-starttime));
		
		starttime = System.currentTimeMillis();
		try (FileInputStream fis = new FileInputStream("\\temp\\uselessfile.txt");
				FileOutputStream fos = new FileOutputStream("\\temp\\backupfile.txt",false);
				BufferedInputStream bis = new BufferedInputStream(fis,4);
				BufferedOutputStream bos = new BufferedOutputStream(fos, 4)){
			int b;
			while((b = bis.read()) != -1)
				bos.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		endtime = System.currentTimeMillis();
		System.out.println("Elapsed time reading bytes to a buffered stream of 4 bytes size: "+(endtime-starttime));
	}

	private static void testRandomAccessFile() {
		File f = new File("\\temp\\uselessfile.txt");
		f.setWritable(true);
		//RandomAccessFile raf = null;
		try(RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
			//raf = new RandomAccessFile(f, "rw");
			long flen = raf.length();
			System.out.println("Initial len: " + flen);
			if (flen > 0) {
				System.out.println("File contents: ");
				String text = raf.readLine();
				while (text != null) {
					//System.out.println(text);
					text = raf.readLine();
				}
			}
			raf.writeBytes("Write at " + System.currentTimeMillis()
					+ System.getProperty("line.separator"));
			System.out.println("Updated len: " + raf.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testBasicFile() {
		File myDir = new File("\\temp\\mydir");
		boolean success = true;
		if (!myDir.exists())
			success = myDir.mkdir();
		listFiles(myDir);
		if (success) {
			File arq = new File(myDir, "FileTest.txt");
			if (!arq.exists())
				try {
					arq.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			System.out.println("-------");
			listFiles(myDir);
			arq.setReadOnly();
			System.out.println("readonly: " + !arq.canWrite());
			arq.delete();
			myDir.deleteOnExit();
			
		}
	}

	private static void listFiles(File arq) {
		String[] flist = arq.list();
		if (flist.length == 0)
			System.out.println("<empty>");
		for (String s : flist)
			System.out.println(s);
	}
}

class LogFilterOutputStream extends FilterOutputStream{

	public LogFilterOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(int b) throws IOException {
		Log(new Date(System.currentTimeMillis()));
		super.write(b);
	}
	
	static void Log(Date d){
		System.out.println("File modified at "+d);
	}
	
}