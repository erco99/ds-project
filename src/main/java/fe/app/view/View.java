package fe.app.view;

import fe.app.controller.Controller;
import fe.app.model.elements.map.StreetMap;

import javax.swing.*;
import java.awt.*;

public class View {

    private MapPanel mapPanel;
    private ControlsPanel controlsPanel;
    private Controller controller;
    static JFrame frame;

    public View() {
    }

    public void start(StreetMap streetMap) {
        frame = new JFrame("App");
        frame.setLayout(new BorderLayout());

        mapPanel = new MapPanel(streetMap);
        frame.add(mapPanel, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel(this.controller);
        frame.add(controlsPanel, BorderLayout.EAST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public void getController(Controller controller) {
        this.controller = controller;
    }
}
