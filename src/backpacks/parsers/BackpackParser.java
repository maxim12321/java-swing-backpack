package backpacks.parsers;

import backpacks.Backpack;
import exceptions.FullBackpackException;
import exceptions.InvalidBackpackFileException;
import exceptions.OpenFileException;
import shapes.Ball;
import shapes.Cube;
import shapes.Parallelepiped;
import shapes.Shape;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class BackpackParser {
    protected Map<String, Shape> shapeTypes;

    private Backpack backpack;

    public BackpackParser() {
        shapeTypes = new HashMap<>();
        shapeTypes.put("Cube", new Cube());
        shapeTypes.put("Parallelepiped", new Parallelepiped());
        shapeTypes.put("Ball", new Ball());
    }

    public Backpack parseBackpack(File fileToParse)
            throws InvalidBackpackFileException, OpenFileException, FullBackpackException {
        backpack = null;
        if (validateFile(fileToParse)) {
            parseFile(fileToParse);
        }
        return backpack;
    }

    protected void createBackpack(BigDecimal backpackCapacity) {
        backpack = new Backpack(backpackCapacity);
    }

    protected void addShape(Shape shape) {
        if (backpack != null) {
            backpack.addShape(shape);
        }
    }

    protected abstract boolean validateFile(File file);
    protected abstract void parseFile(File file);
}
