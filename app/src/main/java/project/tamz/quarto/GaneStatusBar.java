package project.tamz.quarto;

import android.graphics.Canvas;

/**
 * Created by Richard Zvonek on 08/12/2017.
 */

public class GaneStatusBar implements GameEntity {

    private long creationTime;
    private long currentTime;

    private Time gameTime;


    public GaneStatusBar() {
        gameTime = new Time();
        creationTime = System.currentTimeMillis();
        System.out.println("Status bar created in  : " + creationTime);
    }

    public Time getGameTime() {
        currentTime = System.currentTimeMillis();
        gameTime.set(currentTime - creationTime);
        System.out.println(gameTime);
        return gameTime;
    }

    @Override
    public void draw(Canvas canvas) {
        getGameTime();
    }

    @Override
    public void update() {

    }


}
