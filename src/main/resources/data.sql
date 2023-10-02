INSERT INTO "user" (username, password, nickname, activated) VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);
INSERT INTO "user" (username, password, nickname, activated) VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 1);

INSERT INTO AUTHORITY authority_name) VALUES ('ROLE_USER');
INSERT INTO AUTHORITY (authority_name) VALUES ('ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO USER_AUTHORITY (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO USER_AUTHORITY (user_id, authority_name) VALUES (2, 'ROLE_USER');