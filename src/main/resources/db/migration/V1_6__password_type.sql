alter table `user` add column `password_type` ENUM ('SHA1', 'SHA256') NOT NULL DEFAULT 'SHA256';
update `user` set `password_type` = 'SHA1';