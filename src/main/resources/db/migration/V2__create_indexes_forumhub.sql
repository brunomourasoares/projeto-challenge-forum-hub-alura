-- ÍNDICES PARA TABELA: usuarios
CREATE INDEX idx_usuario_email
    ON usuarios (email);

-- ÍNDICES PARA TABELA: topicos
-- Busca frequente por status
CREATE INDEX idx_topico_status
    ON topicos (status);
-- Listagem de tópicos por curso
CREATE INDEX idx_topico_curso
    ON topicos (curso_id);
-- Listagem de tópicos por autor
CREATE INDEX idx_topico_autor
    ON topicos (autor_id);
-- Ordenação e filtros por data
CREATE INDEX idx_topico_data_criacao
    ON topicos (data_criacao);

-- ÍNDICES PARA TABELA: respostas
-- Listar respostas de um tópico
CREATE INDEX idx_resposta_topico
    ON respostas (topico_id);
-- Listar respostas por autor
CREATE INDEX idx_resposta_autor
    ON respostas (autor_id);
-- Buscar solução rapidamente
CREATE INDEX idx_resposta_solucao
    ON respostas (solucao);
