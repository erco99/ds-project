package fe.app.view;

import fe.app.model.elements.StreetMap;

import javax.swing.*;
import java.awt.*;

public class View {

    MapPanel mapPanel;
    static JFrame frame;

    public void start(StreetMap streetMap) {
        frame = new JFrame("App");
        frame.setLayout(new BorderLayout());

        mapPanel = new MapPanel(streetMap);
        frame.add(mapPanel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

}
