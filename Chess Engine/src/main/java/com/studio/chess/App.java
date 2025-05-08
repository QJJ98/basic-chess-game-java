package com.studio.chess;

import com.studio.chess.controller.AIController;
import com.studio.chess.controller.ChessController;
import com.studio.chess.model.Move;
import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.piece.*;
import com.studio.chess.view.ChessBoardView;
import com.studio.chess.view.CustomContextMenus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


/**
 * Hello world!
 *
 */
public class App extends Application {

    private ChessBoard chessBoard;
    private ChessBoardView chessBoardView;
    private ChessController controller;
    private AIController aiController;
    protected static int squareSize;
    private static boolean customMode;
    private MenuBar menuBar;
    private final Menu file = new Menu("File");
    private MenuItem restart = new MenuItem("Restart");
    private MenuItem exit = new MenuItem("Exit");
    private CustomContextMenus contextMenus;
    private List<MenuItem> newGameMenuItems;
    private List<MenuItem> originalMenuItems;


    /**
     * Application start method
     * @param stage the stage to be shown
     */
    public void start(Stage stage) {
        chessBoard = new ChessBoard();
        chessBoardView = new ChessBoardView(chessBoard, 80);
        menuBar = setMenuBar();
        contextMenus = new CustomContextMenus(chessBoard, chessBoardView);
        controller = new ChessController(chessBoard, chessBoardView);
        aiController = new AIController(chessBoard, chessBoardView);
        customMode = false;
        Pane chessPane = new Pane(chessBoardView);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, chessPane);
        Scene scene = new Scene(vBox, chessBoardView.getSquareSize() * 8, chessBoardView.getSquareSize() * 8 + 25);
        stage.setScene(scene);
        scene.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ESCAPE)) {
                Platform.exit();
            }
        });
        stage.setTitle("Chess");
        stage.setResizable(false);
        stage.show();
        chessBoardView.draw();
        System.out.print(chessBoard.getTurnCount() + ") ");
    } // start

    /**
     *
     * @return
     */
    public static boolean isCustomMode() {
        return customMode;
    } // isCustomMode

    /**
     *
     */
    private void enableFreePlacementMode() {
        chessBoardView.redraw();
        contextMenus.enableFreePlacementMenu();
        menuBar.getMenus().get(0).getItems().clear();
        MenuItem clear = new MenuItem("Clear Board");
        clear.setOnAction(e -> {
            chessBoard.clearBoard();
            chessBoardView.redraw();
        });
        MenuItem set = new MenuItem("Set Board");
        set.setOnAction(e -> {
            if (setBoardCondition()) {
                chessBoard.setPopulated(true);
                customMode = true;
                menuBar.getMenus().get(0).getItems().clear();
                // menuBar.getMenus().get(0).getItems().addAll(originalMenuItems);
                setNewGameMenuItems();
                contextMenus.disableFreePlacementMenu();
                controller = new ChessController(chessBoard, chessBoardView);
                aiController = new AIController(chessBoard, chessBoardView);
            }
        });
        menuBar.getMenus().get(0).getItems().addAll(clear, set);
    } // enableFreePlacementMode

    /**
     *
     * @return
     */
    private MenuBar setMenuBar() {
        // TODO: FINISH THIS
        MenuBar menuBar = new MenuBar();
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> {
            chessBoard.setFlipped(false);
            newGameAction();
        });
        MenuItem flip = new MenuItem("Flip Board");
        flip.setOnAction(e -> {
            chessBoard.setFlipped(true);
            newGameAction();
        });
        MenuItem customGame = new MenuItem("Custom Game");
        customGame.setOnAction(e -> {
            chessBoard.clearBoard();
            enableFreePlacementMode();
        });
        exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        file.getItems().addAll(newGame, flip, customGame, exit);
        menuBar.getMenus().add(file);
        originalMenuItems = new ArrayList<>(menuBar.getMenus().get(0).getItems());
        return menuBar;
    } // setMenuBar

    /**
     *
     */
    private void newGameAction() {
        chessBoard.resetBoard();
        chessBoardView.redraw();
        setNewGameMenuItems();
    } // newGameAction

    private void setNewGameMenuItems() {
        file.getItems().clear();
        restart = new MenuItem("Restart Game");
        restart.setOnAction(r -> {
            chessBoard.resetBoard();
            chessBoardView.redraw();
            file.getItems().clear();
            file.getItems().addAll(newGameMenuItems);
        });
        MenuItem undo = new MenuItem("Undo Move");
        undo.setOnAction(u -> {
            if (!chessBoard.getMoveHistory().isEmpty() && chessBoard.getLastMove() != null) {
                Move lastMove = chessBoard.getLastMove();
                chessBoard = lastMove.getChessBoardState();
                chessBoardView.redraw();
            }
        });
        file.getItems().addAll(restart, undo, exit);
        newGameMenuItems = new ArrayList<>(file.getItems());
        menuBar.getMenus().clear();
        menuBar.getMenus().add(file);
    }

    /**
     *
     * @return
     */
    private boolean setBoardCondition() {
        // If neither side has any pieces, then the board cannot be set (return false)
        if (chessBoard.getBlackPieces().isEmpty() || chessBoard.getWhitePieces().isEmpty()) {
            return false;
        }
        // If one or both sides does not have a king, which would not be considered a valid chess game, then
        // the board cannot be set
        if (chessBoard.findKingSquare(Piece.Color.BLACK) == null
                || chessBoard.findKingSquare(Piece.Color.WHITE) == null) {
            return false;
        }
        // If the two sides have bare kings (only a king on both sides), the result of which would be a
        // stalemate, then the board cannot be set
        if (chessBoard.getBlackPieces().size() == 1 && chessBoard.getWhitePieces().size() == 1) {
            return false;
        }
        // If one or both sides have multiple kings, then the board cannot be set
        int kingCounter = 0;
        for (int i = 0; i < chessBoard.getBlackPieces().size(); i++) {
            Piece piece = chessBoard.getBlackPieces().get(i);
            if (piece instanceof King) {
                kingCounter++;
            }
        }
        if (kingCounter > 1) {
            return false;
        }
        kingCounter = 0;
        for (int i = 0; i < chessBoard.getWhitePieces().size(); i++) {
            Piece piece = chessBoard.getWhitePieces().get(i);
            if (piece instanceof King) {
                kingCounter++;
            }
        }
        if (kingCounter > 1) {
            return false;
        }
        // If one side has a bare king and the other side has a king and a bishop or knight, then the board
        // cannot be set
        // Here we assume that the black side has a bare king
        if (chessBoard.getBlackPieces().size() == 1 && chessBoard.getWhitePieces().size() >= 2) {
            for (int i = 0; i < chessBoard.getWhitePieces().size(); i++) {
                Piece piece = chessBoard.getWhitePieces().get(i);
                if (piece instanceof Bishop || piece instanceof Knight) {
                    return false;
                }
            }
        }
        // And in this case, we assume the white side has a bare king
        if (chessBoard.getBlackPieces().size() >= 2 && chessBoard.getWhitePieces().size() == 1) {
            for (int i = 0; i < chessBoard.getBlackPieces().size(); i++) {
                Piece piece = chessBoard.getBlackPieces().get(i);
                if (piece instanceof Bishop || piece instanceof Knight) {
                    return false;
                }
            }
        }
        // Also if the pawns are at the wrong ends of the boards (i.e., black on rank 7 or white on rank 0)...
        // If black is on rank 0, then the set is not valid (black cannot move backwards). Also, if black is
        // on rank 7, then the board cannot be set (black would not receive a promotion)
        for (int i = 0; i < chessBoard.getBlackPieces().size(); i++) {
            Piece piece = chessBoard.getBlackPieces().get(i);
            if (piece instanceof Pawn && (piece.getCurrentSquare().getRank() == 0
                    || piece.getCurrentSquare().getRank() == 7)) {
                return false;
            }
        }
        // If white is on rank 0, then the set is not valid (white cannot move backwards). Also, if white is
        // on rank 7, then the board cannot be set (white would not receive a promotion)
        for (int i = 0; i < chessBoard.getWhitePieces().size(); i++) {
            Piece piece = chessBoard.getWhitePieces().get(i);
            if (piece instanceof Pawn && (piece.getCurrentSquare().getRank() == 0
                    || piece.getCurrentSquare().getRank() == 7)) {
                return false;
            }
        }
        // Finally, if the placement of pieces on the board would result in an immediate checkmate or stalemate
        // condition upon setting the board, then the board cannot be set
        return (!chessBoard.isCheckmate(Piece.Color.BLACK) && !chessBoard.isCheckmate(Piece.Color.WHITE))
                && (!chessBoard.isStalemate(Piece.Color.BLACK) && !chessBoard.isStalemate(Piece.Color.WHITE));
    } // setBoardCondition


    /**
     * Main method.
     * @param args command arguments
     */
    public static void main( String[] args ) {
        launch(args);
    } // main

} // App