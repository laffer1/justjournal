alter table user_pic
  add column `source` ENUM ('UPLOAD', 'GRAVATAR') NOT NULL DEFAULT 'UPLOAD';


alter table user_pic
  add column `filename` varchar(100);