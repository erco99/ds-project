package fe.app.view;

import fe.app.controller.Controller;
import fe.app.model.elements.intersection.SensorsIntersection;
import fe.app.model.elements.map.Sensor;
import fe.app.model.tfmanagement.semaphore.Semaphore;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControlsPanel extends JPanel implements ActionListener {

    private final JButton startButton;
    private final JButton stopButton;
    private final JButton serverStatusButton;
    private final DefaultTableModel sensorsTableModel;
    private final JTable sensorsTable;
    private final DefaultTableModel timingsTableModel;
    private final JTable timingsTable;
    private final JSpinner vehiclesNumberSpinner;
    private final Controller controller;
    private Dimension panelDimension;
    private Map<String,Double> timings = new HashMap<>();

    public ControlsPanel(Controller controller, Dimension dimension) {
        this.controller = controller;
        this.panelDimension = dimension;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(dimension);
        this.setBorder(new MatteBorder(0,1,0,0, Color.BLACK));

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        this.add(new JLabel("Set number of vehicles:"));

        SpinnerModel value = new SpinnerNumberModel(10,1,30,1);
        this.vehiclesNumberSpinner = new JSpinner(value);
        this.vehiclesNumberSpinner.setSize(30,100);
        this.add(this.vehiclesNumberSpinner);

        this.startButton = new JButton("START");
        this.stopButton = new JButton("STOP");
        this.stopButton.setEnabled(false);

        this.add(startButton);
        this.add(stopButton);

        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        this.serverStatusButton = new JButton("Server");
        this.add(serverStatusButton);
        this.serverStatusButton.addActionListener(this);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel sensorsTableLabel = new JLabel("Sensors data:");
        this.add(sensorsTableLabel);

        String[] columnNames = {"Street", "Semaphore", "Vehicles"};
        this.sensorsTableModel = new DefaultTableModel(columnNames, 0);
        this.sensorsTable = new JTable(sensorsTableModel);

        JScrollPane scrollPane = new JScrollPane(sensorsTable);
        this.add(scrollPane);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel timingsTableLabel = new JLabel("Semaphores label:");
        this.add(timingsTableLabel);

        columnNames = new String[]{"Semaphore", "Green", "Red"};
        this.timingsTableModel = new DefaultTableModel(columnNames, 0);
        this.timingsTable = new JTable(timingsTableModel);

        JScrollPane scrollPaneTimings = new JScrollPane(timingsTable);
        this.add(scrollPaneTimings);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.startButton) {
            this.startButton.setEnabled(false);
            this.stopButton.setEnabled(true);
            this.controller.startTraffic((Integer) vehiclesNumberSpinner.getValue());
        }
        if (e.getSource() == this.stopButton) {
            this.stopButton.setEnabled(false);
            this.startButton.setEnabled(true);
            this.controller.stopTraffic();
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
                this.addSensorsTableRow(sensorsIntersection.getHorizontalStreetSensor());
                this.addSensorsTableRow(sensorsIntersection.getVerticalStreetSensor());
            }
        });
    }

    private void addSensorsTableRow(Sensor sensor) {
        String idStreet = sensor.getStreet().getId();
        String idSem = sensor.getSemaphore().getId();
        int vehicles = sensor.getVehiclesNumber();

        this.sensorsTableModel.addRow(new Object[]{idStreet, idSem, vehicles});
    }

    public void updateTimingsTable(Map<String, Double> timeMap) {
        timings.putAll(timeMap);
        SwingUtilities.invokeLater(() -> {
            this.timingsTableModel.getDataVector().removeAllElements();
            this.timingsTableModel.fireTableDataChanged();
            for (Map.Entry<String,Double> timing : timings.entrySet()) {
                if (timing.getValue() != null) {
                    Double greenTime = timing.getValue();
                    this.timingsTableModel.addRow(new Object[]{timing.getKey(), greenTime, Semaphore.CYCLE_TIME - greenTime});
                }
            }
        });
    }
}
