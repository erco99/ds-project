package fe.app.view;

import javax.swing.*;
import java.awt.*;

public class View {

    MapPanel mapPanel;
    static JFrame frame;

    public void start() {
        frame = new JFrame("App");
        frame.setLayout(new BorderLayout());

        mapPanel = new MapPanel();
        frame.add(mapPanel);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public MapPanel getMapPanel()    {
        return mapPanel;
    }

}
