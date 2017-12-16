package project.tamz.quarto;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class GameActivity extends Activity {
    
    private GamePanel panel;
    private TextView elapsedTime;
    private TextView gameStatus;
    private ConstraintLayout statusBarLayout;
    private ConstraintLayout gameWonLayout;
    private ImageView imageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    
        setContentView(R.layout.activity_game);
    
        this.elapsedTime = findViewById(R.id.elapsedTextView);
        this.statusBarLayout = findViewById(R.id.statusBarLayout);
        this.panel = findViewById(R.id.gamePanel);
    
        this.panel.addGameActivity(this);
        this.gameStatus = findViewById(R.id.gameStatusTextView);
        this.gameWonLayout = findViewById(R.id.endOfGameLayout);
    
        this.imageView = findViewById(R.id.imageView);
    }
    
    public void endOfGame(final Canvas canvas) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Canvas size: width " + canvas.getWidth() + " height " + canvas.getHeight());
                    int width = canvas.getWidth();
                    
                    gameWonLayout.setVisibility(View.VISIBLE);
                    if (true) {
                        gameStatus.setText("GAME WON");
                    } else {
                        gameStatus.setText("GAME LOST");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
    
    public void editElapsedTime(final Time input) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    elapsedTime.setText(input.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
