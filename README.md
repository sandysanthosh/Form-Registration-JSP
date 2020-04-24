# LOGIN-JSP

JSP page form username and password validation using mysql


pom.xml:

<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>javax.mail</artifactId>
    <version>1.6.0</version>
</dependency>

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.8.1</version>
</dependency>

INDEX.HTML:

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Reset Password</title>
    <link rel="stylesheet" href="/css/style.css" >
    <script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery.validate.min.js"></script>
</head>
<body>
    <jsp:directive.include file="header.jsp" />
     
    <div align="center">
        <h2>Reset Your Password</h2>
        <p>
        Please enter your login email, we'll send a new random password to your inbox:
        </p>
         
        <form id="resetForm" action="reset_password" method="post">
            <table>
                <tr>
                    <td>Email:</td>
                    <td><input type="text" name="email" id="email" size="20"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <button type="submit">Send me new password</button>
                    </td>
                </tr>    
            </table>
        </form>
    </div>
     
    <jsp:directive.include file="footer.jsp" />
     
<script type="text/javascript">
 
    $(document).ready(function() {
        $("#resetForm").validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }      
            },
             
            messages: {
                email: {
                    required: "Please enter email",
                    email: "Please enter a valid email address"
                }
            }
        });
 
    });
</script>
</body>
</html>

WEB.XML:

<context-param>
    <param-name>host</param-name>
    <param-value>your_stmp_server</param-value>
</context-param>
 
<context-param>
    <param-name>port</param-name>
    <param-value>25</param-value>
</context-param>
 
<context-param>
    <param-name>email</param-name>
    <param-value>your sender email</param-value>
</context-param>
 
<context-param>
    <param-name>name</param-name>
    <param-value>your sender name</param-value>
</context-param>
  
<context-param>
    <param-name>pass</param-name>
    <param-value>your sender email password</param-value>
</context-param>

<context-param>
    <param-name>host</param-name>
    <param-value>smtp.gmail.com</param-value>
</context-param>
 
<context-param>
    <param-name>port</param-name>
    <param-value>587</param-value>
</context-param>
 
<context-param>
    <param-name>email</param-name>
    <param-value>YOUR_EMAIL</param-value>
</context-param>
<context-param>
    <param-name>name</param-name>
    <param-value>YOUR_NAME</param-value>
</context-param>
<context-param>
    <param-name>pass</param-name>
    <param-value>YOUR_PASSWORD</param-value>
</context-param>


SERVELT.JAVA:

import java.io.IOException;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.bookstore.service.CustomerServices;
 
/**
 * A Java Servlet to handle requests to reset password for customer
 *
 * @author www.codejava.net
 *
 */
@WebServlet("/reset_password")
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    private String host;
    private String port;
    private String email;
    private String name;
    private String pass;
 
    public void init() {
        // reads SMTP server setting from web.xml file
        ServletContext context = getServletContext();
        host = context.getInitParameter("host");
        port = context.getInitParameter("port");
        email = context.getInitParameter("email");
        name = context.getInitParameter("name");
        pass = context.getInitParameter("pass");
    }
 
    public ResetPasswordServlet() {
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        String page = "reset_password.jsp";
        request.getRequestDispatcher(page).forward(request, response);
 
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String recipient = request.getParameter("email");
        String subject = "Your Password has been reset";
 
        CustomerServices customerServices = new CustomerServices(request, response);
        String newPassword = customerServices.resetCustomerPassword(recipient);
 
        String content = "Hi, this is your new password: " + newPassword;
        content += "\nNote: for security reason, "
                + "you must change your password after logging in.";
 
        String message = "";
 
        try {
            EmailUtility.sendEmail(host, port, email, name, pass,
                    recipient, subject, content);
            message = "Your password has been reset. Please check your e-mail.";
        } catch (Exception ex) {
            ex.printStackTrace();
            message = "There were an error: " + ex.getMessage();
        } finally {
            request.setAttribute("message", message);
            request.getRequestDispatcher("message.jsp").forward(request, response);
        }
    }
 
}

https://www.codejava.net/coding/how-to-implement-forgot-password-feature-for-java-web-application
