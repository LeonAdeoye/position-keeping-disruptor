package com.leon.service;

import com.leon.io.InboundDeliveryMechanism;
import com.leon.io.OutboundDeliveryMechanism;
import com.leon.io.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService
{
    private InboundDeliveryMechanism inboundDeliveryMechanism;
    private OutboundDeliveryMechanism outboundDeliveryMechanism;

    void setInboundDeliveryMechanism(InboundDeliveryMechanism inboundDeliveryMechanism)
    {
        this.inboundDeliveryMechanism = inboundDeliveryMechanism;
    }

    void setOutboundDeliveryMechanism(OutboundDeliveryMechanism outboundDeliveryMechanism)
    {
        this.outboundDeliveryMechanism = outboundDeliveryMechanism;
    }

    @Override
    public Flux<Payload> readAll()
    {
        return inboundDeliveryMechanism.readAll();
    }

    @Override
    public Mono<Payload> read()
    {
        return inboundDeliveryMechanism.read();
    }

    @Override
    public void write(Mono<Payload> payload)
    {
        outboundDeliveryMechanism.write(payload);
    }

    @Override
    public int writeAll(Flux<Payload> payload)
    {
        return outboundDeliveryMechanism.writeAll(payload);
    }
}
