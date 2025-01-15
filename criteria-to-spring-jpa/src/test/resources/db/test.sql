CREATE TABLE IF NOT EXISTS users
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(50) UNIQUE NOT NULL,
    surname  VARCHAR(50)        NOT NULL,
    enabled  BOOLEAN            NOT NULL,
    attempts INT                NOT NULL
);
DELETE
from users
WHERE true;
/* (id, name, description) */
insert into users
values ('81abaa90-2f91-11ee-be56-0242ac120002', 'Rafa', 'Ferrer', true, 100);
insert into users
values ('0402a2cc-2f93-11ee-be56-0242ac120002', 'Javi', 'Gómez', true, 50);
insert into users
values ('19bac22a-2f93-11ee-be56-0242ac120002', 'Guillermo', 'Mir ', false, 25);
insert into users
values ('5ad5d8a8-59cb-4207-97dc-d9ab17842f55', 'Paco', 'Castelló ', false, 50);
