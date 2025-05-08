module Chess.Engine {

    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.studio.chess to javafx.controls;
    opens com.studio.chess.controller to javafx.controls;
    opens com.studio.chess.model to javafx.controls;
    opens com.studio.chess.model.board to javafx.controls;
    opens com.studio.chess.model.piece to javafx.controls;
    opens com.studio.chess.state to javafx.controls;
    opens com.studio.chess.util to javafx.controls;
    opens com.studio.chess.view to javafx.controls;

    exports com.studio.chess;
    exports com.studio.chess.controller;
    exports com.studio.chess.model;
    exports com.studio.chess.model.piece;
    exports com.studio.chess.model.board;
    exports com.studio.chess.state;
    exports com.studio.chess.util;
    exports com.studio.chess.view;
}