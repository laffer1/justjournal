CREATE TABLE IF NOT EXISTS `entry_statistics` (
  `id`             INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `uid`            INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `year`           SMALLINT UNSIGNED   NOT NULL DEFAULT '2003',
  `entry_count`    BIGINT  UNSIGNED    NOT NULL DEFAULT '0',
  `modified`       TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `entrystats` (`uid`, `year`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci
  COMMENT = 'Entry Statistics By Year';