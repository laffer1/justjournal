<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Cancel Journal</title>
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
            width: 230px;
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
    <h2>Cancel Journal</h2>

    <div style="width: 410px; padding: 5px; margin: 0">
        <form method="post" action="DeleteAccount" name="frmCancelJournal">

            <fieldset>
                <legend><strong>User Information</strong><br/></legend>

                <p>You must be logged in before you can cancel your account. Then, you must type your username
                and password below to verify you want to cancel.  The process will wipe all contents of your blog
                without any way to get it back.</p>

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

            <div class="row"><input type="submit" name="submit" value="submit"/></div>
        </form>

    </div>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
