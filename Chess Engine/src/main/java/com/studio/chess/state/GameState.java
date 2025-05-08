package com.studio.chess.state;

import com.studio.chess.controller.AIController;
import com.studio.chess.controller.ChessController;
import com.studio.chess.model.Move;
import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.King;
import com.studio.chess.model.piece.Piece;

import java.util.List;

/**
 *
 */
public class GameState {

    private ChessBoard chessBoard;
    private ChessController controller;
    private AIController aiController;
    private List<Piece> blackPieces, whitePieces;
    private List<Piece> capturedBlackPieces, capturedWhitePieces;
    private int currentTurn;

    /**
     *
     * @param chessBoard
     */
    public GameState(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        blackPieces = chessBoard.getBlackPieces();
        whitePieces = chessBoard.getWhitePieces();
        capturedBlackPieces = chessBoard.getCapturedBlackPieces();
        capturedWhitePieces = chessBoard.getCapturedWhitePieces();
        currentTurn = 1;
    } // GameState

    /**
     *
     * @param color
     */
    public void makeMove(Piece.Color color) {

    } // makeMove


    /**
     *
     * @return
     */
    public boolean checkMateCondition(Piece.Color color) {

        return false;
    } // checkMateCondition

    /**
     *
     * @return
     */
    public boolean staleMateCondition() {

        return true;
    } // staleMateCondition

} // GameState