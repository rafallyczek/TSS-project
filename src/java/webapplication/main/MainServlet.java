package webapplication.main;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import webapplication.logic.AppLogic;

public class MainServlet extends HttpServlet {
        
    Connection connection;
    String current_user;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        
        HttpSession sesja;
        sesja = request.getSession(false);
        if(sesja.getAttribute("username")==null){
            sesja.setAttribute("username",request.getRemoteUser());
            current_user = (String)sesja.getAttribute("username");
        }else{
            current_user = (String)sesja.getAttribute("username");
        }
                
        ResultSet rs;
        
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
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            out.println("<p><b>Zalogowany użytkownik: </b>"+current_user+"</p>");
            out.println("<style> td { border: 1px solid black; } th { border: 1px solid black; } "
                    + "table { border-collapse: collapse; } .formInputs{ margin-top: 5px; } "
                    + "label, input{ display: block; } form > input{ margin-top: 5px; }"
                    + "td form input{ margin: 0; display: inline-block; }"
                    + "td form{ margin: 0; display: inline-block; }"
                    + "</style>");
            out.println("<p><b>Tabela wygenerowana przez serwlet:</b></p>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Id</th>");
            out.println("<th>Nazwisko</th>");
            out.println("<th>Imię</th>");
            out.println("<th>Usuń</th>");
            out.println("</tr>");
            for(int i=0;i<id.size();i++){
                out.println("<tr>");
                    out.println("<td>");
                        out.println(id.get(i));
                    out.println("</td>");
                    out.println("<td>");
                        out.println(nazwisko.get(i));
                    out.println("</td>");
                    out.println("<td>");
                        out.println(imie.get(i));
                    out.println("</td>");
                    out.println("<td>");
                        out.println("<form method=\"post\" action=\"mainServlet\" >");
                        out.println("<input type=\"hidden\" name=\"param\" value=\"wartosc\">");        
                        out.println("<input id=\"userid\" type=\"hidden\" name=\"userid\" value=\""+id.get(i)+"\">");
                        out.println("<input type=\"submit\" value=\"Usuń\">");
                        out.println("</form>");
                    out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</br>");
            
            out.println("<p><b>Dodaj rekord do bazy:</b></p>");
            out.println("<form method=\"post\" action=\"mainServlet\" >");
            
            out.println("<div class=\"formInputs\">");
            out.println("<label for=\"userid\">Id</label>");
            out.println("<input id=\"userid\" type=\"text\" name=\"userid\" required>");
            out.println("</div>");
            
            out.println("<div class=\"formInputs\">");
            out.println("<label for=\"surname\">Nazwisko</label>");
            out.println("<input id=\"surname\" type=\"text\" name=\"surname\" required>");
            out.println("</div>");
            
            out.println("<div class=\"formInputs\">");
            out.println("<label for=\"firstname\">Imię</label>");
            out.println("<input id=\"firstname\" type=\"text\" name=\"firstname\" required>");
            out.println("</div>");
            
            out.println("<input type=\"submit\" value=\"Dodaj rekord\">");
            out.println("</form>");
            out.println("<a href=\"/WebApplication/\">Strona główna</a>");
        }finally{
            out.close();
        }
        
    }
    
    protected void addRow(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{
        
        String zapytanie = "insert into db_user_table (id,nazwisko,imie) values(?,?,?)";
        
        PreparedStatement st;
        
        try{
            st = connection.prepareStatement(zapytanie);
            st.setInt(1,Integer.parseInt(request.getParameter("userid")));
            st.setString(2,request.getParameter("surname"));
            st.setString(3,request.getParameter("firstname"));
            st.executeUpdate();
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
        response.sendRedirect("/WebApplication/mainServlet");
        
    }
    
    protected void deleteRow(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{
        
        String zapytanie = "delete from db_user_table where id=?";
        
        PreparedStatement st;
        
        try{
            st = connection.prepareStatement(zapytanie);
            st.setInt(1,Integer.parseInt(request.getParameter("userid")));
            st.executeUpdate();
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
        response.sendRedirect("/WebApplication/mainServlet");
        
    }
    
    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.logout();
        response.sendRedirect("/WebApplication/");
    }   
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("param")==null){
            processRequest(request, response);
        }else{
            logout(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        if(request.getParameter("param")==null){
            addRow(request, response);
        }else{
            deleteRow(request, response);
        }        
    }
    
    @Override
    public void init(){
        
        try {       
            connection = AppLogic.connect();
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
    }
    
}
