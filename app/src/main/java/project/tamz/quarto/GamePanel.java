package project.tamz.quarto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    
    private GameActivity gameActivity;
    private GameThread thread;
    
    private GameBoard gameBoard;
    private GaneStatusBar ganeStatusBar;
    private QuartoGameLogic quartoGameLogic;
    private GameAvailableBoard gameAvailableBoard;
    
    public GamePanel(Context ctx) {
        super(ctx);
    
        init();
    }
    
    private void init() {
        gameBoard = new GameBoard();
        ganeStatusBar = new GaneStatusBar();
        gameAvailableBoard = new GameAvailableBoard();
        
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
        gameAvailableBoard.draw(canvas);
        
        List<GameObject> availableObjects = quartoGameLogic.getAvailableGameObjects();
        List<GameObject> availableObjects2 = availableObjects;
        
        float radius = gameBoard.getSmallCirclesRadius();
        List<List<PointF>> gameBoardPlaces = gameBoard.getSmallCircles();
        int i = 0;
        for (int y = 0; y < gameBoardPlaces.size(); y++) {
            for (int x = 0; x < gameBoardPlaces.get(y).size(); x++) {
                availableObjects.get(i).setPosition(gameBoardPlaces.get(y).get(x));
                availableObjects.get(i).setBoardSphereSize(radius);
                i++;
            }
        }
        i = 0;
        
        for (GameObject go : availableObjects)
            go.draw(canvas);
    
        float radius2 = gameAvailableBoard.getCellWidth();
        List<List<PointF>> availablePlaces = gameAvailableBoard.getAvailablePositions();
    
        for (int y = 0; y < availablePlaces.size(); y++) {
            for (int x = 0; x < availablePlaces.get(y).size(); x++) {
                availableObjects2.get(i).setPosition(
                        new PointF(
                                availablePlaces.get(y).get(x).x,
                                availablePlaces.get(y).get(x).y
                        ));
                availableObjects2.get(i).setBoardSphereSize(radius2);
                i++;
            }
        }
    
        for (GameObject go : availableObjects2) {
            go.draw(canvas);
            Log.d(TAG, "draw: " + go.toString());
        }
        
        if (gameActivity != null)
            gameActivity.editElapsedTime(getElapsed());
    }
    
    private Time getElapsed() {
        return ganeStatusBar.getGameTime();
    }
}
