package com.leon.model;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.SizedReader;
import net.openhft.chronicle.hash.serialization.SizedWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventorySerializer implements SizedReader<Inventory>, SizedWriter<Inventory>
{
	private static InventorySerializer INSTANCE = new InventorySerializer();

	public static InventorySerializer getInstance() { return INSTANCE; }

	private InventorySerializer() {}

	@NotNull
	@Override
	public Inventory read(Bytes in, long size, @Nullable Inventory using)
	{
		if (using == null)
			using = new Inventory();

		using.setClientId(in.readInt());
		using.setInstrumentId(in.readInt());
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
	public long size(@NotNull Inventory toWrite)
	{
		return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES
				+ Double.BYTES + Double.BYTES + Double.BYTES;
	}

	@Override
	public void write(Bytes out, long size, @NotNull Inventory toWrite)
	{
		out.writeInt(toWrite.getClientId());
		out.writeInt(toWrite.getInstrumentId());
		out.writeInt(toWrite.getStartOfDayQuantity());
		out.writeInt(toWrite.getExecutedQuantity());
		out.writeInt(toWrite.getReservedQuantity());
		out.writeInt(toWrite.getBorrowedQuantity());
		out.writeDouble(toWrite.getStartOfDayCash());
		out.writeDouble(toWrite.getExecutedCash());
		out.writeDouble(toWrite.getReservedCash());
	}
}
