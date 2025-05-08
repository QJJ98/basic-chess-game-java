package com.studio.chess.model.board;

import com.studio.chess.model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Square {

    private final int rank; // Lettered columns
    private final int file; // Numbered Rows
    private Piece piece; // Piece
    private final List<Piece> threatenedBy; // Pieces that can attack this square

    /**
     *
     * @param rank
     * @param file
     */
    public Square(int rank, int file) {
        this.rank = rank;
        this.file = file;
        this.piece = null;
        this.threatenedBy = new ArrayList<>();
    } // Square


    // GETTERS & SETTERS

    /**
     *
     * @return
     */
    public int getRank() {
        return rank;
    } // getRank

    /**
     *
     * @return
     */
    public int getFile() {
        return file;
    } // getFile

    /**
     *
     * @return
     */
    public Piece getPiece() {
        return piece;
    } // getPiece

    /**
     *
     * @param piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    } // setPiece


    // PUBLIC METHODS

    /**
     *
     * @param piece
     */
    public void addThreat(Piece piece) {
        threatenedBy.add(piece);
    } // addThreat


    /**
     *
     * @return
     */
    public List<Piece> getThreats() {
        return threatenedBy;
    } // getThreats


    // OVERRIDDEN METHODS

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        return letters[file] + (8 - rank);
    } // toString

} // Square