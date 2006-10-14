<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Support</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="."/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Support</h2>

    <p><a href="faq.jsp">FAQ</a></p>

    <h3>Lose your password?</h3>

    <p>Passwords are encrypted in the database and can not be recovered. Send an e-mail to
        <a href="mailto:luke@foolishgames.com">luke@foolishgames.com</a> with your username from
        the e-mail address you signed up with. I can reset the password for you.</p>

    <h3>Bugs</h3>

    <p><a href="bugs.jsp">Bug List</a> (reported bugs and bugs that were fixed)</p>

    <h3>What features are working?</h3>
    <ul>
        <li>Creating Accounts</li>
        <li>Adding Journal Entries (no validation). If you check disable auto format you can input
            XHTML for journal entries.</li>
        <li>Viewing Journals. goto http://www.justjournal.com/users/<strong>yourUserName</strong> (the
            username is not case sensitive)</li>
        <li>Loggging in to post entries and view private/friends/public entries.</li>
        <li><a href="../users/jjsite">Journal </a>to track server upgrades and new features</li>
        <li>Security Levels Public, friends, and Private</li>
        <li>Comments</li>
        <li>Friends Viewer</li>
        <li>Friends Add / Remove</li>
        <li>More Style Templates</li>
        <li>RSS representation of recent entries (public entries only)</li>
        <li>125 moods</li>
        <li>View friends on LJ, Add / Remove</li>
        <li>5 Themes</li>
        <li>Customize Journal colors and layout through CSS.</li>
        <li>LJ Friends page now shows some HTML code, user pictures, hyperlinks
            to LJ journals (via user image)</li>
        <li>Calendar allows you to view old entries, and see when you made entries.</li>
        <li>Spell Check entries when posting</li>
        <li>delete entries and comments</li>
        <li>edit entries</li>
        <li>Autoformat preference so html can be added safely (not in comments)</li>
        <li>RSS subscriptions (other sources)</li>
        <li>Favorites</li>
        <li>Client for Windows 98/2000/ME/XP</li>
        <li>RSS 2.0</li>
    </ul>

    <h3>What features are you working on?</h3>
    <ul>
        <li>documentation / manual for jj </li>
        <li>e-mailing comments</li>
        <li>disable e-mailing comments </li>
        <li>User Preferences</li>
        <li>View old LJ? </li>
        <li>Profile (waiting on user preferences)</li>
        <li>Pictures</li>
        <li>Cancel Account</li>
        <li>Password reset</li>
        <li>More Style Templates (always)</li>
        <li>HTML templates (this one is on the back burner)</li>
        <li>Security Level ( password protected ). This partially works. The comment
            viewer still displays entries if they are set public.</li>
        <li>Client for Mac OS X</li>
        <li>Java Client (in progress)</li>
        <li>Considering adding support for RSS 1.0 RDF</li>
        <li>Export Journal as XML</li>
        <li>Ditching JSP in favor of new template system based on open standards
            including XML and XSL. This would allow me to revise the journal viewer
            to use XML directly which was my original plan. I'm experimenting with this now.
            The entry edit system uses XML.</li>
        <li>Communities. Caryn is working on this. (stalled) </li>
        <li>Photo gallery</li>
        <li>custom links</li>
        <li>many more...</li>
    </ul>

    <h3>How do I ask a question or report a bug?</h3>

    <p>E-mail <a href="mailto:luke@foolishgames.com">Luke@FoolishGames.com</a>.
        In the subject, please include just journal.</p>

    <h3>Getting Started</h3>

    <dl>
        <dt><a href="../create.jsp"><strong>Create A Journal</strong></a>
        <dd>Come and create your very own JustJournal!

            <dt><a href="../update.jsp"><strong>Update Journal</strong></a>
        <dd>Update your JustJournal from the web.
    </dl>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>