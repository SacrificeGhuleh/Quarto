package project.tamz.quarto;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        for (int row = 0; row < commonHighlights.length; row++) {
            for (int col = 0; col < commonHighlights[row].length; col++) {
                commonHighlights[row][col] = false;
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
    
    public int getPlacedCount() {
        int placedNum = 0;
        
        for (GameObject g[] : gameBoard) {
            for (GameObject go : g) {
                if (go != null) placedNum++;
            }
        }
        
        return placedNum;
    }
    
    public void printBoard() {
        
        for (int row = 0; row < 4; row++) {
            String s = "";
            for (int col = 0; col < 4; col++) {
                if (gameBoard[row][col] == null) s += "  ";
                else s += "x ";
            }
            Log.d("Game board:", s);
        }
    }
    
    public boolean isPlayerWon() {
        return playerWon;
    }
    
    public boolean isGameEnd() {
        return checkGameBoard();
    }
    
    private boolean checkGameBoard() {
        List<GameObject> rows = new ArrayList<>();
        List<GameObject> cols = new ArrayList<>();
        List<GameObject> firstDiagonal = new ArrayList<>();
        List<GameObject> secondDiagonal = new ArrayList<>();
        
        boolean foundCommonAttribute = false;
        for (int row = 0; row < 4; row++) {
            rows.clear();
            cols.clear();
            if (gameBoard[row][row] != null)
                firstDiagonal.add(gameBoard[row][row]);
            for (int col = 0; col < 4; col++) {
                if (gameBoard[row][col] != null)
                    cols.add(gameBoard[col][row]);
            
                if (gameBoard[row][col] != null)
                    rows.add(gameBoard[row][col]);
            
                if (row + col == 3 && gameBoard[row][col] != null)
                    secondDiagonal.add(gameBoard[row][col]);
            }
            if (rows.size() == 4) {
                if (hasCommon(rows)) {
                    Log.d("Game Logic:", "Common attribute found in row " + row);
                    foundCommonAttribute = true;
                    highlightRow(row);
                }
            }
            
            if (cols.size() == 4) {
                if (hasCommon(cols)) {
                    Log.d("Game Logic:", "Common attribute found in column " + row);
                    foundCommonAttribute = true;
                    highlightCol(row);
                }
            }
        }
    
        if (firstDiagonal.size() == 4) {
            if (hasCommon(firstDiagonal)) {
                Log.d("Game Logic:", "Common attribute found in first diagonale.");
                foundCommonAttribute = true;
                highlightFirstDiagonale();
            }
        }
    
        if (secondDiagonal.size() == 4) {
            if (hasCommon(secondDiagonal)) {
                Log.d("Game Logic:", "Common attribute found in second diagonale.");
                foundCommonAttribute = true;
                highlightSecondDiagonale();
            }
        }
        return foundCommonAttribute;
    }
    
    public static boolean hasCommon(@NonNull List<GameObject> args) {
        int commonAttributes = getCommonAttributes(args);
        if (args.size() == 4) {
            Log.d("Game Logic:", "Number of common attributes: " + commonAttributes);
            return commonAttributes != 0;
        }
        return false;
    }
    
    private void highlightRow(int row) {
        for (int col = 0; col < commonHighlights[row].length; col++)
            commonHighlights[row][col] = true;
    }
    
    private void highlightCol(int col) {
        for (int row = 0; row < commonHighlights.length; row++)
            commonHighlights[row][col] = true;
    }
    
    private void highlightFirstDiagonale() {
        for (int rowcol = 0; rowcol < 4; rowcol++) {
            commonHighlights[rowcol][rowcol] = true;
        }
    }
    
    private void highlightSecondDiagonale() {
        for (int row = 0; row < commonHighlights.length; row++) {
            for (int col = 0; col < commonHighlights[row].length; col++) {
                if (row + col == 3)
                    commonHighlights[row][col] = true;
            }
        }
    }
    
    public static int getCommonAttributes(@NonNull List<GameObject> args) {
        if (args.size() < 2) return 0;
        byte common = 0;
        byte andResult = 0;
        byte andNotResult = 0;
        boolean isSet = false;
        for (GameObject go : args) {
            if (go == null) return 0;
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
            Log.d("Game Logic:", "Common attribute found: HOLE");
            count++;
        }
        if ((common & 0b10) != 0) {
            Log.d("Game Logic:", "Common attribute found: SHAPE");
            count++;
        }
        if ((common & 0b100) != 0) {
            Log.d("Game Logic:", "Common attribute found: COLOR");
            count++;
        }
        if ((common & 0b1000) != 0) {
            Log.d("Game Logic:", "Common attribute found: SIZE");
            count++;
        }
        return count;
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
            Log.d("Game Logic:", "Found selected object");
            
            selecting = false;
            placing = true;
            AIMove = ! AIMove;
            return true;
        }
        Log.d("Game Logic:", "setSelectedObject unsuccessful");
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
}
