package com.keylion.action.thread;

import com.keylion.action.db.Connection;
import com.keylion.action.db.SqlSessionFactory;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 */
public class SemaphoreTest {

    private static int INIT_SIZE = 10;

    private static LinkedList<Connection> pool = new LinkedList<>();

    private final Semaphore useful,unUseful;

    public SemaphoreTest() {
        this.unUseful = new Semaphore(0);
        this.useful = new Semaphore(INIT_SIZE);
    }

    public SemaphoreTest(Integer initSize) {
        this.unUseful = new Semaphore(0);
        this.useful = new Semaphore(INIT_SIZE);
        if (initSize<=0){
            return;
        }
        for (int i = 0; i < initSize; i++) {
            pool.addLast(SqlSessionFactory.fetchConnection());
        }
    }

    public void releaseConnection(Connection connection) throws InterruptedException {
        if (connection != null) {
            unUseful.acquire();
            synchronized (pool) {
               pool.addLast(connection);
            }
            useful.release();
        }
    }

    public Connection getConnection(long timeout) throws InterruptedException {
        synchronized (pool) {
            // 不是等待超时
            if (timeout <= 0) {
                useful.acquire();
            } else {
                // 等待超时
                long totalTime = System.currentTimeMillis() + timeout;
                while (pool.isEmpty() && totalTime > 0) {
                    try {
                        totalTime = totalTime - System.currentTimeMillis();
                        if (totalTime>0){
                            pool.wait(totalTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Connection connection = null;
                if (!pool.isEmpty()) {
                    connection = pool.removeFirst();
                    System.out.println(connection);
                }
                return connection;
            }
            return null;
        }
    }
}
