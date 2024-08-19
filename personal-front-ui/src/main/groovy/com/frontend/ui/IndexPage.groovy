package com.frontend.ui

/**
 *
 * @author FizzPu
 * @since 2024/3/30 20:33
 * */
 class IndexPage implements UiComponent {
     @Override
     String getHtml() {
         return """
        <html>
            <head>
                <title>Index Page</title>
            </head>
            <body>
                <div><a href="/flamegraph/logs">火焰图性能诊断工具</a></div> 
            </body>
        </html>
            """
     }
 }
