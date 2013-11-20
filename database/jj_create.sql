-- 
-- Database: `jj`
-- justjournal.com  DATABASEJJ 
CREATE DATABASE `jj` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE jj;

-- --------------------------------------------------------

-- 
-- Table structure for table `comments`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `comments` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `eid` int(10) unsigned NOT NULL default '0',
  `uid` int(10) unsigned NOT NULL default '0',
  `subject` varchar(150) default NULL,
  `date` datetime NOT NULL default '0000-00-00 00:00:00',
  `body` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;


-- --------------------------------------------------------

-- 
-- Table structure for table `content`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `content` (
  `id` int(10) unsigned NOT NULL default '0',
  `uri_id` int(10) unsigned NOT NULL default '0',
  `userid` int(10) unsigned NOT NULL default '0',
  `mime_type` varchar(75) NOT NULL default '',
  `preferred` enum('0','1') NOT NULL default '0',
  `datasize` int(11) NOT NULL default '0',
  `data` mediumblob NOT NULL,
  `metadata` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `country`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `country` (
  `id` tinyint(3) unsigned NOT NULL auto_increment,
  `title` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

-- 
-- Dumping data for table `country`
-- 

INSERT DELAYED IGNORE INTO `country` (`id`, `title`) VALUES (1, 'United States'),
(2, 'Canada'),
(3, 'United Kingdom'),
(4, 'Germany'),
(5, 'France'),
(6, 'Sweden'),
(7, 'Belgium'),
(8, 'Norway'),
(9, 'Finland'),
(10, 'Mexico'),
(11, 'Russia'),
(12, 'China'),
(13, 'Slovenia'),
(15, 'Japan');

-- --------------------------------------------------------

-- 
-- Table structure for table `entry`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `entry` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `uid` int(10) unsigned NOT NULL default '0',
  `date` datetime NOT NULL default '0000-00-00 00:00:00',
  `subject` varchar(150) default NULL,
  `mood` tinyint(3) unsigned NOT NULL default '0',
  `music` varchar(125) NOT NULL default '',
  `location` tinyint(3) unsigned NOT NULL default '0',
  `body` text NOT NULL,
  `security` tinyint(4) unsigned NOT NULL default '0',
  `autoformat` enum('Y','N') NOT NULL default 'Y',
  `allow_comments` enum('Y','N') NOT NULL default 'Y',
  `email_comments` enum('Y','N') NOT NULL default 'Y',
  PRIMARY KEY  (`id`),
  KEY `sec` (`security`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=573 ;

-- --------------------------------------------------------

-- 
-- Table structure for table `entry_security`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `entry_security` (
  `id` tinyint(4) NOT NULL default '0',
  `title` char(8) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `entry_security`
-- 

INSERT DELAYED IGNORE INTO `entry_security` (`id`, `title`) VALUES (0, 'private'),
(1, 'friends'),
(2, 'public');

-- --------------------------------------------------------

-- 
-- Table structure for table `friends`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `friends` (
  `id` int(10) unsigned NOT NULL default '0',
  `friendid` int(10) unsigned NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `friends_lj`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `friends_lj` (
  `id` int(10) unsigned NOT NULL default '0',
  `username` char(15) NOT NULL default '',
  `community` enum('Y','N') NOT NULL default 'N'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `friends_lj_cache`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `friends_lj_cache` (
  `ljusername` varchar(15) NOT NULL default '',
  `community` enum('Y','N') NOT NULL default 'N',
  `lastupdated` datetime NOT NULL default '0000-00-00 00:00:00',
  `content` text,
  PRIMARY KEY  (`ljusername`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `friends_lj_cache`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `location`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `location` (
  `id` tinyint(3) unsigned NOT NULL default '0',
  `title` varchar(15) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `location`
-- 

INSERT DELAYED IGNORE INTO `location` (`id`, `title`) VALUES (0, 'Not Specified'),
(1, 'Home'),
(2, 'Work'),
(3, 'School'),
(5, 'Other');

-- --------------------------------------------------------

-- 
-- Table structure for table `mood`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `mood` (
  `id` tinyint(3) unsigned NOT NULL auto_increment,
  `parentmood` tinyint(3) unsigned NOT NULL default '0',
  `title` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=126 ;

-- 
-- Dumping data for table `mood`
-- 

INSERT DELAYED IGNORE INTO `mood` (`id`, `parentmood`, `title`) VALUES (1, 0, 'Happy'),
(2, 0, 'Sad'),
(3, 0, 'Awake'),
(4, 0, 'Tired'),
(5, 0, 'Angry'),
(6, 5, 'Mad'),
(7, 1, 'Gay'),
(8, 0, 'Silly'),
(9, 0, 'Confused'),
(10, 0, 'Bewildered'),
(11, 0, 'Smart'),
(12, 0, 'Not Specified'),
(13, 0, 'Hungry'),
(14, 0, 'Accomplished'),
(15, 1, 'Amused'),
(16, 0, 'Annoyed'),
(17, 0, 'Anxious'),
(18, 0, 'Bored'),
(19, 0, 'Accepted'),
(20, 0, 'Alone'),
(21, 0, 'Ashamed'),
(22, 0, 'Bittersweet'),
(23, 0, 'Blissful'),
(24, 0, 'Dark'),
(25, 5, 'Aggravated'),
(26, 5, 'Bitchy'),
(27, 5, 'Cranky'),
(28, 5, 'Cynical'),
(29, 5, 'Enraged'),
(30, 5, 'Frustrated'),
(31, 5, 'Grumpy'),
(32, 5, 'Infuriated'),
(33, 5, 'Irate'),
(34, 5, 'Irritated'),
(35, 5, 'Moody'),
(36, 5, 'Pissed off'),
(37, 5, 'Stressed'),
(38, 37, 'Rushed'),
(39, 9, 'Curious'),
(40, 0, 'Determined'),
(41, 40, 'Predatory'),
(42, 0, 'Devious'),
(43, 0, 'Energetic'),
(44, 43, 'Bouncy'),
(45, 43, 'Hyper'),
(46, 0, 'Enthralled'),
(47, 1, 'Cheerful'),
(48, 1, 'Horny'),
(49, 1, 'Chipper'),
(50, 1, 'High'),
(51, 1, 'Ecstatic'),
(52, 1, 'Excited'),
(53, 1, 'Good'),
(54, 1, 'Grateful'),
(55, 1, 'Impressed'),
(56, 1, 'Jubilant'),
(57, 1, 'Loved'),
(58, 1, 'Optimistic'),
(59, 58, 'Hopeful'),
(60, 1, 'Pleased'),
(61, 1, 'Refreshed'),
(62, 61, 'Rejuvenated'),
(63, 1, 'Relaxed'),
(64, 63, 'Calm'),
(65, 63, 'Mellow'),
(66, 63, 'Peaceful'),
(67, 63, 'Recumbent'),
(68, 63, 'Satisfied'),
(69, 68, 'Content'),
(70, 69, 'Complacent'),
(71, 69, 'Indifferent'),
(72, 68, 'Full'),
(73, 68, 'Relieved'),
(74, 1, 'Thankful'),
(75, 1, 'Touched'),
(76, 1, 'Surprised'),
(77, 76, 'Shocked'),
(78, 8, 'Crazy'),
(79, 8, 'Ditzy'),
(80, 8, 'Flirty'),
(81, 8, 'Giddy'),
(82, 8, 'Giggly'),
(83, 8, 'Mischievous'),
(84, 83, 'Naughty'),
(85, 8, 'Quixotic'),
(86, 8, 'Weird'),
(87, 0, 'Indescribable'),
(88, 0, 'Nerdy'),
(89, 88, 'Dorky'),
(90, 88, 'Geeky'),
(91, 0, 'Okay'),
(92, 91, 'Blah'),
(93, 91, 'Lazy'),
(94, 93, 'Lethargic'),
(95, 93, 'Listless'),
(96, 93, 'Exanimate'),
(97, 96, 'Apathetic'),
(98, 96, 'Blank'),
(99, 2, 'Crappy'),
(100, 2, 'Crushed'),
(101, 2, 'Depressed'),
(102, 2, 'Disappointed'),
(103, 2, 'Discontent'),
(104, 2, 'Envious'),
(105, 2, 'Gloomy'),
(106, 2, 'Pessimistic'),
(107, 2, 'Jealous'),
(108, 2, 'Lonely'),
(109, 2, 'Melancholy'),
(110, 2, 'Morose'),
(111, 2, 'Numb'),
(112, 2, 'Rejected'),
(113, 2, 'Sympathetic'),
(114, 2, 'Uncomfortable'),
(115, 2, 'Cold'),
(116, 2, 'Dirty'),
(117, 2, 'Drunk'),
(118, 2, 'Exhausted'),
(119, 2, 'Drained'),
(120, 2, 'Groggy'),
(121, 2, 'Sleepy'),
(122, 2, 'Guilty'),
(123, 2, 'Hot'),
(124, 2, 'Restless'),
(125, 2, 'Sick');

-- --------------------------------------------------------

-- 
-- Table structure for table `mood_theme_data`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `mood_theme_data` (
  `moodthemeid` int(10) unsigned NOT NULL default '0',
  `moodid` int(10) unsigned NOT NULL default '0',
  `picurl` varchar(100) default NULL,
  `width` tinyint(3) unsigned NOT NULL default '0',
  `height` tinyint(3) unsigned NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `mood_theme_data`
-- 

INSERT DELAYED IGNORE INTO `mood_theme_data` (`moodthemeid`, `moodid`, `picurl`, `width`, `height`) VALUES (1, 1, 'smile.gif', 15, 15),
(1, 2, 'sad.gif', 15, 15),
(1, 4, 'tired.gif', 15, 15),
(1, 5, 'angry.gif', 15, 15),
(1, 9, 'confused.gif', 15, 15),
(1, 42, 'devious.gif', 15, 15),
(1, 43, 'energetic.gif', 15, 15);

-- --------------------------------------------------------

-- 
-- Table structure for table `mood_themes`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `mood_themes` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `owner` int(10) unsigned NOT NULL default '0',
  `name` varchar(50) default NULL,
  `des` varchar(100) default NULL,
  `is_public` enum('Y','N') NOT NULL default 'Y',
  PRIMARY KEY  (`id`),
  KEY `owner` (`owner`,`is_public`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- 
-- Dumping data for table `mood_themes`
-- 

INSERT DELAYED IGNORE INTO `mood_themes` (`id`, `owner`, `name`, `des`, `is_public`) VALUES (1, 0, 'Default', NULL, 'Y');

-- --------------------------------------------------------

-- 
-- Table structure for table `resources`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `resources` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(200) NOT NULL default '',
  `active` enum('0','1') NOT NULL default '1',
  `security` tinyint(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

-- 
-- Table structure for table `rss_cache`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `rss_cache` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `interval` int(10) unsigned NOT NULL default '0',
  `lastupdated` datetime NOT NULL default '0000-00-00 00:00:00',
  `uri` tinytext NOT NULL,
  `content` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=32 ;


-- --------------------------------------------------------

-- 
-- Table structure for table `rss_subscriptions`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `rss_subscriptions` (
  `id` int(10) unsigned NOT NULL default '0',
  `uri` tinytext NOT NULL,
  UNIQUE KEY `UNIQUE` (`id`,`uri`(300))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

-- 
-- Table structure for table `style`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `style` (
  `id` int(10) unsigned NOT NULL default '0',
  `title` varchar(50) NOT NULL default '''''',
  `desc` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `user`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(15) NOT NULL default '',
  `password` varchar(40) NOT NULL default '',
  `type` tinyint(3) unsigned NOT NULL default '0',
  `name` varchar(20) NOT NULL default '',
  `since` smallint(4) unsigned NOT NULL default '2005',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=51 ;


-- --------------------------------------------------------

-- 
-- Table structure for table `user_bio`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_bio` (
  `id` int(10) unsigned NOT NULL default '0',
  `content` text NOT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `user_contact`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_contact` (
  `id` int(10) unsigned NOT NULL default '0',
  `email` varchar(100) NOT NULL default '',
  `icq` varchar(20) default NULL,
  `aim` varchar(30) default NULL,
  `yahoo` varchar(30) default NULL,
  `msn` varchar(150) default NULL,
  `hp_uri` varchar(250) default NULL,
  `hp_title` varchar(50) default NULL,
  `phone` varchar(14) default NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `user_location`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_location` (
  `id` int(10) unsigned NOT NULL default '0',
  `city` varchar(35) NOT NULL default '',
  `state` smallint(6) NOT NULL default '0',
  `country` smallint(6) NOT NULL default '0',
  `zip` varchar(10) default NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

-- 
-- Table structure for table `user_pic`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_pic` (
  `id` int(10) unsigned NOT NULL default '0',
  `image` blob NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `user_pref`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_pref` (
  `id` int(10) unsigned NOT NULL default '0',
  `allow_spider` enum('N','Y') NOT NULL default 'N',
  `style` tinyint(6) unsigned NOT NULL default '1',
  `owner_view_only` enum('Y','N') NOT NULL default 'N',
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `user_style`
-- 
-- Creation: Aug 30, 2005 at 09:49 AM
-- 

CREATE TABLE IF NOT EXISTS `user_style` (
  `id` int(10) unsigned NOT NULL default '0',
  `url` text,
  `doc` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

