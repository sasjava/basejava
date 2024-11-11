package ru.basejava;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter = 0;
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(Thread.currentThread().getName());
//        Thread thread0 = new Thread() {
//            @Override
//            public void run() {
//                System.out.println(getName() + ", " + getState());
//                throw new IllegalStateException();
//            }
//        };
//        thread0.start();
//
//        new Thread(
//                () -> System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState())
//        ).start();
//        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

//        for (int i = 0; i < THREADS_NUMBER; i++) {
//            Thread thread = new Thread(() -> {
//                for (int j = 0; j < 100; j++) {
//                    mainConcurrency.inc();
//                }
//            });
//            thread.start();
//            threads.add(thread);
//        }
//        Thread.sleep(100);
//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        System.out.println(mainConcurrency.counter);

        mainConcurrency.doDeadLock();
    }

//    private synchronized void inc() {
//        counter++;
//    }

    private void doDeadLock() {
        counter = 0;
        Thread thread1 = new Thread(() -> {
            System.out.println("Start thread1");
            inc1();
            System.out.println("End thread1 " + counter);
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("Start thread2");
            inc2();
            System.out.println("End thread2 " + counter);
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("counter = " + counter);
    }

    private void inc1() {
        synchronized (LOCK1) {
            display("LOCK1");
            synchronized (LOCK2) {
                display("  LOCK2");
                counter++;
            }
        }
    }

    private void inc2() {
        synchronized (LOCK2) {
            display("LOCK2");
            synchronized (LOCK1) {
                display("  LOCK1");
                counter++;
            }
        }
    }

    private void display(String lockName) {
        System.out.println(lockName + ": " + Thread.currentThread().getName() + " " + Thread.currentThread().getState());
    }
}