package project.tamz.quarto;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class GameObject implements GameEntity {
    private boolean tall;
    private boolean black;
    private boolean square;
    private boolean hole;
    private byte code;
    private int boardSphereSize = 0;
    private Point position;

    public GameObject(byte code) {
        super();

        this.tall = (code & 8) == 0;
        this.black = (code & 4) == 0;
        this.square = (code & 2) == 0;
        this.hole = (code & 1) == 0;
        this.code = code;
    }

    public int getBoardSphereSize() {
        return boardSphereSize;
    }

    public void setBoardSphereSize(int boardSphereSize) {
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

    @Override
    public void draw(Canvas canvas) {
        if (position == null || boardSphereSize <= 0)
            return;

        //todo vykreslovani



    }

    @Override
    public void update() {

    }
}
