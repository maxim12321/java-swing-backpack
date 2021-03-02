package controller;

import backpacks.Backpack;
import backpacks.exporters.*;
import backpacks.parsers.*;
import exceptions.*;
import shapes.*;
import view.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;

public class Controller {
    private final BackpackWindow backpackWindow;
    private Backpack backpack;

    private BackpackExporter backpackExporter;

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

    public void loadBackpack() {
        String defaultPath = Paths.get("").resolve("res/files").toAbsolutePath().toString();
        JFileChooser fileChooser = new JFileChooser(defaultPath);
        fileChooser.setDialogTitle("Load backpack from");
        fileChooser.setAcceptAllFileFilterUsed(false);

        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files", "xml");
        fileChooser.setFileFilter(xmlFilter);

        int userSelection = fileChooser.showOpenDialog(backpackWindow);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToOpen = fileChooser.getSelectedFile();
        if (!fileToOpen.exists()) {
            JOptionPane.showMessageDialog(backpackWindow,
                    "Please, select existing file",
                    "File doesn't exist",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BackpackParser backpackParser;
        if (xmlFilter.accept(fileToOpen)) {
            backpackParser = new XMLBackpackParser();
        } else {
            JOptionPane.showMessageDialog(backpackWindow,
                    "Please, select correct file",
                    "File has incorrect extension",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            backpack = backpackParser.parseBackpack(fileToOpen);
            refreshShapes();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(backpackWindow, e.getMessage(),
                    "Error loading backpack", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void exportBackpackAsXML() {
        backpackExporter = new XMLBackpackExporter();
        exportBackpack();
    }

    private void exportBackpack() {
        String defaultPath = Paths.get("").resolve("res/files").toAbsolutePath().toString();
        JFileChooser fileChooser = new JFileChooser(defaultPath);
        fileChooser.setDialogTitle("Export backpack as");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        int userSelection = fileChooser.showSaveDialog(backpackWindow);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();

        backpackExporter.beginSavingBackpack(backpack.getCapacity());
        for (int i = 0; i < backpack.size(); i++) {
            backpackExporter.addShape(backpack.getShape(i));
        }
        try {
            backpackExporter.export(fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(backpackWindow, "Backpack saved!",
                    "Saving complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(backpackWindow, e.getMessage(),
                    "Error saving backpack", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshShapes() {
        backpackWindow.clearBackpack();
        for (int i = 0; i < backpack.size(); i++) {
            backpackWindow.addShape(backpack.getShape(i), i);
        }
        backpackWindow.setCapacityValue(backpack.getVolume(), backpack.getCapacity());
    }
}
