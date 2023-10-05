package fe.app.model.elements.street;

import java.awt.*;

public class Line {

    public final int x1;
    public final int x2;
    public final int y1;
    public final int y2;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    public void paint(Graphics g) {
        g.drawLine(this.x1, this.y1, this.x2, this.y2);
    }
}
