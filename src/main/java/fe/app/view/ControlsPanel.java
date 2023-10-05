package fe.app.view;

import fe.app.controller.Controller;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlsPanel extends JPanel implements ActionListener {

    private final JButton addVehicleButton;
    private final Controller controller;
    private Dimension panelDimension;

    public ControlsPanel(Controller controller, Dimension dimension) {
        this.controller = controller;
        this.panelDimension = dimension;

        this.setPreferredSize(dimension);
        this.setBorder( new MatteBorder(0,1,0,0, Color.BLACK));

        this.addVehicleButton = new JButton("add vehicle");
        this.add(addVehicleButton);
        this.addVehicleButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addVehicleButton) {
            this.controller.addVehicle();
        }
    }
}
