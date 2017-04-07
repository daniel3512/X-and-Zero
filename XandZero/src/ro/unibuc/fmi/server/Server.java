package ro.unibuc.fmi.server;

import java.io.IOException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ro.unibuc.fmi.TicTacToeGame;

public class Server
{

    private ServerSocket server;
    private TicTacToeGame gameController;

    private List<Socket> clients;
    private List<ObjectInputStream> clientInputStreams;
    private List<ObjectOutputStream> clientOutputStreams;

    public boolean online;

    public Server(TicTacToeGame gameController, int port) throws IOException
    {
        // TODO: What to do with exceptions?
        server = new ServerSocket(port);
        this.gameController = gameController;
        clients = new ArrayList<Socket>();
        clientInputStreams = new ArrayList<ObjectInputStream>();
        clientOutputStreams = new ArrayList<ObjectOutputStream>();
    }

    public void startServer() throws IOException
    {
        // TODO: What to do with exceptions?
        // TODO: Start server code, adaugat de studenti
    	online = true;
    	Socket socket;
    	socket = server.accept();
        clients.add(socket);
        gameController.startGame();
 
    }

    public void sendMessage(int pointerConexiune, Object message) throws IOException
    {
    	//TODO
    	OutputStream os = clients.get(pointerConexiune).getOutputStream();
    	ObjectOutputStream out = new ObjectOutputStream(os);
    	out.writeObject(message);
    	clientOutputStreams.add(out);
    	
    }

    public Object readMessage(int pointerConexiune) throws ClassNotFoundException, IOException
    {
        // TODO
    	Object ob = null;
    	InputStream is ;
    	is = clients.get(pointerConexiune).getInputStream();
    	ObjectInputStream ois = new ObjectInputStream(is);
    	clientInputStreams.add((ObjectInputStream) ob);
    	return ois.readObject();
    }

    public void stopListening()
    {
        online = false;
    }

    public void stopServer()
    {
        stopListening();
        for (int i = 0; i < clients.size(); i++)
        {
            try
            {
                clientInputStreams.get(i).close();
                clientOutputStreams.get(i).close();
                clients.get(i).close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
