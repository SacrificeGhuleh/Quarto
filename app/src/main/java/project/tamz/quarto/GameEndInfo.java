package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Richard Zvonek on 17/12/2017.
 */

public class GameEndInfo implements GameEntity {
    private RectF background;
    private Paint backgroundPaint;
    
    private Paint textPaint;
    
    private String info;
    private Rect r;
    private boolean initialized = false;
    
    public GameEndInfo(GameEndType end) {
        switch (end) {
            case AI:
                info = "YOU LOST";
                break;
            case PLAYER:
                info = "YOU WON";
                break;
            case DRAW:
                info = "DRAW";
                break;
        }
        
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (! initialized)
            initialize(canvas);
        canvas.drawRect(background, backgroundPaint);
        drawCenter(canvas, textPaint, info);
    }
    
    @Override
    public void update() {
    
    }
    
    private void initialize(Canvas canvas) {
        r = new Rect();
        background = new RectF(
                0.f,
                canvas.getWidth(),
                canvas.getWidth(),
                canvas.getHeight()
        );
        backgroundPaint = new Paint();
        backgroundPaint.setColor(MyColorPalette.PrimaryDark);
        
        textPaint = new Paint();
        textPaint.setTextSize(50.f);
        textPaint.setColor(MyColorPalette.PrimaryText);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        initialized = true;
    }
    
    private void drawCenter(Canvas canvas, Paint paint, String text) {
        //canvas.getClipBounds(r);
        background.round(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = (cHeight / 2f + r.height() / 2f - r.bottom) + canvas.getWidth();
        canvas.drawText(text, x, y, paint);
    }
}
