package fe.app.view;

import fe.app.controller.Controller;
import fe.app.controller.NetworkController;
import fe.app.model.elements.map.MapDimension;
import fe.app.model.elements.map.StreetMap;

import javax.swing.*;
import java.awt.*;

public class View {

    private MapPanel mapPanel;
    private ControlsPanel controlsPanel;
    private Controller controller;
    private NetworkController networkController;
    private static final int CONTROLS_PANEL_WIDTH = 300;
    static JFrame frame;

    public View() {
    }

    public void start(StreetMap streetMap) {
        frame = new JFrame("App");
        frame.setLayout(new BorderLayout());

        frame.setBackground(new Color(205, 210, 214));

        mapPanel = new MapPanel(streetMap, new Dimension(MapDimension.MAP_WIDTH, MapDimension.MAP_HEIGHT));
        frame.add(mapPanel, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel(this.controller, new Dimension(CONTROLS_PANEL_WIDTH, MapDimension.MAP_HEIGHT));
        frame.add(controlsPanel, BorderLayout.EAST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public ControlsPanel getControlsPanel() {
        return controlsPanel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setNetworkController(NetworkController networkController) {
        this.networkController = networkController;
    }
}
