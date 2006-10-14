<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Create a Journal</title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>

    <style type="text/css" media="all">
        <!--

        div.row {
            clear: both;
            padding-top: 5px;
        }

        div.row span.label {
            float: left;
            width: 140px;
            text-align: right;
        }

        div.row span.formw {
            float: right;
            width: 225px;
            text-align: left;
        }

        div.spacer {
            clear: both;
        }

        -->
    </style>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <h2>Create a Journal</h2>

    <p>Sign up for a free account.</p>

    <p>Usernames can only contain letters (upper and lower case) and numbers. It must
        be at 3-15 characters long.</p>

    <p>Passwords must be at least 5 characters in length and up to 18 characters. You can use letters, numbers, and
        the following: <br/>
        _ @ . # $</p>

    <div style="width: 400px; padding: 5px; margin: 0px;">
        <form method="post" action="newAccount" name="frmCreateJournal">

            <fieldset>
                <legend><strong>User Information</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="fname">First Name</label></span> <span class="formw"><input
                        type="text" name="fname" id="fname" maxlength="50" size="25"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="email">E-mail Address</label></span> <span class="formw"><input
                        type="text" name="email" id="email" maxlength="100" size="25"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="username">Username</label></span> <span class="formw"><input
                        type="text" name="username" id="username" maxlength="15" size="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="password">Password</label></span> <span class="formw"><input
                        type="text" name="password" id="password" maxlength="18" size="18"/></span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer">
                    &nbsp;
                </div>
            </fieldset>

            <div class="row"><input type="submit" name="submit" value="Sign up"/></div>
        </form>

        <p>Already have an account? <a href="login.jsp">Login</a></p>

    </div>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>