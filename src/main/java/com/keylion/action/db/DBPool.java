package com.keylion.action.db;

import java.util.LinkedList;

public class DBPool {


    private static final LinkedList<Connection> POOLS = new LinkedList<>();

    public DBPool(int init) {
        // 初始化
        if (init > 0) {
            for (int i = 0; i < init; i++) {
                POOLS.add(SqlSessionFactory.fetchConnection());
            }
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (POOLS) {
                POOLS.addLast(connection);
                POOLS.notifyAll();
            }
        }
    }

    public Connection getConnection(long timeout) {
        synchronized (POOLS) {
            // 不是等待超时
            if (timeout <= 0) {
                while (POOLS.isEmpty()) {
                    try {
                        POOLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // 等待超时
                long totalTime = System.currentTimeMillis() + timeout;
                while (POOLS.isEmpty() && totalTime > 0) {
                    try {
                        totalTime = totalTime - System.currentTimeMillis();
                        if (totalTime>0){
                            POOLS.wait(totalTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Connection connection = null;
                if (!POOLS.isEmpty()) {
                    connection = POOLS.removeFirst();
                    System.out.println(connection);
                }
                return connection;
            }
            return null;
        }
    }


}
