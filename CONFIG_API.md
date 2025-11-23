# 🔧 Configuração da URL da API

## Como alterar a URL da API

A aplicação agora utiliza um arquivo de configuração para definir a URL da API. Isso facilita a mudança entre diferentes ambientes (localhost, rede local, produção, etc.).

### Arquivo de Configuração

O arquivo de configuração está localizado em:
```
src/main/resources/application.properties
```

### Configurações Disponíveis

```properties
# URL base da API (inclua o /api/ no final)
api.base.url=http://192.168.0.136:5000/api/

# Timeout de conexão em segundos
api.timeout=30
```

### Exemplos de Configuração

#### 1. **Localhost (desenvolvimento local)**
```properties
api.base.url=http://localhost:5165/api/
```

#### 2. **Rede Local (IP específico)**
```properties
api.base.url=http://192.168.0.136:5000/api/
```

#### 3. **Servidor de Produção**
```properties
api.base.url=https://api.suptec.com.br/api/
```

### Como Aplicar as Mudanças

1. Abra o arquivo `src/main/resources/application.properties`
2. Altere o valor de `api.base.url` para a URL desejada
3. **IMPORTANTE**: Certifique-se de incluir `/api/` no final da URL
4. Recompile e execute a aplicação:
   ```bash
   mvn clean package
   java -jar target/suptec-desktop-1.0-SNAPSHOT.jar
   ```

### Verificação

Ao iniciar a aplicação, você verá no console a URL que está sendo utilizada:
```
🔧 ApiService inicializado com URL: http://192.168.0.136:5000/api/
Configurações carregadas com sucesso!
API URL: http://192.168.0.136:5000/api/
```

### Troubleshooting

- **Erro ao carregar configurações**: Se o arquivo não for encontrado, a aplicação usará `http://localhost:5165/api/` como padrão
- **URL incorreta**: Verifique se você incluiu o `/api/` no final da URL
- **Timeout**: Se a API está demorando muito para responder, aumente o valor de `api.timeout`

### Nota Importante

⚠️ **Não esqueça** de incluir a barra e o "api" no final da URL: `/api/`

❌ Errado: `http://192.168.0.136:5000`  
✅ Correto: `http://192.168.0.136:5000/api/`
