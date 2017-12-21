package project.tamz.quarto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    private final static String PREF_DIFFICULTY = "Difficulty";
    private static int difficulty;
    private TextView difficultyTextView;
    private SeekBar difficultySeekBar;
    private Button backButton;
    private SharedPreferences preferences;
    
    public static int getDifficulty() {
        return difficulty;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    
        preferences = getPreferences(MODE_PRIVATE);
        if (preferences.contains(PREF_DIFFICULTY)) {
            difficulty = preferences.getInt(PREF_DIFFICULTY, 1);
        } else
            difficulty = 1;
        
        difficultyTextView = findViewById(R.id.difficultyDescTextView);
        difficultySeekBar = findViewById(R.id.difficultySeekBar);
        backButton = findViewById(R.id.backButton);
        
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        difficultySeekBar.setProgress(difficulty);
        
        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficulty = progress;
    
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(PREF_DIFFICULTY, progress);
                editor.commit();
                
                setDescription(progress);
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            
            }
        });
        
        setDescription(difficultySeekBar.getProgress());
        
    }
    
    private void setDescription(int value) {
        String desc;
        
        switch (value) {
            case 0:
                desc = "Random";
                break;
            case 1:
                desc = "Novice";
                break;
            case 2:
                desc = "Advanced";
                break;
            case 3:
                desc = "Master";
                break;
            case 4:
                desc = "Asian kid";
                break;
            default:
                
                desc = "";
                break;
        }
        
        difficultyTextView.setText(desc);
    }
}
