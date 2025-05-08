package com.studio.chess.view;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

public class CustomContextMenus {

    private ChessBoardView chessBoardView;
    private ChessBoard chessBoard;
    private ContextMenu pieceSelectionMenu;
    private ContextMenu optionMenu;
    private int clickedRank = -1;
    private int clickedFile = -1;
    private double lastScreenX, lastScreenY;

    /**
     *
     * @param chessBoard
     * @param chessBoardView
     */
    public CustomContextMenus(ChessBoard chessBoard, ChessBoardView chessBoardView) {
        this.chessBoard = chessBoard;
        this.chessBoardView = chessBoardView;
    } // CustomContextMenus

    /**
     *
     */
    public void enableFreePlacementMenu() {
        pieceSelectionMenu = new ContextMenu();

        // Example piece menu items:
        MenuItem bp = new MenuItem("Black Pawn");
        MenuItem wp = new MenuItem("White Pawn");
        MenuItem br = new MenuItem("Black Rook");
        MenuItem wr = new MenuItem("White Rook");
        MenuItem bn = new MenuItem("Black Knight");
        MenuItem wn = new MenuItem("White Knight");
        MenuItem bb = new MenuItem("Black Bishop");
        MenuItem wb = new MenuItem("White Bishop");
        MenuItem bq = new MenuItem("Black Queen");
        MenuItem wq = new MenuItem("White Queen");
        MenuItem bk = new MenuItem("Black King");
        MenuItem wk = new MenuItem("White King");

        // Add menu items to context menu
        pieceSelectionMenu.getItems().addAll(bp, wp, br, wr, bn, wn, bb, wb, bq, wq, bk, wk);

        // For each menu item add an event handler
        bp.setOnAction(e -> placePieceInClickedSquare(new Pawn(Piece.Color.BLACK)));
        wp.setOnAction(e -> placePieceInClickedSquare(new Pawn(Piece.Color.WHITE)));
        br.setOnAction(e -> placePieceInClickedSquare(new Rook(Piece.Color.BLACK)));
        wr.setOnAction(e -> placePieceInClickedSquare(new Rook(Piece.Color.WHITE)));
        bn.setOnAction(e -> placePieceInClickedSquare(new Knight(Piece.Color.BLACK)));
        wn.setOnAction(e -> placePieceInClickedSquare(new Knight(Piece.Color.WHITE)));
        bb.setOnAction(e -> placePieceInClickedSquare(new Bishop(Piece.Color.BLACK)));
        wb.setOnAction(e -> placePieceInClickedSquare(new Bishop(Piece.Color.WHITE)));
        bq.setOnAction(e -> placePieceInClickedSquare(new Queen(Piece.Color.BLACK)));
        wq.setOnAction(e -> placePieceInClickedSquare(new Queen(Piece.Color.WHITE)));
        bk.setOnAction(e -> placePieceInClickedSquare(new King(Piece.Color.BLACK)));
        wk.setOnAction(e -> placePieceInClickedSquare(new King(Piece.Color.WHITE)));

        // Make an occupied Square context menu
        optionMenu = new ContextMenu();
        MenuItem replace = new MenuItem("Replace");
        MenuItem delete = new MenuItem("Delete");
        optionMenu.getItems().addAll(replace, delete);

        // When the user right-clicks on the board, the context menu for piece selection is shown
        chessBoardView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                lastScreenX = event.getScreenX();
                lastScreenY = event.getScreenY();

                // Local coordinates for identifying what square was clicked
                recordClickedSquare(event.getX(), event.getY());

                // Check if valid Square
                if (clickedRank >= 0 && clickedFile >= 0) {
                    Piece piece = chessBoard.getSquare(clickedRank, clickedFile).getPiece();
                    if (piece == null) {
                        pieceSelectionMenu.show(chessBoardView, event.getScreenX(), event.getScreenY());
                        optionMenu.hide();
                    } else {
                        optionMenu.show(chessBoardView, event.getScreenX(), event.getScreenY());
                        pieceSelectionMenu.hide();
                    }
                }
            // If any other mouse button is pressed or clicked, then close the menus
            } else {
                pieceSelectionMenu.hide();
                optionMenu.hide();
            }
        });
        // Set On Actions (Replace shows original piece selection menu)
        replace.setOnAction(e -> {
            optionMenu.hide();
            pieceSelectionMenu.show(chessBoardView, lastScreenX, lastScreenY);
        });
        delete.setOnAction(e -> {
            removePieceInClickedSquare();
            pieceSelectionMenu.hide();
            optionMenu.hide();
        });
    } // enableCustomPlacementMenu

    /**
     *
     */
    public void disableFreePlacementMenu() {
        chessBoardView.setOnMouseClicked(null);
        // If the context menu is open, then close it
        if (pieceSelectionMenu != null) {
            pieceSelectionMenu.hide();
            pieceSelectionMenu = null;
        }
    } // disableCustomPlacement

    /**
     *
     * @param piece
     */
    private void placePieceInClickedSquare(Piece piece) {
        if (clickedRank < 0 || clickedFile < 0) {
            return;
        }

        // Add pieces to white and black roster
        if (piece.getColor().equals(Piece.Color.BLACK)) {
            chessBoard.getBlackPieces().add(piece);
        } else {
            chessBoard.getWhitePieces().add(piece);
        }


        if (piece instanceof Pawn) {
            if ((clickedRank == 1 && piece.getColor().equals(Piece.Color.BLACK))
                    || (clickedRank == 6 && piece.getColor().equals(Piece.Color.WHITE))) {
                piece.setTurnCount(0);
            } else {
                piece.setTurnCount(1);
            }
        }
        // Access to the board model
        Square targetSquare = chessBoard.getSquare(clickedRank, clickedFile);
        // If the targetSquare already has a piece, then remove it from the board
        if (targetSquare.getPiece() != null) {
            if (targetSquare.getPiece().getColor().equals(Piece.Color.BLACK)) {
                chessBoard.getBlackPieces().remove(targetSquare.getPiece());
            } else {
                chessBoard.getWhitePieces().remove(targetSquare.getPiece());
            }
        }
        // Set the new piece on the board
        targetSquare.setPiece(piece);
        piece.setCurrentSquare(targetSquare);

        // Updates the threats
        chessBoard.updateThreats();

        // Redraws to show the updated piece
        chessBoardView.redraw();

    } // placePieceInClickedSquare

    /**
     *
     */
    private void removePieceInClickedSquare() {
        if (clickedRank < 0 || clickedFile < 0) {
            return;
        }
        // Remove piece for white or black roster
        Square targetSquare = chessBoard.getSquare(clickedRank, clickedFile);
        if (targetSquare.getPiece() != null && targetSquare.getPiece().getColor().equals(Piece.Color.BLACK)) {
            chessBoard.getBlackPieces().remove(targetSquare.getPiece());
        } else if (targetSquare.getPiece() != null && targetSquare.getPiece().getColor().equals(Piece.Color.WHITE)) {
            chessBoard.getWhitePieces().remove(targetSquare.getPiece());
        }
        // Sets the square to null (containing no piece)
        targetSquare.setPiece(null);
        chessBoardView.redraw();
    } // removePieceInClickedSquare

    /**
     *
     * @param mouseX
     * @param mouseY
     */
    private void recordClickedSquare(double mouseX, double mouseY) {
        double boardLeft = 30;
        double boardTop = 30;
        double boardRight = chessBoardView.getWidth() - 30;
        double boardBottom = chessBoardView.getHeight() - 30;
        double boardWidth = boardRight - boardLeft;
        double boardHeight = boardBottom - boardTop;
        double squareWidth = boardWidth / 8.0;
        double squareHeight = boardHeight / 8.0;

        // Check if the click is inside the 8x8 board area
        double relX = mouseX - boardLeft;
        double relY = mouseY - boardTop;

        if (relX < 0 || relY < 0) {
            clickedRank = -1;
            clickedFile = -1;
            return;
        }

        int file = (int) (relX / squareWidth);
        int rank = (int) (relY / squareHeight);

        // If row/col within board boundaries
        if (rank >= 0 && rank < 8 && file >= 0 && file < 8) {
            clickedRank = rank;
            clickedFile = file;
        } else {
            clickedRank = -1;
            clickedFile = -1;
        }
    } // recordClickedSquare

} // CustomContextMenus