DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`listmoods` $$
CREATE PROCEDURE `jj`.`listmoods`()
  BEGIN
    SELECT
      id,
      parentmood,
      title
    FROM mood
    ORDER BY parentmood, title;
  END $$

DELIMITER ;