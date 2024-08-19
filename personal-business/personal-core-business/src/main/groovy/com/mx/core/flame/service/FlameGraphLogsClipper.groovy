package com.mx.core.flame.service

import org.springframework.stereotype.Component

/**
 * @author FizzPu
 * @since 2024/4/29 16:52
 */
@Component
class FlameGraphLogsClipper {
	private static final String LOGS_END_TOKEN = 'end$>';
	private static final String LOGS_START_TOKEN = '<$start';
	private static final String LOGS_TASK_INFO_TOKEN = "taskInfo";
	private static final String LOGS_STACK_TRACE_TOKEN = "stackTrace";
	private static final String LOGS_CLASS_ID_MAP_TOKEN = "classIdMap";
	private static final String LOGS_TASK_CONTEXT_TOKEN = "taskContext";

	public Collection<FlameGraphLogEntry> clip(String logs) {
		Objects.requireNonNull(logs);
		List<List<String>> tokens = clipByToken(logs);

		Collection<FlameGraphLogEntry> logEntries = new ArrayList<>();
		for (List<String> token : tokens) {
			clipSingleToken(token).ifPresent(logEntries::add);
		}
		return logEntries;
	}

	Optional<FlameGraphLogEntry> clipSingleToken(List<String> token) {
		if (token.size() < 2) {
			return Optional.empty();
		}
		if (!token.get(0).contains(LOGS_START_TOKEN)) {
			return Optional.empty();
		}

		if (!token.get(token.size() - 1).contains(LOGS_END_TOKEN)) {
			return Optional.empty();
		}

		String taskInfo = token.get(1);
		if (!taskInfo.startsWith(LOGS_TASK_INFO_TOKEN)) {
			return Optional.empty();
		}

		if (token.size() < 3) {
			return Optional.empty();
		}
		String taskContext = token.get(2);
		if (!taskContext.startsWith(LOGS_TASK_CONTEXT_TOKEN)) {
			return Optional.empty();
		}

		if (token.size() < 4 || !token.get(3).contains(LOGS_STACK_TRACE_TOKEN)) {
			return Optional.empty();
		}

		StringBuilder stackTrace = new StringBuilder();
		int index = 0;
		for (int i = 4; i < token.size(); i++) {
			if (token.get(i).startsWith(LOGS_END_TOKEN)) {
				index = i;
				break;
			}
			if (token.get(i).startsWith(LOGS_CLASS_ID_MAP_TOKEN)) {
				index = i;
				break;
			}
			stackTrace.append(token.get(i).trim()).append("\n");
		}

		String classIdMap = null;
		if (index != 0 && token.get(index).contains(LOGS_CLASS_ID_MAP_TOKEN)) {
			classIdMap = token.get(index + 1);
		}

		return Optional.of(new FlameGraphLogEntry(taskInfo, taskContext, stackTrace.toString(), classIdMap));
	}

	// clip by the token <$start ... end$>
	List<List<String>> clipByToken(String logs) {
		String[] logArray = logs.split("\n");

		boolean startClip = false;
		List<String> innerList = null;
		List<List<String>> logStrList = new ArrayList<>();

		for (String seq : logArray) {
			if (!startClip && seq != null && seq.contains(LOGS_START_TOKEN)) {
				startClip = true;
				innerList = new ArrayList<>();
				innerList.add(seq);
			}
			else if (startClip && seq != null && seq.contains(LOGS_END_TOKEN)) {
				innerList.add(seq);
				logStrList.add(new ArrayList<>(innerList));
				innerList = null;
				startClip = false;
			}
			else if (startClip) {
				innerList.add(seq);
			}
		}
		return logStrList;
	}



}
