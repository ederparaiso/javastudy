package org.eder.learning;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainTest {
	 static long nextID = 0;
	private static final int NTHREDS = 5;
	static final Lock lock = new ReentrantLock();
	
	synchronized static long staticgetNextID(String tname)	{
		System.out.println(tname+" - currentId: "+nextID);
		return nextID++; 
	}
	
	/** Equivalent to staticgetNextID */
	static long internalStaticSyncgetNextID(String tname)	{
		synchronized (MainTest.class) {
			System.out.println(tname+" - currentId: "+nextID);
			return nextID++;
		}
	}
	
	synchronized long getNextID(String tname)	{
		System.out.println(tname+" - currentId: "+nextID);
		return nextID++;
	}
	
	/**	Equivalent to getNextID	*/
	long internalInstanceSyncgetNextID(String tname){
		synchronized (this) {
			System.out.println(tname+" - currentId: "+nextID);
			return nextID++;
		}
	}
	
	public static void main(String[] args) throws InterruptedException,	ClassNotFoundException, InstantiationException, IllegalAccessException {
		//MainTest.testReference();
		//MainTest.testReflection();
		//testBenchmarkArrayCopy();
		//testThread();
		//testConcurrentRunnable();
		testConcurrentCallable();		
	}

	static void testConcurrentCallable() {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		List<Future<Long>> list = new ArrayList<Future<Long>>();
		for (int i = 0; i < 50; i++) {
			Callable<Long> worker = new MyCallable();
			Future<Long> submit = executor.submit(worker);
			list.add(submit);
		}
		long sum = 0;
		System.out.println(list.size());
		// now retrieve the result
		for (Future<Long> future : list) {
			try {
				sum += future.get();
			} catch (InterruptedException e) {} 
			catch (ExecutionException e) {}
		}
		System.out.println(sum);
		executor.shutdown();
	}

	static void testConcurrentRunnable() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
	    for (int i = 0; i < 50; i++) {
	      Runnable worker = new MyRunnable(1000L + i,i);
	      executor.execute(worker);
	    }
	    // This will make the executor accept no new threads
	    // and finish all existing threads in the queue
	    executor.shutdown();
	    // After shutdown, this statements doesnt allowed
	    //Runnable worker = new MyRunnable(1000L + 50,50);
	    //executor.execute(worker);
	    
	    //Attempt to stop all actively executing tasks, halt the processing of waiting 
	    //tasks, and return a list of the tasks that were awaiting execution
	    //List<Runnable> awaitingTasks = executor.shutdownNow();
	    //System.out.println("awaiting tasks: "+awaitingTasks.size());
	    // Wait until all threads are finish
	    boolean allThreadsFinished = false;
	    allThreadsFinished = executor.awaitTermination(5, TimeUnit.SECONDS);
	    System.out.println(allThreadsFinished?"Finished all threads":"Finished before all threads complete.");
	}
	
	static void testBenchmarkArrayCopy() {
		Carro[] iar = new Carro[10000000];
		for (int i = 0; i < iar.length; i++)
			iar[i] = new Carro();
		Carro[] iar2 = new Carro[10000000];
		Carro[] iar3 = new Carro[10000000];
		
		long starttime1 = System.currentTimeMillis();
		System.arraycopy(iar, 0, iar2, 0, 1000);
		long stoptime1 = System.currentTimeMillis();
		
		long starttime2 = System.currentTimeMillis();
		for (int i = 0; i < iar.length; i++) {
			iar3[i] = iar[i];
		}
		long stoptime2 = System.currentTimeMillis();

		System.out.println("Exec time arraycopy: " + (starttime1 - stoptime1));
		System.out.println("Exec time manual: " + (stoptime2 - starttime2));
	}

	static void testReflection() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		int nroclass = Class.forName("org.eder.learning.Carro").getClasses().length;
		System.out.println(nroclass);
		
		int nroclass2 = Carro.class.getClasses().length;
		System.out.println(nroclass2);
		
		Carro c = new Carro(2005,"Gol");
		int nroclass3 = c.getClass().getDeclaredClasses().length;
		System.out.println(nroclass3);
		
		((Carro) Class.forName("org.eder.learning.Carro").newInstance()).desc();
		try {
			Carro vectra = (Carro)Class.forName("org.eder.learning.Carro").getDeclaredConstructor(int.class, String.class).newInstance(2010,"Vectra");
			vectra.desc();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	static void testReference() {
		Image image = Image.getImage("large.png");
		System.out.println("caching image");
		SoftReference<Image> cache = new SoftReference<>(image);
		image = null;
		byte[] b = new byte[512];

		while (cache.get() != null) {
			System.out.println("image is still cached ");
			b = new byte[b.length * 10];
		}
		if (cache.get() == null)
			System.out.println("image is no longer cached");
		b = null;
		System.out.println("reloading and recaching image");
		cache = new SoftReference<>(Image.getImage("large.png"));
		int counter = 0;
		while (cache.get() != null && ++counter != 7)
			System.out.println("image is still cached");
	}

	static void testThread(){
		final MainTest testInstance = new MainTest();
		Runnable r = new Runnable() {
			Object o = new Object();
			MainTest testInst = testInstance;
			@Override
			public void run() {
				//testSyncLocalBlock();
				//testSyncInstanceMethod();
				//testSyncStaticMethod();
				//testSyncBlockStaticMethod();
				//testSyncBlockInstaceMethod();

			}
			
			private void testSyncInstanceMethod() {
				try {
					for(int i=0;i<10;i++){
						testInst.getNextID(Thread.currentThread().getName());
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {}
			}
			private void testSyncStaticMethod() {
				try {
					for(int i=0;i<10;i++){
						MainTest.staticgetNextID(Thread.currentThread().getName());
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {}
			}
			private void testSyncLocalBlock() {
				try {
					for(int i=0;i<10;i++){
						synchronized (o) {
							System.out.println(Thread.currentThread().getName()+" - currentId: "+MainTest.nextID);
							MainTest.nextID++;
						}
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {}
			}
			private void testSyncBlockStaticMethod() {
				try {
					for(int i=0;i<10;i++){
						MainTest.internalStaticSyncgetNextID(Thread.currentThread().getName());
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {}
			}
			private void testSyncBlockInstaceMethod() {
				try {
					for(int i=0;i<10;i++){
						testInst.internalInstanceSyncgetNextID(Thread.currentThread().getName());
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {}
			}
		};
		Thread th1 = new Thread(r,"th1");
		th1.start();
		Thread th2 = new Thread(r,"th2");
		th2.start();
	}
}
