package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
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
    private PointF position;
    
    public GameObject(byte code) {
        super();
    
        this.tall = (code & 0b1000) == 0;
        this.black = (code & 0b100) == 0;
        this.square = (code & 0b10) == 0;
        this.hole = (code & 0b1) == 0;
        this.code = code;
    }
    
    public static GameObject[][] clone(GameObject[][] in) {
        GameObject[][] out = new GameObject[in.length][];
        
        for (int i = 0; i < in.length; i++) {
            out[i] = new GameObject[in[i].length];
        }
        
        for (int row = 0; row < in.length; row++) {
            for (int col = 0; col < in[row].length; col++) {
                out[row][col] = in[row][col];
            }
        }
        
        return out;
    }
    
    public static boolean hasSomeMatrix(GameObject[][] in1, GameObject[][] in2) {
        if (in1 == null || in2 == null) return false;
        for (int row = 0; row < in1.length; row++) {
            for (int col = 0; col < in1[row].length; col++) {
                if (in1[row][col] != in2[row][col]) return false;
            }
        }
        return true;
    }
    
    public float getBoardSphereSize() {
        return boardSphereSize;
    }
    
    public void setBoardSphereSize(float boardSphereSize) {
        this.boardSphereSize = boardSphereSize;
    }
    
    public PointF getPosition() {
        return position;
    }
    
    public void setPosition(PointF position) {
        this.position = position;
    }
    
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
        if (position == null || boardSphereSize <= 0.f)
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
        
        float size = (float) sqrt(2.f) * boardSphereSize;
        
        if (! tall) {
            size *= (2.f / 3.f);
        }
        //Log.d("big", "Size: " + size);
        
        if (square) {
            RectF rect = new RectF(
                    position.x - (size / 2) * 0.9f,
                    position.y - (size / 2) * 0.9f,
                    position.x + (size / 2) * 0.9f,
                    position.y + (size / 2) * 0.9f);
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
    
    @Override
    public int hashCode() {
        
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof GameObject) {
            return ((GameObject) obj).getCode() == this.getCode();
        }
        return false;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GameObject(this.code);
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
