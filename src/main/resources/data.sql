-- Popula o banco de dados em memória
INSERT INTO public.cidade
    (id, nome, estado)
VALUES
    (1, 'Campo Grande', 'Mato Grosso Do Sul'),
    (2, 'Recife', 'Pernambuco'),
    (3, 'Belo Horizonte', 'Minas Gerais'),
    (4, 'Belém', 'Pará'),
    (5, 'São Paulo', 'São Paulo');

INSERT INTO public.cliente
    (id, nome, sexo, nascimento, idade, id_cidade)
VALUES
    (1, 'Wender Galan', 0, '1997-06-16', 23, 1);
