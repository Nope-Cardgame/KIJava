import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserdataFileReader {

    private static final Logger LOG = NopeLogger.getLogger(UserdataFileReader.class.getSimpleName());

    public UserdataFileReader(){
    }

    public String[] getUserData(){
        String[] userData = new String[2];
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\userdata.txt"));
            userData[0] = reader.readLine();
            userData[1] = reader.readLine();

            LOG.info("Username and password were loaded from file.");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }

    public boolean isEmpty(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\userdata.txt"));
            if(reader.readLine() == null) {
                LOG.info("No userdata found in file, creating new account.");
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

    public String[] addNewUser(){
        String[] userData = new String[2];
        try {
            FileWriter writer = new FileWriter("src\\main\\java\\userdata.txt");

            LOG.info("Please enter your username: ");
            Scanner sc = new Scanner(System.in);
            String username = sc.nextLine();
            writer.write(username + "\n");
            userData[0] = username;

            LOG.info("Please enter your password: ");
            String password = sc.nextLine();
            writer.write(password);
            userData[1] = password;

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }
}
