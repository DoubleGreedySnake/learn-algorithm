package com.keylion.action.site;

public class NotifyAction {



    private int km;

    private String city = "Beijing";


    public synchronized void waitKm(){
        while (this.km<100){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Km is change ["+this.km+"]");
    }

    public synchronized void waitCity(){
        while (this.city.equals("Beijing")){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("City is change ["+this.city+"]");
    }


    public synchronized void changeKm(int km){
        this.km = km;
        notifyAll();
    }


    public synchronized void changeCity(String city){
        this.city = city;
        notifyAll();
    }

}
