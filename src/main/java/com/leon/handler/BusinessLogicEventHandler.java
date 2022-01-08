package com.leon.handler;

import com.leon.model.DisruptorEvent;
import com.leon.model.PositionInventory;
import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.map.ChronicleMap;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
ChronicleMap Interface
There are few specials methods which are provided by ChronicleMap. See below
V getUsing(K key, V value); getUsing is same as get(key) but getUsing will return the value in value parameter without creating a new object whereas get will create a new object for returning the value with key.
V acquireUsing(K key, V value); acquireUsing is again same as getUsing but if there is no value defined with key, it will insert a new entry with key and returns the same value.

ChronicleMap can read and write the data from/to a JSON object. The following methods can be used to do so
void getAll(File toFile) throws IOException; To read map from the file which was created by another ChronicleMap using JSON format.
void putAll(File fromFile) throws IOException; To dump the entire map into a file using a JSON format.
void close();
As the data is stored off-heap, it's recommended to close the map to release the heap data and persist the data.
*/

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;
    private  ChronicleMap<String, PositionInventory> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor, String startOfDayInventoryPositionFilePath)
    {
        initializeChronicleMap();
        uploadSODPositions(startOfDayInventoryPositionFilePath);
        outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        logger.debug(event.getPayload().toString());
        outboundDisruptor.push(event.getPayload());
    }

    public void uploadSODPositions(String startOfDayInventoryPositionFilePath)
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(startOfDayInventoryPositionFilePath))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray positionInventories = (JSONArray) obj;
            positionInventories.forEach( positionInventory ->
            {
                System.out.println(positionInventory.toString());
                System.out.println(String.format("key: %06d%s", positionInventory, "0001.HK"));

                // persistedDisruptorMap.put(key, (PositionInventory) positionInventory);
                // System.out.println(persistedDisruptorMap.get(key).toString());
                //logger.info("Loaded Chrionicle map with " + positionInventories.size() + " inventory positions.");
            });
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void initializeChronicleMap()
    {
//        try
//        {
//            persistedDisruptorMap = ChronicleMapBuilder
//                    .of(String.class, PositionInventory.class)
//                    .name("disruptor-map")
//                    .entries(1_000_000)
//                    .averageKey("America")
//                    .averageValue(new PositionInventory())
//                    .createPersistedTo(new File("../logs/position-inventory.txt"));
//        }
//        catch(IOException ioe)
//        {
//            logger.error(ioe.getMessage());
//        }
    }

    public void close()
    {
        persistedDisruptorMap.close();
    }
}
