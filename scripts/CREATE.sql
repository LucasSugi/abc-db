CREATE TABLE Endereco (
	id_endereco				INT NOT NULL,
	rua						VARCHAR(60) NULL,
	cidade					VARCHAR(30) NOT NULL,
	estado 					VARCHAR(30) NOT NULL,
	pais					VARCHAR(30) NOT NULL,
	CONSTRAINT pk_endereco PRIMARY KEY (id_endereco)
);

CREATE TABLE Pessoa (
	id_pessoa				INT NOT NULL,
	nome					VARCHAR(200) NOT NULL,
	senha					VARCHAR(200) NOT NULL,
	email 					VARCHAR(200) NOT NULL,
	id_endereco 			INT NOT NULL,
	CONSTRAINT pk_pessoa PRIMARY KEY (id_pessoa),
    CONSTRAINT fk_pessoa_endereco FOREIGN KEY (id_endereco) REFERENCES Endereco(id_endereco) ON DELETE CASCADE
);

CREATE TABLE Telefone (
	id_pessoa 				INT NOT NULL,
	numero 					VARCHAR(30) NOT NULL,
	tipo					INT NOT NULL,
	CONSTRAINT pk_telefone PRIMARY KEY (id_pessoa, numero, tipo),
	CONSTRAINT fk_pessoa_telefone FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE Empregado (
	id_pessoa 				INT NOT NULL,
	documento				VARCHAR(20) NOT NULL,
	funcao	 				VARCHAR(50) NOT NULL,
	data_contratacao 		DATE NOT NULL,
	login  					VARCHAR(256) NOT NULL,
	estado_civil 			NCHAR(1) NOT NULL,
	sexo		 			NCHAR(1) NOT NULL,
	data_nascimento 		DATE NOT NULL,
	CONSTRAINT pk_empregado PRIMARY KEY (documento),
	CONSTRAINT fk_pessoa_empregado FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE Departamento (
	id_departamento 		INT NOT NULL,
	nome 					VARCHAR(200) NOT NULL,
	grupo 					VARCHAR(200) NOT NULL,
	CONSTRAINT pk_departamento PRIMARY KEY (id_departamento),
	CONSTRAINT check_nome_grupo
 		CHECK (grupo IN ('Executive General and Administration', 'Inventory Management', 'Manufacturing', 'Quality Assurance', 'Research and Development', 'Sales and Marketing'))
);

CREATE TABLE HistoricoDepartamento (
	id_pessoa				INT NOT NULL,
	id_departamento 		INT NOT NULL,
	turno					VARCHAR(10),
	data_entrada			DATE NOT NULL,
	data_saida				DATE,
	CONSTRAINT pk_historicodepartamento PRIMARY KEY (id_pessoa, id_departamento, data_entrada),
	CONSTRAINT fk_pessoa_histdep FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa),
	CONSTRAINT fk_dep_histdep FOREIGN KEY (id_departamento) REFERENCES Departamento(id_departamento),
	CONSTRAINT check_turno_trabalho
 		CHECK (turno IN ('dia','tarde','noite'))
);

CREATE TABLE Vendedor (
	id_pessoa				INT NOT NULL,
	bonus 					NUMBER(15,2) NOT NULL,
	comissao 				NUMBER(15,2) NOT NULL,
	vendas_ano 				NUMBER(15,2) NOT NULL,
	vendas_ano_anterior 	NUMBER(15,2) NOT NULL,
	cota_vendas				NUMBER(15,2) NOT NULL,
	CONSTRAINT pk_vendedor PRIMARY KEY (id_pessoa),
	CONSTRAINT fk_pessoa_vendedor FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE Cliente (
	id_cliente 				INT NOT NULL,
	id_pessoa				INT NOT NULL,
	cartao_tipo				VARCHAR(50) NOT NULL,
	cartao_numero			VARCHAR(50) NOT NULL,
	cartao_validade_mes		SMALLINT,
	cartao_validade_ano		SMALLINT,
	CONSTRAINT pk_cliente PRIMARY KEY (id_cliente),
	CONSTRAINT fk_pessoa_cliente FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE Categoria (
	id_categoria			INT NOT NULL,
	nome					VARCHAR(20) NOT NULL,
	CONSTRAINT pk_categoria PRIMARY KEY (id_categoria)
);

CREATE TABLE Subcategoria (
	id_subcategoria			INT NOT NULL,
    id_categoria			INT NOT NULL,
	nome					VARCHAR(20) NOT NULL,
	CONSTRAINT pk_subcategoria PRIMARY KEY (id_subcategoria),
    CONSTRAINT pk_subcat_cat FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria) ON DELETE CASCADE
);

CREATE TABLE Produto (
	id_produto				INT NOT NULL,
	nome					VARCHAR(250) NOT NULL,
	numero_produto			VARCHAR(250) NOT NULL,
	preco 					NUMERIC NOT NULL,
	id_subcategoria			INT NOT NULL,
	cor 					VARCHAR(25),
	tamanho 				VARCHAR(10),
	peso 					NUMERIC(8,2),
	descricao				VARCHAR(2500),
	quantidade				SMALLINT,
	modelo					VARCHAR(50),
	CONSTRAINT pk_produto PRIMARY KEY (id_produto),
	CONSTRAINT fk_produto_subcat FOREIGN KEY (id_subcategoria) REFERENCES Subcategoria(id_subcategoria) ON DELETE CASCADE
);

CREATE TABLE Venda (
	id_venda				INT NOT NULL,
	id_cliente				INT NOT NULL,
	id_vendedor				INT,
	data_venda				DATE NOT NULL,
	data_vencimento			DATE NOT NULL,
	status 					SMALLINT NOT NULL,
	subtotal				NUMERIC NOT NULL,
	codigo_aprovacao_cartao	VARCHAR(15),
	data_envio				DATE NOT NULL,
	valor_frete				NUMERIC NOT NULL,
	total					NUMERIC NOT NULL,
	CONSTRAINT pk_venda PRIMARY KEY (id_venda),
	CONSTRAINT fk_cliente_venda FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE,
	CONSTRAINT fk_vendedor_venda FOREIGN KEY (id_vendedor) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
);

CREATE TABLE ItemVenda (
	id_item					INT NOT NULL,
	id_venda				INT NOT NULL,
	id_produto				INT NOT NULL,
	codigo_rastreio			VARCHAR(50),
	quantidade_estoque		SMALLINT,
	preco_unitario			NUMERIC,
	desconto				NUMERIC,
	total_item				NUMERIC,
	CONSTRAINT pk_itemvenda PRIMARY KEY (id_item),
	CONSTRAINT fk_venda_itemvenda FOREIGN KEY (id_venda) REFERENCES Venda(id_venda) ON DELETE CASCADE,
	CONSTRAINT fk_produto_itemvenda FOREIGN KEY (id_produto) REFERENCES Produto(id_produto) ON DELETE CASCADE
);

CREATE TABLE SYS_LOG (
    ID_LOG NUMBER(38,0) PRIMARY KEY,
    ID_PESSOA NUMBER(38,0),
    HORARIO varchar(255)
);

--Create VIEW's log
CREATE MATERIALIZED VIEW LOG ON PRODUTO WITH PRIMARY KEY;
CREATE MATERIALIZED VIEW LOG ON SUBCATEGORIA WITH PRIMARY KEY;
CREATE MATERIALIZED VIEW LOG ON VENDA WITH PRIMARY KEY;

COMMIT;
