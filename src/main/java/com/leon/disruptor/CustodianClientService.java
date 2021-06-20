package com.leon.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class CustodianClientService
{
    private static final Logger logger = LoggerFactory.getLogger(CustodianClientService.class);

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
