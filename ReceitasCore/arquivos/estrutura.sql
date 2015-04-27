create table RECEITA_ID (
	ID integer identity,
	primary key (ID)
);

create table RECEITA (
	ID integer,
	DESCRICAO varchar(250),
	primary key (ID)
);

create table PASSOS (
	ID_RECEITA integer,
	SEQUENCIA integer,
	DESCRICAO varchar(500)
);

create table ESPECIARIA_ID (
	ID integer identity,
	primary key (ID)
);

create table ESPECIARIA (
	ID integer,
	DESCRIACAO varchar(200),
	primary key (ID)
);

create table INGREDIENTE (
	ID_RECEITA integer,
	ID_ESPECIARIA integer,
	UNIDADE varchar(20),
	QUANTIDADE decimal(18,6)
);

alter table PASSOS add constraint FK_PASSOS_RECEITA foreign key (ID_RECEITA) references RECEITA (ID);
alter table INGREDIENTE add constraint FK_INGREDIENTE_RECEITA foreign key (ID_RECEITA) references RECEITA (ID);
alter table INGREDIENTE add constraint FK_INGREDIENTE_ESPECIARIA foreign key (ID_ESPECIARIA) references ESPECIARIA (ID);

CREATE PROCEDURE PROXIMO_ID @A_TABELA CHAR(30), @A_PROX INTEGER OUTPUT AS
BEGIN
    DECLARE @SQL nVARCHAR (200)
    DECLARE @PARAMS nvarchar(500)
    DECLARE @TranCounter INT;
    SET @TranCounter = @@TRANCOUNT;
    IF @TranCounter > 0
    SAVE TRANSACTION PROC_ID
  ELSE
    BEGIN TRANSACTION
  SET @SQL = 'INSERT INTO ' + @A_TABELA + ' DEFAULT VALUES; select @A_PROXOUT = scope_identity();'
  SET @PARAMS = N'@A_PROXOUT INT OUTPUT'
    EXEC sp_executesql @SQL, @PARAMS, @A_PROXOUT = @A_PROX OUTPUT
    IF @TranCounter > 0
    ROLLBACK TRANSACTION PROC_ID
  ELSE
    ROLLBACK TRANSACTION;
END;

-- Testar a geracao de ID
begin
	declare @id integer
	exec PROXIMO_ID 'RECEITA_ID', @id out
	print @id
end;

SELECT * FROM RECEITA;