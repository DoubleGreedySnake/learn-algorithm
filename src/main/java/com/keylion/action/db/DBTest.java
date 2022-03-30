package com.keylion.action.db;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DBTest {


    static DBPool dbPool = new DBPool(10);

    static CountDownLatch countDownLatch;

    public static void main(String[] args) throws InterruptedException {

        // 线程数 50
        int threadCount = 50;
        // 每个线程获取20次
        int count = 20;

        // 获取次数统计
        final AtomicInteger get = new AtomicInteger();

        final AtomicInteger notGet = new AtomicInteger();

        countDownLatch = new CountDownLatch(threadCount * count);

        for (int i = 0; i < threadCount; i++) {
            new Thread(new Work(count, get, notGet, countDownLatch)).start();
        }
        countDownLatch.await();
        System.out.println("请求次数：" + (threadCount * count));
        System.out.println("拿到链接：" + get.get());
        System.out.println("未拿到链接L：" + notGet.get());

    }

    static class Work implements Runnable {

        private int count;

        private final AtomicInteger got;

        private final AtomicInteger notGet;

        private CountDownLatch countDownLatch;

        public Work(int count, AtomicInteger got, AtomicInteger notGet, CountDownLatch countDownLatch) {
            this.count = count;
            this.got = got;
            this.notGet = notGet;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            while (count > 0) {
                Connection connection = dbPool.getConnection(1000);
                if (connection != null) {
                    try {
                        // TODO
                        connection.commit();
                        got.incrementAndGet();
                    } finally {
                        connection.close();
                        dbPool.releaseConnection(connection);
                        countDownLatch.countDown();
                    }
                } else {
                    notGet.incrementAndGet();
                    countDownLatch.countDown();
                }
                count--;
            }

        }
    }
}
