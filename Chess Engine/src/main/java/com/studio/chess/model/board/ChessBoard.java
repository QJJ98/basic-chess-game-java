package com.studio.chess.model.board;

import com.studio.chess.model.Move;
import com.studio.chess.model.piece.*;

import java.util.*;

/**
 *
 *
 */
public class ChessBoard {

    private final Square[][] board;
    private boolean flipped;
    private boolean populated;
    private List<Piece> blackPieces;
    private List<Piece> whitePieces;
    private List<Piece> capturedBlackPieces;
    private List<Piece> capturedWhitePieces;
    private Move lastMove;
    private Stack<Move> moveHistory; // Stores past moves for undo
    private Square enPassantTarget;
    private Piece enPassantCapturedPiece;
    private Piece promotedPiece;
    private int lastMoveTurn = -1;
    private Piece.Color currentTurn;
    private int turnCount, movesMade;

    /**
     *
     */
    public ChessBoard() {
        board = new Square[8][8];
        flipped = false;
        populated = false;
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        capturedBlackPieces = new ArrayList<>();
        capturedWhitePieces = new ArrayList<>();
        moveHistory = new Stack<>();
        enPassantCapturedPiece = null;
        promotedPiece = null;
        turnCount = 1;
        movesMade = 0;
        currentTurn = Piece.Color.WHITE; // For custom placements to track whose turn it is
        initBoard();
    } // ChessBoard

    /**
     *
     * @return
     */
    public Square[][] getBoard() {
        return board;
    } // getBoard

    /**
     *
     * @return
     */
    public boolean isFlipped() {
        return flipped;
    } // isFlipped

    /**
     *
     * @param flipped
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    } // setFlipped

    /**
     *
     * @return
     */
    public boolean isPopulated() {
        return populated;
    } // getPopulated

    /**
     *
     * @param populated
     * @return
     */
    public void setPopulated(boolean populated) {
        this.populated = populated;
    } // setPopulated

    /**
     *
     * @param
     * @return
     */
    public Piece getPromotedPiece() {
        return this.promotedPiece;
    } // setPromotedPiece

    /**
     *
     * @param promotedPiece
     */
    public void setPromotedPiece(Piece promotedPiece) {
        this.promotedPiece = promotedPiece;
    } // setPromotedPiece

    /**
     *
     * @param rank
     * @param file
     * @return
     */
    public Square getSquare(int rank, int file) {
        for (Square[] squares : board) {
            for (Square square : squares) {
                if (square.getRank() == rank && square.getFile() == file) {
                    return square;
                }
            }
        }
        return null;
    } // getSquare

    /**
     *
     * @return
     */
    public List<Piece> getBlackPieces() {
        return blackPieces;
    } // getBlackPieces

    /**
     *
     * @return
     */
    public List<Piece> getWhitePieces() {
        return whitePieces;
    } // getWhitePieces

    /**
     *
     * @return
     */
    public List<Piece> getCapturedBlackPieces() {
        return capturedBlackPieces;
    } // getCapturedBlackPieces

    /**
     *
     * @return
     */
    public List<Piece> getCapturedWhitePieces() {
        return capturedWhitePieces;
    } // getCapturedWhitePieces

    /**
     *
     * @return
     */
    public Stack<Move> getMoveHistory() {
        return moveHistory;
    } // getMoveHistory

    /**
     *
     * @return
     */
    public Move getLastMove() {
        return lastMove;
    } // getLastMove

    /**
     *
     * @return
     */
    public Square getEnPassantTarget() {
        return enPassantTarget;
    } // getEnPassantTarget

    /**
     *
     * @return
     */
    public Piece.Color getCurrentTurn() {
        return currentTurn;
    } // getCurrentTurn

    /**
     *
     * @param color
     */
    public void setCurrentTurn(Piece.Color color) {
        currentTurn = color;
    } // setCurrentTurn

    /**
     *
     * @return
     */
    public int getTurnCount() {
        return turnCount;
    } // getTurnCount

