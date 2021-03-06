package com.simon.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {

	private AtomicInteger counter;
	private String name;
	private List<String> stats;

	public CustomThreadFactory(String name) {
		counter = new AtomicInteger(0);
		this.name = name;
		stats = new ArrayList<>();
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + "-Thread_" + counter.get());
		counter.incrementAndGet();
		stats.add(String.format("created thread %d with name %s on %s\n", t.getId(), t.getName(), new Date()));
		return t;
	}

	public String getStats() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = stats.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			sb.append("\n");
		}
		return sb.toString();
	}

	public class Task implements Runnable {

		@Override
		public void run() {
			try {
//				TimeUnit.SECONDS.sleep(5);
				TimeUnit.MILLISECONDS.sleep(10);
				System.out.println(Thread.currentThread().getName() + ": executed");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws InterruptedException {
		CustomThreadFactory factory = new CustomThreadFactory("CustomThreadFactory");
		Task task = factory.new Task();
		Thread t;
		System.out.println("Starting the thread");
		for (int i = 0; i < 100; i++) {
			t = factory.newThread(task);
			t.start();
			t.join();
		}
		System.out.printf("Factory stats :\n%s\n", factory.getStats());
	}

}
