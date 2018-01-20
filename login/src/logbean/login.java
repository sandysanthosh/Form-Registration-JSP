package logbean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
public class login extends HttpServlet{ 
 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException,IOException{
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();
  System.out.println("MySQL Connect Example.");
  Connection conn = null;
  String url = "jdbc:mysql://localhost:3306/sandy";
  String dbName = "";
  String driver = "com.mysql.jdbc.Driver";
  String userName = "root"; 
  String password = "sandy";
 String username="";
 String userpass="";
 String strQuery= ""; 
  Statement st=null;
  ResultSet rs=null;
  HttpSession session = request.getSession(true);
  try {
  Class.forName(driver).newInstance();
  conn = (Connection) DriverManager.getConnection(url+dbName,userName,password);
  if(request.getParameter("username")!=null &&
     request.getParameter("username")!="" && request.getParameter("password")!=null &&
     request.getParameter("password")!="")
  {
  username = request.getParameter("username").toString();
  userpass = request.getParameter("password").toString();
  strQuery="select * from userregister where username='"+username+"' and  password='"+userpass+"'";
 System.out.println(strQuery);
  st = (Statement) conn.createStatement();
  rs = st.executeQuery(strQuery);
  int count=0;
  while(rs.next())
  {
  session.setAttribute("username",rs.getString(2));
  count++;
  }
  if(count>0)
  {
  response.sendRedirect("welcome.jsp");
  }
  else
  {
 response.sendRedirect("error.jsp");
  }
  }
  else
  {
 response.sendRedirect("login.jsp");
  }
  System.out.println("Connected to the database"); 
  conn.close();
  System.out.println("Disconnected from database");
  } catch (Exception e) {
  e.printStackTrace();
  }
  }
}