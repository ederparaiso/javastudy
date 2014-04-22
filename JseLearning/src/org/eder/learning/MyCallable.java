package org.eder.learning;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<Long> {
	@Override
	public Long call() throws Exception {
		//test alternate to synchronized block
		MainTest.lock.lock();
		
		MainTest.nextID++;
		System.out.println("ID: "+MainTest.nextID);
		
		MainTest.lock.unlock();
		
		long sum = 0;
		for (long i = 0; i <= 100; i++) {
			sum += i;
		}
		return sum;
	}
}
