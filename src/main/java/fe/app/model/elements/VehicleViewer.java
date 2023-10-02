package fe.app.model.elements;

import fe.app.view.MapPanel;

public class VehicleViewer extends Thread {

    private static final int FRAMES_PER_SEC = 30;
    private boolean stop;
    private MapPanel mapPanel;
    private MapContext mapContext;

    public VehicleViewer(MapPanel mapPanel, MapContext mapContext) {
        this.stop = false;
        this.mapPanel = mapPanel;
        this.mapContext = mapContext;
    }

    public void run() {
        while (!stop) {
            long t0 = System.currentTimeMillis();
            mapPanel.updateVehiclesPosition(mapContext.getVehicles());
            long t1 = System.currentTimeMillis();
            //log("update pos");
            long dt = (1000 / FRAMES_PER_SEC) - (t1-t0);
            if (dt > 0){
                try {
                    Thread.sleep(dt);
                } catch (Exception ex){
                }
            }
        }
    }
}
