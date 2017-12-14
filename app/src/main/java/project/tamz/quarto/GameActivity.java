package project.tamz.quarto;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends Activity {
    
    private GamePanel panel;
    private TextView elapsedTime;
    private TextView gameStatus;
    private ConstraintLayout statusBarLayout;
    private ConstraintLayout gameWonLayout;
    
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
    }
    
    public void endOfGame(final boolean gameWon) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                
                try {
                    gameWonLayout.setVisibility(View.VISIBLE);
                    if (gameWon) {
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
