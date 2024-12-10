package org.mx.yahaha.toolchain.ui.flamegraph

import com.fasterxml.jackson.annotation.JsonProperty
import org.mx.yahaha.toolchain.support.JsonConvertor
import org.mx.yahaha.toolchain.ui.UiComponent

/**
 *
 * @author FizzPu
 * @since 2024/4/7 22:06
 * */
class FlameGraphDetail implements UiComponent {
    private String rawTaskInfo
    private TaskInfo taskInfo;

    private String taskContext

    FlameGraphDetail(String taskInfo, String taskContext) {
        this.rawTaskInfo = taskInfo
        this.taskInfo = TaskInfo.of(taskInfo.substring(taskInfo.indexOf('{'), taskInfo.lastIndexOf('}') + 1))
        this.taskContext = taskContext
    }

    TaskInfo getTaskInfo() {
        return taskInfo
    }

    @Override
    String getHtml() {
        return """"""
    }

    static class TaskInfo {
        @JsonProperty("id")
        String taskId
        @JsonProperty("datetime")
        String requestTime
        @JsonProperty("duration")
        int durationInMs
        @JsonProperty("sampleInterval")
        int sampleIntervalInMs
        @JsonProperty("sampleStartTime")
        String sampleStartTime
        @JsonProperty("threadName")
        String sampleThreadName

        private TaskInfo() {
        }

        static TaskInfo of(String json) {
            return JsonConvertor.me().fromJson(TaskInfo.class, json)
        }
    }
}

