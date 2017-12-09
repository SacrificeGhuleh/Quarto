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
    private GameObject selectedObject = null;
    private boolean selecting = false;
    private boolean placing = false;
    
    public QuartoGameLogic() {
        for (byte b = 0; b < 16; b++) {
            availableGameObjects.add(new GameObject(b));
        }
        
        for (GameObject go : availableGameObjects) {
            System.out.println(go + " loaded");
        }
        
        selecting = true;
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
    
    private boolean hasCommon(GameObject args[]) {
        int val = 0;
        
        for (GameObject go : args) {
            val &= go.getCode();
        }
        
        return val != 0;
    }
    
    public boolean place(Point coords) {
        if (coords.x < 0 || coords.y < 0 || coords.x > 4 || coords.y > 4)
            return false;
        
        if (gameBoard[coords.x][coords.y] == null && selectedObject != null) {
            gameBoard[coords.x][coords.y] = selectedObject;
            
            selectedObject = null;
            
            placing = false;
            selecting = true;
            
            return true;
        }
        return false;
    }
}
