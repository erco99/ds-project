package fe.app.view;

import fe.app.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    private ArrayList<Pair<Double, Double>> positions;
    private static final int WIDTH_DIMENSION = 1200;
    private static final int HEIGHT_DIMENSION = 1000;
    private long dx;
    private long dy;
    public MapPanel() {
        this.setPreferredSize(new Dimension(WIDTH_DIMENSION,HEIGHT_DIMENSION));
        dx = WIDTH_DIMENSION/2 - 20;
        dy = HEIGHT_DIMENSION/2 - 20;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0,0,this.getWidth(),this.getHeight());
        synchronized (this){
            if (positions!=null){
                positions.forEach(p -> {
                    int x0 = (int)(dx+p.getX()*dx);
                    int y0 = (int)(dy-p.getY()*dy);


                    final AffineTransform saved = g2.getTransform();
                    final AffineTransform rotate = AffineTransform.getRotateInstance(
                            Math.toRadians(360), x0, y0);
                    g2.transform(rotate);

                    Rectangle rect = new Rectangle(x0,y0,20,10);
                    g2.draw(rect);
                    g2.setTransform(saved);
                });
            }

            g2.drawLine(30,30,60,60);
            g2.drawLine(30,40,60,70);
        }
    }

    public void updateVehiclesPosition(ArrayList<Pair<Double, Double>> positions){
        synchronized(this){
            this.positions = positions;
        }
        System.out.println(this.positions);
        repaint(new Rectangle(30,30,90,90));
    }
}
