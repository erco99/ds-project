package fe.app.model.tfmanagement.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerView implements ActionListener {

    static JFrame frame;
    static JPanel panel;
    private final Server server;
    private JButton serverType;
    private JButton crashButton;
    private JButton stateButton;
    private JButton restartButton;

    public ServerView(Server server) {
        this.server = server;
    }

    public void start() {
        frame = new JFrame("App");

        panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        panel.setPreferredSize(new Dimension(600,200));
        frame.add(panel);

        serverType = new JButton("");
        panel.add(serverType);

        stateButton = new JButton("UP");
        panel.add(stateButton);
        stateButton.setBackground(Color.GREEN);

        crashButton = new JButton("CRASH");
        panel.add(crashButton);
        crashButton.addActionListener(this);

        restartButton = new JButton("RESTART");
        panel.add(restartButton);
        restartButton.addActionListener(this);
        this.restartButton.setEnabled(false);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.crashButton) {
            this.server.crash();
            this.stateButton.setBackground(Color.RED);
            this.stateButton.setText("DOWN");
            this.serverType.setText("");
            this.restartButton.setEnabled(true);
            this.crashButton.setEnabled(false);
        }
        if(e.getSource() == this.restartButton) {
            this.server.restart();
            this.restartButton.setEnabled(false);
            this.crashButton.setEnabled(true);
            this.stateButton.setBackground(Color.GREEN);
            this.stateButton.setText("UP");
        }
   }

   public void setServerType(String serverType) {
        this.serverType.setText(serverType);
   }
}
