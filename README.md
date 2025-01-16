# SIGResAPI

## ✅ TODO LIST

### Pendências na API
- [X] Autenticação
- [X] Produtos
- [X] Variações de produtos
- [X] Mesas
- [X] Usuários
- [X] Estoque
- [X] Pedidos
- [X] Vendas
- [ ] Relatórios
- [ ] Armazenar configurações do estabelecimento (Num mesas, nome, bate-papo ativo, ícone)
- [ ] Mensagens (Bate-papo)
- [ ] Organizar permissões (Rotas) de ADMINS e de USERS (Funcionários)

### Pendências Front Web
- [ ] Ajustar com base nas alterações da API
- [ ] Integrar a autenticação com a API
- [ ] Integrar totalmente com os dados da API

### Pendências Front Mobile
- [ ] Ajustar com base nas alterações da API
- [ ] Integrar a autenticação com a API
- [ ] Integrar totalmente com os dados da API

## 📦 Como Rodar a API
```bash
# Clone o repositório
git clone https://github.com/gili-julio/SIGResAPI.git

# Acesse a pasta do projeto
cd SIGResAPI

# Acesse a pasta resources
cd src/main/resources

# Crie a tabela "sigres" no postgresql,
# Altere os dados no arquivo example-application.properties
# E renomeie para application.properties
# Agora, é só executar o projeto
mvn spring-boot:run
```

## 📖 Tecnologias Utilizadas
- Java 21
- Spring Boot
- PostgreSQL
- Lombok
- Spring Security
- JWT
- Maven
