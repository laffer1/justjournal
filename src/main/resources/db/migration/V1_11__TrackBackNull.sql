alter table trackback modify column `author_name`  VARCHAR(50) NULL;
alter table trackback modify column `author_email` VARCHAR(150) NULL;
alter table trackback modify column `blogname`     VARCHAR(150) NULL;
alter table trackback modify column `type`         ENUM ('trackback', 'pingback', 'postit', 'webmention') NOT NULL DEFAULT 'trackback';