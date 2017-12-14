package project.tamz.quarto;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static android.content.ContentValues.TAG;

/**
 * Created by Richard Zvonek on 10/12/2017.
 */

public class GameArtificialIntelligence {
    
    private QuartoGameLogic game;
    private int difficulty;
    
    private GameObject toSelect;
    private Point toPlace;
    
    public GameArtificialIntelligence(@NonNull QuartoGameLogic qgm) {
        this.game = qgm;
        difficulty = SettingsActivity.getDifficulty();
        Log.d(TAG, "Difficulty: " + difficulty);
    }
    
    public void analyzeBoard() {
        getToPlace();
        getToSelect();
    }
    
    private boolean getToPlace() {
        if (game == null) return false;
        
        List<Point> available = new ArrayList<>();
        
        GameObject placed[][] = game.getPlacedObjects();
        
        for (int y = 0; y < placed.length; y++) {
            for (int x = 0; x < placed[y].length; x++) {
                if (placed[y][x] == null) available.add(new Point(x, y));
            }
        }
        /*
        * Random choosing
        * */
        int availableCount = available.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toPlace = available.get(0);
                return true;
            }
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
            
            Log.d(TAG, "Radnom number toPlace " + rand + " Size: " + availableCount);
            toPlace = available.get(rand);
            return true;
        }
        return false;
    }
    
    private boolean getToSelect() {
        if (game == null) return false;
        
        List<GameObject> available = game.getAvailableGameObjects();
        /*
        * Random choosing
        * */
        
        int availableCount = available.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toSelect = available.get(0);
                return true;
            }
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
            Log.d(TAG, "Radnom number toSelect " + rand + " Size: " + availableCount);
            
            toSelect = available.get(rand);
            return true;
        }
        return false;
    }
    
    public boolean place() {
        if (toPlace != null) {
            if (! game.place(toPlace)) {
                if (! getToPlace()) return false;
                return place();
            }
            return true;
        }
        return false;
    }
    
    public boolean select() {
        if (toSelect != null) {
            game.setSelectedObject(toSelect);
            return true;
        }
        return false;
    }
    
}
