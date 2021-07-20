package com.leon.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.CharBuffer;

public class MemoryWriter
{
    private String fileName = "MEMORY_WRITE_FILE";
    private CharBuffer buffer;
    private RandomAccessFile file;

    public MemoryWriter()
    {

    }

    public boolean initialize()
    {
        try
        {
            file = new RandomAccessFile(fileName, "rw");
            buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0,4096).asCharBuffer();
            return true;
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
        return false;
    }

    public void shutdown()
    {
        try
        {
            file.close();
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void write(String input)
    {

    }
}
