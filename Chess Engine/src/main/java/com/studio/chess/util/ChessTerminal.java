package com.studio.chess.util;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.*;

import java.awt.*;

/**
 *
 */
public class ChessTerminal {

    private String[][] pieceBoard;
    private String blackPawn = "p", whitePawn = "P";
    private String blackRook = "r", whiteRook = "R";
    private String blackKnight = "n", whiteKnight = "N";
    private String blackBishop = "b", whiteBishop = "B";
    private String blackQueen = "q", whiteQueen = "Q";
    private String blackKing = "k", whiteKing = "K";


    private ChessBoard chessBoard;

    /**
     *
     */
    public ChessTerminal(ChessBoard chessBoard) {
        pieceBoard = new String[8][8];
        this.chessBoard = chessBoard;
    } // ChessTerminal

    /**
     *
     */
    public void reflectMove() {
        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                Square square = chessBoard.getSquare(r, f);
                Piece piece = square.getPiece();
                if (piece != null) {
                    String pStr = "";
                    if (piece instanceof Pawn) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackPawn;
                        } else {
                            pStr = whitePawn;
                        }
                    } else if (piece instanceof Rook) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackRook;
                        } else {
                            pStr = whiteRook;
                        }
                    } else if (piece instanceof Knight) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackKnight;
                        } else {
                            pStr = whiteKnight;
                        }
                    } else if (piece instanceof Bishop) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackBishop;
                        } else {
                            pStr = whiteBishop;
                        }
                    } else if (piece instanceof Queen) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackQueen;
                        } else {
                            pStr = whiteQueen;
                        }
                    } else if (piece instanceof King) {
                        if (piece.getColor().equals(Piece.Color.BLACK)) {
                            pStr = blackKing;
                        } else {
                            pStr = whiteKing;
                        }
                    }
                    pieceBoard[r][f] = pStr;
                }
            }
        }
    } // reflectMove

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String boardStr = "";
        for (int r = 0; r < pieceBoard.length; r++) {
            for (int f = 0; f < pieceBoard[r].length; f++) {
                if (r == 0) {
                    boardStr += "|";
                } else if (r == 7) {
                    boardStr += "|";
                }
                if (pieceBoard[r][f].isEmpty()) {
                    boardStr += "|  |";
                } else {
                    boardStr += "| " + pieceBoard[r][f] + " |";
                }
            }
        }
        return boardStr;
    } // toString

    public static void main(String[] args) {

    } // main

} // ChessTerminal