package project.tamz.quarto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard Zvonek on 03/12/2017.
 */

public class QuartoGameLogic {
    List<GameObject> gameObjects = new ArrayList<GameObject>();

    public QuartoGameLogic() {
        for (byte b = 0; b < 16; b++) {
            gameObjects.add(new GameObject(b));
        }

        for (GameObject go : gameObjects) {
            System.out.println(go + " loaded");
        }
    }
}
