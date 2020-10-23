package com.infrrd.api.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataPrcoseeService, Callable<Map<String, Integer>> {

	private static Map<String, Integer> map = new LinkedHashMap<>();
	private static Queue<Thread> queue = new LinkedList<>();
	private static boolean interrupted = false;
	private ReentrantLock lock = ReentrantLockSingleton.getInstance();
	Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Integer> call() throws Exception {
		// Add the thread to the queue and interrupt it when end api is called.
		queue.add(Thread.currentThread());

		if (lock.tryLock()) {
			System.out.print("Thread " + Thread.currentThread().getName() + " aquired the lock");
			processData();
			lock.unlock();
		} else {
			// executed by 2nd thread which doesn't gets lock.
			synchronized (DataServiceImpl.class) {
				Thread thread = queue.poll();
				if (thread != null) {
					thread.stop();
					map.clear();
					interrupted = true;
				}
			}
		}

		if (interrupted || queue.size() == 0) {
			processData();
		}

		return map;
	}

	private void processData() {
		for (int i = 100; i <= 150; i++) {
			// Add new employee and sleep
			map.put("EMPLOYEE" + i, i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.debug("Invalidating the first request");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public String endProcess() {
		if (queue.size() > 0) {
			Thread th = queue.poll();
			th.stop();
			return "Data processing has been stopped";
		} else {
			// No thread in queue
			return "Data processing is already disrupted";
		}
	}

}
