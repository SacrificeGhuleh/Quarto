package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;

import static java.lang.Math.min;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class GameBoard implements GameEntity {


    public GameBoard() {
        super();
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
        Size size = new Size(canvas.getWidth(), canvas.getHeight());
        int squareSize = min(size.getWidth(), size.getHeight());
        Point center = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);

        Paint gameBoardBackgroundPaint = new Paint();
        gameBoardBackgroundPaint.setColor(MyColorPalette.Primary);
        Rect gameBoardBackground = new Rect(center.x - squareSize / 2, center.y - squareSize / 2, center.x + squareSize / 2, center.y + squareSize / 2);

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

        float smallCirclesRadius = (helperRectSize / 4.f) / 2.f;
        Point centerOfHelperSubrects = new Point();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                centerOfHelperSubrects.set(
                        (int) (helperRect.left + smallCirclesRadius + x * smallCirclesRadius * 2),
                        (int) (helperRect.top + smallCirclesRadius + y * smallCirclesRadius * 2));
                canvas.drawCircle(centerOfHelperSubrects.x, centerOfHelperSubrects.y, smallCirclesRadius - (smallCirclesRadius * 0.05f), outline);
            }
        }
    }

    @Override
    public void update() {

    }
}
