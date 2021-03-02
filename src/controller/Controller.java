package controller;

import backpacks.Backpack;
import exceptions.FullBackpackException;
import shapes.*;
import view.*;

import javax.swing.*;
import java.math.BigDecimal;

public class Controller {
    private final BackpackWindow backpackWindow;
    private final Backpack backpack;

    public Controller() {
        backpackWindow = new BackpackWindow(this);
        backpackWindow.setVisible(true);

        backpack = new Backpack(new BigDecimal(10000));
        refreshShapes();
    }

    public void addShape(Shape shape) {
        try {
            backpack.addShape(shape);
        } catch (FullBackpackException e) {
            JOptionPane.showMessageDialog(backpackWindow,
                    e.getMessage(),
                    "Backpack is full",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        refreshShapes();
    }

    public void createNewShape() {
        CreateShapeWindow createShapeWindow = new CreateShapeWindow(this);
        createShapeWindow.setVisible(true);
    }

    public void removeShape(int index) {
        backpack.removeShape(index);
        refreshShapes();
    }

    private void refreshShapes() {
        backpackWindow.clearBackpack();
        for (int i = 0; i < backpack.size(); i++) {
            backpackWindow.addShape(backpack.getShape(i), i);
        }
        backpackWindow.setCapacityValue(backpack.getVolume(), backpack.getCapacity());
    }
}
