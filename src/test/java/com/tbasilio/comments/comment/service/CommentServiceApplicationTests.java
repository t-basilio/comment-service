package com.tbasilio.comments.comment.service;

import com.tbasilio.comments.comment.service.commom.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@SpringBootTest
class CommentServiceApplicationTests {

	@Test
	void validateUUIDExtractDate() {
		UUID uuid = IdGenerator.generatorTimeBasedUUID();
		var today = OffsetDateTime.now();

		Assertions.assertThat(
				IdGenerator.extractOffsetDateTime(uuid).truncatedTo(ChronoUnit.MINUTES))
				.isEqualTo(today.truncatedTo(ChronoUnit.MINUTES));
	}

}
