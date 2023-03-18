package Controller;

import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IOSystem {

    private static IOSystem ioSystemInstance;
    private IOSystem() {
    }
    public static IOSystem getInstance() {
        if (ioSystemInstance == null) {
            ioSystemInstance = new IOSystem();
        }
        return ioSystemInstance;
    }
    public  <T> List<T> read(String file) throws IOException, ClassNotFoundException {
        List<T> list = new ArrayList<>();
        try {
            ObjectInputStream a = new ObjectInputStream(new FileInputStream(file));
            list = (List<T>) a.readObject();
            a.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found!");
        } catch (IOException e) {
            throw new IOException("Read file failed!");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found!");
        }
        return list;
    }

    public  <T> void write( List<T> list, String file) throws IOException {
        try {
            ObjectOutputStream a = new ObjectOutputStream(new FileOutputStream(file));
            a.writeObject(list);
            System.out.println("Write file successfully!");
            a.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found!");
        } catch (SocketException e) {
            throw new SocketException("Socket exception!");
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Unknown host exception!");
        } catch (IOException e) {
            throw new IOException("Write file failed!");
        }
    }

    public  <T> void clearData(String file) {
        List<T> list = new ArrayList<>();
        try {
            ObjectOutputStream a = new ObjectOutputStream(new FileOutputStream(file));
            a.writeObject(list);
            a.close();
        } catch (Exception e) {
        }
    }
}