package com.intern.hub.ticket.infra.persistence.adapter;

import org.springframework.stereotype.Component;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.IdGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SnowflakeIdGenerator implements IdGenerator {

    private final Snowflake snowflake;

    @Override
    public Long nextId() {
        return snowflake.next();
    }

}
