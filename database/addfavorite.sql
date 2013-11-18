DELIMITER $$

CREATE PROCEDURE `jj`.`addfavorite`(IN own INTEGER, IN entry INTEGER)
READS SQL DATA
  COMMENT 'add favorite entry'
  BEGIN
    INSERT INTO favorites (owner, entryid) VALUES (own, entry);
  END $$

DELIMITER ;