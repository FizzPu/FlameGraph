package com.mx.core.flame.repository

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.mx.core.flame.service.FlameGraphLogEntry
import org.springframework.stereotype.Component

import java.time.Duration
/**
 * @author FizzPu
 * @since 2024/5/9 16:28
 */
@Component
class FlameGraphRepository {
	private final Cache<String, FlameGraphLogEntry> lodIdToFlameGraphLogEntry = CacheBuilder.newBuilder()
		.expireAfterAccess(Duration.ofDays(1)).softValues().maximumSize(1000).build()

  void save(FlameGraphLogEntry flameGraphLogEntry) {
		Objects.requireNonNull(flameGraphLogEntry)
		Objects.requireNonNull(flameGraphLogEntry.getLogId())
		lodIdToFlameGraphLogEntry.put(flameGraphLogEntry.getLogId(), flameGraphLogEntry)
	}

  Optional<FlameGraphLogEntry> queryByLogId(String logId) {
		return Optional.of(lodIdToFlameGraphLogEntry.getIfPresent(logId))
	}

  Collection<FlameGraphLogEntry> queryList() {
		return new ArrayList<>(lodIdToFlameGraphLogEntry.asMap().values())
	}
}
