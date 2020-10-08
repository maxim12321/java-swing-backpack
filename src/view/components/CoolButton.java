package view.components;

import javax.swing.*;
import java.awt.*;

public class CoolButton extends JButton {
    private final Color defaultColor;

    public CoolButton(String text, Color defaultColor, Color textColor) {
        super(text);
        setContentAreaFilled(false);

        this.defaultColor = defaultColor;
        this.setForeground(textColor);

        this.setFont(new Font("Arial", Font.BOLD, 26));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(defaultColor);
        g2.setStroke(new BasicStroke(10));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);

        if (model.isPressed()) {
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else if (model.isRollover()) {
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.dispose();
    }
}
