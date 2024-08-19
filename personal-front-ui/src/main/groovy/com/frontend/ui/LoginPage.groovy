package com.frontend.ui

class LoginPage implements UiComponent {

    def html = """
    <html>
        <head>
            <title>Login Page</title>
        </head>
        <body>
            <form action="/auth/login" method="POST">
                User: <input type="text" name="username" id = "username"><br></br>
                Pass: <input type="text" name="password" id = "password"><br></br>
                <button type="submit">Submit</button>            
            </form>
        </body>
    </html>
    """

    String getHtml() {
        return html
    }
}
