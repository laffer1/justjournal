-- 
-- Database: `jj`
-- justjournal.com  DATABASEJJ 
-- CREATE DATABASE `jj`
--  DEFAULT CHARACTER SET latin1
--  COLLATE latin1_swedish_ci;
-- USE jj;

  -- --------------------------------------------------------

--
-- Table structure for table `comments`
--
-- Creation: Feb 14, 2014 at 02:40 AM
--

CREATE TABLE IF NOT EXISTS `comments` (
  `id`       SMALLINT(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `eid`      INT(10) UNSIGNED     NOT NULL DEFAULT '0',
  `uid`      INT(10) UNSIGNED     NOT NULL DEFAULT '0',
  `subject`  VARCHAR(150)
             CHARACTER SET latin1 DEFAULT NULL,
  `date`     DATETIME             NOT NULL,
  `body`     TEXT
             CHARACTER SET latin1 NOT NULL,
  `modified` TIMESTAMP            NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `eid_uid` (`eid`, `uid`),
  KEY `userid` (`uid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COMMENT ='Journal Entry Comments'
  AUTO_INCREMENT =501;


-- --------------------------------------------------------

--
-- Table structure for table `content`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `content` (
  `id`        INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `uri_id`    INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `userid`    INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `mime_type` VARCHAR(75)      NOT NULL DEFAULT '',
  `preferred` ENUM('0', '1')   NOT NULL DEFAULT '0',
  `datasize`  INT(11)          NOT NULL DEFAULT '0',
  `data`      MEDIUMBLOB       NOT NULL,
  `metadata`  TEXT             NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `country`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `country` (
  `id`        INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`     VARCHAR(80)
              CHARACTER SET latin1    NOT NULL,
  `iso`       CHAR(2)
              COLLATE utf8_unicode_ci NOT NULL,
  `iso3`      CHAR(3)
              COLLATE utf8_unicode_ci DEFAULT NULL,
  `numcode`   SMALLINT(6) DEFAULT NULL,
  `iso_title` VARCHAR(50)
              COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `country_title` (`title`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  AUTO_INCREMENT =1199;

-- --------------------------------------------------------

--
-- Table structure for table `entry`
--
-- Creation: Feb 14, 2014 at 03:01 AM
--

CREATE TABLE IF NOT EXISTS `entry` (
  `id`             INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `uid`            INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `date`           DATETIME            NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modified`       TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `subject`        VARCHAR(255) DEFAULT NULL,
  `mood`           TINYINT(3) UNSIGNED DEFAULT NULL,
  `music`          VARCHAR(125)        NOT NULL DEFAULT '',
  `location`       TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `body`           TEXT                NOT NULL,
  `security`       TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `autoformat`     ENUM('Y', 'N')      NOT NULL DEFAULT 'Y',
  `allow_comments` ENUM('Y', 'N')      NOT NULL DEFAULT 'Y',
  `email_comments` ENUM('Y', 'N')      NOT NULL DEFAULT 'Y',
  `draft`          ENUM('Y', 'N')      NOT NULL DEFAULT 'Y'
  COMMENT 'is a draft entry',
  `attach_image`   INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `attach_file`    INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sec` (`security`),
  KEY `dateindex` (`date`),
  KEY `user_id` (`uid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =33659;

--
-- RELATIONS FOR TABLE `entry`:
--   `security`
--       `entry_security` -> `id`
--   `uid`
--       `user` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `entry_security`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `entry_security` (
  `id`    TINYINT(4)           NOT NULL DEFAULT '0',
  `title` CHAR(7)
          CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  COMMENT ='Journal Entry Security Setting';

-- --------------------------------------------------------

--
-- Table structure for table `entry_tags`
--
-- Creation: Feb 12, 2014 at 02:41 AM
--

CREATE TABLE IF NOT EXISTS `entry_tags` (
  `id`      INT(10) UNSIGNED      NOT NULL AUTO_INCREMENT,
  `entryid` INT(10) UNSIGNED      NOT NULL,
  `tagid`   SMALLINT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `entrytags` (`entryid`, `tagid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =8343;

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `favorites` (
  `id`      INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `owner`   INT(10) UNSIGNED NOT NULL,
  `entryid` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FAVORITE` (`owner`, `entryid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  COMMENT ='Favorite Entries'
  AUTO_INCREMENT =170;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `friends` (
  `pk`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `id`       INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `friendid` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`pk`),
  KEY `FRIEND` (`id`, `friendid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =111;

-- --------------------------------------------------------

--
-- Table structure for table `friends_lj`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `friends_lj` (
  `id`        INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `username`  CHAR(15)         NOT NULL DEFAULT '',
  `community` ENUM('Y', 'N')   NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`, `username`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `friends_lj_cache`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `friends_lj_cache` (
  `ljusername`  VARCHAR(15)    NOT NULL DEFAULT '',
  `community`   ENUM('Y', 'N') NOT NULL DEFAULT 'N',
  `lastupdated` DATETIME       NOT NULL DEFAULT '0000-00-00 00:00:00',
  `content`     TEXT,
  PRIMARY KEY (`ljusername`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hitcount`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `hitcount` (
  `resource` VARCHAR(200)
             COLLATE utf8_unicode_ci NOT NULL,
  `count`    MEDIUMINT(5) UNSIGNED   NOT NULL DEFAULT '0',
  `id`       INT(20) UNSIGNED        NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_resource` (`resource`) USING HASH
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  AUTO_INCREMENT =90714;

-- --------------------------------------------------------

--
-- Table structure for table `location`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `location` (
  `id`    TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `title` VARCHAR(15)         NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `mood`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `mood` (
  `id`         TINYINT(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `parentmood` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `title`      VARCHAR(30)         NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =126;

-- --------------------------------------------------------

--
-- Table structure for table `mood_themes`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `mood_themes` (
  `id`        INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `owner`     INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `name`      VARCHAR(50) DEFAULT NULL,
  `des`       VARCHAR(100) DEFAULT NULL,
  `is_public` ENUM('Y', 'N')   NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`, `is_public`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2;

-- --------------------------------------------------------

--
-- Table structure for table `mood_theme_data`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `mood_theme_data` (
  `id`          INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `moodthemeid` INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `moodid`      TINYINT(3) UNSIGNED    NOT NULL DEFAULT '0',
  `picurl`      VARCHAR(100) DEFAULT NULL,
  `width`       TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `height`      TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =8;

-- --------------------------------------------------------

--
-- Table structure for table `queue_mail`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `queue_mail` (
  `id`      INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `to`      VARCHAR(100)
            COLLATE utf8_bin NOT NULL,
  `from`    VARCHAR(100)
            COLLATE utf8_bin NOT NULL,
  `subject` VARCHAR(100)
            COLLATE utf8_bin NOT NULL,
  `body`    TEXT
            COLLATE utf8_bin NOT NULL,
  `purpose` VARCHAR(150)
            COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_bin
  AUTO_INCREMENT =1;

-- --------------------------------------------------------

--
-- Table structure for table `resources`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `resources` (
  `id`       INT(10) UNSIGNED     NOT NULL AUTO_INCREMENT,
  `name`     VARCHAR(200)         NOT NULL DEFAULT '',
  `active`   ENUM('0', '1')       NOT NULL DEFAULT '1',
  `security` TINYINT(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =2;

-- --------------------------------------------------------

--
-- Table structure for table `rss_cache`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `rss_cache` (
  `id`          INT(10) UNSIGNED     NOT NULL AUTO_INCREMENT,
  `interval`    TINYINT(10) UNSIGNED NOT NULL DEFAULT '0',
  `lastupdated` DATETIME             NOT NULL DEFAULT '0000-00-00 00:00:00',
  `uri`         TINYTEXT             NOT NULL,
  `content`     TEXT,
  PRIMARY KEY (`id`),
  KEY `rssurl` (`uri`(100))
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =67;

-- --------------------------------------------------------

--
-- Table structure for table `rss_subscriptions`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `rss_subscriptions` (
  `id`    INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `uri`   TINYTEXT         NOT NULL,
  `subid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'unique subscription id for easier changes',
  PRIMARY KEY (`subid`),
  UNIQUE KEY `UNIQUE` (`id`, `uri`(256))
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =18;

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `settings` (
  `name`  VARCHAR(25)      NOT NULL,
  `value` VARCHAR(100)     NOT NULL,
  `id`    INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =26;

-- --------------------------------------------------------

--
-- Table structure for table `state`
--
-- Creation: Feb 14, 2014 at 02:44 AM
--

CREATE TABLE IF NOT EXISTS `state` (
  `id`         INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `country_id` INT(11) UNSIGNED NOT NULL,
  `title`      VARCHAR(100)     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  KEY `country_id` (`country_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  AUTO_INCREMENT =1;

-- --------------------------------------------------------

--
-- Table structure for table `style`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `style` (
  `id`       TINYINT(10) UNSIGNED NOT NULL DEFAULT '0',
  `title`    VARCHAR(30)          NOT NULL DEFAULT '''''',
  `desc`     TINYTEXT             NOT NULL,
  `modified` TIMESTAMP            NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `tags` (
  `id`   SMALLINT(10) UNSIGNED   NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30)
         COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  AUTO_INCREMENT =1559;

-- --------------------------------------------------------

--
-- Table structure for table `timezones`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `timezones` (
  `id`   INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `names` (`name`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  PACK_KEYS =1
  AUTO_INCREMENT =1726;

-- --------------------------------------------------------

--
-- Table structure for table `trackback`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `trackback` (
  `id`           INT(10) UNSIGNED                        NOT NULL AUTO_INCREMENT,
  `eid`          INT(10) UNSIGNED                        NOT NULL
  COMMENT 'Entry ID',
  `date`         DATETIME                                NOT NULL
  COMMENT 'Trackback Date',
  `type`         ENUM('trackback', 'pingback', 'postit') NOT NULL DEFAULT 'trackback',
  `url`          VARCHAR(150)                            NOT NULL,
  `author_name`  VARCHAR(50)                             NOT NULL
  COMMENT 'dc:creator',
  `author_email` VARCHAR(150)                            NOT NULL,
  `blogname`     VARCHAR(150)                            NOT NULL,
  `subject`      VARCHAR(150)                            NOT NULL
  COMMENT 'title, name',
  `body`         TEXT                                    NOT NULL
  COMMENT 'description, excert, comment',
  `modified`     TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  COMMENT ='Trackback, Pingback, Post-it comments for blog entries'
  AUTO_INCREMENT =1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--
-- Creation: Feb 08, 2014 at 08:20 PM
--

CREATE TABLE IF NOT EXISTS `user` (
  `id`        INT(10) UNSIGNED     NOT NULL AUTO_INCREMENT,
  `username`  VARCHAR(15)          NOT NULL DEFAULT '',
  `password`  CHAR(40)             NOT NULL,
  `type`      TINYINT(3) UNSIGNED  NOT NULL DEFAULT '0',
  `name`      VARCHAR(20)          NOT NULL DEFAULT '',
  `lastname`  VARCHAR(20)
              CHARACTER SET utf8
              COLLATE utf8_unicode_ci DEFAULT NULL,
  `since`     SMALLINT(4) UNSIGNED NOT NULL DEFAULT '2014',
  `lastlogin` DATETIME DEFAULT NULL,
  `modified`  TIMESTAMP            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2908;

-- --------------------------------------------------------

--
-- Table structure for table `user_bio`
--
-- Creation: Feb 09, 2014 at 02:42 PM
--

CREATE TABLE IF NOT EXISTS `user_bio` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `content`  TEXT             NOT NULL,
  `modified` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2187;

-- --------------------------------------------------------

--
-- Table structure for table `user_contact`
--
-- Creation: Feb 09, 2014 at 02:34 PM
--

CREATE TABLE IF NOT EXISTS `user_contact` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `email`    VARCHAR(100)     NOT NULL DEFAULT '',
  `icq`      VARCHAR(20) DEFAULT NULL,
  `aim`      VARCHAR(30) DEFAULT NULL,
  `yahoo`    VARCHAR(30) DEFAULT NULL,
  `msn`      VARCHAR(150) DEFAULT NULL,
  `hp_uri`   VARCHAR(250) DEFAULT NULL,
  `hp_title` VARCHAR(50) DEFAULT NULL,
  `phone`    VARCHAR(14) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2168;

--
-- RELATIONS FOR TABLE `user_contact`:
--   `user_id`
--       `user` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_files`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_files` (
  `id`            INT(10) UNSIGNED        NOT NULL AUTO_INCREMENT,
  `ownerid`       INT(10) UNSIGNED        NOT NULL
  COMMENT 'user id',
  `title`         VARCHAR(150)
                  COLLATE utf8_unicode_ci NOT NULL,
  `date_modified` TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mimetype`      VARCHAR(40)
                  COLLATE utf8_unicode_ci NOT NULL,
  `data`          LONGBLOB                NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ownerid` (`ownerid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  AUTO_INCREMENT =1;

-- --------------------------------------------------------

--
-- Table structure for table `user_images`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_images` (
  `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`    VARCHAR(150) DEFAULT '',
  `owner`    INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `modified` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mimetype` VARCHAR(45)      NOT NULL DEFAULT '',
  `image`    MEDIUMBLOB       NOT NULL,
  PRIMARY KEY (`id`),
  KEY `imagesowner` (`owner`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  PACK_KEYS =0
  CHECKSUM =1
  AUTO_INCREMENT =135;

-- --------------------------------------------------------

--
-- Table structure for table `user_images_album`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_images_album` (
  `id`          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `owner`       INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `title`       VARCHAR(100)     NOT NULL DEFAULT '',
  `description` TEXT,
  `modified`    TIMESTAMP        NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  CHECKSUM =1
  AUTO_INCREMENT =1;

-- --------------------------------------------------------

--
-- Table structure for table `user_images_album_map`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_images_album_map` (
  `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `owner`    INT(10) UNSIGNED NOT NULL,
  `album_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `image_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `modified` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `map` (`owner`, `album_id`, `image_id`),
  KEY `FK_user_images_album_map_1` (`image_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHECKSUM =1
  AUTO_INCREMENT =1;

--
-- RELATIONS FOR TABLE `user_images_album_map`:
--   `image_id`
--       `user_images` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_link`
--
-- Creation: Feb 09, 2014 at 02:44 PM
--

CREATE TABLE IF NOT EXISTS `user_link` (
  `linkid`  MEDIUMINT(8) UNSIGNED   NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED        NOT NULL,
  `title`   VARCHAR(50)
            COLLATE utf8_unicode_ci NOT NULL,
  `uri`     VARCHAR(255)
            COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`linkid`),
  UNIQUE KEY `id` (`user_id`, `title`, `uri`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci
  COMMENT ='user''s link list'
  AUTO_INCREMENT =68;

-- --------------------------------------------------------

--
-- Table structure for table `user_location`
--
-- Creation: Feb 14, 2014 at 01:44 AM
--

CREATE TABLE IF NOT EXISTS `user_location` (
  `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `city`     VARCHAR(35)      NOT NULL DEFAULT '',
  `state`    INT(11) UNSIGNED DEFAULT NULL,
  `country`  INT(11) UNSIGNED DEFAULT NULL,
  `zip`      VARCHAR(10) DEFAULT NULL,
  `modified` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2195;

-- --------------------------------------------------------

--
-- Table structure for table `user_pic`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_pic` (
  `id`            INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `date_modified` DATETIME DEFAULT NULL,
  `mimetype`      VARCHAR(75)
                  CHARACTER SET utf8
                  COLLATE utf8_bin NOT NULL,
  `image`         BLOB             NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_pref`
--
-- Creation: Feb 09, 2014 at 02:42 PM
--

CREATE TABLE IF NOT EXISTS `user_pref` (
  `id`              INT(11) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `user_id`         INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `allow_spider`    ENUM('N', 'Y')      NOT NULL DEFAULT 'N',
  `style`           TINYINT(6) UNSIGNED NOT NULL DEFAULT '1',
  `owner_view_only` ENUM('Y', 'N')      NOT NULL DEFAULT 'N',
  `show_avatar`     ENUM('Y', 'N')      NOT NULL DEFAULT 'N',
  `journal_name`    VARCHAR(150)
                    CHARACTER SET utf8 DEFAULT NULL,
  `ping_services`   ENUM('Y', 'N')      NOT NULL DEFAULT 'N',
  `modified`        TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1
  AUTO_INCREMENT =2192;

-- --------------------------------------------------------

--
-- Table structure for table `user_style`
--
-- Creation: Feb 09, 2013 at 05:05 PM
--

CREATE TABLE IF NOT EXISTS `user_style` (
  `id`       INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `url`      TEXT,
  `doc`      TEXT,
  `modified` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_entry_new_unique`
--
CREATE TABLE IF NOT EXISTS `v_entry_new_unique` (
  `id` INT(10) UNSIGNED
);
-- --------------------------------------------------------

--
-- Structure for view `v_entry_new_unique`
--
DROP TABLE IF EXISTS `v_entry_new_unique`;

CREATE ALGORITHM = UNDEFINED
  DEFINER =`laffer1`@`70.91.226.204`
  SQL SECURITY DEFINER VIEW `v_entry_new_unique` AS
  SELECT
    max(`entry`.`id`) AS `id`
  FROM `entry`
  WHERE (`entry`.`security` = 2)
  GROUP BY `entry`.`uid`;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
ADD CONSTRAINT `fk_entry_id` FOREIGN KEY (`eid`) REFERENCES `entry` (`id`),
ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);

--
-- Constraints for table `entry`
--
ALTER TABLE `entry`
-- ADD CONSTRAINT `fk_entry_entry_security` foreign key (security) references entry_security(id),
ADD CONSTRAINT `fk_entry_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE mood_theme_data add constraint FK_mood_theme_data_mood foreign key (moodid) references mood (id);

-- ALTER TABLE mood_themes
-- add constraint FK_mood_themes_user foreign key (owner) references user (id);

-- ALTER TABLE rss_subscriptions
-- add constraint FK_rss_subscriptions_user foreign key (id) references user (id);

ALTER TABLE state
add constraint FK_state_country foreign key (country_id) references country (id);

 ALTER TABLE user_location
 add constraint FK_user_location_state foreign key (state) references state (id),
 add constraint FK_user_location_country foreign key (country) references country (id);

--
-- Constraints for table `user_images_album_map`
--
ALTER TABLE `user_images_album_map`
ADD CONSTRAINT `FK_user_images_album_map_1` FOREIGN KEY (`image_id`) REFERENCES `user_images` (`id`);
