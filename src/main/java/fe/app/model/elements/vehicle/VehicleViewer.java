package fe.app.model.elements.vehicle;

import fe.app.model.elements.map.MapContext;
import fe.app.view.MapPanel;

public class VehicleViewer extends Thread {

    private static final int FRAMES_PER_SEC = 30;
    private final MapPanel mapPanel;
    private final MapContext mapContext;

    public VehicleViewer(MapPanel mapPanel, MapContext mapContext) {
        this.mapPanel = mapPanel;
        this.mapContext = mapContext;
    }

    public void run() {
        while (true) {
            long t0 = System.currentTimeMillis();
            mapPanel.updateVehiclesPosition(mapContext.getVehicles());
            long t1 = System.currentTimeMillis();
            //log("update pos");
            long dt = (1000 / FRAMES_PER_SEC) - (t1-t0);
            if (dt > 0){
                try {
                    //noinspection BusyWait
                    Thread.sleep(dt);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
