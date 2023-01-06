CREATE TABLE if not exists member (
  member_id bigint NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  reserve int NOT NULL,
  PRIMARY KEY (`member_id`)
);

CREATE TABLE if not exists orders (
  id bigint NOT NULL AUTO_INCREMENT,
  itemname varchar(255) DEFAULT NULL,
  itemprice int NOT NULL,
  member_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKpuyun1nwd8fupsib8ekn7vrpm (member_id),
  CONSTRAINT FKpuyun1nwd8fupsib8ekn7vrpm FOREIGN KEY (member_id) REFERENCES member (member_id)
);