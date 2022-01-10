package com.leon.model;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.SizedReader;
import net.openhft.chronicle.hash.serialization.SizedWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RequestSerializer implements SizedReader<InventoryCheckRequest>, SizedWriter<InventoryCheckRequest>
{
    private static RequestSerializer INSTANCE = new RequestSerializer();

    public static RequestSerializer getInstance() { return INSTANCE; }

    private RequestSerializer() {}

    @NotNull
    @Override
    public InventoryCheckRequest read(Bytes in, long size, @Nullable InventoryCheckRequest using)
    {
        if (using == null)
            using = new InventoryCheckRequest();

        using.setSide(in.readChar());
        using.setQuantity(in.readLong());
        using.setStockCode(in.readUtf8());
        using.setClientCode(in.readChar());
        using.setOrderId(in.readUtf8());
        using.setSequenceId(in.readInt());
        using.setRequestType(in.readUtf8());
        using.setRequestSubType(in.readUtf8());

        return using;
    }

    @Override
    public long size(@NotNull InventoryCheckRequest toWrite)
    {
        // TODO add 4 string fields
        return Character.BYTES + Long.BYTES + Integer.BYTES + Integer.BYTES;
    }

    @Override
    public void write(Bytes out, long size, @NotNull InventoryCheckRequest toWrite)
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
