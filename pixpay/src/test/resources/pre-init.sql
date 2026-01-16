-- O comando abaixo tenta criar o banco se ele não existir
SELECT 'CREATE DATABASE pixpay_test' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pixpay_test')\gexec