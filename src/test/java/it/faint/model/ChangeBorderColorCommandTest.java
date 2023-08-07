package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

public class ChangeBorderColorCommandTest {
    private List<ChangeBorderColorCommand> changeBorderColorCommandList;
    private List<Shape> shapes;
    private Field shapeField, borderColorField, prevBorderColorField;
    private List<Color> colors, targetColors;
    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {
        colors = List.of(
        Color.ALICEBLUE,
        Color.ANTIQUEWHITE,
        Color.AQUA,
        Color.BLACK,
        Color.DARKSLATEGRAY,
        Color.HONEYDEW,
        Color.KHAKI,
        Color.TURQUOISE,
        Color.TOMATO,
        Color.FIREBRICK
        );
        targetColors = new LinkedList<>();
        for(int i=0; i<colors.size(); i++){
            targetColors.add(0, colors.get(i));
        }
        changeBorderColorCommandList = new ArrayList<>();
        shapes = new ArrayList<>();
        for(int i=0; i<10; i++){
            Shape s = new Rectangle();
            s.setStroke(colors.get(i));
            shapes.add(s);
            s = new Ellipse();
            s.setStroke(colors.get(i));
            shapes.add(s);
            s = new Line();
            s.setStroke(colors.get(i));
            shapes.add(s);
        }

        for(int i=0; i<10; i++){
            changeBorderColorCommandList.add(new ChangeBorderColorCommand(shapes.get(i*3), colors.get(i), targetColors.get(i)));
            changeBorderColorCommandList.add(new ChangeBorderColorCommand(shapes.get(i*3+1), colors.get(i), targetColors.get(i)));
            changeBorderColorCommandList.add(new ChangeBorderColorCommand(shapes.get(i*3+2), colors.get(i), targetColors.get(i)));
        }

        shapeField = ChangeBorderColorCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true);
    }

    @Test
    void testExecute() throws IllegalArgumentException, IllegalAccessException {
        int i=0;
        for(ChangeBorderColorCommand command: changeBorderColorCommandList){
            Shape shape = (Shape) shapeField.get(command);
            assertEquals(colors.get(i/3),shape.strokeProperty().get());
            command.execute();
            assertEquals(targetColors.get(i/3),shape.strokeProperty().get());
            i++;
            assertThrows(RuntimeException.class, ()->{command.execute();});
        }

    }

    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException {
        int i=0;
        for(ChangeBorderColorCommand command: changeBorderColorCommandList){
            command.execute();
        }
        for(ChangeBorderColorCommand command: changeBorderColorCommandList){
            Shape shape = (Shape) shapeField.get(command);
            assertEquals(targetColors.get(i/3),shape.strokeProperty().get());
            command.undo();
            assertEquals(colors.get(i/3),shape.strokeProperty().get());
            i++;
            assertThrows(RuntimeException.class, ()->{command.undo();});
        }
    }
}
