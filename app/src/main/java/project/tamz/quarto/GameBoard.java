package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
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
    private float squareSize;
    private Paint gameBoardBackgroundPaint;
    private RectF gameBoardBackground;
    private PointF center;
    private Paint outline;
    private float mainCircleRadius;
    private float helperRectSize;
    private RectF helperRect;
    
    private boolean initialized = false;
    private List<List<PointF>> smallCircles;
    
    public GameBoard() {
        super();
    }
    
    public float getSmallCirclesRadius() {
        return smallCirclesRadius;
    }
    
    public List<List<PointF>> getSmallCircles() {
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
        
        canvas.drawRect(gameBoardBackground, gameBoardBackgroundPaint);
        canvas.drawCircle(center.x, center.y, mainCircleRadius, outline);
        PointF centerOfHelperSubrects = new PointF();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                centerOfHelperSubrects = smallCircles.get(x).get(y);
                canvas.drawCircle(centerOfHelperSubrects.x, centerOfHelperSubrects.y, smallCirclesRadius - (smallCirclesRadius * 0.05f), outline);
            }
        }
    }
    
    public void initialize(Canvas canvas) {
        smallCircles = new ArrayList<List<PointF>>(4);
        for (int i = 0; i < 4; i++) {
            smallCircles.add(new ArrayList<PointF>(4));
        }
        size = new Size(canvas.getWidth(), canvas.getHeight());
        squareSize = min(size.getWidth(), size.getHeight());
    
        gameBoardBackgroundPaint = new Paint();
        gameBoardBackgroundPaint.setColor(MyColorPalette.Primary);
        gameBoardBackground = new RectF(0, 0, (int) squareSize, + (int) squareSize);
    
        center = new PointF(gameBoardBackground.centerX(), gameBoardBackground.centerY());
        
        outline = new Paint();
        outline.setColor(MyColorPalette.PrimaryDark);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(4.f);
        outline.setAntiAlias(true);
        
        mainCircleRadius = (squareSize - (squareSize * 0.05f)) / 2;
    
        helperRectSize = mainCircleRadius * (float) Math.sqrt(2);
        helperRect = new RectF(
                center.x - helperRectSize / 2.f,
                center.y - helperRectSize / 2.f,
                center.x + helperRectSize / 2.f,
                center.y + helperRectSize / 2.f);
        smallCirclesRadius = (helperRectSize / 4.f) / 2.f;
    
        PointF centerOfHelperSubrects = new PointF();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {/*
                centerOfHelperSubrects.set(
                        (int) (helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2.f),
                        (int) (helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2.f));
                smallCircles.get(x).add(y, new PointF(centerOfHelperSubrects));*/
                smallCircles.get(y).add(
                        new PointF(
                                helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2.f,
                                helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2.f
                        ));
            }
        }
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
    
}
