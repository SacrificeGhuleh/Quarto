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
    
    private float smallCirclesRadius;
    private Size size;
    private float squareSize;
    private Paint gameBoardBackgroundPaint;
    private RectF gameBoardBackground;
    private PointF center;
    private Paint outline;
    private Paint highlightOutline;
    private float mainCircleRadius;
    private float helperRectSize;
    private RectF helperRect;
    private boolean highLights[][];
    
    private boolean initialized = false;
    private List<List<PointF>> smallCircles;
    
    public GameBoard() {
        super();
    
        highLights = new boolean[4][4];
        for (int row = 0; row < highLights.length; row++) {
            for (int col = 0; col < highLights[row].length; col++) {
                highLights[row][col] = false;
            }
        }
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
        PointF centerOfHelperSubrects;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Paint currPaint;
                if (highLights[row][col])
                    currPaint = highlightOutline;
                else
                    currPaint = outline;
            
                centerOfHelperSubrects = smallCircles.get(row).get(col);
                canvas.drawCircle(
                        centerOfHelperSubrects.x,
                        centerOfHelperSubrects.y,
                        smallCirclesRadius - (smallCirclesRadius * 0.05f),
                        currPaint);
            }
        }
    }
    
    private void initialize(Canvas canvas) {
        smallCircles = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            smallCircles.add(new ArrayList<PointF>(4));
        }
        size = new Size(canvas.getWidth(), canvas.getHeight());
        squareSize = min(size.getWidth(), size.getHeight());
        
        gameBoardBackgroundPaint = new Paint();
        gameBoardBackgroundPaint.setColor(MyColorPalette.Primary);
        gameBoardBackground = new RectF(0.f, 0.f, squareSize, + squareSize);
        
        center = new PointF(gameBoardBackground.centerX(), gameBoardBackground.centerY());
        
        outline = new Paint();
        outline.setColor(MyColorPalette.PrimaryDark);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(4.f);
        outline.setAntiAlias(true);
    
        highlightOutline = new Paint();
        highlightOutline.setColor(MyColorPalette.PrimaryHighlight);
        highlightOutline.setStyle(Paint.Style.STROKE);
        highlightOutline.setStrokeWidth(4.f);
        highlightOutline.setAntiAlias(true);
        
        mainCircleRadius = (squareSize - (squareSize * 0.05f)) / 2.f;
        
        helperRectSize = mainCircleRadius * (float) Math.sqrt(2.f);
        helperRect = new RectF(
                center.x - helperRectSize / 2.f,
                center.y - helperRectSize / 2.f,
                center.x + helperRectSize / 2.f,
                center.y + helperRectSize / 2.f);
        smallCirclesRadius = (helperRectSize / 4.f) / 2.f;
    
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                smallCircles.get(col).add(
                        new PointF(
                                helperRect.left + smallCirclesRadius + row * smallCirclesRadius * 2.f,
                                helperRect.top + smallCirclesRadius + col * smallCirclesRadius * 2.f
                        ));
            }
        }
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
    
    public void setHighLights(boolean[][] highLights) {
        this.highLights = highLights;
    }
}
