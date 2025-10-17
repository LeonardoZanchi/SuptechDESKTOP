# SUPTEC Desktop - Sistema de Chamados Técnicos

Aplicativo desktop JavaFX para gerenciamento completo de chamados e usuários do sistema SUPTEC, com arquitetura modular e interface moderna.

## Visão Geral

O SUPTEC Desktop é uma aplicação desktop completa desenvolvida em JavaFX que oferece uma interface gráfica moderna e intuitiva para técnicos e administradores gerenciarem chamados técnicos. O sistema implementa autenticação segura via API REST, navegação modular por cards interativos, e está preparado para expansão com novos módulos funcionais.

## Funcionalidades Implementadas

### **Autenticação e Login**
- Tela de login profissional com logo e formulário centralizado
- Validação de credenciais via API REST (atualmente em modo MVP com validação local)
- Sistema de "lembrar usuário" com persistência local
- Feedback visual para erros de autenticação
- Transição suave para o menu principal após login

### **Menu Principal Modular**
- Interface organizada com cabeçalho, área central e rodapé
- **Cards interativos** para navegação:
  - **Usuários**: Gerenciamento de usuários do sistema
  - **Chamados**: Sistema de chamados técnicos
  - **Relatórios**: Visualização de dados e estatísticas
  - **Configurações**: Preferências e configurações do sistema
- Informações do usuário logado no cabeçalho
- Botão de logout com confirmação
- Design responsivo com efeitos hover e transições

### **Arquitetura Modular**
- **Componentes FXML reutilizáveis**: Header, Footer, MenuGrid, MenuCard
- **Controllers especializados**: Separação clara de responsabilidades
- **Serviços desacoplados**: ApiService, UsuarioService
- **Utilitários compartilhados**: AlertUtils, JsonUtils, SceneManager

### **Interface Moderna e Profissional**
- **Paleta de cores corporativa** azul profissional
- **Sistema de estilos CSS modular** com 11 arquivos organizados
- **Tipografia consistente** com hierarquia visual clara
- **Componentes customizados**: Botões, inputs, containers com efeitos visuais
- **Responsividade** e centralização automática
- **Ícones e logos** integrados na interface

## Tecnologias Utilizadas

- **Java 17** - Linguagem principal com recursos modernos
- **JavaFX 21** - Framework para interface gráfica rica
- **Maven** - Gerenciamento de dependências e build automatizado
- **Gson 2.10.1** - Serialização/Deserialização JSON
- **HttpClient (java.net.http)** - Cliente HTTP nativo do Java 11+
- **Preferences API** - Persistência local de configurações
- **Padrões de Projeto**: MVC (Model-View-Controller), Component Pattern

## Estrutura Completa do Projeto

```
suptec-desktop/
├── pom.xml                          # Configuração Maven com dependências
├── README.md                        # Esta documentação completa
├── src/
│   └── main/
│       ├── java/br/com/suptec/
│       │   ├── app/
│       │   │   └── MainApp.java     # Ponto de entrada da aplicação JavaFX
│       │   ├── controllers/
│       │   │   ├── LoginController.java      # Controller da tela de login
│       │   │   ├── MainMenuController.java   # Controller principal do menu
│       │   │   └── components/               # Controllers de componentes modulares
│       │   │       ├── HeaderController.java # Controller do cabeçalho
│       │   │       └── MenuGridController.java # Controller do grid de cards
│       │   ├── core/
│       │   │   └── SceneManager.java         # Gerenciador de cenas e navegação
│       │   ├── models/
│       │   │   └── Usuario.java              # Modelo de dados do usuário
│       │   ├── services/
│       │   │   ├── ApiService.java           # Cliente HTTP para API REST
│       │   │   └── UsuarioService.java       # Lógica de negócio do usuário
│       │   └── utils/
│       │       ├── AlertUtils.java           # Utilitários para alertas e diálogos
│       │       └── JsonUtils.java            # Utilitários para manipulação JSON
│       └── resources/
│           ├── css/                          # Sistema de estilos CSS modular
│           │   ├── main.css                  # Arquivo principal que importa todos
│           │   ├── colors.css                # Paleta de cores e variáveis
│           │   ├── typography.css            # Estilos de texto e tipografia
│           │   ├── buttons.css               # Estilos para botões
│           │   ├── inputs.css                # Estilos para campos de entrada
│           │   ├── containers.css            # Estilos para containers
│           │   ├── login.css                 # Estilos específicos do login
│           │   ├── menu.css                  # Estilos do menu (importa submódulos)
│           │   ├── menu-header.css           # Estilos do cabeçalho do menu
│           │   ├── menu-cards.css            # Estilos dos cards clicáveis
│           │   └── menu-layout.css           # Estilos de layout do menu
│           ├── fxml/                         # Interfaces FXML
│           │   ├── LoginView.fxml            # Tela de login completa
│           │   ├── MainMenuView.fxml         # Menu principal com componentes
│           │   └── components/               # Componentes FXML modulares
│           │       ├── Header.fxml           # Cabeçalho reutilizável
│           │       ├── Footer.fxml           # Rodapé informativo
│           │       ├── MenuGrid.fxml         # Grid de cards do menu
│           │       └── MenuCard.fxml         # Template de card individual
│           └── images/                       # Recursos visuais
│               ├── LogoPrincipal.jpg         # Logo principal da aplicação
│               └── LogoSuptechLogin.jpg      # Logo específica para login
└── target/                                   # Arquivos gerados pelo Maven
    ├── classes/                              # Classes Java compiladas
    ├── css/                                  # Recursos CSS copiados
    ├── fxml/                                 # Recursos FXML copiados
    ├── images/                               # Imagens copiadas
    ├── generated-sources/annotations/        # Código gerado
    ├── maven-status/                         # Status do build Maven
    └── suptec-desktop-1.0.0.jar              # JAR executável final
```

