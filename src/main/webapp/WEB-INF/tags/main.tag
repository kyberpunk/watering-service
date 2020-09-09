<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="body" fragment="true" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>

<!DOCTYPE html>
<html lang="${pageContext.request.locale}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><c:out value="${title}"/></title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"  crossorigin="anonymous">
        <jsp:invoke fragment="head"/>
        <script>
            window.onload = function () {
                var url = window.location.href;
                var searchIndex = url.indexOf("?");
                var param = url.substring(searchIndex + 1);
                if (param === 'deleteError') {
                    document.getElementById('error_message').style.display = 'block';
                }
            };
        </script>
    </head>
    <body>
        <!-- navigation bar -->
        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/home/"><f:message key="navigation.project"/></a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><f:message key="navigation.admin"/><b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><my:a href="/service/list"><f:message key="navigation.admin.service"/></my:a></li>
                                <li><my:a href="/person/list"><f:message key="navigation.admin.person"/></my:a></li>
                                <li><my:a href="/tires/list"><f:message key="navigation.admin.tire"/></my:a></li>
                                <li><my:a href="/order/list"><f:message key="navigation.admin.order"/></my:a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                <c:if test="${not empty User}">
                    <div class="row">
                        <div class="col-xs-6 col-sm-8 col-md-9 col-lg-10"></div>
                        <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    Login:<c:out value="${User}"/>
                                    <li><my:a href="/logout/logout">Logout</my:a></li>
                                    </div>
                                </div>
                            </div>
                        </div>
                </c:if>
                <c:if test="${not empty Admin}">
                    <div class="row">
                        <div class="col-xs-6 col-sm-8 col-md-9 col-lg-10"></div>
                        <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    Login:<c:out value="${Admin}"/>
                                    <li><my:a href="/logout/logout">Logout</my:a></li>
                                    </div>
                                </div>
                            </div>
                        </div>
                </c:if>
            </div>
        </nav>

        <div class="container">
            <!-- page title -->
            <c:if test="${not empty title}">
                <div class="page-header">
                    <img class="centerImage" src="${pageContext.request.contextPath}/logo.png" height="100" width="200" border="0"/><br>
                    <h1><c:out value="${title}"/></h1>
                </div>
            </c:if>

            <!-- authenticated user info -->
            <!-- TODO after implementing ProtectedFilter.class -->
            <!-- alerts -->
        <c:if test="${not empty alert_danger}">
          <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <c:out value="${alert_danger}"/></div>
        </c:if>
        <c:if test="${not empty alert_info}">
          <div class="alert alert-info" role="alert"><c:out value="${alert_info}"/></div>
        </c:if>
        <c:if test="${not empty alert_success}">
          <div class="alert alert-success" role="alert"><c:out value="${alert_success}"/></div>
        </c:if>
        <c:if test="${not empty alert_warning}">
          <div class="alert alert-warning" role="alert"><c:out value="${alert_warning}"/></div>
        </c:if>

            <!-- page body -->
            <jsp:invoke fragment="body"/>

            <!-- footer -->
            <footer class="footer">
                <p>&copy;&nbsp;<%=java.time.Year.now().toString()%>&nbsp;Group #2</p>
            </footer>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </body>
</html>