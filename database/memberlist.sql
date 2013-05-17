DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`memberlist` $$
CREATE PROCEDURE `jj`.`memberlist` ()
BEGIN
  SELECT id, username, name, since FROM user ORDER BY username;
END $$

DELIMITER ;