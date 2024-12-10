package org.mx.yahaha.toolchain.flamegraph.service;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author FizzPu
 * @since 2024/4/29 16:52
 */
@Component
public class FlameGraphLogsClipper {
	private static final String LOGS_END_TOKEN = "end$>";
	private static final String LOGS_START_TOKEN = "<$start";
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
		for (int i = 4; i < token.size(); i++) {
			if (token.get(i).startsWith(LOGS_END_TOKEN)) {
				break;
			}
			if (token.get(i).startsWith(LOGS_CLASS_ID_MAP_TOKEN)) {
				break;
			}
			stackTrace.append(token.get(i).trim()).append("\n");
		}
		
		return Optional.of(new FlameGraphLogEntry(taskInfo, taskContext, stackTrace.toString()));
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


	/**
	 * @author FizzPu
	 * @since 2024/4/29 12:09
	 */
	static public class FlameGraphLogEntry {
		private final String logId;
		private final String taskInfo;
		private final String taskContext;
		private final String stackTrace;
	
		FlameGraphLogEntry(String taskInfo, String taskContext, String stackTrace) {
			this.taskInfo = taskInfo;
			this.stackTrace = stackTrace;
			this.taskContext = taskContext;
			this.logId = UUID.randomUUID().toString();
		}
		
		public String getTaskInfo() {
			return taskInfo;
		}
		
		public String getTaskContext() {
			return taskContext;
		}
	
		String getStackTrace() {
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder replace = new StringBuilder();
			boolean encounter = false;
			char[] stackChars = stackTrace.toCharArray();
			for (char ch : stackChars) {
				if (!encounter && ch == 'C') {
					replace = new StringBuilder("C");
					encounter = true;
					continue;
				}
				
				if (encounter && ch != '(') {
					replace.append(ch);
					continue;
				}
				
				if (encounter) {
					stringBuilder.append(ch);
					replace = new StringBuilder();
					encounter = false;
					continue;
				}
				stringBuilder.append(ch);
			}
			
			return stringBuilder.toString();
		}
		
		public String getLogId() {
			return logId;
		}
	}
}
