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
- Spring Boot 4 - Framework principal
- Spring Security + JWT - Segurança e autenticação
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
- [x] Validação de CPF/CNPJ, email, senha
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
- [x] Links com HATEOAS
- [x] Observabilidade
- [x] Logs estruturados
- [x] Containerização com Docker
- [x] Balanceamento de carga com Nginx

### Funcionalidades Planejadas
- [ ] Migrações com Flyway
- [ ] Integração com front-end (Angular)

### Rodando o projeto
- dentro da pasta onde contem os arquivos Dockerfile e docker-compose.yml
    - rode no terminal
    ```javascript
    docker compose up -d
    ```
    - verificar containers
        - pixpay-backend-app-1
        - pixpay-backend-app-2
        - nginx
        - postgres
        - broker
        - redis-master
        - redis-slave
        - redis-sentinel-1
        - redis-sentinel-2
        - redis-sentinel-3
        - grafana-lgtm
    ```javascript
    docker ps
    ```
    - verificar terminal das aplicações 
    ```javascript
    docker logs pixpay-backend-app-1
    ```

### Rotas 
http://localhost/swagger-ui/index.html [desativado no perfil prod (Arquivo Docker Compose)]

### Observabilidade
http://localhost:3000/drilldown

---
### Fontes e Referências:
- Desafio PicPay Java
- Spring Boot Documentation
- Clean Architecture principles
- Arquitetura baseada em diagramas de sistema distribuído
