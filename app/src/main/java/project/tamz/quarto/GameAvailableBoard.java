package project.tamz.quarto;

import android.graphics.Canvas;

/**
 * Created by Richard Zvonek on 09/12/2017.
 */

public class GameAvailableBoard implements GameEntity {
    
    boolean initialized = false;
    
    @Override
    public void draw(Canvas canvas) {
        if (! initialized)
            initialize(canvas);
        
    }
    
    void initialize(Canvas canvas) {
        /*
        * todo: initialize Available game board
        * */
        
        initialized = true;
    }
    
    @Override
    public void update() {
    
    }
}
