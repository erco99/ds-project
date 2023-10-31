package fe.app.view;

import fe.app.controller.Controller;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ControlsPanel extends JPanel implements ActionListener {

    private final JButton addVehicleButton;
    private final JButton turnGreenButton;
    private final JButton serverStatusButton;
    private final DefaultTableModel sensorsTableModel;
    private final JTable sensorsTable;
    private final Controller controller;
    private Dimension panelDimension;

    public ControlsPanel(Controller controller, Dimension dimension) {
        this.controller = controller;
        this.panelDimension = dimension;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(dimension);
        this.setBorder(new MatteBorder(0,1,0,0, Color.BLACK));

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        this.addVehicleButton = new JButton("add vehicle");
        this.add(addVehicleButton);
        this.addVehicleButton.addActionListener(this);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        this.turnGreenButton = new JButton("turn green");
        this.add(turnGreenButton);
        this.turnGreenButton.addActionListener(this);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        this.serverStatusButton = new JButton("Server");
        this.add(serverStatusButton);
        this.serverStatusButton.addActionListener(this);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel tableLabel = new JLabel("Sensors data:");
        this.add(tableLabel);

        String[] columnNames = {"Street", "Semaphore", "Vehicles"};
        this.sensorsTableModel = new DefaultTableModel(columnNames, 0);
        this.sensorsTable = new JTable(sensorsTableModel);

        JScrollPane scrollPane = new JScrollPane(sensorsTable);
        this.add(scrollPane);
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

    public void setSensorsLabel(ArrayList<SensorsIntersection> sensorsIntersections) {
        SwingUtilities.invokeLater(() -> {
            this.sensorsTableModel.getDataVector().removeAllElements();
            this.sensorsTableModel.fireTableDataChanged();
            for (SensorsIntersection sensorsIntersection : sensorsIntersections) {
                this.addTableRow(sensorsIntersection.getHorizontalStreetSensor());
                this.addTableRow(sensorsIntersection.getVerticalStreetSensor());
            }
        });
    }

    private void addTableRow(Sensor sensor) {
        String idStreet = sensor.getStreet().getId();
        String idSem = sensor.getSemaphore().getId();
        int vehicles = sensor.getVehiclesNumber();

        this.sensorsTableModel.addRow(new Object[]{idStreet, idSem, vehicles});
    }

}
