-- Cria topicos
INSERT INTO tb_topics (title, description) VALUES ('Topic1', 'Description1');
INSERT INTO tb_topics (title, description) VALUES ('Topic2', 'Description2');

-- Cria sessões
INSERT INTO tb_sessions (data_start, data_end, is_open, topic_id) VALUES
('2023-03-20T14:00:00', '2030-03-20T14:00:00', true, 1);
INSERT INTO tb_sessions (data_start, data_end, is_open, topic_id) VALUES
('2023-03-20T14:00:00', '2030-03-20T14:00:00', true, 2);
