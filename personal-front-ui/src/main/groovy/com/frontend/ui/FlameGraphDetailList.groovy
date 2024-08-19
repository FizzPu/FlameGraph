package com.frontend.ui

import groovy.text.SimpleTemplateEngine

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
            font-family: sans-serif;
        }
        table, th, td {
            border: 1px solid;
            padding: 5px;
        }
        </style>

        <body> 
            <table>
                <tr>
                    <th>火焰图</th>
                    <th>采样信息</th>
                    <th>采样上下文</th>
                </tr>
                <% history.each { row -> %>
                <tr>
                    <th><a href=<%= row.flameGraphUrl %>>火焰图</th>
                    <th><%= row.taskInfo %></th>
                    <th><%= row.taskContext %></th>
                </tr>
                <% } %>
            </table> 
        </body>
    </html>
    """).make([history:flameGraphDetails]).toString()
    }
}
