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
- Spring Boot 4.0.1 - Framework principal
- Java 17 - Linguagem de programação
- PostgreSQL - Banco de dados
- Redis - Cache e armazenamento temporário (cluster)
- Nginx - Balanceamento de carga
- Micrometer - Observabilidade e integração
- Prometheus - Coleta de métricas
- Grafana - Visualização de dashboards
- Lombok - Redução de boilerplate code
- JMail - Validação de emails
- Jakarta Validation - Validações de entrada
- Testcontainers - Testes com containers
- GitHub - Controle de versão e CICD

### Funcionalidades Implementadas
- [x] Criação de contas (pessoal/empresarial) com validações
- [x] Validação de CPF/CNPJ
- [x] Validação de email
- [x] Validação de senha (requisitos de complexidade)
- [x] Persistência de contas no banco de dados
- [x] Testes de integração com PostgreSQL via Testcontainers
- [x] Autenticação e autorização (JWT)

### Funcionalidades Planejadas
- [ ] Transferências entre contas (POST /transacoes, JSON com valor e payee)
- [ ] Pagamentos e recebimentos
- [ ] Notificações via POST (com retry em fila se falhar)
- [ ] Filas para processamento assíncrono (RabbitMQ)
- [ ] Cache com Redis (salvamento de transações)
- [ ] Containerização com Docker
- [ ] Balanceamento de carga com Nginx (ativo/reserva)
- [ ] Integração com front-end (Angular/React)
- [ ] Aplicativo mobile (React Native)
- [ ] Conversões de dinheiro (decimomilésimo para Real e vice-versa)
- [ ] Override para contas empresariais poderem fazer transações

---
### Fontes e Referências:
- Desafio PicPay Java
- Spring Boot Documentation
- Clean Architecture principles
- Arquitetura baseada em diagramas de sistema distribuído
