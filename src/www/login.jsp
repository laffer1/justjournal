<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Login</title>
    <link rel="stylesheet" type="text/css" href="./layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="./font-normal.css" media="all"/>
    <link rel="home" title="Home" href="./index.jsp"/>
    <link rel="contents" title="Site Map" href="./sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="./support/index.jsp"/>

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
    <script language="JavaScript" type="text/javascript" src="js/sha1.js"></script>
    <script language="JavaScript" type="text/javascript">
        <!--
        function sendForm(formid, checkuser)
        {

            if (formid == null)
                formid = 'alogin';

            // 'checkuser' is the element id name of the username textfield.
            // only use it if you care to verify a username exists before hashing.

            if (! document.getElementById)
                return true;

            var loginform = document.getElementById(formid);
            if (! loginform) return true;

            // Avoid accessing the password field if there is no username.
            // This works around Opera < 7 complaints when commenting.
            if (checkuser) {
                var username = null;
                for (var i = 0; username == null && i < loginform.elements.length; i++) {
                    if (loginform.elements[i].id == checkuser) username = loginform.elements[i];
                }
                if (username != null && username.value == "") return true;
            }

            if (! loginform.password)
                return true;

            var pass = loginform.password.value;

            loginform.password_hash.value = hex_sha1(pass);
            loginform.password.value = "";  // dont send clear-text password!
            return true;
        }
        -->
    </script>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <h2>Login</h2>

    <p>The password policy recently changed. If your password contains
        certain special characters, you will need to reset it. ()*+-=,:;? ^ &amp; % are no longer allowed.
        You may e-mail luke@justjournal.com to have a temporary password
        set which you can then change.</p>

    <p>Authentication is required to view private and friends
        journal entries or to post new entries.</p>

    <p>Optional: Switch to <a href="https://www.justjournal.com/login.jsp">SSL</a>
        so that your password is encrypted. (padlock)
    </p>

    <div style="width: 400px; padding: 5px; margin: 0px;">
        <form method="post" name="alogin" action="./loginAccount" id="blogin">
            <input type="hidden" name="password_hash" id="ipassword_hash" value=""/>

            <fieldset>
                <legend><strong>Just Journal Account</strong><br/>
                </legend>


                <div class="row">
                    <span class="label">Username</span>
      <span class="formw">
     <input type="text" name="username" id="iusername" size="18" maxlength="15"
            style="background: url(images/userclass_16.png) no-repeat; background-color: #fff; background-position: 0px 1px; padding-left: 18px; color: black; font-weight: bold;"/></span>
                </div>

                <div class="row">
                    <span class="label">Password</span>
      <span class="formw"><input type="password" name="password" id="ipassword" size="18"
                                 style="background: white; color: black; font-weight: bold;"/>
      </span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer"> &nbsp; </div>
            </fieldset>

            <div class="row">
                <input type="submit" name="submitlogin" value="Login" onclick="return sendForm()"/>
            </div>

            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer"> &nbsp; </div>
        </form>

        <p>Don't have a JJ account yet? <a href="create.jsp">Create an account.</a></p>

    </div>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
