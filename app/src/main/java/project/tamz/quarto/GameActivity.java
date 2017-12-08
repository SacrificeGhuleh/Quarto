package project.tamz.quarto;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends Activity {


    GamePanel panel;
    TextView elapsedTime;
    ConstraintLayout statusBarLayout;

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
