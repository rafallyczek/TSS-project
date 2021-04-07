<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <style>
        .formInputs{ margin-top: 5px; }
        label, input{ display: block; }
        form > input{ margin-top: 5px; }
    </style>
    <body>   
        
        <form method="post" action="j_security_check">
            
            <div class="formInputs">
                <label for="username">Login</label>
                <input id="username" type="text" name="j_username">
            </div>
            
            <div class="formInputs">
                <label for="password">Hasło</label>
                <input id="password" type="password" name="j_password">
            </div>
            
            <input type="submit" value="Zaloguj">
            
        </form>
        <% 
            HttpSession sesja = request.getSession(true);
        %>
    </body>
</html>
