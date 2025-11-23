# 🔧 Configuração da Aplicação Suptec Desktop

## Histórico de Alterações

### ✅ 23/11/2025 - Correções Implementadas

#### 1. **Sistema de Configuração Centralizado**
- Criado arquivo `application.properties` para gerenciar configurações
- Criada classe `ConfigLoader` para carregar configurações automaticamente
- Migração de URL hardcoded para configuração dinâmica

#### 2. **Correção de Endpoints de Exclusão**
- **Problema**: Endpoints de exclusão usavam formatos diferentes
  - ❌ Gerente: `/Excluir/{id}` (correto)
  - ❌ Técnico: `/Excluir?id={id}` (errado)
  - ❌ Usuário: `/Excluir?id={id}` (errado)
- **Solução**: Padronizado todos os endpoints para usar o formato `/Excluir/{id}`
  - ✅ `Usuario/Excluir/{id}`
  - ✅ `Tecnico/Excluir/{id}`
  - ✅ `Gerente/Excluir/{id}`

#### 3. **URL Configurável**
- URL padrão: `http://localhost:5000/api/`
- Agora totalmente configurável via `application.properties`
- Suporta localhost, rede local e servidores remotos

---

## 📍 Como Alterar a URL da API

### Arquivo de Configuração

**Localização**: `src/main/resources/application.properties`

### Configurações Disponíveis

```properties
# URL base da API (inclua o /api/ no final)
api.base.url=http://localhost:5000/api/

# Timeout de conexão em segundos
api.timeout=30
```

### Exemplos de Configuração

#### 1. **Localhost (desenvolvimento local)**
```properties
api.base.url=http://localhost:5000/api/
```

#### 2. **Rede Local - Computador 1**
```properties
api.base.url=http://192.168.1.100:5000/api/
```

#### 3. **Rede Local - Computador 2**
```properties
api.base.url=http://192.168.1.200:5000/api/
```

#### 4. **Servidor de Produção**
```properties
api.base.url=https://api.suptec.com.br/api/
```

---

## 🚀 Como Aplicar as Mudanças

1. Abra o arquivo `src/main/resources/application.properties`
2. Altere o valor de `api.base.url` para a URL desejada
3. **IMPORTANTE**: Certifique-se de incluir `/api/` no final da URL
4. Salve o arquivo
5. Recompile e execute:
   ```bash
   mvn clean compile
   mvn javafx:run
   ```
   Ou simplesmente execute novamente (o arquivo é recarregado automaticamente)

---

## ✅ Verificação

Ao iniciar a aplicação, você verá no console:
```
🔧 ApiService inicializado com URL: http://localhost:5000/api/
Configurações carregadas com sucesso!
API URL: http://localhost:5000/api/
```

---

## 🔧 Endpoints da API

### Autenticação
- **Login**: `POST /AuthDesktop/LoginDesktop`

### Usuários
- **Listar**: `GET /Usuario/ListarUsuarios`
- **Criar**: `POST /Usuario/Cadastrar`
- **Editar**: `PUT /Usuario/Editar/{id}`
- **Excluir**: `DELETE /Usuario/Excluir/{id}` ✅

### Técnicos
- **Listar**: `GET /Tecnico/ListarTecnicos`
- **Criar**: `POST /Tecnico/Cadastrar`
- **Editar**: `PUT /Tecnico/Editar/{id}`
- **Excluir**: `DELETE /Tecnico/Excluir/{id}` ✅

### Gerentes
- **Listar**: `GET /Gerente/ListarGerentes`
- **Criar**: `POST /Gerente/Cadastrar`
- **Editar**: `PUT /Gerente/Editar/{id}`
- **Excluir**: `DELETE /Gerente/Excluir/{id}` ✅

### Chamados
- **Listar**: `GET /Chamado/ListarChamados`
- **Editar**: `PUT /Chamado/Editar/{id}`
- **Excluir**: `DELETE /Chamado/Excluir/{id}`

---

## 🐛 Troubleshooting

### Erro ao carregar configurações
Se o arquivo `application.properties` não for encontrado, a aplicação usará valores padrão:
```
api.base.url=http://localhost:5165/api/
api.timeout=30
```

### URL incorreta
Verifique se você incluiu o `/api/` no final da URL.

❌ Errado: `http://192.168.1.100:5000`  
✅ Correto: `http://192.168.1.100:5000/api/`

### Timeout de conexão
Se a API está demorando para responder, aumente o valor:
```properties
api.timeout=60
```

### Erro 401 - Não autorizado
- Token inválido ou expirado
- Solução: Faça login novamente

### Erro 403 - Acesso negado
- Verifique permissões do usuário
- Verifique se o endpoint está correto

### Erro 404 - Não encontrado
- Verifique se a URL da API está correta
- Verifique se o recurso existe

### Erro de exclusão não funciona
- ✅ **CORRIGIDO**: Agora todos os endpoints usam `/Excluir/{id}`
- Certifique-se de estar usando a versão compilada após 23/11/2025

---

## 📝 Notas Importantes

⚠️ **Sempre inclua `/api/` no final da URL**

⚠️ **Após alterar configurações, recompile o projeto**

⚠️ **O arquivo `application.properties` deve estar em `src/main/resources/`**

---

## 👨‍💻 Arquivos Modificados

- `src/main/resources/application.properties` - Configurações da aplicação
- `src/main/java/br/com/suptec/utils/ConfigLoader.java` - Carregador de configurações
- `src/main/java/br/com/suptec/services/ApiService.java` - Uso de ConfigLoader
- `src/main/java/br/com/suptec/services/UserManagementService.java` - Correção endpoints de exclusão
