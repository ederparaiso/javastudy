package org.eder.learning;

/**
 * MyRunnable will count the sum of the number from 1 to the parameter
 * countUntil and then write the result to the console.
 * <p>
 * MyRunnable is the task which will be performed
 * 
 * @author Lars Vogel
 * 
 */

public class MyRunnable implements Runnable {
	private final long countUntil;
	private final int tid;
	MyRunnable(long countUntil, int id) {
		this.countUntil = countUntil;
		tid = id;
	}

	@Override
	public void run() {
		long sum = 0;
		for (long i = 1; i < countUntil; i++) {
			sum += i;
		}
		System.out.println(tid+" - "+sum);
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}*/
	}
}
