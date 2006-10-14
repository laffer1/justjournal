<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet href="#UserStyleSheet" type="text/css"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>$JOURNAL_TITLE$</title>
	<link rel=stylesheet" type="text/css" href="/styles/users.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="/styles/$USER_STYLESHEET$" />
    <link rel="alternate" type="application/rss+xml" title="RSS" href="http://www.justjournal.com/users/$USER$/rss" />
    $USER_STYLESHEET_ADD$
</head>
<body>
	<!-- Header: Begin -->
	<div id="header">
		<h1>$JOURNAL_TITLE$</h1>
	</div>
	<!-- Header: End -->

	<!-- Menu: Begin -->
	<div id="menu">
		$USER_AVATAR$
	<p id="muser">
		<a href="/users/$USER$">Journal Entries</a><br />
		<a href="/users/$USER$/calendar">Calendar</a><br />
		<a href="/users/$USER$/friends">Friends</a><br />
		<a href="/favorite/view.h">Favorites</a><br />
		<a href="/users/$USER$/ljfriends">LJ Friends</a><br />
		<a href="/profile.jsp?user=$USER$">Profile</a><br />
	</p>
	<p id="mgen">
		<a href="/update.jsp">Update Journal</a><br />
		$LOGIN_MENU$
	</p>
	<p id="mrss">
		<a href="/users/$USER$/rss"><img src="/images/rss2.gif" alt="Journal RSS feed" /></a><br />
        <a href="/users/$USER$/subscriptions">RSS Reader</a>
	</p>
	$RECENT_ENTRIES$
    </div>
	<!-- Menu: End -->

	<!-- Content: Begin -->
    <div id="content">
    $CONTENT$
	</div>
	<!-- Content: End -->

	<!-- Footer: Begin -->
	<div id="footer">
		<a href="/index.jsp" title="JustJournal.com: Online Journals">JustJournal.com</a> 	</div>
	<!-- Footer: End -->

</body>
</html>
