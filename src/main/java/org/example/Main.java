package org.example;


import reader.Readvault;

public class Main {


    public static void main(String[] args) {
        System.setProperty("org.example", "/C:\\Users\\akash\\Downloads\\JavaHasiCorpVaultProject\\src\\main\\resources");
        Readvault.loadPropertiesFromProperties("application");
       String apiKey = Readvault.getKey("JAVA_API");
        if (apiKey != null) {
            System.out.println("JAVA_API: " + apiKey);
        } else {
            System.out.println("JAVA_API not found.");
        }
    }


}