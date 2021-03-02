package view;

import controller.Controller;
import exceptions.IncorrectParametersException;
import shapes.*;
import shapes.Shape;
import view.components.CoolButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class CreateShapeWindow extends JFrame {
    private final Map<String, Shape> shapeTypes;

    private final JTextField nameTextField;
    private final JComboBox shapeTypeBox;

    private final Font defaultFont = new Font("Arial", Font.PLAIN, 26);
    private final GridBagConstraints layoutConstraints;

    private final List<JLabel> loadableFieldLabels = new ArrayList<>();
    private final List<JTextField> loadableFieldValues = new ArrayList<>();

    private final Controller controller;

    public CreateShapeWindow(Controller controller) {
        super("Create new shape");
        this.setSize(600, 500);
        this.setMinimumSize(new Dimension(400, 500));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.controller = controller;

        shapeTypes = new HashMap<>();
        shapeTypes.put("Cube", new Cube());
        shapeTypes.put("Parallelepiped", new Parallelepiped());
        shapeTypes.put("Ball", new Ball());

        nameTextField = new JTextField(10);
        nameTextField.setFont(defaultFont);

        shapeTypeBox = new JComboBox(shapeTypes.keySet().toArray());
        shapeTypeBox.setFont(defaultFont);
        shapeTypeBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpLoadableFields();
            }
        });

        CoolButton createButton = new CoolButton(
                "Create",
                new Color(155, 89, 182),
                new Color(255, 255, 255));
        createButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewShape();
            }
        });

        CoolButton cancelButton = new CoolButton(
                "Cancel",
                new Color(149, 165, 166),
                new Color(255, 255, 255));
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelCreatingShape();
            }
        });

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(defaultFont);
        JLabel shapeTypeLabel = new JLabel("Shape Type");
        shapeTypeLabel.setFont(defaultFont);

        Container container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        layoutConstraints = new GridBagConstraints();

        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        layoutConstraints.weightx = 0.5;
        layoutConstraints.ipady = 20;

        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridy = 0;
        layoutConstraints.gridx = 0;
        container.add(nameLabel, layoutConstraints);

        layoutConstraints.gridx = 1;
        container.add(nameTextField, layoutConstraints);

        layoutConstraints.gridy = 1;
        layoutConstraints.insets = new Insets(0, 0, 30, 0);
        layoutConstraints.gridx = 0;
        container.add(shapeTypeLabel, layoutConstraints);

        layoutConstraints.gridx = 1;
        container.add(shapeTypeBox, layoutConstraints);

        layoutConstraints.insets = new Insets(30, 0, 0, 0);
        layoutConstraints.gridy = 100;
        layoutConstraints.gridx = 0;
        container.add(createButton, layoutConstraints);

        layoutConstraints.gridx = 1;
        container.add(cancelButton, layoutConstraints);

        layoutConstraints.gridy = 2;
        layoutConstraints.insets = new Insets(0, 0, 0, 0);
        setUpLoadableFields();
    }

    private void createNewShape() {
        Shape loadable = shapeTypes.get((String) shapeTypeBox.getSelectedItem());
        int parametersCount = loadable.getParametersList().length;
        BigDecimal[] parameters = new BigDecimal[parametersCount];

        if (nameTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "'Name' field cannot be empty. Please, enter shape name",
                    "Incorrect shape name",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadable.setName(nameTextField.getText());

        int index = 0;
        try {
            for (; index < parametersCount; index++) {
                parameters[index] = BigDecimal.valueOf(Double.parseDouble(loadableFieldValues.get(index).getText()));
                if (parameters[index].compareTo(BigDecimal.ZERO) < 0) {
                    throw new IncorrectParametersException();
                }
            }
            loadable.load(parameters);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Value '" + loadableFieldLabels.get(index).getText() + "' is filled incorrectly. " +
                            "Please, enter positive double values for shape parameters",
                    "Incorrect parameter values",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        controller.addShape(loadable);
        this.dispose();
    }

    private void cancelCreatingShape() {
        this.dispose();
    }

    private void setUpLoadableFields() {
        Shape loadable = shapeTypes.get((String) shapeTypeBox.getSelectedItem());
        if (loadable == null) {
            return;
        }
        Container container = this.getContentPane();
        layoutConstraints.gridy -= loadableFieldLabels.size() / 2;

        for (Component component : loadableFieldLabels) {
            container.remove(component);
        }
        for (Component component : loadableFieldValues) {
            container.remove(component);
        }
        loadableFieldLabels.clear();
        loadableFieldValues.clear();

        String[] fieldNames = loadable.getParametersList();
        for (String parameter : fieldNames) {
            JLabel label = new JLabel(parameter);
            label.setFont(defaultFont);
            loadableFieldLabels.add(label);

            JTextField textField = new JTextField();
            textField.setFont(defaultFont);
            loadableFieldValues.add(textField);

            layoutConstraints.gridx = 0;
            container.add(label, layoutConstraints);
            layoutConstraints.gridx = 1;
            container.add(textField, layoutConstraints);

            layoutConstraints.gridy++;
        }

        this.validate();
        this.repaint();
    }
}
