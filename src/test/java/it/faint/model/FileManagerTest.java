package it.faint.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform;


public class FileManagerTest {
    private FileManager fileManager;
    private DrawEditor editor;
    private Field tField;
    private List<Shape> list;
    private Drawing drawing;

    @BeforeEach
    public void setUp(){

        drawing=new Drawing();
        editor=new DrawEditor(drawing);

        list=new ArrayList<>();
        list.add((Shape) new Rectangle(1.0, 1.0, 10.0, 20.0));
        list.add((Shape) new Rectangle(10.0, 10.0, 10.0, 20.0));
        list.add((Shape) new Rectangle(20.0, 20.0, 10.0, 20.0));
        list.add((Shape) new Ellipse(1.0, 1.0, 10.0, 20.0));
        list.add((Shape) new Ellipse(25.0, 100.0, 100.0, 20.0));
        list.add((Shape) new Ellipse(100.5, 100.5, 20.0, 20.0));
        list.add((Shape) new Line(2.0, 2.0, 10.0, 10.0));
        list.add((Shape) new Line(100.0, 10.0, 10.0, 10.0));
        list.add((Shape) new Line(42.5, 42.5, 20.5, 20.5));
        editor.insertAllShapes(list);
    }

    private synchronized static void initFX(){
        try{
        Platform.startup(()->{});
        } catch (IllegalStateException e){
            
        }
    }

    private static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    
    }

    /* Creo il file e la cartella temporanei, istanzio il fileManager, salvo la lista di shapes del Drawing, carico le shape sul file a mano, poi vado a carcare dal file 
        e confronto che le shape caricate siano quelle */
    @Test
    void testLoad(@TempDir Path tempDir) throws IOException, NoSuchFieldException, SecurityException, InterruptedException, IllegalArgumentException, IllegalAccessException {
        initFX();
        Path tempFile = tempDir.resolve("test.faint");
        Files.createFile(tempFile);
        File file = tempFile.toFile();
        fileManager = new FileManager(file, drawing);
        tField = FileManager.class.getDeclaredField("t");
        tField.setAccessible(true);

        List<Shape> expectedResult=list;
        try (ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(expectedResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editor.clearAllShapes();
        fileManager.loadShapes();
        Thread t = (Thread)tField.get(fileManager);     // Bisogna aspettare il Thread di load
        t.join();
        waitForRunLater();      // Bisogna aspettare il Thread Platform
        List<Shape> result=editor.getAllShapes();
        assertEquals(expectedResult,result);
    }

    /* Creo il file e la cartella temporanei, istanzio il fileManager, salvo la lista di shapes del Drawing, salvo e poi vado a leggere il file 
        e confronto che le shape nel file siano effettivamente le shape che erano nel drawing */
    @Test
    void testSave(@TempDir Path tempDir) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Path tempFile = tempDir.resolve("test.faint");
        Files.createFile(tempFile);
        File file = tempFile.toFile();
        fileManager = new FileManager(file, drawing);
        tField = FileManager.class.getDeclaredField("t");
        tField.setAccessible(true);

        List<Shape> expectedResult=editor.getAllShapes();

        fileManager.saveShapes();
        Thread t = (Thread)tField.get(fileManager);     // Bisogna aspettare il Thread di salvataggio
        t.join();
        List<Shape> result=null;
        try (ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            result=(List<Shape>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expectedResult,result);
    }
}
