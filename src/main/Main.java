package main;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        UIManager.put("Panel.background", new Color(234, 234, 234));
        UIManager.put("Table.background", new Color(234, 234, 234));
        UIManager.put("ScrollPane.background", new Color(234, 234, 234));
        UIManager.put("ScrollBar.background", new Color(234, 234, 234));
        UIManager.put("OptionPane.background", new Color(234, 234, 234));
        UIManager.put("Viewport.background", new Color(234, 234, 234));
        UIManager.put("TableHeader.background", new Color(247, 247, 247));
        UIManager.put("TableHeader.foreground", new Color(255, 86, 63));
        Controller controller = new Controller();
    }
}
