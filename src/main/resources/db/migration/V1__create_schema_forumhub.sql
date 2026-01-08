-- Tabela: curso
CREATE TABLE cursos
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    categoria VARCHAR(255) NOT NULL
);

-- Tabela: perfil
CREATE TABLE perfis
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Tabela: usuario
CREATE TABLE usuarios
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    perfil_id BIGINT NOT NULL,
    CONSTRAINT fk_usuarios_perfil FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);

-- Tabela: topico
CREATE TABLE topicos
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo       VARCHAR(255) NOT NULL,
    mensagem     TEXT         NOT NULL,
    data_criacao DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(50)  NOT NULL,
    autor_id     BIGINT       NOT NULL,
    curso_id     BIGINT       NOT NULL,

    CONSTRAINT fk_topico_autor
        FOREIGN KEY (autor_id)
            REFERENCES usuarios (id),

    CONSTRAINT fk_topico_curso
        FOREIGN KEY (curso_id)
            REFERENCES cursos (id)
);

-- Tabela: resposta
CREATE TABLE respostas
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem     TEXT     NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    solucao      BOOLEAN  NOT NULL DEFAULT FALSE,
    autor_id     BIGINT   NOT NULL,
    topico_id    BIGINT   NOT NULL,

    CONSTRAINT fk_resposta_autor
        FOREIGN KEY (autor_id)
            REFERENCES usuarios (id),

    CONSTRAINT fk_resposta_topico
        FOREIGN KEY (topico_id)
            REFERENCES topicos (id)
            ON DELETE CASCADE
);
