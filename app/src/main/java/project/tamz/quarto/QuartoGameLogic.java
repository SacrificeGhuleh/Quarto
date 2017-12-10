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
    }
    
    public boolean[][] getCommonHighlights() {
        return commonHighlights;
    }
    
    public GameObject[][] getPlacedObjects() {
        return gameBoard;
    }
    
    public boolean isSelecting() {
        return selecting;
    }
    
    public boolean isPlacing() {
        return placing;
    }
    
    public GameObject getSelectedObject() {
        return selectedObject;
    }
    
    public boolean place(int x, int y) {
        if (selectedObject == null || gameBoard[y][x] != null) return false;
        
        gameBoard[y][x] = selectedObject;
        selectedObject = null;
        
        placing = false;
        selecting = true;
        
        return true;
    }
    
    public boolean setSelectedObject(@NonNull GameObject selectedObject) {
        if (availableGameObjects.contains(selectedObject)) {
            availableGameObjects.remove(selectedObject);
            this.selectedObject = selectedObject;
            Log.d(TAG, "Found selected object");
    
            selecting = false;
            placing = true;
            
            return true;
        }
        Log.d(TAG, "setSelectedObject unsuccessful");
        return false;
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
    
            checkGameBoard();
            
            return true;
        }
        return false;
    }
    
    private boolean checkGameBoard() {
        //check rows and cols
        List<GameObject> rows = new ArrayList<>();
        List<GameObject> cols = new ArrayList<>();
        
        boolean foundCommonAttribute = false;
        
        for (int y = 0; y < 4; y++) {
            rows.clear();
            cols.clear();
            for (int x = 0; x < 4; x++) {
                if (gameBoard[y][x] != null)
                    cols.add(gameBoard[y][x]);
                
                if (gameBoard[x][y] != null)
                    rows.add(gameBoard[x][y]);
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
        
        return foundCommonAttribute;
    }
    
    private boolean hasCommon(@NonNull List<GameObject> args) {
        boolean valIsSet = false;
        
        int result = 0;
        
        if (args.size() == 4) {
            //TODO: This is stupid, but works... So it is not stupid?
            result = ~ ((args.get(0).getCode() & args.get(1).getCode() & args.get(2).getCode() & args.get(3).getCode()) | (~ args.get(0).getCode() & ~ args.get(1).getCode() & ~ args.get(2).getCode() & ~ args.get(3).getCode()));
            return result != 0b1111;
        }
        
        Log.d(TAG, "XOR value: " + result + " Binary: " + Integer.toBinaryString(result));
        
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
}
