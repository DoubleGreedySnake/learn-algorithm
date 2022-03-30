package com.keylion.action.db;

public class SqlSessionFactory {


    public static Connection fetchConnection(){
        return new Connection();
    }
}