    /**
     *
     * @param lastMoveTurn
     */
    public void setLastMoveTurn(int lastMoveTurn) {
        this.lastMoveTurn = lastMoveTurn;
    } // setLastMoveTurn

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public boolean movePiece(Square from, Square to) {
        // Updates the threats to all squares at the beginning of the move
        if (from.equals(to)) {
            return false;
        }
        lastMove = moveHistory.peek();
        updateThreats();
        // Gets the piece from the from square (if any) and if there is none, then return false
        Piece movingPiece = from.getPiece();
        if (movingPiece == null) {
            return false;
        }
        // Check to see if the square to be moving to is a valid move to make, if not, then return
        // false
        if (!movingPiece.getValidMoves(this).contains(to)) {
            return false;
        }

        // Make an attempt at castling and if successful, exit the method with a return of true
        if (tryCastling(movingPiece, from, to)) {
            return true;
        }

        // Make an attempt at an en passant capture
        boolean enPassant = handleEnPassant(movingPiece, from, to);

        // Attempts a normal capture
        // If the square that the piece is going to has a piece occupying it, then check the color of
        // that piece
        Piece capturedPiece;
        if (enPassant) {
            capturedPiece = enPassantCapturedPiece;
        } else {
            capturedPiece = to.getPiece();
        }
        if (capturedPiece != null) {
            // Prevent the capture of the king on either side by returning false
            if (capturedPiece instanceof King) {
                return false;
            }
            // Capture the piece from the board
            capturePiece(capturedPiece);
        }

        // Perform the actual move
        doBasicMove(from, to);

        // Check to see if a pawn has been promoted after making the move
        handlePromotion(movingPiece, to);

        // Update the threats to reflect the new piece positions and switch turns to the other color
        updateThreats();

        // Check to see if king is in check or checkmate
        boolean isInCheck, checkMated;
        if (movingPiece.getColor().equals(Piece.Color.BLACK)) {
            isInCheck = isCheck(Piece.Color.WHITE);
        } else {
            isInCheck = isCheck(Piece.Color.BLACK);
        }
        // Check to see if king is checkmated and if so, remove the isInCheck condition
        if (movingPiece.getColor().equals(Piece.Color.BLACK)) {
            checkMated = isCheckmate(Piece.Color.WHITE);
        } else {
            checkMated = isCheckmate(Piece.Color.BLACK);
        }
        if (checkMated) {
            isInCheck = false;
        }

        Move move = new Move(deepCopy(), movingPiece, from, to, capturedPiece, false, enPassant, false,
                null, isInCheck, checkMated);
        moveHistory.push(move);
        switchTurn();
        // Return true to reflect a successful move
        return true;
    } // movePiece

    /**
     *
     * @param piece
     * @param from
     * @param to
     * @return
     */
    public boolean simulateMove(Piece piece, Square from, Square to) {

        // Do a basic move of pieces to test threats without any special conditions (castling, en passant, etc.)
        // Save the piece that will get captured in this move (if any)
        Piece captured = to.getPiece();
        doBasicMove(from, to);

        // Temporarily update threats and check if the king is in check or not after the move
        updateThreats();
        // Checks to see if the king is in check via the threat to the temporary square that the king is on
        boolean kingIsInCheck = isCheck(piece.getColor());

        // Undo the move so that the board reverts to its previous state
        undoBasicMove(from, to, captured);
        // Refresh the threats back to they were before the move was simulated
        updateThreats();

        // Return if the position was legal (i.e., king was not in check after such a move was made)
        return !kingIsInCheck;
    } // simulateMove


    /**
     *
     * @param from
     * @param to
     */
    private void doBasicMove(Square from, Square to) {
        Piece movingPiece = from.getPiece();
        to.setPiece(movingPiece);
        from.setPiece(null);
        if (movingPiece != null) {
            movingPiece.setCurrentSquare(to);
            movingPiece.incrementTurnCount();
        }
    } // doBasicMove

    /**
     *
     * @param from
     * @param to
     * @param capturedPiece
     */
    private void undoBasicMove(Square from, Square to, Piece capturedPiece) {
        Piece movedPiece = to.getPiece();
        from.setPiece(movedPiece);
        if (movedPiece != null) {
            movedPiece.setCurrentSquare(from);
            movedPiece.setTurnCount(movedPiece.getTurnCount() - 1);
        }
        to.setPiece(capturedPiece);
        if (capturedPiece != null) {
            capturedPiece.setCurrentSquare(to);
        }
    } // undoBasicMove

    /**
     *
     * @param capturedPiece
     */
    private void capturePiece(Piece capturedPiece) {
        capturedPiece.setCurrentSquare(null);
        if (capturedPiece.getColor().equals(Piece.Color.BLACK)) {
            blackPieces.remove(capturedPiece);
            capturedBlackPieces.add(capturedPiece);
        } else {
            whitePieces.remove(capturedPiece);
            capturedWhitePieces.add(capturedPiece);
        }
    } // capturePiece

