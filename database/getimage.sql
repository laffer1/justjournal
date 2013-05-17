DELIMITER $$

DROP PROCEDURE IF EXISTS `jj`.`getimage` $$
CREATE DEFINER=`laffer1`@`70.91.226.204` PROCEDURE `getimage`(IN imgid INTEGER)
    READS SQL DATA
    COMMENT 'image viewer'
SELECT mimetype,image FROM user_pic WHERE id=imgid $$

DELIMITER ;