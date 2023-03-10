--insert into user (ID, EMAIL, PASSWORD, PHONE, REG_DATE, UPDATE_DATE, USER_NAME, STATUS, LOCK_YN)
--values (1, 'test1@naver.com', '1111', '010-1111-1111', '2021-02-01 00:11:11.000000', null, 'user1',
--        1, 0);
insert into user (ID, EMAIL, PASSWORD, PHONE, REG_DATE, UPDATE_DATE, USER_NAME, STATUS, LOCK_YN)
values (1, 'test@gmail.com', '$2a$10$eQG4gQ5tqh2XNWpHjp1Fo.EUMQbwQ5IKxele8eNQNxmX9Ehsk56CW',
        '010-3884-1602', '2023-01-03 00:11:11.000000', null, 'user1',
        1, 0);
insert into user (ID, EMAIL, PASSWORD, PHONE, REG_DATE, UPDATE_DATE, USER_NAME, STATUS, LOCK_YN)
values (2, 'test2@naver.com', '2222', '010-2222-2222', '2023-01-03 00:11:11.000000', null, 'user2',
        1, 0);
insert into user (ID, EMAIL, PASSWORD, PHONE, REG_DATE, UPDATE_DATE, USER_NAME, STATUS, LOCK_YN)
values (3, 'test3@naver.com', '3333', '010-3333-2222', '2021-02-01 00:11:11.000000', null, 'user3',
        2, 0);

insert into notice (ID, CONTENTS, HITS, LIKES, REG_DATE, TITLE, DELETED, USER_ID)
values (1, '내용1', 0, 0, '2021-02-01 00:11:11.000000', '제목1', 0, 1);
insert into notice (ID, CONTENTS, HITS, LIKES, REG_DATE, TITLE, DELETED, USER_ID)
values (2, '내용2', 0, 0, '2023-01-03 00:11:11.000000', '제목2', 0, 1);
insert into notice (ID, CONTENTS, HITS, LIKES, REG_DATE, TITLE, DELETED, USER_ID)
values (3, '내용3', 0, 0, '2021-02-01 00:11:11.000000', '제목3', 0, 1);

INSERT INTO BOARD_TYPE (ID, BOARD_NAME, REG_DATE, USING_YN)
VALUES (1, '게시판1', '2021-02-01 00:11:11.000000', 1)
     , (2, '게시판2', '2021-02-01 00:11:11.000000', 1);

INSERT INTO BOARD (ID, BOARD_TYPE_ID, USER_ID, TITLE, CONTENTS, REG_DATE,
                   TOP_YN)
VALUES (1, 1, 1, '게시글1', '게시글내용1', '2021-02-01 00:11:11.000000', 0)
     , (2, 1, 1, '게시글2', '게시글내용2', '2021-02-01 00:11:11.000000', 0);

INSERT INTO BOARD_COMMENT(ID, COMMENTS, REG_DATE, BOARD_ID, USER_ID)
VALUES (1, '게시글1번에 대한 댓글1', '2021-02-01 00:11:11.000000', 1, 1)
     , (2, '게시글1번에 대한 댓글2', '2021-02-01 00:11:11.000000', 1, 1)
     , (3, '게시글2번에 대한 댓글1', '2021-02-01 00:11:11.000000', 2, 1)
     , (4, '게시글2번에 대한 댓글1', '2021-02-01 00:11:11.000000', 1, 2)
;
