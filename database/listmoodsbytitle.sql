DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`listmoodsbytitle` $$
CREATE PROCEDURE `jj`.`listmoodsbytitle`()
  BEGIN
    SELECT
      id,
      parentmood,
      title
    FROM mood
    ORDER BY title ASC;
  END $$

DELIMITER ;