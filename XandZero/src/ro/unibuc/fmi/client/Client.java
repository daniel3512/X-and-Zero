package ro.unibuc.fmi.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    public Client()
    {
   
    }

    public void connect(int port, String ip) throws UnknownHostException, IOException
    {
        // TODO: adaugati cod
    	socket = new Socket(ip,port);
    	
    }

    public void SendMessage(Object message) throws IOException
    {
        // TODO: adaugati cod
    	OutputStream out = socket.getOutputStream();
    	outputStream = new ObjectOutputStream(out);
    	outputStream.writeObject(message);
    	
    }

    public Object ReadMessage() throws ClassNotFoundException, IOException
    {
    	// TODO adaugati cod.
    	InputStream is;
		is = socket.getInputStream();
		inputStream = new ObjectInputStream(is);
        return inputStream.readObject();
    }
    
    public void clientStop(){
    	try
        {
            inputStream.close();
            outputStream.close();
            socket.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
