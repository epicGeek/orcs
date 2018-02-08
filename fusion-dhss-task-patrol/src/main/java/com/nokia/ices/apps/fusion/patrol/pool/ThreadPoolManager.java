package com.nokia.ices.apps.fusion.patrol.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Scope;

@Scope("singleton")
public class ThreadPoolManager {
	private static ThreadPoolManager manager;
	private static int jobSize = 60;
	private ExecutorService executorService;

	private ThreadPoolManager() {
		initThreadPool(jobSize);
	}

	public static synchronized ThreadPoolManager getInstance() {
		if (manager == null) {
			manager = new ThreadPoolManager();
		}
		return manager;
	}

	private void initThreadPool(int jobSize) {
		executorService = Executors.newFixedThreadPool(jobSize);
	}

	public void execute(Runnable runnable) {
		executorService.execute(runnable);
	}

	public void submit(Callable<?> callable) {
		executorService.submit(callable);
	}

	public void execute(Thread thread) {
		executorService.execute(thread);
	}

}