    /**
     *
     */
    public boolean isValidMove(Piece piece, Square to) {
        List<Square> validMoves = piece.getValidMoves(this);
        return validMoves.contains(to);
    } // isValidMove

    /**
     *
     */
    public void updateThreats() {
        // Clears all current threats
        for (Square[] row : board) {
            for (Square square : row) {
                square.getThreats().clear();
            }
        }

        // Gather new threats
        for (Square[] row : board) {
            for (Square square : row) {
                Piece piece = square.getPiece();
                if (piece != null) {
                    // Get a list of squares that this piece attacks
                    List<Square> attackedSquares = piece.getAttackingSquares(this);
                    for (Square attackedSquare : attackedSquares) {
                        attackedSquare.addThreat(piece);
                    }
                }
            }
        }
    } // updateThreats


    /**
     *
     */
    public void resetBoard() {
        clearBoard();
        initBoard();
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        populated = initPieces();
        currentTurn = Piece.Color.WHITE;
    } // resetBoard

    /**
     *
     */
    public void clearBoard() {
        for (Square[] squares : board) {
            for (Square square : squares) {
                Piece piece = square.getPiece();
                if (piece != null) {
                    square.setPiece(null);
                    piece.setCurrentSquare(null);
                }
            }
        }
        initBoard();
        blackPieces.clear();
        whitePieces.clear();
        capturedBlackPieces.clear();
        capturedWhitePieces.clear();
    } // clearBoard

