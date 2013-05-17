<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Support: Bugs</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Support: Bugs</h2>

    <p>The e-mail address to report bugs is at the bottom of the page. </p>

    <h3>Known Bugs</h3>
    <ul>
        <li>Preferences system is not finished.</li>
        <li>Validation could be better on all forms.</li>
        <li>Cancel account is not automated</li>
        <li>Error reporting is very poor.</li>
        <li>Documentation is weak.</li>
        <li>When spell checking an entry the date is changed and the location setting is lost</li>
        <li>No way to subscribe to RSS content.</li>
        <li>Additional bugs on source forge site</li>
    </ul>

    <h3>Bugs fixed</h3>
    <ul>
        <li>favorites do not work</li>
        <li>edit entries doesn't work</li>
        <li>Spell check, friends security, comments fixed.</li>
        <li>Profile doesn't work. The profile code should work now. There was a problem with the mysql jdbc driver and
            prepared statements. Users still can't edit most of the properties on profiles yet.</li>
        <li>Some mood icons don't display. All moods that have an icon or a parent icon now display properly. Some moods
            do not have a parent mood or an icon. There is not a single parent icon that defaults. </li>
        <li>Paging was brought back for recent entries </li>
        <li>Calendar did not display anything or only entires in 2004. You can now view all entries in the calendar from
            the beginning of your account. If you backdate an entry before the year your account was started, you won't
            see it on the calendar.</li>
        <li>New date handler broke entry submission, friends viewer </li>
        <li>Safari 1.1 rendering fixes. </li>
        <li>Spell check works </li>
        <li>Most mood icons do not show up</li>
        <li>LJ Friends images are not clickable</li>
        <li>Posting entries does not work. (problem with new entry class)</li>
        <li>Style Overrides do not take effect.</li>
        <li>Calendar doesn't work</li>
        <li>Site is slow (changed database table format, increased ram to mysql)</li>
        <li>Style Templates are enabled</li>
        <li>StyleOverrides are saved to the database (but are not seen in viewer yet)</li>
        <li>Style preferences do something in control panel</li>
        <li>Bug fixes with IE6 rendering</li>
        <li>This page wasn't updated. Sorry!</li>
        <li>Comments only appear on recent entries and not on the friends page.</li>
        <li>all journal entries are listed on the same page</li>
        <li>Cancel link does not work</li>
        <li>Sometimes browser does not refresh the journal page. Probably a problem
            with the expires header sent to the browser. </li>
        <li>Login page works.</li>
        <li>Passwords are case sensitive? I decided this is a feature... since it
            makes the site more secure.</li>
        <li>No validation creating accounts</li>
        <li>Usernames are case sensitive</li>
        <li>two entries submitted with the same date and time may not show up newest
            first in the viewer </li>
        <li>You can now use single ticks in journal entry bodies and subjects
            (apostrophe or single quote character). You can say &quot;i'm&quot; now.
            :)</li>
        <li>Hitting submit 3 times on the create page does not create your account
            3 times anymore. (fixed at database level, need code in java servlet
            too)</li>
        <li>Journals are displayed in decending order by date (well usually.. see
            above)</li>
        <li>Site works better in Safari, and Netscape 7</li>
        <li>IE 5.22 mac almost works ok now.</li>
        <li>Journal entries are now displayed without xml garbage</li>
        <li>Journal viewer works</li>
    </ul>

    <h3>How do I ask a question or report a bug?</h3>

    <p>E-mail <a href="mailto:luke@foolishgames.com">Luke@FoolishGames.com</a>.
        In the subject, please include just journal.</p>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>