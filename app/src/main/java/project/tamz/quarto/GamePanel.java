package project.tamz.quarto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
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
        Log.d(TAG, "surfaceCreated");
        this.thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged format: " + format + " width " + width + " height " + height);
        
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        thread.setRunning(false);
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (! thread.isAlive()) return super.onTouchEvent(event);
        if (quartoGameLogic.isAIMove())
            return super.onTouchEvent(event); //disable input when AI move
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
    
                //System.out.println("Clicked on x: " + event.getX() + " y: " + event.getY());
                PointF clickedPos = new PointF(event.getX(), event.getY());
                invalidate();
    
                if (quartoGameLogic.isSelecting()) {
                    return handleSelecting(clickedPos);
                }
    
                if (quartoGameLogic.isPlacing()) {
                    return handlePlacing(clickedPos);
                }
    
        }
        return false;
    }
    
    private boolean handleSelecting(PointF clickedPos) {
        
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
        return false;
    }
    
    private boolean handlePlacing(PointF clickedPos) {
    
        Point coords = null;
        List<List<PointF>> placeCoords = gameBoard.getSmallCircles();
        
        float distance = 0.f;
        float dx = 0.f;
        float dy = 0.f;
        float fingerRadius = 20.f;
        float objectRadius = 0.f;
        
        if (placeCoords == null) return false;
    
        for (int row = 0; row < placeCoords.size(); row++) {
            for (int col = 0; col < placeCoords.get(row).size(); col++) {
                PointF pos = placeCoords.get(row).get(col);
                if (pos != null) {
                    dx = pos.x - clickedPos.x;
                    dy = pos.y - clickedPos.y;
                    distance = (float) Math.sqrt(dx * dx + dy * dy);
                    
                    objectRadius = gameBoard.getSmallCirclesRadius();
                    
                    if (distance <= objectRadius + fingerRadius) {
                        Log.d(TAG, "Collision");
                        
                        if (coords == null) {
                            coords = new Point(row, col);
                        } else {
                            PointF prevPos = placeCoords.get(coords.y).get(coords.x);
                            
                            float prevDx = prevPos.x - clickedPos.x;
                            float prevDy = prevPos.y - clickedPos.y;
                            float prevDistance = (float) Math.sqrt(prevDx * prevDx + prevDy * prevDy);
                            
                            if (prevDistance > distance) {
                                coords = new Point(row, col);
                            }
                        }
                    }
                }
            }
        }
        
        if (coords != null) {
            Log.d(TAG, "selected:  " + coords.toString());
        }
        
        return quartoGameLogic.place(coords);
    }
    
    public void checkGameEnd(Canvas canvas) {
        if (quartoGameLogic.isGameEnd()) {
            Log.d("Game panel", "End of game");
            Log.d("Game panel", "Player won: " + quartoGameLogic.isAIWon());
            
            gameBoard.setHighLights(quartoGameLogic.getCommonHighlights());
            draw(canvas);
            gameActivity.endOfGame(canvas);
            drawGameEndPanel(canvas);
            thread.setRunning(false);
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    
        canvas.drawColor(MyColorPalette.PrimaryLight);
        gameBoard.draw(canvas);
        gameAvailableBoard.draw(canvas);
        drawSelectedObject(canvas);
        drawAvailableObjects(canvas);
        drawPlacedObjects(canvas);
    }
    
    public void drawGameEndPanel(Canvas canvas) {
        GameEndInfo gameEndInfo = new GameEndInfo(quartoGameLogic.getGameEndType());
        
        gameEndInfo.draw(canvas);
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
        List<GameObject> availableObjects = quartoGameLogic.getAvailableGameObjects();
        float radius = gameAvailableBoard.getCellWidth() / 2.f;
        
        List<List<PointF>> availablePlaces = gameAvailableBoard.getAvailablePositions();
    
        int availableCunt = availableObjects.size();
    
        int index = 0;
        for (int row = 0; row < availablePlaces.size(); row++) {
            for (int col = 0; col < availablePlaces.get(row).size(); col++) {
                if (index >= availableCunt) return;
                GameObject go = availableObjects.get(index);
                if (go == null) {
                    Log.d(TAG, "Warning, Game object in drawAvailableObjects is null");
    
                }
                PointF pos = availablePlaces.get(row).get(col);
                if (pos == null) {
                    Log.d(TAG, "Warning, position in drawAvailableObjects is null");
                }
                go.setPosition(pos);
                go.setBoardSphereSize(radius);
                go.draw(canvas);
                index++;
            }
        }
    }
    
    private void drawPlacedObjects(Canvas canvas) {
        GameObject objects[][] = quartoGameLogic.getPlacedObjects();
        if (objects == null) return;
        float radius = gameBoard.getSmallCirclesRadius();
        for (int row = 0; row < objects.length; row++) {
            for (int col = 0; col < objects[row].length; col++) {
                GameObject o = objects[row][col];
                if (o == null) continue;
                
                o.setPosition(gameBoard.getSmallCircles().get(row).get(col));
                o.setBoardSphereSize(radius);
                o.draw(canvas);
            }
        }
        
    }
    
    public void update() {
        if (quartoGameLogic.isAIMove())
            quartoGameLogic.AIAction();
        
        if (gameActivity != null)
            gameActivity.editElapsedTime(getElapsed());
        
        ganeStatusBar.update();
        
        gameBoard.setHighLights(quartoGameLogic.getCommonHighlights());
        
    }
    
    private Time getElapsed() {
        return ganeStatusBar.getGameTime();
    }
}
