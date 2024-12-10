package org.mx.yahaha.toolchain.ui.flamegraph

import groovy.text.SimpleTemplateEngine
import org.mx.yahaha.toolchain.ui.UiComponent

/**
 *
 * @author FizzPu
 * @since 2024/4/30 10:56
 * */
class FlameGraphDetailList implements UiComponent {
    private Collection<FlameGraphDetail> flameGraphDetails = new ArrayList<>()

    def engine = new SimpleTemplateEngine()

    void add(FlameGraphDetail flameGraphDetail) {
        flameGraphDetails.add(flameGraphDetail)
    }

    @Override
    String getHtml() {
        return engine.createTemplate("""
    <!DOCTYPE html>
    <html>
        <head>
            <title>flame graph</title>
        </head>
        <style>
        table {
            border-collapse: collapse;
            font-family: 'SimSun', serif;
        }
        table, th, td {
            border: 0.5px solid;
            padding: 5px;
        }
        </style>

        <body> 
            <table>
                <tr>
                    <th>火焰图</th>
                    <th>采样id</th>
                    <th>请求耗时</th>
                    <th>请求时间</th>
                    <th>采样间隔</th>
                    <th>采样线程</th>
                    <th>采样上下文</th>
                </tr>
                <% history.each { row -> %>
                <tr>
                    <th><a href=/flamegraph/detail?task_id=<%= row.taskInfo.taskId %>>火焰图</th>
                    <th><%= row.taskInfo.taskId %></th>
                    <th><%= row.taskInfo.durationInMs %>毫秒</th>
                    <th><%= row.taskInfo.requestTime %></th>
                    <th><%= row.taskInfo.sampleIntervalInMs%>毫秒</th>
                    <th><%= row.taskInfo.sampleThreadName%></th>
                    <th><%= row.taskContext %></th>
                </tr>
                <% } %>
            </table> 
        </body>
    </html>
    """).make([history:flameGraphDetails]).toString()
    }
}
