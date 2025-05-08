package com.studio.chess.model.piece;

import com.studio.chess.model.Move;
import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Pawn extends Piece {

    private boolean movedTwo;

    /**
     *
     * @param color
     */
    public Pawn(Color color) {
        super(color);
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_pdt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_plt60.png"));
        }
        setWorth(1);
        setTurnCount(0);
        movedTwo = false;
    } // Pawn

    /**
     *
     * @return
     */
    public boolean hasMovedTwo() {
        return movedTwo;
    } // hasMovedTwo

    /**
     *
     * @param movedTwo
     */
    public void setMovedTwo(boolean movedTwo) {
        this.movedTwo = movedTwo;
    } // setMovedTwo

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
        return str + "Pawn";
    } // toString

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        // (White Moves up [-1], Black moves down [+1])
        List<Square> pawnValidMoves = new ArrayList<>();

        // Gather the direction that the piece is moving and get the current rank and file
        // Gather the direction that the piece is moving and get the current rank and file
        int dir = board.getForwardDirection(this);

        Square currentSquare = getCurrentSquare();
        int currentRank = currentSquare.getRank();
        int currentFile = currentSquare.getFile();

        // SINGLE STEP FORWARD
        Square singleStepSquare = board.getSquare(currentRank + dir, currentFile);
        // If the singleStepSquare is initialized and empty (has no occupying piece)
        if (singleStepSquare != null && singleStepSquare.getPiece() == null) {
            // If moving the pawn would put its king in check, then the move is not a valid move
            if (board.simulateMove(this, getCurrentSquare(), singleStepSquare)) {
                pawnValidMoves.add(singleStepSquare);
                // DOUBLE STEP FORWARD -- Cannot be done without a SINGLE STEP FORWARD
                // Can only perform a double step if it is this pawn's first move of the match
                boolean isFirstMove = this.getTurnCount() == 0;
                if (isFirstMove) {
                    Square doubleStepSquare = board.getSquare(currentRank + 2 * dir, currentFile);
                    // If moving the pawn would put its king in check, then the move is not a valid move
                    if (doubleStepSquare != null && doubleStepSquare.getPiece() == null) {
                        if (board.simulateMove(this, getCurrentSquare(), doubleStepSquare)) {
                            pawnValidMoves.add(doubleStepSquare);
                        }
                    }
                }
            }
        }

        // DIAGONAL CAPTURE -- EN PASSANT
        // For each square that is eligible for attack, check to see if the square has a piece, and
        // if it has a piece, make sure that it is of a different color than this pawn
        for (Square attackedSquare : getAttackingSquares(board)) {
            // If the piece is a different color than this pawn, then this is a valid move to make
            if (attackedSquare.getPiece() != null && !attackedSquare.getPiece().getColor().equals(this.getColor())) {
                if (board.simulateMove(this, this.getCurrentSquare(), attackedSquare)) {
                    pawnValidMoves.add(attackedSquare);
                }
            // If the diagonal square (on either side) is empty, and the target for en passant isn't null and
            // The square that can be attacked is the en passant target
            } else if (attackedSquare.getPiece() == null && board.getEnPassantTarget() != null
                    && attackedSquare.equals(board.getEnPassantTarget())) {
                Square behindSquare = board.getSquare(attackedSquare.getRank() - dir, attackedSquare.getFile());
                Piece capturePawn = behindSquare.getPiece();
                // Only add this to the list of valid moves if the piece is a pawn of a different color than
                // this one
                if (capturePawn instanceof Pawn && !capturePawn.getColor().equals(this.getColor())) {
                    if (board.simulateMove(this, this.getCurrentSquare(), attackedSquare)) {
                        pawnValidMoves.add(attackedSquare);
                    }
                }
            }
        }
        return pawnValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();

        // Gather the direction that the piece is moving and get the current rank and file
        int dir = board.getForwardDirection(this);
        // Gather the current rank and file of this pawn
        Square currentSquare = getCurrentSquare();
        int currentRank = currentSquare.getRank();
        int currentFile = currentSquare.getFile();

        // Pawns can attack squares diagonally to them (left or right) so they may
        // be added to this list
        Square leftDiag = board.getSquare(currentRank + dir, currentFile - 1);
        // If the left diagonal square is initialized (not null)
        if (leftDiag != null) {
            attackingSquares.add(leftDiag);
        }
        Square rightDiag = board.getSquare(currentRank + dir, currentFile + 1);
        // If the right diagonal square is initialized (not null)
        if (rightDiag != null) {
            attackingSquares.add(rightDiag);
        }
        return attackingSquares;
    } // getAttackingSquares

} // Pawn