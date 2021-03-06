package project.tamz.quarto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    
    private Button startGameButton;
    private Button settingsButton;
    private Button helpButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        
        startGameButton = findViewById(R.id.startGameButton);
        settingsButton = findViewById(R.id.settingsButton);
        helpButton = findViewById(R.id.helpButton);
        
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help();
            }
        });
    }
    
    private void newGame() {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        
        startActivity(intent);
    }
    
    private void settings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        
        startActivity(intent);
    }
    
    private void help() {
        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
        
        startActivity(intent);
    }
}
