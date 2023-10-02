package fe.app.view;

import fe.app.model.elements.Intersection;
import fe.app.model.elements.Street;
import fe.app.model.elements.StreetMap;
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
    private StreetMap streetMap;

    public MapPanel(StreetMap streetMap) {
        this.streetMap = streetMap;
        streetMap.create();
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
        }
        paintStreetMap(g2);
    }

    public void updateVehiclesPosition(ArrayList<Pair<Double, Double>> positions){
        synchronized(this){
            this.positions = positions;
        }
        System.out.println(this.positions);
        repaint();
    }

    private void paintStreetMap(Graphics2D g2) {
        ArrayList<Street> prova = new ArrayList<>(streetMap.getHorizontalStreets());
        prova.addAll(streetMap.getVerticalStreets());

        for (Street street : prova) {
            street.getFirstSide().paint(g2);
            street.getSecondSide().paint(g2);
        }

        for (Street street : prova) {
            g2.setColor(Color.BLUE);
            street.getRightWay().paint(g2);
            g2.setColor(Color.CYAN);
            street.getLeftWay().paint(g2);
        }

        for (Polygon intersection : streetMap.getStreetSidesIntersections()) {
            g2.setColor(g2.getBackground());
            g2.drawPolygon(intersection);
        }
        for (Intersection intersection: streetMap.getIntersections()) {
            g2.setColor(Color.RED);

            Integer x1 = intersection.getIntersectionPoint().getX();
            Integer y1 = intersection.getIntersectionPoint().getY();

            g2.drawLine(x1,y1,x1+3,y1+3);
        }
    }
}
