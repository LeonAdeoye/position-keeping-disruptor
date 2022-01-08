package com.leon.model;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.SizedReader;
import net.openhft.chronicle.hash.serialization.SizedWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PositionInventorySerializer implements SizedReader<PositionInventory>, SizedWriter<PositionInventory>
{
	private static PositionInventorySerializer INSTANCE = new PositionInventorySerializer();

	public static PositionInventorySerializer getInstance() { return INSTANCE; }

	private PositionInventorySerializer() {}

	@NotNull
	@Override
	public PositionInventory read(Bytes in, long size, @Nullable PositionInventory using)
	{
		if (using == null)
			using = new PositionInventory();

		using.setClientId(in.readInt());
		using.setStockCode(in.readUtf8());
		using.setStartOfDayQuantity(in.readInt());
		using.setExecutedQuantity(in.readInt());
		using.setReservedQuantity(in.readInt());
		using.setBorrowedQuantity(in.readInt());
		using.setStartOfDayCash(in.readDouble());
		using.setExecutedCash(in.readDouble());
		using.setReservedCash(in.readDouble());

		return using;
	}

	@Override
	public long size(@NotNull PositionInventory toWrite)
	{
		return Integer.BYTES + (Byte.BYTES * toWrite.getStockCode().length()) +Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES
				+ Double.BYTES + Double.BYTES + Double.BYTES;
	}

	@Override
	public void write(Bytes out, long size, @NotNull PositionInventory toWrite)
	{
		out.writeInt(toWrite.getClientId());
		out.writeUtf8(toWrite.getStockCode());
		out.writeInt(toWrite.getStartOfDayQuantity());
		out.writeInt(toWrite.getExecutedQuantity());
		out.writeInt(toWrite.getReservedQuantity());
		out.writeInt(toWrite.getBorrowedQuantity());
		out.writeDouble(toWrite.getStartOfDayCash());
		out.writeDouble(toWrite.getExecutedCash());
		out.writeDouble(toWrite.getReservedCash());
	}
}
