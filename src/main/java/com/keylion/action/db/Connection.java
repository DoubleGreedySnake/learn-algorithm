package com.keylion.action.db;


import java.util.concurrent.atomic.AtomicInteger;

public class Connection {

    final AtomicInteger count = new AtomicInteger();


    public void commit() {
        int num = count.incrementAndGet();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // System.out.println("====== Connection commit()"+num);
    }

    public void close() {
        // System.out.println("====== Connection close()");
    }

}
