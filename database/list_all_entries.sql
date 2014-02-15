DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`list_all_entries` $$
CREATE PROCEDURE `jj`.`list_all_entries`()
  BEGIN
    SELECT
      us.id          AS id,
      us.username,
      eh.date        AS date,
      eh.subject     AS subject,
      eh.music,
      eh.body,
      mood.title     AS moodt,
      location.title AS location,
      eh.id          AS entryid,
      eh.security    AS security,
      eh.autoformat,
      eh.allow_comments,
      eh.email_comments,
      location.id    AS locationid,
      mood.id        AS moodid
    FROM user AS us, entry AS eh, mood, location
    WHERE us.id = eh.uid AND mood.id = eh.mood AND location.id = eh.location AND eh.security = 2
    ORDER BY eh.date DESC, eh.id DESC;
  END $$

DELIMITER ;