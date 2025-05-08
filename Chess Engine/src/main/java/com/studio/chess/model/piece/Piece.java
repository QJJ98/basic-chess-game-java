package com.studio.chess.model.piece;

import com.studio.chess.model.board.ChessBoard;
import com.studio.chess.model.board.Square;
import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public abstract class Piece {

    private String abbreviation;
    private final Color pieceColor;
    private double worth;
    private Square currentSquare;
    private int turnCount;
    private Image image;

    /**
     *
     * @param color
     */
    public Piece(Color color) {
        if (color.equals(Color.WHITE)) {
            pieceColor = Color.WHITE;
        } else {
            pieceColor = Color.BLACK;
        }
        currentSquare = null;
        worth = -1;
        turnCount = 0;
    } // Piece

    /**
     *
     * @return
     */
    public String getPieceNotation() {
        return abbreviation;
    } // getAbbreviation

    /**
     *
     * @param abbreviation
     */
    public void setPieceNotation(String abbreviation) {
        this.abbreviation = abbreviation;
    } // setAbbreviation

    /**
     *
     * @param board
     * @return
     */
    public abstract List<Square> getValidMoves(ChessBoard board);

    /**
     *
     * @param board
     * @return
     */
    public abstract List<Square> getAttackingSquares(ChessBoard board);

    /**
     *
     * @return
     */
    public Color getColor() {
        return pieceColor;
    } // getPieceColor

    /**
     *
     * @return
     */
    public Image getImage() {
        return image;
    } // getImage

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    } // setImage

    /**
     *
     * @return
     */
    public double getWorth() {
        return worth;
    } // getWorth

    /**
     *
     * @param worth
     */
    public void setWorth(double worth) {
        this.worth = worth;
    } // setWorth

    /**
     *
     * @return
     */
    public int getTurnCount() {
        return turnCount;
    } // getTurnCount

    /**
     *
     * @param turnCount
     */
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    } // setTurnCount

    /**
     *
     */
    public void incrementTurnCount() {
        turnCount++;
    } // incrementTurnCount


    /**
     *
     * @return
     */
    public Square getCurrentSquare() {
        return currentSquare;
    } // getCurrentSquare

    /**
     *
     * @param currentSquare
     */
    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    } // setCurrentSquare



    /**
     *
     */
    public enum Color { WHITE, BLACK }

} // Piece