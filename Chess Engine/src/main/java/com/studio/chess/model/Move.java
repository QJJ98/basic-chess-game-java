package com.studio.chess.model;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.Pawn;
import com.studio.chess.model.piece.Piece;

/**
 *
 */
public class Move {
    private final ChessBoard chessBoardState;
    private final Piece piece, capturedPiece;
    private final Square from, to;
    private boolean isCastling = false;
    private boolean isEnPassant = false;
    private boolean isPromotion = false;
    private Piece promotedPiece;
    private boolean isCheck;
    private boolean isCheckMate;

    /**
     *
     * @param piece
     */
    public Move(ChessBoard chessBoardState, Piece piece, Square from, Square to, Piece capturedPiece) {
        this.chessBoardState = chessBoardState;
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
    } // Move

    /**
     *
     * @param piece
     * @param from
     * @param to
     * @param capturedPiece
     * @param isCastling
     * @param isEnPassant
     * @param isPromotion
     */
    public Move(ChessBoard chessBoardState, Piece piece, Square from, Square to, Piece capturedPiece,
                boolean isCastling, boolean isEnPassant, boolean isPromotion, Piece promotedPiece,
                boolean isCheck, boolean isCheckMate) {
        this.chessBoardState = chessBoardState;
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
        this.isPromotion = isPromotion;
        this.promotedPiece = promotedPiece;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
    } // Move

    /**
     *
     * @return
     */
    public ChessBoard getChessBoardState() {
        return chessBoardState;
    } // getChessBoard

    /**
     *
     * @return
     */
    public Piece getPiece() {
        return piece;
    } // getPiece

    /**
     *
     * @return
     */
    public Square getFrom() {
        return from;
    } // getFrom

    /**
     *
     * @return
     */
    public Square getTo() {
        return to;
    } // getTo

    /**
     *
     * @return
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    } // getCapturedPiece

    /**
     *
     * @return
     */
    public boolean isCastling() {
        return isCastling;
    } // isCastling

    /**
     *
     * @return
     */
    public boolean isEnPassant() {
        return isEnPassant;
    } // isEnPassant

    /**
     *
     * @return
     */
    public boolean isPromotion() {
        return isPromotion;
    } // isPromotion

    /**
     *
     * @return
     */
    public Piece getPromotedPiece() {
        return promotedPiece;
    } // getPromotedPiece


    /**
     *
     * @return
     */
    public String getMoveNotation() {
        if (isCastling) {
            return (to.getFile() == 6) ? "0-0" : "0-0-0";
        }
        StringBuilder notation = new StringBuilder();
        // Gets the piece notation
        if (!(piece instanceof Pawn)) {
            notation.append(piece.getPieceNotation());
        }
        // Notate any captures
        if (capturedPiece != null) {
            if (piece instanceof Pawn) {
                notation.append(getFileLetter(from.getFile()));
            }
            notation.append("x");
        }

        // Append destination square
        notation.append(getSquareNotation(to));

        // Append en passant capture
        if (isEnPassant) {
            notation.append(" e.p.");
        }

        // Promotion notation
        if (isPromotion) {
            notation.append("=").append(promotedPiece.getPieceNotation());
        }

        // Check|Checkmate notation
        if (isCheck) {
            notation.append("+");
        } else if (isCheckMate) {
            notation.append("#");
        }

        return notation.toString();
    } // notateMove

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Move{" +
                "piece = " + piece +
                ", capturedPiece = " + capturedPiece +
                ", from = " + from +
                ", to = " + to +
                ", isCastling = " + isCastling +
                ", isEnPassant = " + isEnPassant +
                ", isPromotion = " + isPromotion +
                ", promotedPiece = " + promotedPiece +
                '}';
    } // toString

    /**
     *
     * @param square
     * @return
     */
    private String getSquareNotation(Square square) {
        return getFileLetter(square.getFile()) + (8 - square.getRank());
    } // getSquareNotation

    /**
     *
     * @param file
     * @return
     */
    private String getFileLetter(int file) {
        return String.valueOf((char) ('a' + file));
    } // getFile

} // Move