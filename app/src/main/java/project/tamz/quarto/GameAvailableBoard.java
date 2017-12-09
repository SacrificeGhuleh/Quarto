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
        
        /*float count = 7.f;
        Paint linePaint = new Paint();
        linePaint.setColor(MyColorPalette.PrimaryLight);
        
        float width = canvas.getWidth() / count;
        for (int i = 0; i < count; i++) {
            float x = (width * i) + width / 2;
            float y = background.top + (width * i) + width / 2;
            canvas.drawLine(x, background.top, x, background.bottom, linePaint);
            canvas.drawLine(background.left, y, background.right, y, linePaint);
        }*/
    
        RectF selectedRect = new RectF(
                selectedPos.x - cellWidth / 2,
                selectedPos.y - cellWidth / 2,
                selectedPos.x + cellWidth / 2,
                selectedPos.y + cellWidth / 2
        );
        
        /*
        * Selected game object
        * */
        canvas.drawRoundRect(selectedRect, 10, 10, cellPaint);
        canvas.drawRoundRect(selectedRect, 10, 10, cellOutlinePaint);
    
        for (int y = 0; y < availablePositions.size(); y++) {
            for (int x = 0; x < availablePositions.get(y).size(); x++) {
                //canvas.drawCircle(availablePositions.get(y).get(x).x, availablePositions.get(y).get(x).y, 10, cellPaint);
                PointF point = new PointF(availablePositions.get(y).get(x).x, availablePositions.get(y).get(x).y);
                RectF availableSubRect = new RectF(
                        point.x - cellWidth / 2,
                        point.y - cellWidth / 2,
                        point.x + cellWidth / 2,
                        point.y + cellWidth / 2
                );
                canvas.drawRoundRect(availableSubRect, 10, 10, cellPaint);
                canvas.drawRoundRect(availableSubRect, 10, 10, cellOutlinePaint);
            }
        }
        
        /*GameObject test = new GameObject((byte)0b1011);
        test.setPosition(new PointF((int)selectedPos.x, (int)selectedPos.y));
        test.setBoardSphereSize(cellWidth);
        test.draw(canvas);*/
        //canvas.drawCircle(selectedPos.x, selectedPos.y, cellWidth*0.9f/2.f, cellPaint);
        
    }
    
    void initialize(Canvas canvas) {
        /*
        * todo: initialize Available game board
        * */
    
        float count = 7.f;
        cellWidth = canvas.getWidth() / count;
    
        background = new RectF(
                0,
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
    
        availablePositions = new ArrayList<List<PointF>>(3);
        for (int i = 0; i < 3; i++) {
            availablePositions.add(new ArrayList<PointF>(6));
        }
    
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 6; x++) {
                if (y >= 2 && x >= 2) break; //Only 2 cells on 3rd row
                availablePositions.get(y).add(new PointF(cellWidth * (x + 1), background.top + cellWidth * (y + 2)));
            }
        }
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
}
