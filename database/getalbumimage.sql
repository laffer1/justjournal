DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`getalbumimage` $$
CREATE DEFINER=`laffer1`@`70.91.226.204` PROCEDURE `getalbumimage`(IN imgid INTEGER)
    READS SQL DATA
    COMMENT 'album image viewer'
SELECT mimetype,image FROM user_images WHERE id=imgid $$

DELIMITER ;