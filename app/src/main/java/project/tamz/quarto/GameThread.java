package project.tamz.quarto;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

class GameThread extends Thread {
    private static final int FPS_LOCK = 30;
    private static Canvas canvas;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    
    public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }
    
    public void setRunning(boolean running) {
        this.running = running;
    }
    
    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000 / FPS_LOCK;
        long waitTime = 0;
        int frameCunt = 0;
        long totalTime = 0;
        long targetTime = 1000 / FPS_LOCK;
        
        while (this.running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                    this.gamePanel.checkGameEnd();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            totalTime += System.nanoTime() - startTime;
            frameCunt++;
            if (frameCunt == FPS_LOCK) {
                avgFPS = 1000 / ((totalTime / frameCunt) / 1000000);
                frameCunt = 0;
                totalTime = 0;
                //System.out.println("FPS: " + avgFPS);
            }
        }
    }
}
