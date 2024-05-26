create table cidades(
id_cidade int (2) unsigned not null,
nome_cidade char(40),
populacao int (4),
primary key(id_cidade));

create table casos(
id_casos int (2) unsigned not null auto_increment,
id_cidade int (2),
infectados int (4),
obitos int (4),
curados int (4),
dt_caso date,
PRIMARY KEY(id_casos));

INSERT INTO cidades (id_cidade, nome_cidade, populacao) 
VALUES 
(1, "São Paulo", 11451999),
(2, "Osasco", 728615),
(3, "Guarulhos", 1291771),
(4, "Mauá", 418261),
(5, "Suzano", 307429);
#dados da população obetidos ibge(Instituto Brasileiro de Geografia e Estatística) de 2022 #



