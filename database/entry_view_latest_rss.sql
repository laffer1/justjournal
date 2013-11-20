DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`entry_view_latest_rss` $$
CREATE PROCEDURE `jj`.`entry_view_latest_rss` ()
BEGIN
  SELECT us.id As id, us.userName, eh.date As date, eh.subject As subject, eh.music, eh.body, mood.title As moodt,
location.title As location, eh.id As entryid, eh.security as security, eh.autoformat, eh.allow_comments,
eh.email_comments, location.id as locationid, mood.id as moodid
FROM user As us, entry As eh, mood, location WHERE
eh.id IN (Select id FROM v_entry_new_unique ORDER By id) AND
us.id=eh.uid AND mood.id=eh.mood AND location.id=eh.location
AND eh.security=2 ORDER BY eh.date DESC, eh.id DESC Limit 0,15;
END $$

DELIMITER ;