package com.simon.concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author  : simon
 * @version : Aug 3, 2014 1:20:22 PM
 *
 **/
public class CyclicBarrierTest {

	public static void main(String[] args) throws InterruptedException {
		final int n = 4;
		final int t = n * 2;
		final CountDownLatch cdl = new CountDownLatch(t);
		ExecutorService executor = Executors.newCachedThreadPool();
		final Random rand = new Random();
		final CyclicBarrier barrier = new CyclicBarrier(n, new Runnable() {

			@Override
			public void run() {
				System.out.println("Everybody's here, let's go for fun!");
			}
		});

		for (int i = 0; i < t; i++) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(rand.nextInt(10) * 1000);
						System.out.println(Thread.currentThread().getName() + " is here, where is others?");
						barrier.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					} finally {
						cdl.countDown();
					}
				}
			});
		}

		cdl.await();
		System.out.println("Done!");

	}

}
