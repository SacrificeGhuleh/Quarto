package project.tamz.quarto;

import android.graphics.Canvas;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

public interface GameEntity {
    void draw(Canvas canvas);

    void update();
}
