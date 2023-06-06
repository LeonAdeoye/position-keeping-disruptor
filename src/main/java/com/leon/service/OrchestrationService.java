package com.leon.service;

import com.leon.model.Inventory;
import java.util.List;

public interface OrchestrationService
{
    void start();
    void stop();
    void upload(String filePath);
    List<Inventory> getInventory();
    void clearInventory();
    void updateInventory(Inventory inventory);
    void deleteInventory(Inventory inventory);
    boolean togglePrimary();
    void initialize();
}
