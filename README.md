# Desafio PicPay Java

Sistema de pagamentos instantâneos similar ao PicPay, desenvolvido em Java com Spring Boot utilizando arquitetura limpa (hexagonal). O sistema gerencia contas, transações e integrações com observabilidade.

## Conteudo:

### Back-end: Java Spring Boot
- Arquitetura Limpa (Hexagonal)
- API RESTful
- Validações (Jakarta Validation, Jmail)
- Persistência com JPA/Hibernate (PostgreSQL, separação escrita/leitura)
- Observabilidade/Integração com Micrometer, Prometheus e Grafana
- Cache e armazenamento temporário com Redis (cluster: master, slave, sentinel)
- Testes unitários e de integração (JUnit, Testcontainers com PostgreSQL)
- Mapeamentos RESTful, CICD com GitHub

### Ferramentas e Tecnologias
- Maven - Gerenciamento de dependências e build
- Java 17 - Linguagem de programação
- Spring Boot 4.0.1 - Framework principal
- Spring Security + Oauth2 + JWT - Segurança e autenticação
- PostgreSQL - Banco de dados
- Flyway - Migrações de banco de dados
- Redis - Cache e armazenamento temporário (cluster)
- Nginx - Balanceamento de carga
- Swagger - Documentação de código
- Micrometer - Observabilidade e integração
- Prometheus - Coleta de métricas
- Grafana - Visualização de dashboards
- OpenFeign - Cliente Http para requisições de API externas
- Lombok - Redução de boilerplate code
- JMail - Validação de emails
- Jakarta Validation - Validações de entrada
- GitHub - Controle de versão e CICD

### Funcionalidades Implementadas
- [x] Criação de contas (pessoal/empresarial) com validações
- [x] Validação de CPF/CNPJ
- [x] Validação de email
- [x] Validação de senha (requisitos de complexidade)
- [x] Autenticação e autorização (JWT)
- [x] Persistência de contas no banco de dados
- [x] Transferências entre contas
- [x] Autorizador de transferencias via API externa
- [x] Filas para processamento assíncrono (Kafka)
- [x] Notificações via POST (com retry em fila se falhar)
- [x] Cache com Redis (salvamento de consultas)
- [x] Documentação com Swagger
- [x] Testes de integração
- [x] Paginação de resultados
- [x] Lançamento e tratamento de erros detalhados

### Funcionalidades Planejadas
- [ ] Testes unitários
- [ ] Logs de eventos
- [ ] Links com HATEOAS
- [ ] Containerização com Docker
- [ ] Balanceamento de carga com Nginx (ativo/reserva)
- [ ] Integração com front-end (Angular/React)

---
### Fontes e Referências:
- Desafio PicPay Java
- Spring Boot Documentation
- Clean Architecture principles
- Arquitetura baseada em diagramas de sistema distribuído
