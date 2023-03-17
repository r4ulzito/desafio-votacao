delete from tb_associates;
ALTER TABLE tb_associates ALTER COLUMN ID RESTART WITH 1;
delete from tb_votes;
ALTER TABLE tb_votes ALTER COLUMN ID RESTART WITH 1;
delete from tb_topics;
ALTER TABLE tb_topics ALTER COLUMN ID RESTART WITH 1;
delete from tb_sessions;
ALTER TABLE tb_sessions ALTER COLUMN ID RESTART WITH 1;