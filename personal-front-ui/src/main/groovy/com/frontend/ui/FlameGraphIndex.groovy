package com.frontend.ui

/**
 *
 * @author FizzPu
 * @since 2024/4/7 21:24
 * */
class FlameGraphIndex implements UiComponent {
    @Override
    String getHtml() {
        return """
        <!DOCTYPE html>
            <html>
                <head>
                    <meta charset=utf-8"/>
                    <title>flame graph</title>
                </head>

                <body>
                    <div>
                        <h3>索引：</h3>
                        <a href="/flamegraph/history">logs history</a>
                    </div>
                    <div>
                        <h3>新增日志，将采样日志粘贴在下面的输入框中：</h3>
                        <form action="/flamegraph/logs" method="POST">
                        <textarea rows = "20" cols = "100" name = "logs" placeholder="Enter stack trace sample log here..."></textarea>
                        <button type = "submit">Submit</button>
                        </form>
                    </div>
                </body>
            </html>
        """
    }
}
