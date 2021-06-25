import Models.User;
import Views.RegisterMenu;
import com.opencsv.CSVReader;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) throws IOException {
        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.run();
    }
}
