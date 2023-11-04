package grim.readmechess.webapi.chessboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessBoard {
    private String fenString;
    private char[] position;

    public ChessBoard(String fenString) {
        this.fenString = fenString;
    }

    public void printChessBoardToConsole() {
        System.out.println(fenString.substring(0, fenString.indexOf(" "))
                .replace("1", ".")
                .replace("2", "..")
                .replace("3", "...")
                .replace("4", "....")
                .replace("5", ".....")
                .replace("6", "......")
                .replace("7", ".......")
                .replace("8", "........")
                .replace("", " ")
                .replace(" / ", "\n")
        );
    }

    public void setPosition() {
        String fenString = this.fenString;

    }
}
