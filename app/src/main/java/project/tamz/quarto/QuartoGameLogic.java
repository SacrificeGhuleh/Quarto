package project.tamz.quarto;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class QuartoGameLogic {
    private List<GameObject> availableGameObjects = new ArrayList<GameObject>();
    private GameObject gameBoard[][] = new GameObject[4][4];
    public QuartoGameLogic() {
        for (byte b = 0; b < 16; b++) {
            availableGameObjects.add(new GameObject(b));
        }

        for (GameObject go : availableGameObjects) {
            System.out.println(go + " loaded");
        }
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

    public boolean place(Point coords, GameObject gameObject) {
        if (coords.x < 0 || coords.y < 0 || coords.x > 4 || coords.y > 4)
            return false;

        if (gameBoard[coords.x][coords.y] == null) {
            gameBoard[coords.x][coords.y] = gameObject;
            return true;
        }
        return false;
    }
}
