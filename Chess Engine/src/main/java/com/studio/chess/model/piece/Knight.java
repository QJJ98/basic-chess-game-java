package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Knight extends Piece{

    /**
     *
     * @param color
     */
    public Knight(Color color) {
        super(color);
        setPieceNotation("N");
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_ndt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_nlt60.png"));
        }
        setWorth(3);
        setTurnCount(0);
    } // Knight

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
        return str + "Knight";
    } // toString

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        List<Square> knightValidMoves = new ArrayList<>();
        // For each square in the list of attacking squares, check to see if it has a piece
        for (Square square : getAttackingSquares(board)) {
            // If the square is empty, then add it to the list of valid moves
            if (square.getPiece() == null) {
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    knightValidMoves.add(square);
                }
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    knightValidMoves.add(square);
                }
            // Otherwise, check to see if the piece is a King and the color
            } else {
                if (!(square.getPiece() instanceof King) && !square.getPiece().getColor().equals(this.getColor())) {
                    if (board.simulateMove(this, getCurrentSquare(), square)) {
                        if (board.simulateMove(this, getCurrentSquare(), square)) {
                            knightValidMoves.add(square);
                        }
                    }
                }
            }
        }
        return knightValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();
        // Get the current rank and file of this Knight
        int presentRank = getCurrentSquare().getRank();
        int presentFile = getCurrentSquare().getFile();

        // Get the possible movement directions
        int[][] directions = {
                {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
                {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };
        for (int[] dir : directions) {
            int nextRank = presentRank + dir[0];
            int nextFile = presentFile + dir[1];

            if (nextRank >= 0 && nextRank < 8 && nextFile >= 0 && nextFile < 8) {
                Square targetSquare = board.getSquare(nextRank, nextFile);
                attackingSquares.add(targetSquare);
            }
        }
        return attackingSquares;
    } // getAttackingSquares

} // Knight