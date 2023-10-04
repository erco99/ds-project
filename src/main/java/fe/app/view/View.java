package fe.app.view;

import fe.app.model.elements.StreetMap;

import javax.swing.*;
import java.awt.*;

public class View {

    MapPanel mapPanel;
    ControlsPanel controlsPanel;

    static JFrame frame;

    public void start(StreetMap streetMap) {
        frame = new JFrame("App");
        frame.setLayout(new BorderLayout());

        mapPanel = new MapPanel(streetMap);
        frame.add(mapPanel, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel();
        frame.add(controlsPanel, BorderLayout.EAST);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

}
