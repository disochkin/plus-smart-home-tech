

-- Создаем БД
CREATE DATABASE analyzer;
CREATE DATABASE shopping_store;
CREATE DATABASE shopping_cart;
CREATE DATABASE warehouse;

-- Даем права
GRANT ALL PRIVILEGES ON DATABASE analyzer TO postgres;
GRANT ALL PRIVILEGES ON DATABASE shopping_store TO postgres;
GRANT ALL PRIVILEGES ON DATABASE shopping_cart TO postgres;
GRANT ALL PRIVILEGES ON DATABASE warehouse TO postgres;
