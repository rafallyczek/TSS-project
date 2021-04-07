<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="webapplication.logic.AppLogic"%>
<%@page import="java.util.List"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WebApplication</title>
    </head>
    <script type="text/javascript" src="js/canvasjs.min.js"></script>
    <script type="text/javascript">
        var data = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1];
        var timer = setInterval(doSend,333);
        function drawChart(data)
        {
            var chart = new CanvasJS.Chart("chartContainer", {
                title:{
                    text: "Dane WebSocket"              
                },
                axisX:{
                    maximum: 25
                },
                axisY:{
                    maximum: 60
                },
                data: [{
                    type: "splineArea",
                    color: "rgba(100, 149, 237, 0.7)",
                    markerSize: 5,
                    dataPoints: data
                    }]
                });
                chart.render();
        }       
        var uri = getUri() + "/WebApplication/endpoint";
        function getUri(){
            var protocol = (location.protocol === "http:" ? "ws://" : "wss://");
            return protocol + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" + (document.location.port === "" ? "8080" : document.location.port);
        }
        function init(){
            websocket = new WebSocket(uri);
            websocket.onopen = function(evt){
                onOpen(evt);
            };
            websocket.onmessage = function(evt){
                onMessage(evt);
            };
            websocket.onerror = function(evt){
                onError(evt);
            };
        }
        function onOpen(evt){
            writeToScreen("CONNECTED");
        }
        function onMessage(evt){
            writeToScreen("RECEIVED: "+evt.data);
            var dataJSON = JSON.parse(evt.data);
            for(i = 0;i<dataJSON.length;i++){
                data[i]={label:i,y:dataJSON[i]};
            }
            drawChart(data);
        }
        function onError(evt){
            writeToScreen('<span style="color: red;">ERROR:</span> '+evt.data);
        }
        function doSend(message){
            writeToScreen("SENT: "+message);
            websocket.send(message);
        }
        function writeToScreen(message){
            document.getElementById("messageID").innerHTML = message;
        }
        window.addEventListener("load",init,false);
    </script>
    <style>
        td { border: 1px solid black; }
        th { border: 1px solid black; }
        table { border-collapse: collapse; }
        b { color: royalblue; }
        .someClass { color: cornflowerblue }
        #chartContainer { height: 300px; width: 50%; }
        form { margin-bottom: 5px; }
    </style>
    <body>
        <%
            HttpSession sesja;
            sesja = request.getSession(false);
            if(sesja.getAttribute("username")==null){
                sesja.setAttribute("username", request.getRemoteUser());
                pageContext.setAttribute("current_user",sesja.getAttribute("username"));
            }else{
                pageContext.setAttribute("current_user",sesja.getAttribute("username"));
            }             
        %>
        <p><b>Zalogowany użytkownik: </b><c:out value = "${current_user}"/></p>
        <p><b>Autor:</b> Łyczek Rafał</p>
        <%
            
            getServletContext().setAttribute("parametr1", "Wartość parametru 1");
            getServletContext().setAttribute("parametr2", "Wartość parametru 2");
            
            ResultSet rs;
            Connection connection = AppLogic.connect();
            
            List<Integer> id = new ArrayList<>();
            List<String> nazwisko = new ArrayList<>();
            List<String> imie = new ArrayList<>();
            
            try{
                rs = connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY).executeQuery("select * from db_user_table");
                rs.first();
                do{
                    id.add(rs.getInt("id"));
                    nazwisko.add(rs.getString("nazwisko"));
                    imie.add(rs.getString("imie"));
                }while(rs.next());
            } catch (SQLException ex){
                String blad = ex.toString();
                System.err.println(blad);
            }                                    
            
            pageContext.setAttribute("ids",id);
            pageContext.setAttribute("surnames",nazwisko);
            pageContext.setAttribute("names",imie);
             
        %>
        <p><b>Lista wygenerowana za pomocą JSTL:</b></p>
        <table>
            <tr>
                <th>Id</th>
                <th>Nazwisko</th>
                <th>Imię</th>
            </tr>
            <c:forEach items="${ids}" var="id" varStatus="s">
                <tr>
                    <td><c:out value = "${id}"/></td>
                    <td><c:out value = "${surnames[s.index]}"/></td>
                    <td><c:out value = "${names[s.index]}"/></td>    
                </tr>
            </c:forEach>
        </table>
        <p><b>Parametry aplikacji:</b></p>
        <%
            String parametr1 = (String)getServletContext().getAttribute("parametr1");
            String parametr2 = (String)getServletContext().getAttribute("parametr2");
            pageContext.setAttribute("param1",parametr1);
            pageContext.setAttribute("param2",parametr2);
            out.print("<p>"+parametr1+"</p>");
            out.print("<p>"+parametr2+"</p>");
        %>
        <p><b>Parametry aplikacji po pszekształceniu przez funkcje JSTL:</b></p>
        <p class="someClass">UpperCase:</p>
        <p><c:out value = "${fn:toUpperCase(param1)}"/></p>
        <p><c:out value = "${fn:toUpperCase(param2)}"/></p>
        <p class="someClass">LowerCase:</p>
        <p><c:out value = "${fn:toLowerCase(param1)}"/></p>
        <p><c:out value = "${fn:toLowerCase(param2)}"/></p>
        <form method="get" action="mainServlet" >
            <input type="submit" value="Wywołaj serwlet">
        </form>
        <form method="get" action="mainServlet" >
            <input type="hidden" value="wartosc" name="param">
            <input type="submit" value="Wyloguj">
        </form>
        <a href="/WebApplication/webresources/restservice">RESTful service</a>
        <div id="chartContainer"></div>
        <p id="messageID"></p>
    </body>
    
</html>
