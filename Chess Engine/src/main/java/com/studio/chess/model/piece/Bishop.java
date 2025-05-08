package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Bishop extends Piece {

    /**
     *
     * @param color
     */
    public Bishop(Color color) {
        super(color);
        setPieceNotation("B");
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_bdt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_blt60.png"));
        }
        setWorth(3);
        setTurnCount(0);
    } // Bishop

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
        return str + "Bishop";
    } // toString

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        List<Square> bishopValidMoves = new ArrayList<>();
        for (Square square : getAttackingSquares(board)) {
            Piece piece = square.getPiece();
            // If the square contains a piece, then check if it is a king, and it's the same color as this bishop
            // If neither of these conditions are true, then add the square to the list of valid move
            if (piece != null) {
                if (!(piece instanceof King) && !piece.getColor().equals(this.getColor())) {
                    if (board.simulateMove(this, getCurrentSquare(), square)) {
                        bishopValidMoves.add(square);
                    }
                }
            // If the square does not contain a piece, then add it to the list of valid moves
            } else {
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    bishopValidMoves.add(square);
                }
            }
        }
        return bishopValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();
        // Get all directions (diagonal)
        int presentRank = getCurrentSquare().getRank();
        int presentFile = getCurrentSquare().getFile();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : directions) {
            int nextRank = presentRank + dir[0];
            int nextFile = presentFile + dir[1];
            // If the next rank and file (square next to) are located within the chessboard's bounds
            while (nextRank >= 0 && nextRank < 8 && nextFile >= 0 && nextFile < 8) {
                // Get the nextSquare add it to the list of attacking squares
                Square targetSquare = board.getSquare(nextRank, nextFile);
                if (targetSquare != null) {
                    attackingSquares.add(targetSquare);
                    // If the square contains a piece, terminate the loop as the bishop cannot see or move
                    // beyond the piece
                    Piece piece = targetSquare.getPiece();
                    if (piece != null) {
                        break;
                    }
                }
                nextRank += dir[0];
                nextFile += dir[1];
            }
        }
        return attackingSquares;
    } // getAttackingSquares

} // Bishop