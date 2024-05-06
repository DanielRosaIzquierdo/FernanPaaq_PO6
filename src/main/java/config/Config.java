package config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String rutaAPPDATA_PATH(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return properties.getProperty("APPDATA_PATH", "src/main/java/appData/");
        } catch (IOException e) {
            return "src/main/java/appData/";
        }
    }

    public static String rutaAPPDATA_PATH_U(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return properties.getProperty("APPDATA_PATH_U", "src/main/java/appData/users");
        } catch (IOException e) {
            return "src/main/java/appData/users";
        }
    }
    public static String rutaAPPDATA_PATH_D(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return properties.getProperty("APPDATA_PATH_D", "src/main/java/appData/drivers");
        } catch (IOException e) {
            return "src/main/java/appData/drivers";
        }
    }

    public static String rutaAPPDATA_PATH_A(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return properties.getProperty("APPDATA_PATH_A", "src/main/java/appData/admins");
        } catch (IOException e) {
            return "src/main/java/appData/admins";
        }
    }

    public static String rutaAPPDATA_PATH_TA(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return  properties.getProperty("APPDATA_PATH_TA", "src/main/java/appData/shipmentsTA");

        } catch (IOException e) {
            return "src/main/java/appData/shipmentsTA";
        }
    }

    public static String rutaAPPDATA_PATH_TNRU(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return  properties.getProperty("APPDATA_PATH_TNRU", "src/main/java/appData/shipmentsTNRU");

        } catch (IOException e) {
            return "src/main/java/appData/shipmentsTNRU";
        }
    }

    public static String rutaAPPDATA_PATH_LOG(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return  properties.getProperty("APPDATA_PATH_LOG", "src/main/java/log/log.txt");

        } catch (IOException e) {
            return "src/main/java/log/log.txt";
        }
    }

    public static String rutaPATH_RECEIPT(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return  properties.getProperty("PATH_RECEIPT", "src/main/java/receipt/CustomerReceipt.pdf");

        } catch (IOException e) {
            return "src/main/java/receipt/CustomerReceipt.pdf";
        }
    }


    public static String rutaPATH_EXCEL(){
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return  properties.getProperty("PATH_EXCEL", "src/main/java/dataShipmentsExcel/ExcelFile.xlsx");

        } catch (IOException e) {
            return "src/main/java/dataShipmentsExcel/ExcelFile.xlsx";
        }
    }
}
