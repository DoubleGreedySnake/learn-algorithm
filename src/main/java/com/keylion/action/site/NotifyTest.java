package com.keylion.action.site;

import java.io.IOException;

public class NotifyTest {

    private static final NotifyAction action = new NotifyAction();


   static class ChangeKm implements Runnable{

        @Override
        public void run() {
            action.waitKm();
        }
    }


    static class ChangeCity implements Runnable{

        @Override
        public void run() {
            action.waitCity();
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            new Thread(new ChangeCity()).start();
        }


        for (int i = 0; i < 3; i++) {
            new Thread(new ChangeKm()).start();
        }


        Thread.sleep(3000);

        System.in.read();

        action.changeKm(111);

        Thread.sleep(3000);

        action.changeCity("Ali");


    }
}
