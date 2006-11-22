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
        width: 325px;
        text-align: left;
    }

    div.spacer {
        clear: both;
    }
</style>
<script type="text/javascript">
var minpwlength = 5;
var fairpwlength = 8;

var STRENGTH_SHORT = 0;  // less than minpwlength
var STRENGTH_WEAK = 1;  // less than fairpwlength
var STRENGTH_FAIR = 2;  // fairpwlength or over, no numbers
var STRENGTH_STRONG = 3; // fairpwlength or over with at least one number

img0 = new Image();
img1 = new Image();
img2 = new Image();
img3 = new Image();

img0.src = 'images/password/tooshort.jpg';
img1.src = 'images/password/fair.jpg';
img2.src = 'images/password/medium.jpg';
img3.src = 'images/password/strong.jpg';

var strengthlevel = 0;

var strengthimages = Array(img0.src,
        img1.src,
        img2.src,
        img3.src);

function updatestrength(pw) {

    if (istoosmall(pw)) {

        strengthlevel = STRENGTH_SHORT;

    }
    else if (!isfair(pw)) {

        strengthlevel = STRENGTH_WEAK;

    }
    else if (hasnum(pw)) {

        strengthlevel = STRENGTH_STRONG;

    }
    else {

        strengthlevel = STRENGTH_FAIR;

    }

    document.getElementById('strength').src = strengthimages[ strengthlevel ];

}

function isfair(pw) {

    if (pw.length < fairpwlength) {

        return false;

    }
    else {

        return true;

    }

}

function istoosmall(pw) {

    if (pw.length < minpwlength) {

        return true;

    }
    else {

        return false;

    }

}

function hasnum(pw) {

    var hasnum = false;

    for (var counter = 0; counter < pw.length; counter ++) {

        if (!isNaN(pw.charAt(counter))) {

            hasnum = true;

        }

    }


    return hasnum;

}
</script>

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

    <div style="width: 500px; padding: 5px; margin: 0px;">
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
                        type="text" name="password" id="password" maxlength="18" size="18"
                        onkeyup="updatestrength( this.value );"/></span>
                </div>

                <div class="row">
                    <span class="label">Password Strength</span>
                    <span class="formw">
                        <img src="images/password/tooshort.jpg" id="strength" alt="Password Strength"/>
                    </span>
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