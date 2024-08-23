package com.mx.core.flame.service
/**
 * @author FizzPu
 * @since 2024/4/29 12:09
 */
class FlameGraphLogEntry {
  private final String logId;
  private final String taskInfo;
  private final String taskContext;
  private final String stackTrace;
  private final String classIdMap;

  FlameGraphLogEntry(String taskInfo, String taskContext, String stackTrace, String classIdMap) {
    this.taskInfo = taskInfo;
    this.stackTrace = stackTrace;
    this.classIdMap = classIdMap;
    this.taskContext = taskContext;
    this.logId = UUID.randomUUID().toString();
  }

  String getClassIdMap() {
    return classIdMap;
  }
  
  public String getTaskInfo() {
    return taskInfo;
  }
  
  public String getTaskContext() {
    return taskContext;
  }

  String getStackTrace() {
    if (classIdMap == null || classIdMap.isBlank()) {
      return stackTrace
    }

    String[] splitClassIdMap = classIdMap.substring(0, classIdMap.size() - 1).split(";");
    Map<String, String> classIdHashMap = new HashMap<>();
    for (String element : splitClassIdMap) {
      String[] tmp = element.split(":");
      classIdHashMap.put(tmp[0], tmp[1])
    }
    
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder replace = new StringBuilder();
    boolean encounter = false
    for (String ch : stackTrace) {
      if (!encounter && ch == 'C') {
        replace = new StringBuilder("C");
        encounter = true
        continue;
      }
      if (encounter && ch != '(') {
        replace.append(ch);
        continue;
      }
      if (encounter && ch == '(') {
        stringBuilder.append(classIdHashMap.get(replace.toString()));
        stringBuilder.append(ch);
        replace = new StringBuilder();
        encounter = false
        continue
      }
      stringBuilder.append(ch)
    }
    return stringBuilder.toString();
  }
  
  public String getLogId() {
    return logId;
  }
}
