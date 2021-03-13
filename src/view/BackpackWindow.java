package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sun.istack.internal.NotNull;
import controller.Controller;
import shapes.Shape;
import view.components.CoolButton;

public class BackpackWindow extends JFrame {
    private final Controller controller;

    private final JTable backpackTable;
    private final DefaultTableModel tableModel;
    private final JProgressBar capacityBar;

    public BackpackWindow(Controller controller) {
        super("Backpack");
        this.setSize(1000, 600);
        this.setMinimumSize(new Dimension(700, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.controller = controller;

        Font defaultFont = new Font("Arial", Font.PLAIN, 26);

        CoolButton addItemButton = new CoolButton(
                "Add Shape",
                new Color(60, 232, 142),
                new Color(255, 255, 255));
        addItemButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.createNewShape();
            }
        });

        CoolButton removeItemButton = new CoolButton(
                "Remove Shape",
                new Color(231, 76, 60),
                new Color(255, 255, 255));
        removeItemButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeShape();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(250, 0));
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 50));
        buttonPanel.add(addItemButton);
        buttonPanel.add(removeItemButton);

        capacityBar = new JProgressBar(0, 1000);
        capacityBar.setValue(0);
        capacityBar.setStringPainted(true);
        capacityBar.setPreferredSize(new Dimension(0, 70));
        capacityBar.setFont(new Font("Arial", Font.BOLD, 26));

        tableModel = new DefaultTableModel();
        backpackTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Type");
        tableModel.addColumn("Volume");

        backpackTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 24));
        backpackTable.setFont(defaultFont);
        backpackTable.setRowHeight(50);
        backpackTable.setDefaultEditor(Object.class, null);
        backpackTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel backpackPanel = new JPanel();
        backpackPanel.setLayout(new BorderLayout(0, 45));
        backpackPanel.add(capacityBar, BorderLayout.NORTH);
        backpackPanel.add(new JScrollPane(backpackTable), BorderLayout.CENTER);

        Container container = this.getContentPane();
        container.add(backpackPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.EAST);

        setJMenuBar(createMenuBar());
    }

    public void addShape(Shape shape, int index) {
        tableModel.addRow(new String[]{
                String.valueOf(index),
                shape.getName(),
                shape.getTypeName(),
                String.valueOf(shape.getVolume())});
    }

    private void removeShape() {
        if (backpackTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(
                    getContentPane(),
                    "Please, select a row to remove.",
                    "Cannot remove shape",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = backpackTable.getSelectedRow();
        int column = 0;
        for (; column < backpackTable.getColumnCount(); column++) {
            if (backpackTable.getColumnName(column).equals("ID")) {
                break;
            }
        }

        int index = Integer.parseInt((String) backpackTable.getValueAt(row, column));
        controller.removeShape(index);
    }

    public void clearBackpack() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    public void setCapacityValue(BigDecimal volume, BigDecimal capacity) {
        BigDecimal percent = volume.divide(capacity, RoundingMode.HALF_UP);
        float value = percent.floatValue();
        capacityBar.setValue(Math.round(value * capacityBar.getMaximum()));
        capacityBar.setString(
                percent.movePointRight(2).setScale(2, RoundingMode.FLOOR) + "% (" +
                        volume.setScale(2, RoundingMode.FLOOR) +
                        " out of " +
                        capacity.setScale(2, RoundingMode.FLOOR)
                        + ")"
        );
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createHelpMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        Font menuFont = new Font("Arial", Font.BOLD, 18);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Arial", Font.BOLD, 20));

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(menuFont);
        openItem.setIcon(new ImageIcon("res/images/open_folder.png"));

        JMenuItem xmlExportItem = new JMenuItem("Export as XML");
        xmlExportItem.setFont(menuFont);

        JMenu exportMenu = new JMenu("Export");
        exportMenu.setFont(menuFont);
        exportMenu.setIcon(new ImageIcon("res/images/save_file.png"));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(menuFont);

        openItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadBackpack();
            }
        });
        xmlExportItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportBackpackAsXML();
            }
        });
        exitItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        exportMenu.add(xmlExportItem);

        fileMenu.add(openItem);
        fileMenu.add(exportMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createHelpMenu() {
        Font menuFont = new Font("Arial", Font.BOLD, 18);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Arial", Font.BOLD, 20));

        JMenuItem openItem = new JMenuItem("About");
        openItem.setFont(menuFont);
        openItem.setIcon(new ImageIcon("res/images/about.png"));

        openItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Here is some text about the program.\n" +
                                "The latest version allows you to save\n" +
                                "backpack as XML-file and load it back\n" +
                                "(JSON requires some extra libraries :[ )",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpMenu.add(openItem);
        return helpMenu;
    }
}
