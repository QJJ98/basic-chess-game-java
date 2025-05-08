package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class King extends Piece {

    private boolean castle;

    /**
     *
     * @param color
     */
    public King(Color color) {
        super(color);
        setPieceNotation("K");
        if (color.equals(Color.BLACK)) {
            setImage(new Image("/Chess Pieces/Chess_kdt60.png"));
        } else {
            setImage(new Image("/Chess Pieces/Chess_klt60.png"));
        }
        setWorth(-1);
        setTurnCount(0);
        castle = false;
    } // King

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
        return str + "King";
    } // toString

    /**
     *
     * @return
     */
    public boolean canCastle() {
        return castle;
    } // canCastle

    /**
     *
     * @param castle
     */
    public void setCastle(boolean castle) {
        this.castle = castle;
    } // setCastle

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getValidMoves(ChessBoard board) {
        List<Square> kingValidMoves = new ArrayList<>();
        Square current = getCurrentSquare();
        int currentRank = current.getRank();
        int currentFile = current.getFile();

        // All 8 possible directions
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int fr = currentRank + dir[0];
            int fc = currentFile + dir[1];
            if (isOnBoard(fr, fc)) {
                Square targetSquare = board.getSquare(fr, fc);
                Piece targetPiece = targetSquare.getPiece();
                // Skip if a friendly piece is there
                if (targetPiece != null && targetPiece.getColor().equals(this.getColor())) {
                    continue;
                }

                // Only add if simulating the move to that square doesn't result in the king
                // being put in check
                if (board.simulateMove(this, current, targetSquare)) {
                    kingValidMoves.add(targetSquare);
                }
            }
        }

        // CASTLING MOVES
        // Only allow castling if the king has not moved and is not in check.
        if (this.getTurnCount() == 0 && !board.isCheck(this.getColor())) {
            // Typically, kings start on file 4. Adjust if your board positions differ.
            // For queenside castling, the king moves two squares to the left.
            boolean canCastleQueen = true;
            // Check that squares between king and destination (currentFile-1 and currentFile-2) are empty and safe.
            for (int f = currentFile - 1; f >= currentFile - 3; f--) {
                // For custom placed kings, if the f is out of bounds, then don't check for castling as the king
                // is not in a position to castle
                if (f < 0) {
                    break;
                }
                Square square = board.getSquare(currentRank, f);
                List<Piece> threatsTo = square.getThreats();
                Piece piece = square.getPiece();
                if (piece != null || threatsTo.stream().anyMatch(e -> !e.getColor().equals(this.getColor()))) {
                    canCastleQueen = false;
                    break;
                }
            }
            // Also check that the square immediately next to the rook (at file 1) is empty (if needed by your rules)
            // and that the rook on the queenside (at file 0) is unmoved.
            Square queensideRookSquare = board.getSquare(currentRank, 0);
            if (queensideRookSquare.getPiece() instanceof Rook &&
                    queensideRookSquare.getPiece().getColor().equals(this.getColor()) &&
                    queensideRookSquare.getPiece().getTurnCount() == 0 &&
                    canCastleQueen) {
                // The king's destination for queenside castling is two squares to the left.
                kingValidMoves.add(board.getSquare(currentRank, currentFile - 2));
            }

            // For kingside castling, the king moves two squares to the right.
            boolean canCastleKing = true;
            // Check that squares between the king and its destination (currentFile+1 and currentFile+2) are empty and safe.
            for (int f = currentFile + 1; f <= currentFile + 2; f++) {
                // For custom placed kings, if the f is out of bounds, then don't check for castling as the king
                // is not in a position to castle
                if (f > 7) {
                    break;
                }
                Square s = board.getSquare(currentRank, f);
                List<Piece> threatsTo = s.getThreats();
                if (s.getPiece() != null || threatsTo.stream().anyMatch(e -> !e.getColor().equals(this.getColor()))) {
                    canCastleKing = false;
                    break;
                }
            }
            Square kingsideRookSquare = board.getSquare(currentRank, 7);
            if (kingsideRookSquare.getPiece() instanceof Rook &&
                    kingsideRookSquare.getPiece().getColor().equals(this.getColor()) &&
                    kingsideRookSquare.getPiece().getTurnCount() == 0 &&
                    canCastleKing) {
                // The king's destination for kingside castling is two squares to the right.
                kingValidMoves.add(board.getSquare(currentRank, currentFile + 2));
            }
        }
        return kingValidMoves;
    } // getValidMoves

    /**
     *
     * @param board
     * @return
     */
    @Override
    public List<Square> getAttackingSquares(ChessBoard board) {
        List<Square> attackingSquares = new ArrayList<>();
        Square current = getCurrentSquare();
        // The king can attack any adjacent square.
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };
        int currentRank = current.getRank();
        int currentFile = current.getFile();
        for (int[] dir : directions) {
            int nextRank = currentRank + dir[0];
            int nextFile = currentFile + dir[1];
            if (nextRank >= 0 && nextRank < 8 && nextFile >= 0 && nextFile < 8) {
                attackingSquares.add(board.getSquare(nextRank, nextFile));
            }
        }
        return attackingSquares;
    } // getAttackingSquares

    /**
     *
     * @param rank
     * @param file
     * @return
     */
    private boolean isOnBoard(int rank, int file) {
        return (rank >= 0 && rank < 8 && file >= 0 && file < 8);
    } // isOnBoard


} // King