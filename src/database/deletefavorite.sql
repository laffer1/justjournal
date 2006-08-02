DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`deletefavorite` $$
CREATE DEFINER=`laffer1`@`70.91.226.204` PROCEDURE `deletefavorite`(IN own INTEGER, IN entry INTEGER)
    READS SQL DATA
    COMMENT 'Delete favorite entry'
DELETE FROM favorites WHERE owner=own AND entryid=entry LIMIT 1 $$

DELIMITER ;