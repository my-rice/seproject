package it.faint.model;

import java.io.*;
import java.util.List;

import javafx.application.Platform;

/** This is a class for 
* 
*/
public class FileManager {
    private File file;
    private Drawing drawing;
    private Thread t;

    public FileManager(File file, Drawing drawing) {
        this.file = file;
        this.drawing = drawing;
    }

    public void saveShapes(){
        t=new Thread(new Runnable() {    // Thread for the save operation in order to avoid the block of the interface when saving

            @Override
            public void run() {
                try (ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                    out.writeObject(drawing.getAllShapes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });
        t.start();
        
    }

    public void loadShapes(){
        t=new Thread(new Runnable() {    // Thread for the save operation in order to avoid the block of the interface when saving
            private List<Shape> list=null;

            @Override
            public void run() {
                try (ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                    list=(List<Shape>) in.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> drawing.insertAllShapes(list)); // Inserito poichè l'intefaccia grafica la si può modificare solo dal thread principale
            }
            
        });
        t.start();
    }
}
