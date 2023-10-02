package fe.app.view;

import fe.app.model.elements.Intersection;
import fe.app.model.elements.Street;
import fe.app.model.elements.StreetMap;
import fe.app.model.elements.Vehicle;
import fe.app.util.Pair;
import fe.app.util.StreetType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    public static final int VEHICLE_WIDTH = 20;
    public static final int VEHICLE_HEIGHT = 10;
    private static final int WIDTH_DIMENSION = 1200;
    private static final int HEIGHT_DIMENSION = 1000;
    private int streetAngle;
    private ArrayList<Vehicle> vehicles;
    private StreetMap streetMap;

    public MapPanel(StreetMap streetMap) {
        this.streetMap = streetMap;
        streetMap.create();
        this.setPreferredSize(new Dimension(WIDTH_DIMENSION,HEIGHT_DIMENSION));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0,0,this.getWidth(),this.getHeight());

        synchronized (this){
            if (this.vehicles!=null){
                this.vehicles.forEach(v -> {
                    int x0 = v.getPosition().getX().intValue();
                    int y0 = v.getPosition().getY().intValue();

                    streetAngle = v.getStreet().getType().equals("HORIZONTAL") ? 0 : 90;

                    final AffineTransform saved = g2.getTransform();
                    final AffineTransform rotate = AffineTransform.getRotateInstance(
                            Math.toRadians(streetAngle), x0, y0);
                    g2.transform(rotate);

                    Rectangle rect = new Rectangle(x0 - (VEHICLE_WIDTH/2),y0 - (VEHICLE_HEIGHT/2) ,
                            VEHICLE_WIDTH, VEHICLE_HEIGHT);
                    g2.draw(rect);
                    g2.setTransform(saved);

                });
            }
        }
        paintStreetMap(g2);
    }

    public void updateVehiclesPosition(ArrayList<Vehicle> vehicles){
        synchronized(this){
            this.vehicles = vehicles;
        }
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
