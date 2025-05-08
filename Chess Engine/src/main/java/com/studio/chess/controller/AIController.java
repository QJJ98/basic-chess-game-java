package com.studio.chess.controller;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.view.ChessBoardView;

public class AIController {

    private ChessBoard chessBoard;
    private ChessBoardView chessBoardView;

    /**
     *
     * @param chessBoard
     * @param chessBoardView
     */
    public AIController(ChessBoard chessBoard, ChessBoardView chessBoardView) {
        this.chessBoard = chessBoard;
        this.chessBoardView = chessBoardView;
    } // AIController

} // AIController