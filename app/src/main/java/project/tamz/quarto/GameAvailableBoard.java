package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard Zvonek on 09/12/2017.
 */

public class GameAvailableBoard implements GameEntity {
    
    private boolean initialized = false;
    private RectF background;
    private Paint backgroundPaint;
    private Paint cellPaint;
    private Paint cellOutlinePaint;
    
    private PointF selectedPos;
    private float cellWidth;
    private List<List<PointF>> availablePositions;
    
    public PointF getSelectedPos() {
        return selectedPos;
    }
    
    public float getCellWidth() {
        return cellWidth;
    }
    
    public List<List<PointF>> getAvailablePositions() {
        return availablePositions;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (! initialized)
            initialize(canvas);
        canvas.drawRect(background, backgroundPaint);
        
        RectF selectedRect = new RectF(
                selectedPos.x - cellWidth / 2.f,
                selectedPos.y - cellWidth / 2.f,
                selectedPos.x + cellWidth / 2.f,
                selectedPos.y + cellWidth / 2.f
        );
        
        /*
        * Selected game object
        * */
        canvas.drawRoundRect(selectedRect, 10.f, 10.f, cellPaint);
        canvas.drawRoundRect(selectedRect, 10.f, 10.f, cellOutlinePaint);
    
        for (int y = 0; y < availablePositions.size(); y++) {
            for (int x = 0; x < availablePositions.get(y).size(); x++) {
                //canvas.drawCircle(availablePositions.get(y).get(x).x, availablePositions.get(y).get(x).y, 10, cellPaint);
                PointF point = new PointF(availablePositions.get(y).get(x).x, availablePositions.get(y).get(x).y);
                RectF availableSubRect = new RectF(
                        point.x - cellWidth / 2.f,
                        point.y - cellWidth / 2.f,
                        point.x + cellWidth / 2.f,
                        point.y + cellWidth / 2.f
                );
                canvas.drawRoundRect(availableSubRect, 10.f, 10.f, cellPaint);
                canvas.drawRoundRect(availableSubRect, 10.f, 10.f, cellOutlinePaint);
            }
        }
    }
    
    private void initialize(Canvas canvas) {
        float count = 7.f;
        cellWidth = canvas.getWidth() / count;
    
        background = new RectF(
                0.f,
                canvas.getWidth(),
                canvas.getWidth(),
                canvas.getHeight()
        );
        backgroundPaint = new Paint();
        backgroundPaint.setColor(MyColorPalette.PrimaryDark);
    
        cellPaint = new Paint();
        cellPaint.setAntiAlias(true);
        cellPaint.setColor(MyColorPalette.Primary);
    
        cellOutlinePaint = new Paint();
        cellOutlinePaint.setAntiAlias(true);
        cellOutlinePaint.setColor(MyColorPalette.PrimaryDark);
        cellOutlinePaint.setStyle(Paint.Style.STROKE);
        cellOutlinePaint.setStrokeWidth(4.f);
    
        selectedPos = new PointF(cellWidth * 6.f, background.top + cellWidth * 1.f);
        
        availablePositions = new ArrayList<>(3);
    
        for (int row = 0; row < 3; row++) {
            availablePositions.add(new ArrayList<PointF>(6));
            for (int col = 0; col < 6; col++) {
                if (row >= 2 && col >= 4) break; //Only 2 cells on 3rd row
                availablePositions.get(row).add(new PointF(cellWidth * (col + 1), background.top + cellWidth * (row + 2)));
            }
        }
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
}
