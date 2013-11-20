DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`list_all_entries` $$
CREATE PROCEDURE `jj`.`list_all_entries` ()
BEGIN
  SELECT us.id As id, us.userName,
eh.date As date, eh.subject As subject, eh.music, eh.body,
mood.title As moodt, location.title As location, eh.id As entryid,
eh.security as security, eh.autoformat, eh.allow_comments, eh.email_comments, location.id as locationid, mood.id as moodid
FROM user As us, entry As eh, mood, location
WHERE us.id=eh.uid AND mood.id=eh.mood AND location.id=eh.location AND eh.security=2 ORDER BY eh.date DESC, eh.id DESC;
END $$

DELIMITER ;