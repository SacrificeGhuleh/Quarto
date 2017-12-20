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
    private GameObject[][] remainingPossibleBoard = null;
    private int remaining = 0;
    
    public GameArtificialIntelligence(@NonNull QuartoGameLogic qgm) {
        this.game = qgm;
        difficulty = SettingsActivity.getDifficulty();
        Log.d("AI", "Difficulty: " + difficulty);
    }
    
    public void analyzeBoard() {
        if (game == null) return;
    
        if (game.getPlacedCount() < 4) {
            getToPlaceRandom();
            getToSelectRandom();
            return;
        }
    
        if (difficulty < 2) {
            getToPlace();
            getToSelect();
        } else {
            analyzeBoardAlphaBeta(difficulty - 1);
        }
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
        List<Point> availablePlaces = getAvailablePlaces(game.getPlacedObjects());
        
        game.printBoard();
        Log.d("AI", "getToPlaceRandom: ");
        Log.d("AI", "AI: available to place: " + availablePlaces.toString());
        /*
        * Random choosing
        * */
        int availableCount = availablePlaces.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toPlace = availablePlaces.get(0);
                return true;
            }
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
            toPlace = availablePlaces.get(rand);
            Log.d("AI", "AI: to place: " + toPlace);
            return true;
        }
        return false;
    }
    
    private boolean getToPlaceBeginner() {
        Log.d("AI", "Placing as beginner ");
        if (game == null) return false;
        GameObject selected = game.getSelectedObject();
        final GameObject placed[][] = game.getPlacedObjects();
        List<Point> availablePlaces = getAvailablePlaces(game.getPlacedObjects());
        GameObject[][] placedCopy;
        for (Point p : availablePlaces) {
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
    
        List<GameObject> availableGameObjects = game.getAvailableGameObjects();
    
        int availableCount = availableGameObjects.size();
        if (availableCount > 0) {
            if (availableCount == 1) {
                toSelect = availableGameObjects.get(0);
                return true;
            }
            int min = 0;
            int max = availableCount;
            
            int rand = ThreadLocalRandom.current().nextInt(min, max);
    
            toSelect = availableGameObjects.get(rand);
            return true;
        }
        return false;
    }
    
    private boolean getToSelectBeginner() {
        Log.d("AI", "Selecting as beginner");
        if (game == null) return false;
    
        List<GameObject> availableGameObjects = game.getAvailableGameObjects();
        final GameObject[][] placedObjects = game.getPlacedObjects();
        List<Point> availablePlaces = new ArrayList<>();
    
        for (int row = 0; row < placedObjects.length; row++) {
            for (int col = 0; col < placedObjects[row].length; col++) {
                if (placedObjects[row][col] == null) availablePlaces.add(new Point(row, col));
            }
        }
        boolean suitable;
        List<GameObject> goodForPlace = new ArrayList<>();
        for (GameObject go : availableGameObjects) {
            suitable = true;
            innerLoop:
            for (Point p : availablePlaces) {
                GameObject[][] placedCopy = GameObject.clone(placedObjects);
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
    
    private List<Point> getAvailablePlaces(GameObject[][] board) {
        List<Point> available = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == null) available.add(new Point(row, col));
            }
        }
        return available;
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
    
    private void analyzeBoardAlphaBeta(int depth) {
        AlphaBetaResult result = alphabeta(game.getPlacedObjects(), game.getSelectedObject(), game.getAvailableGameObjects(), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        
        toSelect = result.piece;
        toPlace = result.field;
        
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
    
    public AlphaBetaResult alphabeta(GameObject[][] board, GameObject piece, List<GameObject> availablePieces, int depth, int alpha, int beta, boolean maximize) {
        AlphaBetaResult bestMove = new AlphaBetaResult();
        
        if (checkBoard(board)) { // check victory
            bestMove.score = maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            return bestMove;
        }
        
        if (availablePieces.isEmpty()) {
            bestMove.score = (maximize ? - 1 : 1) * lastPieceState(board, piece);
            return bestMove;
        }
        
        if (depth == 0) { // leaf node, evaluate
            bestMove.score = (maximize ? - 1 : 1) * evalGameState(board, piece, availablePieces);
            return bestMove;
        }
        
        //Point possibleField;
        GameObject[][] possibleBoard;
        List<GameObject> possibleAvailablePieces;
        
        for (Point possibleField : getAvailablePlaces(board)) {
            for (GameObject possiblePiece : availablePieces) {
                possibleBoard = GameObject.clone(board);//board.clone();
                if (possibleBoard[possibleField.y][possibleField.x] == null)
                    possibleBoard[possibleField.y][possibleField.x] = possiblePiece;
                possibleAvailablePieces = new ArrayList<>(availablePieces);//.clone();
                possibleAvailablePieces.remove(possiblePiece);
                if (maximize) {
                    AlphaBetaResult result = alphabeta(possibleBoard, possiblePiece, possibleAvailablePieces, depth - 1, alpha, beta, false);
                    
                    if (result.score > alpha) {
                        alpha = result.score;
                        bestMove.field = possibleField;
                        bestMove.piece = possiblePiece;
                    }
                    
                    if (alpha >= beta) { // prune
                        break;
                    }
                } else {
                    AlphaBetaResult result = alphabeta(possibleBoard, possiblePiece, possibleAvailablePieces, depth - 1, alpha, beta, true);
                    
                    if (result.score < beta) {
                        beta = result.score;
                        bestMove.field = possibleField;
                        bestMove.piece = possiblePiece;
                    }
                    
                    if (alpha >= beta) { // prune
                        break;
                    }
                }
            }
            if (alpha >= beta) { // prune
                break;
            }
        }
        bestMove.score = maximize ? alpha : beta;
        return bestMove;
    }
    
    private int lastPieceState(GameObject[][] board, GameObject piece) {
        try {
            List<Point> availablePlaces = getAvailablePlaces(board);
            Point p = availablePlaces.get(0);
            board[p.y][p.x] = piece;
            return checkBoard(board) ? Integer.MIN_VALUE : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private int evalGameState(GameObject[][] board, GameObject piece, List<GameObject> availablePieces) {
        int score = 0;
        score += remainingPiecesScore(board, piece, availablePieces);
        return score;
    }
    
    private int remainingPiecesScore(GameObject[][] board, GameObject piece, List<GameObject> availablePieces) {
        if (! GameObject.hasSomeMatrix(board, remainingPossibleBoard)) {
            if (remainingPossibleBoard != null)
                remainingPossibleBoard = null;
            
            remainingPossibleBoard = GameObject.clone(board);
            
            for (GameObject go : availablePieces) {
                for (Point p : getAvailablePlaces(remainingPossibleBoard)) {
                    if (remainingPossibleBoard[p.y][p.x] != null) continue;
                    remainingPossibleBoard[p.y][p.x] = go;
                    boolean victory = this.checkBoard(remainingPossibleBoard);
                    remainingPossibleBoard[p.y][p.x] = null;
                    
                    if (victory) {
                        remaining--;
                        break;
                    }
                    
                }
            }
        }
        return (remaining % 2 == 1) ? (Integer.MAX_VALUE - remaining) : (Integer.MIN_VALUE + remaining);
    }
    
    static class AlphaBetaResult {
        int score;
        GameObject piece;
        Point field;
        
    }
}
