DROP VIEW IF EXISTS `jj`.`v_entry_new_unique`;
CREATE ALGORITHM = UNDEFINED
  DEFINER =`laffer1`@`70.91.226.204`
  SQL SECURITY DEFINER VIEW `v_entry_new_unique` AS
  SELECT
    max(`entry`.`id`) AS `id`
  FROM `entry`
  WHERE (`entry`.`security` = 2)
  GROUP BY `entry`.`uid`;