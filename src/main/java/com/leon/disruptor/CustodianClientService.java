package com.leon.disruptor;

import java.util.HashSet;
import java.util.Set;

public class CustodianClientService
{
    Set<Integer> setOfCustodianClients;

    public CustodianClientService()
    {
        setOfCustodianClients = new HashSet<>();
    }

    public boolean isCustodianClient(int clientId)
    {
        return setOfCustodianClients.contains(clientId);
    }

    public int uploadCustdianClients()
    {
        // TODO load custodian clients from file.
        return 0;
    }
}
