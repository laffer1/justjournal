<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet href="#UserStyleSheet" type="text/css"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>$JOURNAL_TITLE$</title>
	<link rel="stylesheet" type="text/css" href="/styles/users.css"/>
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
		<a href="/users/$USER$/pictures">Pictures</a><br />
		<a href="/profile.jsp?user=$USER$">Profile</a><br />
	</p>
	<p id="mgen">
		<a href="/update.jsp">Update Journal</a><br />
		$LOGIN_MENU$
	</p>

    <p id="mrss">
    	<a href="/users/$USER$/subscriptions">RSS Reader</a><br />
    	<a href="/users/$USER$/rss"><img src="/images/rss2.gif" alt="Journal RSS feed" /></a><br />
    </p>

    <p id="mrssmsn">
    	<a href="http://my.msn.com/addtomymsn.armx?id=rss&amp;ut=http://www.justjournal.com/users/$USER$/rss"><img src="/images/rss_mymsn.gif" alt="Add to My MSN" /></a><br />
    </p>

    <p id="mrsswindowslive">
    	<a href="http://www.live.com/?add=http://www.justjournal.com/users/$USER$/rss"><img src="/images/rss_windowslive.gif" alt="Add to Windows Live" /></a><br />
    </p>

    <p id="mrssyahoo">
    	<a href="http://add.my.yahoo.com/rss?url=http://www.justjournal.com/users/$USER$/rss"><img src="/images/rss_myyahoo.gif" alt="Add to My Yahoo§" /></a><br />
    </p>

    <p id="mrssbloglines">
    	<a href="http://www.bloglines.com/sub/http://www.justjournal.com/users/$USER$/rss"><img src="/images/rss_bloglines.gif" alt="Add to Bloglines" /></a><br />
    </p>

    <p id="mtechnosearch">
    	<a href="http://www.justjournal.com/users/$USER$/tsearch">Search via Technorati</a><br />
    </p>

    <p id="mpdfrtf">
    	<a href="http://www.justjournal.com/users/$USER$/pdf">PDF</a><br />
    	<a href="http://www.justjournal.com/users/$USER$/rtf">RTF</a><br />
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
