<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.db.SecurityDao" %>
<%@ page import="com.justjournal.db.SecurityTo" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN"
  "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page contentType="application/xml+xhtml; charset=utf-8" language="java" %>
<html>

    <head>
        <title>JustJournal.com: Login</title>
        <script language="JavaScript" type="text/javascript" src="js/sha1.js"></script>
        <script language="JavaScript" type="text/javascript">
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
        </script>
    </head>

    <body>
        <h1>JustJournal.com</h1>
        <h2>Login</h2>

        <form method="post" name="alogin" action="../loginAccount?mobile=yes" id="blogin">
                 <input type="hidden" name="password_hash" id="ipassword_hash" value=""/>

                 <fieldset>
                     <legend><strong>Just Journal Account</strong><br/></legend>

                     <div class="row">
                         <span class="label">Username</span>
           <span class="formw">
          <input type="text" name="username" id="iusername" size="18" maxlength="15" /></span>
                     </div>

                     <div class="row">
                         <span class="label">Password</span>
           <span class="formw"><input type="password" name="password" id="ipassword" size="18" />
           </span>
                     </div>

                 </fieldset>

                 <div class="row">
                     <input type="submit" name="submitlogin" value="Login" onclick="return sendForm()"/>
                 </div>

             </form>

 
    </body>
</html>