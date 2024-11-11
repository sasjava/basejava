package ru.basejava;

import java.util.ArrayList;
import java.util.List;

public class DeadLock {
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();
    private int counter = 0;

    public static void main(String[] args) {
        (new DeadLock()).run();
    }

    private void run() {
        List<Thread> threads = new ArrayList<>(2);
        addThread(threads, LOCK1, LOCK2);
        addThread(threads, LOCK2, LOCK1);
        threads.forEach(Thread::start);
    }

    private void addThread(List<Thread> threads, Object lock1, Object lock2) {
        threads.add(new Thread(() -> {
                    System.out.println("Start " + Thread.currentThread().getName());
                    this.inc(lock1, lock2);
                    System.out.println("End " + Thread.currentThread().getName());
                })
        );
    }

    private void inc(Object lock1, Object lock2) {
        synchronized (lock1) {
            display(lock1);
            synchronized (lock2) {
                counter++;
            }
        }
    }

    private void display(Object lock) {
        String lockName;
        if (lock.equals(LOCK1)) {
            lockName = "LOCK1";
        } else {
            lockName = "LOCK2";
        }
        Thread t = Thread.currentThread();
        System.out.println(lockName + ": " + t.getName() + " " + t.getState());
    }
}