## Como Executar

### Pré-requisitos

- **Java 17** ou superior instalado
- **Maven 3.6+** instalado
- **API SUPTEC** rodando (opcional - atualmente usa validação local)

### Instalação e Execução

1. **Clone/baixe o projeto**
2. **Navegue até a pasta do projeto**
3. **Execute os comandos:**

```bash
# Limpar e compilar o projeto
mvn clean compile

# Executar a aplicação
mvn javafx:run
```

**Para Windows PowerShell:**
```powershell
cd 'c:\caminho\para\suptec-desktop'
mvn clean compile
mvn javafx:run
```

### Comandos Adicionais

```bash
# Build completo com JAR executável
mvn clean package

# Executar JAR gerado (se configurado)
java -jar target/suptec-desktop-1.0.0.jar

# Apenas compilar
mvn compile

# Limpar arquivos gerados
mvn clean
```

## Configuração da API

### Modo Atual (MVP)
- **Validação local**: `admin` / `123`
- **Persistência**: Preferences API do Java

### Modo Produção (Futuro)
- **API REST**: `http://localhost:5165/api/`
- **Endpoint**: `POST /api/usuarios/login`
- **Autenticação**: JSON com email/senha

Para alternar entre modos, modifique `UsuarioService.validarLogin()`.

## Sistema de Estilos CSS

O projeto utiliza um sistema CSS completamente modular:

### Arquitetura de Estilos
- **`main.css`**: Importa todos os módulos
- **Cores**: Variáveis centralizadas em `colors.css`
- **Componentes**: Estilos específicos por funcionalidade
- **Tema**: Azul corporativo consistente

### Módulos CSS
1. **colors.css** - Paleta e variáveis de cor
2. **typography.css** - Textos e fontes
3. **buttons.css** - Botões primários e secundários
4. **inputs.css** - Campos de entrada e foco
5. **containers.css** - Containers e painéis
6. **login.css** - Estilos específicos do login
7. **menu.css** - Menu e seus componentes
8. **menu-header.css** - Cabeçalho do menu
9. **menu-cards.css** - Cards interativos
10. **menu-layout.css** - Layout e rodapé

## Arquitetura da Aplicação

### Padrão MVC Implementado
- **Model**: `Usuario.java` - Dados e entidades
- **View**: Arquivos FXML - Interfaces declarativas
- **Controller**: Classes Java - Lógica e eventos

### Componentes Modulares
- **Reutilizáveis**: Header, Footer, Cards
- **Especializados**: Login, MenuGrid
- **Independentes**: Cada componente gerencia seu estado

### Serviços e Utilitários
- **ApiService**: Cliente HTTP genérico
- **UsuarioService**: Lógica de autenticação
- **SceneManager**: Navegação entre telas
- **AlertUtils**: Diálogos padronizados

## 📋 Funcionalidades Detalhadas

### Tela de Login
- ✅ Formulário com e-mail e senha
- ✅ Checkbox "lembrar de mim"
- ✅ Validação e feedback de erro
- ✅ Carregamento automático de logo
- ✅ Transição para menu principal

### Menu Principal
- ✅ Cabeçalho com informações do usuário
- ✅ Grid 2x2 de cards funcionais
- ✅ Efeitos hover e cliques
- ✅ Logout com confirmação
- ✅ Rodapé informativo

### Cards do Menu
- ✅ **Usuários**: Placeholder para gerenciamento
- ✅ **Chamados**: Placeholder para sistema de chamados
- ✅ **Relatórios**: Placeholder para dashboards
- ✅ **Configurações**: Placeholder para preferências

## 🔮 Roadmap e Desenvolvimento Futuro

### Próximas Implementações
- 🔄 **API Integration**: Conectar com backend real
- 🔄 **Módulo de Usuários**: CRUD completo
- 🔄 **Sistema de Chamados**: Criação, edição, status
- 🔄 **Dashboard de Relatórios**: Gráficos e estatísticas
- 🔄 **Configurações**: Temas, preferências do usuário

### Melhorias Planejadas
- 🔄 **Banco de Dados**: Integração com SQL Server
- 🔄 **Notificações**: Sistema de alertas em tempo real
- 🔄 **Temas**: Modo escuro/claro
- 🔄 **Multilingual**: Suporte a múltiplos idiomas
- 🔄 **Logs**: Sistema de auditoria

## 📊 Status do Projeto

- ✅ **Arquitetura**: Completa e modular
- ✅ **Interface**: Profissional e responsiva
- ✅ **Navegação**: Fluida entre telas
- ✅ **Estilos**: Sistema CSS completo
- ✅ **Documentação**: Código e README atualizados
- 🔄 **Funcionalidades**: MVP implementado
- 🔄 **Testes**: Em desenvolvimento
- 🔄 **Integração**: Preparado para API

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📝 Licença

Este projeto é propriedade da SUPTEC. Todos os direitos reservados.

## 📞 Suporte

Para suporte técnico ou dúvidas sobre o desenvolvimento, entre em contato com a equipe SUPTEC.

---

**SUPTEC Desktop v1.0.0** - Sistema de Chamados Técnicos  
*Desenvolvido com JavaFX 21 e Java 17*
