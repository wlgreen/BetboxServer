<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.betbox.server.data.Bet" %>
<%@ page import="com.betbox.server.data.StandPool" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <head>
    <link type="text/css" rel="script" href="/script/main.css" />
  </head>


  <body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
      pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
    }
%>

<%
	Iterable<Entity> bets = Bet.getAllBets();
	if (bets == null) {
		%>
		<p>There are no bets.<p>
		<%
	} else {
		%>
           		<p>
           		<form action="/checkoutBet" method="post">
           		<label for="content">Select bet to checkout:</label>
           		<select id="content" name="content">
				<%
				for (Entity bet : bets) {
				%>
					<option value="<%= (bet.getProperty(Bet.PROPERTY_CONTENT).toString()) %>" > 
					<%= (bet.getProperty(Bet.PROPERTY_CONTENT).toString()) %> &nbsp;|&nbsp; 
					<%= (bet.getProperty(Bet.PROPERTY_TIME).toString()) %> &nbsp;|&nbsp; 
					<%= (bet.getProperty(Bet.PROPERTY_STATUS).toString()) %> &nbsp;|&nbsp;
					<%= (new StandPool(bet.getProperty(Bet.PROPERTY_POOL).toString())) %>
					</option>
				<%
		 		}
				%>
				 </select>
				 </label>
				 <p><input id="edit1" type="submit" name="submit" value="Check Out"></p>
				</form>
		 <%
	 }
%>

<form action="/createBet" method="post">
  <div><textarea name="content" rows="3" cols="60"></textarea></div>
  <div><input type="submit" value="Post new bet" /></div>
</form>
  </body>
</html>