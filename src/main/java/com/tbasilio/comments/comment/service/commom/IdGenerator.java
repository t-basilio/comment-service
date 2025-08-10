package com.tbasilio.comments.comment.service.commom;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class IdGenerator {
    private static final TimeBasedEpochRandomGenerator generator =
            Generators.timeBasedEpochRandomGenerator();

    private IdGenerator() {
    }

    public static UUID generatorTimeBasedUUID() {
        return generator.generate();
    }

    public static OffsetDateTime extractOffsetDateTime(UUID uuid) {
        if(uuid == null)
            return null;

        long timestamp = uuid.getMostSignificantBits() >>> 16;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
