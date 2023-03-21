-- Cria topico
INSERT INTO tb_topics (title, description) VALUES ('Topic1', 'Description1');

-- Cria associado
INSERT INTO tb_associates (name) VALUES ('Associate1');

-- Cria Lista de Votos
INSERT INTO tb_votes (answer, associate_id, topic_id) VALUES
('YES', 1, 1),
('YES', 1, 1),
('NO', 1, 1);