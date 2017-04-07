package ro.unibuc.fmi.messages;

import java.io.Serializable;

import ro.unibuc.fmi.TTTValue;

public class TickMessage implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final int row, col;
    public final TTTValue value;

    public TickMessage(int row, int col, TTTValue value)
    {
        this.row = row;
        this.col = col;
        this.value = value;
    }

}
