package org.mx.yahaha.toolchain.flamegraph.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.mx.yahaha.toolchain.flamegraph.service.FlameGraphLogsClipper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * @author FizzPu
 * @since 2024/5/9 16:28
 */
@Component
public class FlameGraphRepository {
	private final Cache<String, FlameGraphLogsClipper.FlameGraphLogEntry> taskIdToFlameGraphLogEntry = CacheBuilder.newBuilder()
		.expireAfterAccess(Duration.ofDays(1)).softValues().maximumSize(1000).build();

  public void save(String taskId, FlameGraphLogsClipper.FlameGraphLogEntry flameGraphLogEntry) {
		Objects.requireNonNull(flameGraphLogEntry);
		taskIdToFlameGraphLogEntry.put(taskId, flameGraphLogEntry);
	}

  public Optional<FlameGraphLogsClipper.FlameGraphLogEntry> queryByLogId(String logId) {
		return Optional.ofNullable(taskIdToFlameGraphLogEntry.getIfPresent(logId));
	}

  public Collection<FlameGraphLogsClipper.FlameGraphLogEntry> queryList() {
		return new ArrayList<>(taskIdToFlameGraphLogEntry.asMap().values());
	}
}
