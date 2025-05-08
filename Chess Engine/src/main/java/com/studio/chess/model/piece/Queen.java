package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Queen extends Piece {

    /**
     *
     * @param color
     */
    public Queen(Color color) {
        super(color);
        setPieceNotation("Q");
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_qdt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_qlt60.png"));
        }
        setWorth(9);
        setTurnCount(0);
    } // Queen

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String str = "";
        if (getColor().equals(Color.BLACK)) {
            str += "Black ";
        } else {
            str += "White ";
        }
        return str + "Queen";
    } // toString

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        List<Square> queenValidMoves = new ArrayList<>();
        for (Square square : getAttackingSquares(board)) {
            // If the square is not empty, and the piece is not of this color
            if (square.getPiece() == null) {
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    queenValidMoves.add(square);
                }
            } else if (!square.getPiece().getColor().equals(this.getColor())) {
                if (!(square.getPiece() instanceof King)) {
                    if (board.simulateMove(this, getCurrentSquare(), square)) {
                        queenValidMoves.add(square);
                    }
                }
            }
        }
        return queenValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();
        // Get present rank and file of this Queen piece
        int presentRank = getCurrentSquare().getRank();
        int presentFile = getCurrentSquare().getFile();
        // Get all available directions
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // cardinal directions
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // diagonal directions
        };
        // For each set of directions that the queen can move in
        for (int[] dir : directions) {
            int nextRank = presentRank + dir[0];
            int nextFile = presentFile + dir[1];
            // While the next square is on the board, then add it to the list of squares
            // that can be attacked by the Queen
            while (nextRank >= 0 && nextRank < 8 && nextFile >= 0 && nextFile < 8) {
                Square targetSquare = board.getSquare(nextRank, nextFile);
                attackingSquares.add(targetSquare);
                Piece piece = targetSquare.getPiece();
                if (piece != null) {
                    break;
                }
                nextRank += dir[0];
                nextFile += dir[1];
            }
        }
        return attackingSquares;
    } // getAttackingSquares

} // Queen