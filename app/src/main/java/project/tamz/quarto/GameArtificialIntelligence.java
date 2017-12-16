package project.tamz.quarto;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static project.tamz.quarto.QuartoGameLogic.hasCommon;

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
        Log.d("AI", "Difficulty: " + difficulty);
    }
    
    public void analyzeBoard() {
        if (game == null) return;
        getToPlace();
        getToSelect();
        
        /*
        getToPlaceRandom();
        getToSelectRandom();
    */
    }
    
    private boolean getToPlace() {
        if (game.getPlacedCount() < 4) {
            return getToPlaceRandom();
        }
        boolean result = false;
        switch (difficulty) {
            case 0:
                result = getToPlaceRandom();
                break;
            case 1:
                result = getToPlaceBeginner();
                if (! result)
                    result = getToPlaceRandom();
                //Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 2:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 3:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 4:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            default:
                result = getToPlaceRandom();
        }
        return result;
    }
    
    private boolean getToSelect() {
        if (game.getPlacedCount() < 4) {
            return getToSelectRandom();
        }
        boolean result = false;
        switch (difficulty) {
            case 0:
                result = getToSelectRandom();
                break;
            case 1:
                result = getToSelectBeginner();
                if (! result)
                    result = getToSelectRandom();
                //Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 2:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 3:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            case 4:
                Log.d("AI", "difficulty " + difficulty + " not implemented yet");
                break;
            default:
                result = getToSelectRandom();
        }
        return result;
    }
    
    private boolean getToPlaceRandom() {
        if (game == null) return false;
        
        List<Point> available = new ArrayList<>();
        
        GameObject placed[][] = game.getPlacedObjects();
        
        for (int row = 0; row < placed.length; row++) {
            for (int col = 0; col < placed[row].length; col++) {
                if (placed[row][col] == null) available.add(new Point(row, col));
            }
        }
        
        game.printBoard();
        Log.d("AI", "getToPlaceRandom: ");
        Log.d("AI", "AI: available to place: " + available.toString());
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
            toPlace = available.get(rand);
            Log.d("AI", "AI: to place: " + toPlace);
            return true;
        }
        return false;
    }
    
    private boolean getToPlaceBeginner() {
        Log.d("AI", "Placing as beginner ");
        if (game == null) return false;
        
        List<Point> available = new ArrayList<>();
        
        GameObject selected = game.getSelectedObject();
        final GameObject placed[][] = game.getPlacedObjects();
        
        for (int row = 0; row < placed.length; row++) {
            for (int col = 0; col < placed[row].length; col++) {
                if (placed[row][col] == null) available.add(new Point(row, col));
            }
        }
        GameObject[][] placedCopy;
        for (Point p : available) {
            placedCopy = GameObject.clone(placed);
            placedCopy[p.x][p.y] = selected;
            if (checkBoard(placedCopy)) {
                toPlace = p;
                return true;
            }
            placedCopy = null;
        }
        return false;
    }
    
    private boolean getToSelectRandom() {
        if (game == null) return false;
        
        List<GameObject> available = game.getAvailableGameObjects();
        
        int availableCount = available.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toSelect = available.get(0);
                return true;
            }
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
            
            toSelect = available.get(rand);
            return true;
        }
        return false;
    }
    
    private boolean getToSelectBeginner() {
        Log.d("AI", "Selecting as beginner");
        if (game == null) return false;
        
        List<GameObject> availableObjects = game.getAvailableGameObjects();
        final GameObject[][] placed = game.getPlacedObjects();
        List<Point> availablePlaces = new ArrayList<>();
        
        for (int row = 0; row < placed.length; row++) {
            for (int col = 0; col < placed[row].length; col++) {
                if (placed[row][col] == null) availablePlaces.add(new Point(row, col));
            }
        }
        boolean suitable = true;
        List<GameObject> goodForPlace = new ArrayList<>();
        for (GameObject go : availableObjects) {
            suitable = true;
            innerLoop:
            for (Point p : availablePlaces) {
                GameObject[][] placedCopy = GameObject.clone(placed);
                placedCopy[p.x][p.y] = go;
                
                if (checkBoard(placedCopy)) {
                    suitable = false;
                    break innerLoop;
                }
                
            }
            if (suitable)
                goodForPlace.add(go);
        }
        int availableCount = goodForPlace.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toSelect = goodForPlace.get(0);
            }
            
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
            
            toSelect = goodForPlace.get(rand);
            return true;
        } else {
            getToSelectRandom();
        }
        return false;
    }
    
    private boolean checkBoard(GameObject[][] board) {
        
        List<GameObject> rows = new ArrayList<>();
        List<GameObject> cols = new ArrayList<>();
        List<GameObject> firstDiagonal = new ArrayList<>();
        List<GameObject> secondDiagonal = new ArrayList<>();
        
        for (int row = 0; row < 4; row++) {
            if (board[row][row] != null) firstDiagonal.add(board[row][row]);
            rows.clear();
            cols.clear();
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != null)
                    cols.add(board[col][row]);
                
                if (board[row][col] != null)
                    rows.add(board[row][col]);
                
                if (row + col == 3 && board[row][col] != null)
                    secondDiagonal.add(board[row][col]);
            }
            
            if (rows.size() == 4) {
                if (hasCommon(rows)) {
                    Log.d("AI", "Common attribute found in row " + row);
                    return true;
                }
            }
            
            if (cols.size() == 4) {
                if (hasCommon(cols)) {
                    Log.d("AI", "Common attribute found in column " + row);
                    return true;
                }
            }
        }
        
        if (firstDiagonal.size() == 4) {
            if (hasCommon(firstDiagonal)) {
                Log.d("AI", "Common attribute found in first diagonale.");
                return true;
            }
        }
        
        if (secondDiagonal.size() == 4) {
            if (hasCommon(secondDiagonal)) {
                Log.d("AI", "Common attribute found in second diagonale.");
                return true;
            }
        }
        
        return false;
    }
    
    public boolean place() {
        if (toPlace != null) {
            if (! game.place(toPlace)) {
                getToPlace();
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
