package backpacks;

import backpacks.exporters.BackpackExporter;
import backpacks.exporters.XMLBackpackExporter;
import exceptions.FullBackpackException;
import shapes.*;

import java.math.BigDecimal;
import java.util.*;

public class Backpack {
    List<Shape> shapes;
    BigDecimal capacity;

    public Backpack(BigDecimal capacity) {
        this.capacity = capacity;

        shapes = new ArrayList<>();
    }

    public void addShape(Shape shape) throws FullBackpackException {
        if (getVolume().add(shape.getVolume()).compareTo(capacity) > 0) {
            throw new FullBackpackException();
        }
        int insertIndex = 0;
        while (insertIndex < shapes.size() &&
                shapes.get(insertIndex).getVolume().compareTo(shape.getVolume()) > 0) {
            insertIndex++;
        }
        shapes.add(insertIndex, shape);
    }

    public void removeShape(int index) {
        shapes.remove(index);
    }

    public Shape getShape(int index) {
        return shapes.get(index);
    }

    public int size() {
        return shapes.size();
    }

    public BigDecimal getVolume() {
        BigDecimal volume = new BigDecimal(0);
        for (Shape shape : shapes) {
            volume = volume.add(shape.getVolume());
        }
        return volume;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Backpack(\n" + shapes + "\n). Total volume = " + getVolume() + "\n";
    }
}
