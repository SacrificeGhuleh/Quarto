package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import static java.lang.Math.sqrt;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class GameObject implements GameEntity {
    private boolean tall;
    private boolean black;
    private boolean square;
    private boolean hole;
    private byte code;
    private float boardSphereSize = 0.f;
    private Point position;
    
    public GameObject(byte code) {
        super();
        
        this.tall = (code & 8) == 0;
        this.black = (code & 4) == 0;
        this.square = (code & 2) == 0;
        this.hole = (code & 1) == 0;
        this.code = code;
    }
    
    public float getBoardSphereSize() {
        return boardSphereSize;
    }
    
    public void setBoardSphereSize(float boardSphereSize) {
        this.boardSphereSize = boardSphereSize;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public void setPosition(Point position) {
        this.position = position;
    }

    /*public GameObject() {
        super();
    }*/
    
    public byte getCode() {
        return code;
    }

    /*public GameObject(boolean tall, boolean black, boolean square, boolean hole) {
        super();

        this.tall = tall;
        this.black = black;
        this.square = square;
        this.hole = hole;
    }*/
    
    public boolean isTall() {
        return tall;
    }
    
    public boolean isBlack() {
        return black;
    }
    
    public boolean isSquare() {
        return square;
    }
    
    public boolean isHole() {
        return hole;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (position == null || boardSphereSize <= 0)
            return;
        
        Paint objectColor = new Paint();
        if (black) {
            objectColor.setColor(MyColorPalette.PrimaryText);
        } else {
            objectColor.setColor(MyColorPalette.PrimaryDark);
        }
        objectColor.setAntiAlias(true);
        
        Paint boardColor = new Paint();
        boardColor.setColor(MyColorPalette.Primary);
        boardColor.setAntiAlias(true);
        
        float size = (float) (sqrt(2) * boardSphereSize);
        
        if (! tall) {
            size *= (2.f / 3.f);
        }
        //Log.d("big", "Size: " + size);
        
        if (square) {
            RectF rect = new RectF(
                    (int) (position.x - (size / 2) * 0.9),
                    (int) (position.y - (size / 2) * 0.9),
                    (int) (position.x + (size / 2) * 0.9),
                    (int) (position.y + (size / 2) * 0.9));
            canvas.drawRoundRect(rect, 10, 10, objectColor);
            //canvas.drawRect(rect, objectColor);
        } else {
            canvas.drawCircle(position.x, position.y, size / 2, objectColor);
        }
        
        if (hole) {
            canvas.drawCircle(position.x, position.y, size / 4, boardColor);
        }
    }
    
    @Override
    public void update() {
    
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
        return "GameObject ID:" + hashCode() + " Tall: " + tall + " Black " + black + " Square " + square + " Hole " + hole;
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
    
}
