package fe.app.view;

import fe.app.controller.Controller;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlsPanel extends JPanel implements ActionListener {

    private final JButton addVehicleButton;
    private final JButton turnGreenButton;
    private final JButton serverStatusButton;

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

        this.turnGreenButton = new JButton("turn green");
        this.add(turnGreenButton);
        this.turnGreenButton.addActionListener(this);

        this.serverStatusButton = new JButton("Server");
        this.add(serverStatusButton);
        this.serverStatusButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addVehicleButton) {
            this.controller.addVehicle();
        }
        if (e.getSource() == this.turnGreenButton) {
            this.controller.turnGreen();
        }
    }

    public void changeServerStatus(boolean status) {
        if (status) {
            this.serverStatusButton.setBackground(Color.GREEN);
        }  else {
            this.serverStatusButton.setBackground(Color.RED);
        }
    }
}
