package fe.app.view;

import fe.app.model.tfmanagement.semaphore.Semaphore;
import fe.app.model.tfmanagement.semaphore.SemaphoreState;
import fe.app.model.elements.street.Street;
import fe.app.model.elements.map.StreetMap;
import fe.app.model.elements.vehicle.Vehicle;
import fe.app.util.Pair;
import fe.app.util.StreetType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Objects;

public class MapPanel extends JPanel {

    public static final int VEHICLE_WIDTH = 20;
    public static final int VEHICLE_HEIGHT = 10;
    private Dimension panelDimension;
    private int streetAngle;
    private ArrayList<Vehicle> vehicles;
    private StreetMap streetMap;

    public MapPanel(StreetMap streetMap, Dimension dimension) {
        setToolTipText("");
        this.streetMap = streetMap;
        this.panelDimension = dimension;
        streetMap.create();
        this.setPreferredSize(dimension);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0,0,this.getWidth(),this.getHeight());

        paintStreetMap(g2);

        synchronized (this){
            if (this.vehicles!=null){
                for (Vehicle v: new ArrayList<>(this.vehicles)) {
                    int x0 = v.getPosition().getX().intValue();
                    int y0 = v.getPosition().getY().intValue();

                    streetAngle = v.getStreet().getType().equals(StreetType.HORIZONTAL) ? 0 : 90;

                    final AffineTransform saved = g2.getTransform();
                    final AffineTransform rotate = AffineTransform.getRotateInstance(
                            Math.toRadians(streetAngle), x0, y0);
                    g2.transform(rotate);

                    Rectangle rect = new Rectangle(x0 - (VEHICLE_WIDTH/2),y0 - (VEHICLE_HEIGHT/2) ,
                            VEHICLE_WIDTH, VEHICLE_HEIGHT);

                    g2.setColor(Color.BLACK);

                    g2.draw(rect);
                    g2.setTransform(saved);

                }
            }
        }
    }
    @Override
    public String getToolTipText(MouseEvent e) {
        for (Semaphore semaphore: streetMap.getSemaphores()) {
            if (semaphore.getShape().getFirstSideShape().contains(e.getPoint())) {
                return semaphore.getId();
            }
            if (semaphore.getShape().getSecondSideShape().contains(e.getPoint())) {
                return semaphore.getId();
            }
        }
        return null;
    }

    public void updateVehiclesPosition(ArrayList<Vehicle> vehicles){
        synchronized(this){
            this.vehicles = vehicles;
        }
        repaint();
    }

    private void paintStreetMap(Graphics2D g2) {
        ArrayList<Street> streets = new ArrayList<>(streetMap.getHorizontalStreets());
        streets.addAll(streetMap.getVerticalStreets());

        for (Street street : streets) {
            g2.setColor(new Color(238,238,238));

            if (Objects.equals(street.getType(), StreetType.HORIZONTAL)) {
                g2.fillRect(street.getFirstSide().x1, street.getFirstSide().y1, this.getWidth(), Street.ROADWAY_SIZE);
            } else {
                g2.fillRect(street.getFirstSide().x1, street.getFirstSide().y1, Street.ROADWAY_SIZE, this.getHeight());

            }
            g2.setColor(Color.BLACK);

            street.getFirstSide().paint(g2);
            street.getSecondSide().paint(g2);

            if (Objects.equals(street.getType(), StreetType.HORIZONTAL)) {
                g2.drawString(street.getId(), street.getFirstSide().x1 + 10, street.getFirstSide().y1 - 10);
            } else {
                g2.drawString(street.getId(), street.getFirstSide().x1 - 20, street.getFirstSide().y1 + 20);
            }
        }

        for (Polygon intersection : streetMap.getStreetSidesIntersections()) {
            g2.setColor(new Color(238,238,238));
            g2.drawPolygon(intersection);
        }

        for (Semaphore semaphore : streetMap.getSemaphores()) {
            if (semaphore.getCurrentState() == SemaphoreState.RED) g2.setColor(Color.RED);
            if (semaphore.getCurrentState() == SemaphoreState.GREEN) g2.setColor(Color.GREEN);
            if (semaphore.getCurrentState() == SemaphoreState.YELLOW) g2.setColor(Color.ORANGE);

            g2.fill(semaphore.getShape().getFirstSideShape());
            g2.fill(semaphore.getShape().getSecondSideShape());
        }
    }
}
