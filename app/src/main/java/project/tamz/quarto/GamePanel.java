package project.tamz.quarto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    
    private GameActivity gameActivity;
    private GameThread thread;
    
    private GameBoard gameBoard;
    private GaneStatusBar ganeStatusBar;
    private QuartoGameLogic quartoGameLogic;
    
    public GamePanel(Context ctx) {
        super(ctx);
    
        init();
    }
    
    public void init() {
        
        gameBoard = new GameBoard();
        ganeStatusBar = new GaneStatusBar();
        
        getHolder().addCallback(this);
        
        this.thread = new GameThread(getHolder(), this);
        quartoGameLogic = new QuartoGameLogic();
        setFocusable(true);
    }
    
    public GamePanel(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        
        init();
    }
    
    public void addGameActivity(GameActivity activity) {
        this.gameActivity = activity;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Clicked on x: " + event.getX() + " y: " + event.getY());
        
        return super.onTouchEvent(event);
    }
    
    public void update() {
    
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(MyColorPalette.PrimaryLight);
        
        ganeStatusBar.draw(canvas);
        gameBoard.draw(canvas);
        
        List<GameObject> availableObjects = quartoGameLogic.getAvailableGameObjects();
        
        float radius = gameBoard.getSmallCirclesRadius();
        List<List<Point>> gameBoardPlaces = gameBoard.getSmallCircles();
        int i = 0;
        for (int x = 0; x < gameBoardPlaces.size(); x++) {
            for (int y = 0; y < gameBoardPlaces.get(x).size(); y++) {
                availableObjects.get(i).setPosition(gameBoardPlaces.get(x).get(y));
                availableObjects.get(i).setBoardSphereSize(radius);
                i++;
            }
        }
        
        for (GameObject go : availableObjects)
            go.draw(canvas);
        
        
        if (gameActivity != null)
            gameActivity.editElapsedTime(getElapsed());
    }
    
    Time getElapsed() {
        return ganeStatusBar.getGameTime();
    }
}
