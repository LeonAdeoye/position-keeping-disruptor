package com.leon.model;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.SizedReader;
import net.openhft.chronicle.hash.serialization.SizedWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RequestSerializer implements SizedReader<Request>, SizedWriter<Request>
{
    private static RequestSerializer INSTANCE = new RequestSerializer();

    public static RequestSerializer getInstance() { return INSTANCE; }

    private RequestSerializer() {}

    @NotNull
    @Override
    public Request read(Bytes in, long size, @Nullable Request using)
    {
        if (using == null)
            using = new Request();

        using.setSide(in.readChar());
        using.setQuantity(in.readLong());
        return using;
    }

    @Override
    public long size(@NotNull Request toWrite)
    {
        return 0;
    }

    @Override
    public void write(Bytes out, long size, @NotNull Request toWrite)
    {
        out.writeChar(toWrite.getSide());
        out.writeLong(toWrite.getQuantity());
        out.writeUtf8(toWrite.getStockCode());
        out.writeInt(toWrite.getClientCode());
        out.writeUtf8(toWrite.getOrderId());
        out.writeInt(toWrite.getSequenceId());
        out.writeUtf8(toWrite.getRequestType());
        out.writeUtf8(toWrite.getRequestSubType());
    }
}
