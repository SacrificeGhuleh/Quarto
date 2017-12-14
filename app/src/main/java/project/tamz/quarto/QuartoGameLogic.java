package project.tamz.quarto;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

class QuartoGameLogic {
    private List<GameObject> availableGameObjects = new ArrayList<>();
    private GameObject gameBoard[][] = new GameObject[4][4];
    private boolean commonHighlights[][];
    private GameObject selectedObject = null;
    private boolean selecting = false;
    private boolean placing = false;
    private boolean AIMove = false;
    private GameArtificialIntelligence AI;
    private boolean gameEnd = false;
    private boolean playerWon = false;
    
    public QuartoGameLogic() {
        
        commonHighlights = new boolean[4][4];
        for (int i = 0; i < commonHighlights.length; i++) {
            for (int j = 0; j < commonHighlights[i].length; j++) {
                commonHighlights[i][j] = false;
            }
        }
        
        for (byte b = 0; b < 16; b++) {
            availableGameObjects.add(new GameObject(b));
        }
        
        for (GameObject go : availableGameObjects) {
            System.out.println(go + " loaded");
        }
        
        selecting = true;
        
        AI = new GameArtificialIntelligence(this);
    }
    
    public boolean isPlayerWon() {
        return playerWon;
    }
    
    public boolean isGameEnd() {
        return gameEnd;
    }
    
    public boolean[][] getCommonHighlights() {
        return commonHighlights;
    }
    
    public GameObject[][] getPlacedObjects() {
        return gameBoard;
    }
    
    public GameObject getSelectedObject() {
        return selectedObject;
    }
    
    public boolean setSelectedObject(@NonNull GameObject selectedObject) {
        if (availableGameObjects.contains(selectedObject)) {
            availableGameObjects.remove(selectedObject);
            this.selectedObject = selectedObject;
            Log.d(TAG, "Found selected object");
    
            selecting = false;
            placing = true;
            AIMove = ! AIMove;
            return true;
        }
        Log.d(TAG, "setSelectedObject unsuccessful");
        return false;
    }
    
    public void AIAction() {
        if (isAIMove()) {
            AI.analyzeBoard();
            AI.place();
            AI.select();
        }
    }
    
    public boolean isAIMove() {
        return AIMove;
    }
    
    public boolean isPlacing() {
        return placing;
    }
    
    public boolean isSelecting() {
        return selecting;
    }
    
    public List<GameObject> getAvailableGameObjects() {
        return availableGameObjects;
    }
    
    public boolean place(Point coords) {
        if (coords == null) return false;
        if (coords.x < 0 || coords.y < 0 || coords.x > 4 || coords.y > 4)
            return false;
        
        if (gameBoard[coords.x][coords.y] == null && selectedObject != null) {
            gameBoard[coords.x][coords.y] = selectedObject;
            
            selectedObject = null;
            
            placing = false;
            selecting = true;
    
            gameEnd = checkGameBoard();
            if (gameEnd)
                playerWon = AIMove;
            
            return true;
        }
        return false;
    }
    
    private boolean checkGameBoard() {
        //check rows and cols
        List<GameObject> rows = new ArrayList<>();
        List<GameObject> cols = new ArrayList<>();
        List<GameObject> firstDiagonale = new ArrayList<>();
        List<GameObject> secondDiagonale = new ArrayList<>();
        
        boolean foundCommonAttribute = false;
        for (int y = 0; y < 4; y++) {
            rows.clear();
            cols.clear();
            if (gameBoard[y][y] != null)
                firstDiagonale.add(gameBoard[y][y]);
            for (int x = 0; x < 4; x++) {
                if (gameBoard[y][x] != null)
                    cols.add(gameBoard[x][y]);
    
                if (gameBoard[y][x] != null)
                    rows.add(gameBoard[y][x]);
    
                if (y + x == 3 && gameBoard[y][x] != null)
                    secondDiagonale.add(gameBoard[y][x]);
            }
            if (rows.size() == 4) {
                if (hasCommon(rows)) {
                    Log.d(TAG, "Common attribute found in row " + y);
                    foundCommonAttribute = true;
                    highlightRow(y);
                }
            }
            
            if (cols.size() == 4) {
                if (hasCommon(cols)) {
                    Log.d(TAG, "Common attribute found in column " + y);
                    foundCommonAttribute = true;
                    highlightCol(y);
                }
            }
        }
    
        if (firstDiagonale.size() == 4) {
            if (hasCommon(firstDiagonale)) {
                Log.d(TAG, "Common attribute found in first diagonale.");
                foundCommonAttribute = true;
                highlightFirstDiagonale();
            }
        }
    
        if (secondDiagonale.size() == 4) {
            if (hasCommon(secondDiagonale)) {
                Log.d(TAG, "Common attribute found in second diagonale.");
                foundCommonAttribute = true;
                highlightSecondDiagonale();
            }
        }
        return foundCommonAttribute;
    }
    
    private boolean hasCommon(@NonNull List<GameObject> args) {
        int commonAttributes = getCommonAttributes(args);
        if (args.size() == 4) {
            Log.d(TAG, "Number of common attributes: " + commonAttributes);
            return commonAttributes != 0;
        }
        return false;
    }
    
    private void highlightRow(int row) {
        for (int i = 0; i < commonHighlights[row].length; i++)
            commonHighlights[row][i] = true;
    }
    
    private void highlightCol(int col) {
        for (int i = 0; i < commonHighlights.length; i++)
            commonHighlights[i][col] = true;
    }
    
    private void highlightFirstDiagonale() {
        for (int i = 0; i < 4; i++) {
            commonHighlights[i][i] = true;
        }
    }
    
    private void highlightSecondDiagonale() {
        for (int y = 0; y < commonHighlights.length; y++) {
            for (int x = 0; x < commonHighlights[y].length; x++) {
                if (y + x == 3)
                    commonHighlights[y][x] = true;
            }
            
        }
    }
    
    private int getCommonAttributes(@NonNull List<GameObject> args) {
        if (args.size() < 2) return 0;
        byte common = 0;
        byte andResult = 0;
        byte andNotResult = 0;
        boolean isSet = false;
        for (GameObject go : args) {
            if (go == null) Log.d(TAG, "game object is null!!");
            if (! isSet) {
                andResult = go.getCode();
                andNotResult = (byte) ~ go.getCode();
                isSet = true;
            } else {
                andResult &= go.getCode();
                andNotResult &= ~ go.getCode();
            }
        }
        common = (byte) (andResult | andNotResult);
        int count = 0;
        if ((common & 0b1) != 0) {
            Log.d(TAG, "Common attribute found: HOLE");
            count++;
        }
        if ((common & 0b10) != 0) {
            Log.d(TAG, "Common attribute found: SHAPE");
            count++;
        }
        if ((common & 0b100) != 0) {
            Log.d(TAG, "Common attribute found: COLOR");
            count++;
        }
        if ((common & 0b1000) != 0) {
            Log.d(TAG, "Common attribute found: SIZE");
            count++;
        }
        return count;
    }
}
