package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Rook extends Piece {

    /**
     *
     * @param color
     */
    public Rook(Color color) {
        super(color);
        setPieceNotation("R");
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_rdt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_rlt60.png"));
        }
        setWorth(5);
        setTurnCount(0);
    } // Rook

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
        return str + "Rook";
    } // toString

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        List<Square> rookValidMoves = new ArrayList<>();
        for (Square square : getAttackingSquares(board)) {
            // If the square is empty (no occupying piece), then it is considered a valid
            // move for the rook.
            Piece attackedPiece = square.getPiece();
            if (attackedPiece == null) {
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    rookValidMoves.add(square);
                }
                // If it isn't empty (piece is occupying the square), then check if the piece is an instance of a king
                // (which cannot be captured). If not a king, then check if the piece is a different color than
                // the rook. If so, then add the square to the list of valid moves
            } else if (!(attackedPiece instanceof King) && !attackedPiece.getColor().equals(this.getColor())) {
                if (board.simulateMove(this, getCurrentSquare(), square)) {
                    rookValidMoves.add(square);
                }
            }
        }
        return rookValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();
        int currentRank = getCurrentSquare().getRank();
        int currentFile = getCurrentSquare().getFile();
        // Get all the cardinal directions (north, south, east, west)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        // For every direction, add it to the current rank and file to get the next square
        for (int[] dir : directions) {
            int nextRank = currentRank + dir[0];
            int nextFile = currentFile + dir[1];
            // If the next rank and file (square next to) are located within the chessboard's bounds
            while (nextRank >= 0 && nextRank < 8 && nextFile >= 0 && nextFile < 8) {
                // Get the nextSquare add it to the list of attacking squares
                Square targetSquare = board.getSquare(nextRank, nextFile);
                if (targetSquare != null) {
                    attackingSquares.add(targetSquare);
                    // If the square contains a piece then, the rook cannot see anymore squares beyond it
                    if (targetSquare.getPiece() != null) {
                        break;
                    }
                }
                // Iterate to the next rank and file
                nextRank += dir[0];
                nextFile += dir[1];
            }
        }

        return attackingSquares;
    } // getAttackingSquares

} // Rook