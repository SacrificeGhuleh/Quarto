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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //System.out.println("Clicked on x: " + event.getX() + " y: " + event.getY());
                PointF clickedPos = new PointF(event.getX(), event.getY());
                invalidate();
            
                GameObject selected = null;
            
                List<GameObject> availableObjects = quartoGameLogic.getAvailableGameObjects();
            
                float distance = 0.f;
                float dx = 0.f;
                float dy = 0.f;
                float fingerRadius = 20.f;
                float objectRadius = 0.f;
                for (GameObject go : availableObjects) {
                    if (go.getPosition() != null) {
                        PointF pos = go.getPosition();
                    
                        dx = pos.x - clickedPos.x;
                        dy = pos.y - clickedPos.y;
                        distance = (float) Math.sqrt(dx * dx + dy * dy);
                        //Log.d(TAG, "Distance : " + distance);
                    
                        objectRadius = go.getBoardSphereSize();
                    
                        if (distance <= objectRadius + fingerRadius) {
                            Log.d(TAG, "Collision");
                            if (selected == null)
                                selected = go;
                        
                            else {
                            
                                float selectedDx = selected.getPosition().x - clickedPos.x;
                                float selectedDy = selected.getPosition().y - clickedPos.y;
                            
                                float selectedDistance = (float) Math.sqrt(selectedDx * selectedDx + selectedDy * selectedDy);
                            
                                if (selectedDistance > distance) {
                                    selected = go;
                                }
                            }
                        }
                    }
                }
            
                if (selected != null) {
                    return quartoGameLogic.setSelectedObject(selected);
                }
        
        }
        return false;
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
        drawSelectedObject(canvas);
        drawAvailableObjects(canvas);
    
        if (gameActivity != null)
            gameActivity.editElapsedTime(getElapsed());
    }
    
    public void drawSelectedObject(Canvas canvas) {
        GameObject selectedObject = quartoGameLogic.getSelectedObject();
        if (selectedObject == null) return;
        float radius = gameAvailableBoard.getCellWidth() / 2.f;
        selectedObject.setBoardSphereSize(radius);
        selectedObject.setPosition(gameAvailableBoard.getSelectedPos());
        selectedObject.draw(canvas);
    }
    
    public void drawAvailableObjects(Canvas canvas) {
        //List<GameObject> availableObjects = quartoGameLogic.getAvailableGameObjects();
        float radius = gameAvailableBoard.getCellWidth() / 2.f;
        
        List<List<PointF>> availablePlaces = gameAvailableBoard.getAvailablePositions();
        
        int availableCunt = quartoGameLogic.getAvailableGameObjects().size();
        
        int i = 0;
        for (int y = 0; y < availablePlaces.size(); y++) {
            for (int x = 0; x < availablePlaces.get(y).size(); x++) {
                if (i >= availableCunt) break;
                quartoGameLogic.getAvailableGameObjects().get(i).setPosition(availablePlaces.get(y).get(x));
                quartoGameLogic.getAvailableGameObjects().get(i).setBoardSphereSize(radius);
                quartoGameLogic.getAvailableGameObjects().get(i).draw(canvas);
                i++;
            }
        }
    }
    
    private Time getElapsed() {
        return ganeStatusBar.getGameTime();
    }
}
