package com.studio.chess.controller;

import com.studio.chess.App;
import com.studio.chess.model.Move;
import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.Piece;
import com.studio.chess.view.ChessBoardView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChessController {

    private final ChessBoard chessBoard;
    private final ChessBoardView chessBoardView;
    private Square selectedSquare; // Tracks the square the user clicks on
    private List<Square> listOfValidMoves; // Used for highlighting squares

    public ChessController(ChessBoard chessBoard, ChessBoardView chessBoardView) {
        this.chessBoard = chessBoard;
        this.chessBoardView = chessBoardView;
        selectedSquare = null;
        listOfValidMoves = new ArrayList<>();
        chessBoardView.setOnMouseClicked(this::handleMouseClick);
    } // ChessController

    /**
     *
     * @param event
     */
    private void handleMouseClick(MouseEvent event) {
        Square clickedSquare = selectSquare(event);
        // If the board isn't populated or the square that is being clicked is null (not initialized), then
        // the method exits and nothing happens
        if (!chessBoard.isPopulated() || clickedSquare == null) {
            return;
        }
        // Deselects the square if it is clicked a second time
        if (selectedSquare != null && selectedSquare.equals(clickedSquare)) {
            selectedSquare = null;
            listOfValidMoves.clear();
            chessBoardView.redraw();
            return;
        }
        // If a piece is selected and the next clicked on square is a valid square (included in the list of
        // that piece's valid moves)
        if (selectedSquare != null && listOfValidMoves.contains(clickedSquare)) {
            int ltc = chessBoard.getTurnCount(); // Last turn count
            boolean successful = chessBoard.movePiece(selectedSquare, clickedSquare);
            if (successful) {
                Move currentMove = chessBoard.getMoveHistory().peek();
                System.out.print(currentMove.getMoveNotation() + " ");
                if (chessBoard.isCheckmate(chessBoard.getCurrentTurn())) {
                    // TODO: Implement End Game Logic
                } else if (chessBoard.isStalemate(chessBoard.getCurrentTurn())) {
                    System.out.println("Stalemate! The game is drawn");
                    // TODO: Implement End Game Logic
                }
            }
            // Clear selection after the move attempt
            selectedSquare = null;
            listOfValidMoves.clear();
            chessBoardView.redraw();
            if (chessBoard.getTurnCount() > ltc) {
                System.out.print("\n" + chessBoard.getTurnCount() + ") ");
            }
            return;
        }
        // Handle selection of the pieces on the board
        Piece piece = clickedSquare.getPiece();
        if (piece != null) {
            if (App.isCustomMode()) {
                // If the game was set as a custom game, and a move has already been made, then enforce the turn
                // order based on the color moved first.
                if (chessBoard.getMoveHistory().size() > 2
                        && !piece.getColor().equals(chessBoard.getCurrentTurn())) {
                    return;
                }
                chessBoard.setCurrentTurn(piece.getColor());
            } else {
                // If the game was set as a new game, then white always moves first
                if (!piece.getColor().equals(chessBoard.getCurrentTurn())) {
                    return;
                }
            }
            // Selects the piece and highlights its valid moves onto the board view
            selectedSquare = clickedSquare;
            listOfValidMoves = piece.getValidMoves(chessBoard);
            chessBoardView.setHighlightedSquares(listOfValidMoves);
            chessBoardView.redraw();
            return;
        }
        // If the clicked square is empty (no piece occupying it) or is not a valid move for the selected piece,
        // then clear the selection
        selectedSquare = null;
        listOfValidMoves.clear();
        chessBoardView.redraw();
    } // handleMouseClick


    /**
     *
     * @param event
     * @return
     */
    private Square selectSquare(MouseEvent event) {
        double canvasWidth = chessBoardView.getWidth();
        double canvasHeight = chessBoardView.getHeight();
        // Subtract 30px used in draw
        double boardLeft = 30; // left offset
        double boardTop = 30; // top offset
        double boardRight = canvasWidth - 30;
        double boardBottom = canvasHeight - 30;
        double boardWidth = boardRight - boardLeft;
        double boardHeight = boardBottom - boardTop;
        double squareWidth = boardWidth / 8.0;
        double squareHeight = boardHeight / 8.0;
        // Compute the local offset of the mouse inside the board
        double relX = event.getX() - boardLeft;
        double relY = event.getY() - boardTop;
        if (relX < 0 || relY < 0) {
            return null;
        }
        int file = (int) (relX / squareWidth);
        int rank = (int) (relY / squareHeight);
        if (rank < 0 || rank >= 8 || file < 0 || file >= 8) {
            return null;
        }
        return chessBoard.getSquare(rank, file);
    } // selectSquare

} // ChessController