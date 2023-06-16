DELETE FROM unverified_user;
DELETE FROM user;

INSERT INTO `kamstest`.`unverified_user`
(`activation_token`, `creation_time`, `email`, `password`, `role`)
VALUES
("klhglghljkhglhglhgbl", now(), "test1@test.com", "lkjblkjblkbjlkbhlbh", "USER");