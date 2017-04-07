package ro.unibuc.fmi.uilisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextField;

import ro.unibuc.fmi.TicTacToeGame;

public class ClientCreateListener implements ActionListener
{

    private TicTacToeGame gameController;
    private JTextField port;
    private JTextField ip;

    public ClientCreateListener(TicTacToeGame gameController, JTextField port, JTextField ip)
    {
        this.gameController = gameController;
        this.port = port;
        this.ip = ip;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO: Error handling! Exception treating!
        try
        {
            JButton buton = (JButton) e.getSource();
            buton.setText("Waiting for partner");
            ip.setEditable(false);
            buton.setEnabled(false);
            port.setEditable(false);
            gameController.clientCreate(ip.getText(), Integer.parseInt(port.getText()));
        }
        catch (NumberFormatException ex)
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

    }
}
