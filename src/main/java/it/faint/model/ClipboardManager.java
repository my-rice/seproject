package it.faint.model;

import java.io.*;
import java.util.Base64;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public final class ClipboardManager {
    
    private static ClipboardManager instance;
    private Clipboard clipboard;
    private BooleanProperty isEmptyClipboard;

    private ClipboardManager (){
        this.clipboard= Clipboard.getSystemClipboard();
        this.isEmptyClipboard= new SimpleBooleanProperty(true);
    }

    public static ClipboardManager getIstance() {
        if (instance == null){
            instance= new ClipboardManager();
        }
        return instance;
    }

    public ReadOnlyBooleanProperty getIsEmptyClipboard() {
        return BooleanProperty.readOnlyBooleanProperty(this.isEmptyClipboard);
    }
    
    public void fillClipboardManager(Shape s){
        try {
            Shape clone=(Shape)s.clone();
            ClipboardContent content=new ClipboardContent();
            String serializedShape=ClipboardManager.serializableToString(clone);

            content.putString(serializedShape);
            this.isEmptyClipboard.set(false);
            this.clipboard.setContent(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Shape getClipboardContent(){
        try{
            if (clipboard.hasString()){
                String serializedShape = this.clipboard.getString();
                Object obj=ClipboardManager.objectFromString(serializedShape);
                if(!(obj instanceof Shape)){
                    this.clipboard.clear();
                    this.isEmptyClipboard.set(true);
                    return null;
                } 
                Shape shape= (Shape) ClipboardManager.objectFromString(serializedShape);

                Rectangle2D bounding= shape.boundsProperty().get();
                Point2D offset = new Point2D(bounding.getMinX() + 50, bounding.getMinY() + 50);
                shape.moveShape(offset);
                return shape;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
        
    }

    public static String serializableToString( Serializable o ) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }

    public static Object objectFromString(String s) throws IOException, ClassNotFoundException{
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
   }
   
}
