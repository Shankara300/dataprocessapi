package com.infrrd.api.service;

import java.util.concurrent.locks.ReentrantLock;
/*
 * Gives singleton object for ReentrantLock.
 */
public class ReentrantLockSingleton {
	private static ReentrantLock singleton = null;

    private ReentrantLockSingleton() {
      
    }

    public static ReentrantLock getInstance() {
        if (singleton == null) {
            synchronized (ReentrantLockSingleton.class) {
                if (singleton == null) {
                	singleton = new ReentrantLock();
                }
            }
        }
        return singleton;
    }
}
