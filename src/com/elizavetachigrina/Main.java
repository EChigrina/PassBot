package com.elizavetachigrina;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String pass = PasswordGenerator.generatePassword();
        System.out.println(pass);
        DatabaseConnector.connect();
//        try {
//            DatabaseConnector.createDbTables();
//            DatabaseConnector.addPerson("id109878987");
//            if(!DatabaseConnector.ifPasswordExists("id109878987", "gmail")) {
//                System.out.println("not exists");
//            }
//            DatabaseConnector.addPassword("id109878987", "gmail", "0000");
//            if(DatabaseConnector.ifPasswordExists("id109878987", "gmail")) {
//                System.out.println("exists");
//            }
//            DatabaseConnector.updatePerson("id109878987", "pass");
//            DatabaseConnector.updatePassword("id109878987", "gmail", "1111");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        DatabaseConnector.closeConnection();
    }
}
