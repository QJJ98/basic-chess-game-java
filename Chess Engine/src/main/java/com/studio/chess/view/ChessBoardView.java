package com.studio.chess.view;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import com.studio.chess.model.piece.Piece;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChessBoardView extends Canvas {
    
    private final ChessBoard board;
    private final int SIZE = 8;
    private double squareSize; // default = 80 (can be changed later)
    private final GraphicsContext cgc;
    private List<Square> highlightedSquares;
    private final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
    // private final CustomContextMenus contextMenus;

    /**
     *
     * @param board
     * @param squareSize
     */
    public ChessBoardView(ChessBoard board, double squareSize) {
        this.board = board;
        this.squareSize = squareSize;
        setWidth(squareSize * 8);
        setHeight(squareSize * 8);
        cgc = getGraphicsContext2D();
        highlightedSquares = new ArrayList<>();
        // contextMenus = new CustomContextMenus(board, this);
    } // ChessBoardView

    // GETTERS & SETTERS

    /**
     *
     * @return
     */
    public double getSquareSize() {
        return squareSize;
    } // getSquareSize

    /**
     *
     * @param squareSize
     */
    public void setSquareSize(double squareSize) {
        this.squareSize = squareSize;
    } // setSquareSize

    /**
     *
     * @param highlightedSquares
     */
    public void setHighlightedSquares(List<Square> highlightedSquares) {
        this.highlightedSquares = highlightedSquares;
    } // setHighlightedSquares

    /**
     *
     */
    public void draw() {
        // Outer Boundary
        cgc.clearRect(0, 0, squareSize * SIZE, squareSize * SIZE);
        cgc.setStroke(Color.WHITE);
        cgc.setFill(Color.GRAY);
        cgc.fillRect(0, 0, squareSize * SIZE, squareSize * SIZE + 30);
        cgc.strokeRect(0, 0, squareSize * SIZE, squareSize * SIZE);

        // Inner Boundary (Checkered Board Boundary)
        double boardLeft = 30;
        double boardTop = 30;
        double boardRight = getWidth() - 30;
        double boardBottom = getHeight() - 30;

        double boardWidth = boardRight - boardLeft;
        double boardHeight = boardBottom - boardTop;
        double squareWidth = boardWidth / SIZE;
        double squareHeight = boardHeight / SIZE;

        cgc.setLineWidth(5);
        cgc.setFill(Color.GRAY);
        cgc.fillRect(boardLeft, boardTop, boardWidth, boardHeight);
        cgc.strokeRect(boardLeft, boardTop, boardWidth, boardHeight);

        // Grid Labeling
        Font font = new Font("Arial", 15);
        cgc.setFont(font);

        // Index of the letters
        int letterIn = 0;

        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                // Draw column letters over the cells of the first rank
                if (rank == 0) {
                    cgc.setFill(Color.WHITE);
                    double labelX = boardLeft + file * squareWidth + (squareWidth / 2) - 4;
                    double labelY = boardTop - 10;
                    cgc.fillText(letters[letterIn++], labelX, labelY);
                }
                // Draw rank numbers to the side of the first column
                if (file == 0) {
                    cgc.setFill(Color.WHITE);
                    double labelX = boardLeft - 20;
                    double labelY = boardTop + rank * squareHeight + (squareHeight / 2);
                    cgc.fillText(String.valueOf(8 - rank), labelX, labelY);
                }
                // Checker Color
                if ((rank + file) % 2 == 0) {
                    cgc.setFill(Color.GRAY);
                } else {
                    cgc.setFill(Color.WHITE);
                }
                // Fill Cell
                double cellX = boardLeft + file * squareWidth;
                double cellY = boardTop + rank * squareHeight;
                cgc.fillRect(cellX, cellY, squareWidth, squareHeight);
            }
        }
    } // draw

    /**
     *
     */
    public void redraw() {
        draw();
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                Square square = board.getSquare(rank, file);
                Piece piece = square.getPiece();
                if (piece != null) {
                    drawPieceAt(rank, file, piece.getImage());
                }
            }
        }
        highlightSquares();
    } // redraw

    /**
     *
     *
     */
    public void enableFreePlacement() {

    } // enableFreePlacement

    public void disableFreePlacement() {

    } // disableFreePlacement

    /**
     *
     */
    private void highlightSquares() {
        // For coordinate math, we re‐use the same logic from draw()
        double boardLeft = 30;
        double boardTop = 30;
        double boardWidth = getWidth() - 60;
        double boardHeight = getHeight() - 60;
        double squareWidth = boardWidth / 8.0;
        double squareHeight = boardHeight / 8.0;
        // Translucent yellow
        cgc.setFill(Color.color(1, 1, 0, 0.2));
        for (Square square : highlightedSquares) {
            int row = square.getRank();
            int col = square.getFile();
            double squareX = boardLeft + col * squareWidth;
            double squareY = boardTop + row * squareHeight;

            // Fill the square with highlight color
            cgc.fillRect(squareX, squareY, squareWidth, squareHeight);
        }
    }

    /**
     *
     * @param rank
     * @param file
     * @param image
     */
    private void drawPieceAt(int rank, int file, Image image) {
        double boardLeft = 30;
        double boardTop = 30;
        double boardWidth = getWidth() - 60;
        double boardHeight = getHeight() - 60;
        double squareWidth = boardWidth / SIZE;
        double squareHeight = boardHeight / SIZE;

        // Top-Left corner of the cell
        double cellX = boardLeft + file * squareWidth;
        double cellY = boardTop + rank * squareHeight;

        // Center
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double drawX = cellX + (squareWidth - imageWidth) / 2.0;
        double drawY = cellY + (squareHeight - imageHeight) / 2.0;

        // Draw Image
        cgc.drawImage(image, drawX, drawY);
    } // drawPieceAt

} // ChessBoardView