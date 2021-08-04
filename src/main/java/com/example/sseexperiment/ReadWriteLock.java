package com.example.sseexperiment;

import com.example.sseexperiment.exceptions.LockTimeOutException;

public class ReadWriteLock {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;
    private final long LOCK_TIME_OUT = 1000;


    public synchronized void lockRead() throws InterruptedException, LockTimeOutException {
        long startTime = System.currentTimeMillis();
        while (writers > 0 || writeRequests > 0) {

            if (System.currentTimeMillis() - startTime > LOCK_TIME_OUT) {
                throw new LockTimeOutException();
            }
            wait(LOCK_TIME_OUT);
        }
        readers++;

    }

    public synchronized void unlockRead() {
        readers--;
        notifyAll();

    }

    public synchronized void lockWrite() throws InterruptedException, LockTimeOutException {
        writeRequests++;
        long startTime = System.currentTimeMillis();
        while (readers > 0 || writers > 0) {
            if (System.currentTimeMillis() - startTime > LOCK_TIME_OUT) {
                throw new LockTimeOutException();
            }
            wait(LOCK_TIME_OUT);

        }
        writeRequests--;
        writers++;

    }


    public synchronized void unlockWrite() {
        writers--;
        notifyAll();
    }


}

