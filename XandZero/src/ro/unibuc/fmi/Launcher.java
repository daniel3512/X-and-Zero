package ro.unibuc.fmi;

import java.io.IOException;

public class Launcher
{

    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        TicTacToeGame game = new TicTacToeGame();
        game.showNewGameWindow();
    }

}
