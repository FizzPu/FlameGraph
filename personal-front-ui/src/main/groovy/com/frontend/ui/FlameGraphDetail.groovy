package com.frontend.ui

/**
 *
 * @author FizzPu
 * @since 2024/4/7 22:06
 * */
class FlameGraphDetail implements UiComponent {
    private String taskInfo
    private String stackStace
    private String taskContext
    private String flameGraphUrl

    FlameGraphDetail(String taskInfo, String stackStace, String taskContext, String flameGraphUrl) {
        this.taskInfo = taskInfo
        this.stackStace = stackStace
        this.taskContext = taskContext
        this.flameGraphUrl = flameGraphUrl
    }

    String getStackStace() {
        return stackStace
    }

    @Override
    String getHtml() {
        return """
   <!DOCTYPE html>
    <html>
        <head>
            <title>flame graph</title>
        </head>
        <style>
            table {
                border-collapse: collapse;
                }
            table, th, td {
                border: 1px solid black;
                padding: 5px;
            }
        </style>

        <body>
            <h3>Stack trace detail info:</h3>
                <table>
                <tr>
                    <th>task_id</th>
                    <td><a href="/flamegraph/detail?task_id =$taskId">${taskId}</a></td>
                </tr> 
                <tr>
                    <th>taskInfo</th>
                    <td>${taskInfo}s</td>
                </tr>
                <tr>
                    <th>sampleStartTime</th>
                    <td>${taskContext}</td>
                </tr>  
        </table>
    </body>
    </html>
    """
    }
}
