package ro.unibuc.fmi;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ro.unibuc.fmi.client.Client;
import ro.unibuc.fmi.messages.TickMessage;
import ro.unibuc.fmi.server.Server;
import ro.unibuc.fmi.uilisteners.ButtonListener;
import ro.unibuc.fmi.uilisteners.ClientCreateListener;
import ro.unibuc.fmi.uilisteners.ServerCreateListener;

public class TicTacToeGame
{

    private Container mainPanel;

    private Server server = null;
    private Client client = null;

    private TTTValue currentTurn;

    private TTTValue[][] matrix;
    private JButton[][] buttons;

    private int partnerId = -1;

    public void showNewGameWindow()
    {
        JFrame frame = new JFrame("TicTacToe");
        mainPanel = new JPanel();
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 500);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints;

        JButton butonClient = new JButton("Client");
        JLabel labelServerPort = new JLabel("Server port : ");
        JLabel labelServerIp = new JLabel("Server IP : ");
        JTextField serverPortText = new JTextField("12345");
        JTextField serverIpText = new JTextField("localhost");
        butonClient.addActionListener(new ClientCreateListener(this, serverPortText, serverIpText));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(labelServerIp, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 3;
        mainPanel.add(serverIpText, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        mainPanel.add(labelServerPort, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(serverPortText, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(butonClient, constraints);

        JButton butonServer = new JButton("Server");
        JLabel labelListenPort = new JLabel("Listen port : ");
        JTextField listenPortText = new JTextField("12345");
        butonServer.addActionListener(new ServerCreateListener(this, listenPortText));

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        mainPanel.add(labelListenPort, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(listenPortText, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(butonServer, constraints);

        frame.setVisible(true);
    }

    public void serverCreate(int port) throws IOException
    {
    	// TODO Exception should be thrown here?

        // De implementat - conexiune   
    	  server = new Server(this,port);
          server.startServer();
    }

    public void clientCreate(String host, int port) throws UnknownHostException, IOException
    {
        // TODO Exception should be thrown here?

        // De implementat - conexiune la server
    	client = new Client();
    	client.connect(port, host);
    	

        startGame();
        (new Thread(new Runnable(){

			@Override
			public void run() {
				asyncWaitForPartnerMove(); 
			}
    	})).start();
    }

    public void playerJoined(int playerId)
    {
        if (partnerId == -1)
        {
            partnerId = playerId;
        }
        startGame();
    }

    public void startGame()
    {
        showGameUiAndReset();
    }

    public void showGameUiAndReset()
    {
        mainPanel.removeAll();
        matrix = new TTTValue[3][];
        buttons = new JButton[3][];
        currentTurn = TTTValue.X;
        GridBagConstraints constraints;
        for (int col = 0; col < 3; col++)
        {
            matrix[col] = new TTTValue[3];
            buttons[col] = new JButton[3];
            for (int row = 0; row < 3; row++)
            {
                matrix[col][row] = TTTValue.Empty;
                constraints = new GridBagConstraints();
                constraints.gridx = col;
                constraints.gridy = row;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.fill = GridBagConstraints.BOTH;

                buttons[col][row] = new JButton();
                buttons[col][row].addActionListener(new ButtonListener(this, row, col));
                buttons[col][row].setEnabled(server != null);
                mainPanel.add(buttons[col][row], constraints);
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void tickAt(int row, int col)
    {
        if (matrix[col][row].equals(TTTValue.Empty))
        {
            matrix[col][row] = currentTurn;
            buttons[col][row].setText(currentTurn.toString());
            int c = 0;
            // TODO: De trimis mesajul la partener, ca server intr-un fel client in alt fel.
            if(currentTurn.toString().equals("X")){
            	
            	try {
            		 // deactivate buttons
            		
                    for (int icol = 0; icol < 3; icol++)
                    {
                        for (int irow = 0; irow < 3; irow++)
                        {
                            buttons[icol][irow].setEnabled(false);
                            if (!(matrix[icol][irow].equals(TTTValue.Empty)))
                            {
                             c++;
                            }
                        }
                        
                    }
					server.sendMessage(0,new TickMessage(row,col,currentTurn));
					// wait for incoming tick
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else if(currentTurn.toString().equals("O")){
            	try{
            	 // deactivate buttons
            	for (int icol = 0; icol < 3; icol++)
                {
                    for (int irow = 0; irow < 3; irow++)
                    {
                        buttons[icol][irow].setEnabled(false);
                    }
                }
				client.SendMessage(new TickMessage(row,col,currentTurn));
            	}
            	catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            System.out.println(c);
            if(c<9){
            (new Thread(new Runnable(){

    			@Override
    			public void run() {
    				asyncWaitForPartnerMove(); 
    			}
        	})).start();
            }

        }
    }

    private void asyncWaitForPartnerMove()
    {
        // TODO: implementare
    	try{
    	waitForOtherPlayer();
    	}
    	catch(ClassNotFoundException cnf){
    		cnf.printStackTrace();
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    }

    private void waitForOtherPlayer() throws ClassNotFoundException, IOException
    {
        // TODO : is throw really right here?
        // TODO: Implementare, citim mesaj de la partener.
    	if(client!=null)
    		incomingTick((TickMessage)client.ReadMessage());
    	if(server!=null)
    		incomingTick((TickMessage)server.readMessage(0));
    	
    }

    public void incomingTick(TickMessage message)
    {
        matrix[message.col][message.row] = message.value;
        buttons[message.col][message.row].setText(message.value.toString());
        // if incoming tick, it is our turn.
        // activate buttons.
        int c = 0;
        for (int col = 0; col < 3; col++)
        {
            for (int row = 0; row < 3; row++)
            {
                if (matrix[col][row].equals(TTTValue.Empty))
                {
                    buttons[col][row].setEnabled(true);
                }else c++;
            }
            if (c==9){
            	if(server!=null)
            		server.stopServer();
            }
        }
        // change turn.
        currentTurn = message.value.equals(TTTValue.O) ? TTTValue.X : TTTValue.O;
    }
}