    /**
     *
     */
    public void switchTurn() {
    // Clears en passant target between each turn (whether it was used in that turn or not)
        movesMade++;
        if (turnCount > lastMoveTurn + 1) {
            enPassantTarget = null;
            lastMoveTurn = -1;
        }
        currentTurn = (currentTurn == Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        turnCount = (movesMade / 2) + 1;
    } // switchTurn

    /**
     *
     * @param piece
     * @return
     */
    public int getForwardDirection(Piece piece) {
        // If board NOT flipped: White goes UP (−1), Black goes DOWN (+1)
        // If board IS flipped:  White goes DOWN (+1), Black goes UP (−1)
        if (!flipped) {
            return (piece.getColor() == Piece.Color.BLACK) ? 1 : -1;
        } else {
            return (piece.getColor() == Piece.Color.BLACK) ? -1 : 1;
        }
    }

    /**
     *
     * @param movingPiece
     * @param from
     * @param to
     * @return
     */
    private boolean tryCastling(Piece movingPiece, Square from, Square to) {
        if (!(movingPiece instanceof King)) {
            return false;
        }
        // If the king will not move 2 spaces exactly, then return false
        if (Math.abs(to.getFile() - from.getFile()) != 2) {
            return false;
        }

        // Perform castling
        doBasicMove(from, to);

        // Handle queenside castling
        Square rookTo = null, rookFrom = null;
        if (to.getFile() == 2) {
            rookFrom = getSquare(from.getRank(), 0);
            rookTo = getSquare(from.getRank(), to.getFile() + 1);
        } else if (to.getFile() == 6) {
            rookFrom = getSquare(from.getRank(), 7);
            rookTo = getSquare(from.getRank(), to.getFile() - 1);
        }
        doBasicMove(rookFrom, rookTo);

        /*
        // Move the rook after moving the king
        if (to.getFile() > from.getFile()) {
            // Handle kingside castling
            Square rookFrom = getSquare(from.getRank(), 7);
            Square rookTo = getSquare(from.getRank(), to.getFile() - 1);
            doBasicMove(rookFrom, rookTo);
        } else {
            // Handle queenside castling
            Square rookFrom = getSquare(from.getRank(), 0);
            Square rookTo = getSquare(from.getRank(), to.getFile() + 1);
            doBasicMove(rookFrom, rookTo);
        }
         */

        // Record the move that just occurred in history
        Move castleMove = new Move(this, movingPiece, from, to, null, true, false,
                false, null, false, false);
        moveHistory.push(castleMove);
        updateThreats();
        switchTurn();
        return true;
    } // tryCastling





    /**
     *
     * @param movingPiece
     * @param from
     * @param to
     */
    private boolean handleEnPassant(Piece movingPiece, Square from, Square to) {
        // Resets the piece that was captured via en passant (if any)
        enPassantCapturedPiece = null;
        // If the piece isn't an instance of a pawn object, then terminate method
        if (!(movingPiece instanceof Pawn)) {
            return false;
        }

        int d = getForwardDirection(movingPiece);
        boolean enPassantOccurred = false;
        if (Math.abs(to.getRank() - from.getRank()) == 2) {
            enPassantTarget = getSquare(from.getRank() + d, from.getFile());
            lastMoveTurn = movesMade;
        }

        if (from.getFile() != to.getFile() && to.getPiece() == null) {
            // 'to' is the square the capturing pawn is moving onto (the middle square)
            if (to.equals(enPassantTarget) && movesMade == lastMoveTurn + 1) {
                // Remove the jumped pawn from its final square
                int direction = getForwardDirection(movingPiece);
                int jumpedPawnRank = to.getRank() - direction;
                int jumpedPawnFile = to.getFile();

                Square jumpedPawnSquare = getSquare(jumpedPawnRank, jumpedPawnFile);
                if (jumpedPawnSquare != null && jumpedPawnSquare.getPiece() instanceof Pawn) {
                    capturePiece(jumpedPawnSquare.getPiece());
                    enPassantCapturedPiece = jumpedPawnSquare.getPiece();
                    jumpedPawnSquare.setPiece(null);
                    enPassantOccurred = true;
                }
            }
        }
        return enPassantOccurred;
    } // handleEnPassant

    /**
     *
     * @param movingPiece
     * @param to
     */
    private void handlePromotion(Piece movingPiece, Square to) {

        // TODO: Find some way to integrate a context menu to choose whatever piece upon the piece
        //  receiving a promotion

        // If the piece being "promoted" isn't a pawn, then it can't receive a promotion
        if (!(movingPiece instanceof Pawn)) {
            return;
        }
        // If the piece is white, then it must be on rank 0, and if it is black, then it must be on rank 7
        // to receive a promotion
        int promotionRank = (movingPiece.getColor().equals(Piece.Color.WHITE)) ? 0 : 7;
        if (to.getRank() == promotionRank) {
            // Promote to a Queen until, I can integrate a context menu to select any piece
            promotedPiece = new Queen(movingPiece.getColor());
            promotedPiece.setCurrentSquare(to);
            to.setPiece(promotedPiece);
        }
    } // handlePromotion

    /**
     *
     * @return
     */
    public boolean isCheck(Piece.Color color) {
        // Find the King's Square
        Square kingSquare = findKingSquare(color);
        List<Piece> threatsTo = kingSquare.getThreats();
        return threatsTo.stream().anyMatch(e -> !e.getColor().equals(color));
    } // isCheck

    /**
     *
     * @param color
     * @return
     */
    public boolean isCheckmate(Piece.Color color) {
        // A checkmate happens if the king is in check and no legal move exists for any piece of that color
        // If there is no check, then there cannot be a checkmate
        if (!isCheck(color)) {
            return false;
        }
        // Find any valid move that a piece can make to get out of check
        List<Piece> pieces = (color == Piece.Color.WHITE) ? getWhitePieces() : getBlackPieces();
        for (Piece piece : pieces) {
            if (!piece.getValidMoves(this).isEmpty()) {
                return false; // Found at least one legal move.
            }
        }
        return true; // King is in check and no piece can move.
    } // isCheckMate

    /**
     *
     * @param color
     * @return
     */
    public boolean isStalemate(Piece.Color color) {
        // A stalemate happens if the king is NOT in check, but no legal move exists for any piece of that color
        // If a king is in check, then there cannot be a stalemate
        if (isCheck(color)) {
            return false;
        }
        // Find any legal move that can be made
        List<Piece> pieces = (color == Piece.Color.WHITE) ? getWhitePieces() : getBlackPieces();
        for (Piece piece : pieces) {
            if (!piece.getValidMoves(this).isEmpty()) {
                return false; // Found at least one legal move.
            }
        }
        return true; // No legal moves but king is not in check.
    } // isStalemate

    // UTILITY METHODS

    /**
     *
     * @param color
     * @return
     */
    public Square findKingSquare(Piece.Color color) {
        Square kingSquare = null;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                kingSquare = getSquare(rank, file);
                Piece piece = kingSquare.getPiece();
                if (piece instanceof King king) {
                    if (king.getColor().equals(color)) {
                        return kingSquare;
                    }
                }
            }
        }
        return kingSquare;
    } // findKingSquare

    /**
     *
     * @return
     */
    public ChessBoard deepCopy() {
        ChessBoard chessBoardCopy = new ChessBoard();
        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                chessBoardCopy.getBoard()[r][f] = this.getBoard()[r][f];
            }
        }
        chessBoardCopy.populated = this.populated;
        chessBoardCopy.moveHistory = this.getMoveHistory();
        chessBoardCopy.lastMove = this.getLastMove();
        chessBoardCopy.blackPieces = this.getBlackPieces();
        chessBoardCopy.whitePieces = this.getWhitePieces();
        chessBoardCopy.capturedBlackPieces = this.getCapturedBlackPieces();
        chessBoardCopy.capturedWhitePieces = this.getCapturedWhitePieces();
        chessBoardCopy.enPassantCapturedPiece = this.enPassantCapturedPiece;
        chessBoardCopy.enPassantTarget = this.getEnPassantTarget();
        chessBoardCopy.promotedPiece = this.getPromotedPiece();
        chessBoardCopy.turnCount = this.getTurnCount();
        chessBoardCopy.lastMoveTurn = this.lastMoveTurn;
        chessBoardCopy.movesMade = this.movesMade;
        return chessBoardCopy;
    } // deepCopy




    // INITIALIZATION METHODS

    /**
     *
     */
    private void initBoard() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square square = new Square(rank, file);
                board[rank][file] = square;
            }
        }
        Move firstMoveState = new Move(this, null, null, null, null);
        moveHistory.push(firstMoveState);
    } // initBoard

    /**
     *
     */
    private boolean initPieces() {
        if (flipped) {
            for (int f = 0; f < 8; f++) {
                placePiece(new Pawn(Piece.Color.WHITE), 1, f);
                placePiece(new Pawn(Piece.Color.BLACK), 6, f);
            }
            placePiece(new Rook(Piece.Color.WHITE), 0, 0);
            placePiece(new Rook(Piece.Color.WHITE), 0, 7);
            placePiece(new Knight(Piece.Color.WHITE), 0, 1);
            placePiece(new Knight(Piece.Color.WHITE), 0, 6);
            placePiece(new Bishop(Piece.Color.WHITE), 0, 2);
            placePiece(new Bishop(Piece.Color.WHITE), 0, 5);
            placePiece(new Queen(Piece.Color.WHITE), 0, 3);
            placePiece(new King(Piece.Color.WHITE), 0, 4);
            placePiece(new Rook(Piece.Color.BLACK), 7, 0);
            placePiece(new Rook(Piece.Color.BLACK), 7, 7);
            placePiece(new Knight(Piece.Color.BLACK), 7, 1);
            placePiece(new Knight(Piece.Color.BLACK), 7, 6);
            placePiece(new Bishop(Piece.Color.BLACK), 7, 2);
            placePiece(new Bishop(Piece.Color.BLACK), 7, 5);
            placePiece(new Queen(Piece.Color.BLACK), 7, 3);
            placePiece(new King(Piece.Color.BLACK), 7, 4);
        } else {
            for (int f = 0; f < 8; f++) {
                placePiece(new Pawn(Piece.Color.BLACK), 1, f);
                placePiece(new Pawn(Piece.Color.WHITE), 6, f);
            }
            placePiece(new Rook(Piece.Color.BLACK), 0, 0);
            placePiece(new Rook(Piece.Color.BLACK), 0, 7);
            placePiece(new Knight(Piece.Color.BLACK), 0, 1);
            placePiece(new Knight(Piece.Color.BLACK), 0, 6);
            placePiece(new Bishop(Piece.Color.BLACK), 0, 2);
            placePiece(new Bishop(Piece.Color.BLACK), 0, 5);
            placePiece(new Queen(Piece.Color.BLACK), 0, 3);
            placePiece(new King(Piece.Color.BLACK), 0, 4);
            placePiece(new Rook(Piece.Color.WHITE), 7, 0);
            placePiece(new Rook(Piece.Color.WHITE), 7, 7);
            placePiece(new Knight(Piece.Color.WHITE), 7, 1);
            placePiece(new Knight(Piece.Color.WHITE), 7, 6);
            placePiece(new Bishop(Piece.Color.WHITE), 7, 2);
            placePiece(new Bishop(Piece.Color.WHITE), 7, 5);
            placePiece(new Queen(Piece.Color.WHITE), 7, 3);
            placePiece(new King(Piece.Color.WHITE), 7, 4);
        }
        return true;
    } // initPieces

    /**
     *
     * @param piece
     * @param rank
     * @param file
     */
    private void placePiece(Piece piece, int rank, int file) {
        board[rank][file].setPiece(piece);
        piece.setCurrentSquare(getSquare(rank, file));
        if (piece.getColor().equals(Piece.Color.BLACK)) {
            blackPieces.add(piece);
        } else {
            whitePieces.add(piece);
        }
    } // placePiece


} // ChessBoard