DELIMITER $$

CREATE PROCEDURE `viewfavorite`(IN ownerid INTEGER)
    READS SQL DATA

BEGIN
  SELECT id,owner,entryid FROM favorites WHERE owner=ownerid ORDER BY entryid DESC;
 END $$

 DELIMITER ;