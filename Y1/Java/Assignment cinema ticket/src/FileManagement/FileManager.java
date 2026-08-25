/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileManagement;

/**
 *
 * @author User
 */
import java.io.*;

public class FileManager { 

    // handle any type <T> and write the object to binary format file, all the object must implements serializable
    public static <T> void saveToFile(String fileName, T object) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(object);
            oos.flush();
        }
    }
@SuppressWarnings("unchecked")
    public static <T> T loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (T) ois.readObject();
        }
    }

}
