DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`entry_view_latest_rss` $$
CREATE PROCEDURE `jj`.`entry_view_latest_rss`()
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
    WHERE
      eh.id IN (SELECT
                  id
                FROM v_entry_new_unique
                ORDER BY id) AND
      us.id = eh.uid AND mood.id = eh.mood AND location.id = eh.location
      AND eh.security = 2
    ORDER BY eh.date DESC, eh.id DESC
    LIMIT 0, 15;
  END $$

DELIMITER ;