package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class GameBoard implements GameEntity {
    
    public float smallCirclesRadius;
    private Size size;
    private int squareSize;
    private Paint gameBoardBackgroundPaint;
    private Rect gameBoardBackground;
    private Point center;
    private Paint outline;
    private float mainCircleRadius;
    private int helperRectSize;
    private Rect helperRect;
    
    private boolean initialized = false;
    private List<List<Point>> smallCircles;
    
    public GameBoard() {
        super();
    }
    
    public float getSmallCirclesRadius() {
        return smallCirclesRadius;
    }
    
    public List<List<Point>> getSmallCircles() {
        return smallCircles;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
    
    @Override
    public void draw(Canvas canvas) {
        
        if (! initialized)
            initialize(canvas);
/*
        Size size = new Size(canvas.getWidth(), canvas.getHeight());
        int squareSize = min(size.getWidth(), size.getHeight());
        //Point center = new Point(canvas.getWidth() / 2, canvas.getHeight() * 1 / 3);

        Paint gameBoardBackgroundPaint = new Paint();
        gameBoardBackgroundPaint.setColor(MyColorPalette.Primary);
        //Rect gameBoardBackground = new Rect(center.x - squareSize / 2, center.y - squareSize / 2, center.x + squareSize / 2, center.y + squareSize / 2);
        Rect gameBoardBackground = new Rect(0, 0, squareSize, +squareSize);

        Point center = new Point(gameBoardBackground.centerX(), gameBoardBackground.centerY());

        canvas.drawRect(gameBoardBackground, gameBoardBackgroundPaint);

        Paint outline = new Paint();
        outline.setColor(MyColorPalette.PrimaryDark);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(4.f);

        float mainCircleRadius = (squareSize - (squareSize * 0.05f)) / 2;
        canvas.drawCircle(center.x, center.y, mainCircleRadius, outline);

        int helperRectSize = (int) (mainCircleRadius * Math.sqrt(2));
        Rect helperRect = new Rect(center.x - helperRectSize / 2, center.y - helperRectSize / 2, center.x + helperRectSize / 2, center.y + helperRectSize / 2);
        //canvas.drawRect(helperRect, outline);

        smallCirclesRadius = (helperRectSize / 4.f) / 2.f;
        Point centerOfHelperSubrects = new Point();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                centerOfHelperSubrects.set(
                        (int) (helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2),
                        (int) (helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2));
                canvas.drawCircle(centerOfHelperSubrects.x, centerOfHelperSubrects.y, smallCirclesRadius - (smallCirclesRadius * 0.05f), outline);
            }
        }

        */
        
        canvas.drawRect(gameBoardBackground, gameBoardBackgroundPaint);
        canvas.drawCircle(center.x, center.y, mainCircleRadius, outline);
        Point centerOfHelperSubrects = new Point();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                centerOfHelperSubrects = smallCircles.get(x).get(y);
                /*centerOfHelperSubrects.set(
                        (int) (helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2),
                        (int) (helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2));*/
                canvas.drawCircle(centerOfHelperSubrects.x, centerOfHelperSubrects.y, smallCirclesRadius - (smallCirclesRadius * 0.05f), outline);
            }
        }
    }
    
    public void initialize(Canvas canvas) {
        smallCircles = new ArrayList<List<Point>>(4);
        for (int i = 0; i < 4; i++) {
            smallCircles.add(new ArrayList<Point>(4));
        }
        /*Size */
        size = new Size(canvas.getWidth(), canvas.getHeight());
        /*int*/
        squareSize = min(size.getWidth(), size.getHeight());
        //Point center = new Point(canvas.getWidth() / 2, canvas.getHeight() * 1 / 3);

        /*Paint*/
        gameBoardBackgroundPaint = new Paint();
        gameBoardBackgroundPaint.setColor(MyColorPalette.Primary);
        //Rect gameBoardBackground = new Rect(center.x - squareSize / 2, center.y - squareSize / 2, center.x + squareSize / 2, center.y + squareSize / 2);
        /*Rect*/
        gameBoardBackground = new Rect(0, 0, squareSize, + squareSize);

        /*Point*/
        center = new Point(gameBoardBackground.centerX(), gameBoardBackground.centerY());
        
        //canvas.drawRect(gameBoardBackground, gameBoardBackgroundPaint);

        /*Paint*/
        outline = new Paint();
        outline.setColor(MyColorPalette.PrimaryDark);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(4.f);

        /*float*/
        mainCircleRadius = (squareSize - (squareSize * 0.05f)) / 2;
        //canvas.drawCircle(center.x, center.y, mainCircleRadius, outline);

        /*int*/
        helperRectSize = (int) (mainCircleRadius * Math.sqrt(2));
        /*Rect*/
        helperRect = new Rect(center.x - helperRectSize / 2, center.y - helperRectSize / 2, center.x + helperRectSize / 2, center.y + helperRectSize / 2);
        //canvas.drawRect(helperRect, outline);
        
        smallCirclesRadius = (helperRectSize / 4.f) / 2.f;
        
        Point centerOfHelperSubrects = new Point();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                centerOfHelperSubrects.set(
                        (int) (helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2),
                        (int) (helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2));
                
                smallCircles.get(x).add(y, new Point(centerOfHelperSubrects));
                //canvas.drawCircle(centerOfHelperSubrects.x, centerOfHelperSubrects.y, smallCirclesRadius - (smallCirclesRadius * 0.05f), outline);
            }
        }
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
    
}
