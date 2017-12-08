package project.tamz.quarto;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Richard Zvonek on 02/12/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {


    private GameActivity gameActivity;
    private GameThread thread;

    private GameBoard gameBoard;
    private GaneStatusBar ganeStatusBar;

    public GamePanel(Context ctx) {
        super(ctx);

        init();
    }


    public GamePanel(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        init();
    }


    public void addGameActivity(GameActivity activity) {
        this.gameActivity = activity;
    }

    public void init() {


        gameBoard = new GameBoard();
        ganeStatusBar = new GaneStatusBar();

        getHolder().addCallback(this);

        this.thread = new GameThread(getHolder(), this);
        QuartoGameLogic quartoGameLogic = new QuartoGameLogic();
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Clicked on x: " + event.getX() + " y: " + event.getY());

        return super.onTouchEvent(event);
    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(MyColorPalette.PrimaryLight);


        ganeStatusBar.draw(canvas);
        gameBoard.draw(canvas);

        if (gameActivity != null)
            gameActivity.editElapsedTime(getElapsed());
    }

    Time getElapsed() {
        return ganeStatusBar.getGameTime();
    }
}
