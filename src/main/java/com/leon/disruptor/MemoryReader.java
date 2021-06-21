package com.leon.disruptor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MemoryReader
{
    private String fileName = "MEMORY_READ_FILE";
    private MappedByteBuffer mappedByteBuffer;
    private RandomAccessFile file;
    private CharBuffer charBuffer;

    public MemoryReader()
    {

    }

    public boolean initialize()
    {
        try
        {
            file = new RandomAccessFile(fileName, "r");
            mappedByteBuffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,4096);

            if (mappedByteBuffer != null)
                charBuffer = Charset.forName("UTF-8").decode(mappedByteBuffer);

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

    public String read()
    {
        return "";
    }
}
