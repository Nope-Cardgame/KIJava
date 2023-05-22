package logic;

import logging.NopeLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class UserdataFileReader {

    private static final Logger LOG = NopeLogger.getLogger(UserdataFileReader.class.getSimpleName());

    public static String[] getUserData(){
        String[] userData = new String[2];
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\client\\userdata.txt"));
            userData[0] = reader.readLine();
            userData[1] = reader.readLine();

            LOG.info("Username and password were loaded from file.");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static boolean isEmpty(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\client\\userdata.txt"));
            if(reader.readLine() == null) {
                LOG.info("No userdata found in file");
                reader.close();
                return true;
            } else {
                reader.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
