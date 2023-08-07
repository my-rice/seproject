package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoundingBoxTest {
    private BoundingBox boundingBox;
       
    @Test
    void testGetBoundingBox() throws IllegalArgumentException, IllegalAccessException {

        Line expectedShape = new Line(1.0, 1.0, 5.0, 5.0);

        boundingBox = new BoundingBox(expectedShape);
        assertNull(boundingBox.getBoundingBox());
    }
}
