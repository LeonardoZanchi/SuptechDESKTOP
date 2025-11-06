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

### **Gerenciamento de Usuários Completo**
- **Tela de Listagem**: Visualização de usuários em tabela organizada com busca avançada
- **Formulário de Cadastro**: Interface moderna para criação de novos usuários
- **Tela de Edição**: Interface profissional para modificação de dados existentes
- **Funcionalidade de Exclusão**: Sistema seguro de exclusão com confirmações
- **Validação em Tempo Real**: Feedback imediato para campos obrigatórios
- **Campos Condicionais**: Setor (Gerentes/Usuários) e Especialidade (Técnicos)
- **Gestão de Senhas**: Criação segura e alteração opcional com orientações
- **Design Responsivo**: Layout organizado por seções temáticas
- **Integração API**: CRUD completo conectado aos endpoints backend

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
│       │   │   ├── LoginController.java              # Controller da tela de login
│       │   │   ├── MainMenuController.java           # Controller principal do menu
│       │   │   ├── UserListController.java           # Controller da listagem de usuários
│       │   │   ├── UserFormController.java           # Controller do cadastro de usuários
│       │   │   ├── UserEditController.java           # Controller da edição de usuários
│       │   │   ├── ChamadoListController.java       # Controller da listagem de chamados
│       │   │   ├── ChamadoEditController.java       # Controller de edição/visualização de chamados
│       │   │   ├── ReportsController.java           # Controller dos relatórios/dashboards
│       │   │   ├── ConfigController.java            # Controller da tela de configurações
│       │   │   ├── components/                       # Controllers de componentes modulares
│       │   │   │   ├── HeaderController.java         # Controller do cabeçalho
│       │   │   │   └── MenuGridController.java       # Controller do grid de cards
│       │   │   └── helpers/                          # Classes auxiliares (padrão SOLID)
│       │   │       ├── UserActionHandler.java        # Gerencia ações sobre usuários
│       │   │       ├── UserSearchHandler.java        # Gerencia busca e filtros
│       │   │       ├── UserSelectionManager.java     # Gerencia seleção na tabela
│       │   │       └── UserTableConfigurator.java    # Configura colunas da tabela
│       │   ├── core/
│       │   │   └── SceneManager.java         # Gerenciador de cenas e navegação
│       │   ├── models/
│       │   │   ├── Usuario.java                      # Modelo principal de usuário
│       │   │   ├── Chamado.java                      # Modelo de chamados de suporte
│       │   │   └── api/                              # Models de response da API
│       │   │       ├── AuthResponse.java             # Response de autenticação
│       │   │       ├── BaseResponse.java             # Response base com status
│       │   │       ├── UserResponse.java             # Response específica de usuários
│       │   │       └── ErrorDetails.java             # Detalhes de erros da API
│       │   ├── services/
│       │   │   ├── ApiService.java                   # Cliente HTTP base para API REST
│       │   │   ├── AuthService.java                  # Serviços de autenticação
│       │   │   ├── UserManagementService.java        # Serviços de gerenciamento de usuários
│       │   │   ├── UserRegistrationService.java      # Serviços de cadastro de usuários
│       │   │   └── ChamadoService.java               # Serviços para sistema de chamados
│       │   └── utils/
│       │       ├── AlertUtils.java                   # Utilitários para alertas e diálogos
│       │       ├── JsonUtils.java                    # Utilitários para manipulação JSON
│       │       ├── FieldValidator.java               # Validadores de campos de formulário
│       │       └── SettingsService.java              # Persistência local de preferências (Properties)
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
│           │   ├── menu-layout.css           # Estilos de layout do menu
│           │   └── user-forms.css            # Estilos unificados para todas as telas de usuário
│           ├── fxml/                         # Interfaces FXML
│           │   ├── LoginView.fxml            # Tela de login completa
│           │   ├── MainMenuView.fxml         # Menu principal com componentes
│           │   ├── UserFormView.fxml         # Tela de cadastro de usuário (estilizada)
│           │   ├── UserEditView.fxml         # Tela de edição de usuário (estilizada)
│           │   ├── UserListView.fxml         # Tela de listagem de usuários
│           │   ├── ConfigView.fxml           # Tela de configurações (tema/idioma/autosave)
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
- **API SUPTEC** rodando

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
cd 'C:\Users\roxic\DesktopAPP\suptec-desktop'
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

### Modo Atual (Produção Integrada)
- **API REST**: `http://localhost:5165/api/`
- **Endpoints Implementados**:
  - `GET /api/Usuario/Listar` - Listagem de usuários
  - `GET /api/Gerente/Listar` - Listagem de gerentes
  - `GET /api/Tecnico/Listar` - Listagem de técnicos
  - `DELETE /api/Usuario/Excluir/{id}` - Exclusão de usuários
  - `DELETE /api/Gerente/Excluir/{id}` - Exclusão de gerentes
  - `DELETE /api/Tecnico/Excluir/{id}` - Exclusão de técnicos
- **Autenticação**: Implementada com validação de sessão
- **Logs**: Sistema completo de debug e monitoramento

### Configuração de Desenvolvimento
- **Host**: localhost
- **Porta**: 5165
- **Protocolo**: HTTP (desenvolvimento)
- **Headers**: Content-Type: application/json
- **Timeout**: 30 segundos

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
11. **user-forms.css** - Sistema unificado para todas as telas

## **Melhorias Visuais Implementadas (v1.1)**

### **Sistema Unificado de Telas de Usuário**
-  **Interface Consistente**: Padrão visual unificado entre listagem, cadastro e edição
-  **Organização por Seções**: Agrupamento lógico em "Informações Pessoais", "Profissionais" e "Segurança"
-  **Cabeçalhos Modernos**: Fundo gradiente azul com títulos e subtítulos informativos
-  **Campos Condicionais Destacados**: Seções especiais com fundo diferenciado para campos específicos por tipo
-  **Feedback Visual Aprimorado**: Bordas azuis, sombras e transições suaves
-  **Dicas de Segurança**: Orientações visuais para criação de senhas seguras
-  **Responsividade**: Layout adaptativo para diferentes resoluções

### **Componentes Visuais Modernos**
- **Seções Categorizadas**: Visual organizado por tipo de informação
- **Campos com Estados**: Normal, foco, erro com cores e efeitos distintos
- **Botões com Ícones**: Emojis para melhor identificação visual (💾 Salvar, ✏ Editar, 🗑 Excluir)
- **Scroll Personalizado**: Barra de rolagem discreta com hover azul
- **Tooltips Informativos**: Textos de ajuda integrados ao design
- **Tabelas Estilizadas**: Headers azuis e hover effects para melhor usabilidade

### **Otimizações de CSS**
-  **Arquivo Unificado**: `user-forms.css` consolidou todos os estilos de usuário
-  **Eliminação de Redundância**: Removido `user-list.css` duplicado
-  **Manutenção Simplificada**: Estilos centralizados em um local
-  **Performance**: Menos arquivos CSS para carregar

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

##  Funcionalidades Detalhadas

### Tela de Login
-  Formulário com e-mail e senha
-  Checkbox "lembrar de mim"
-  Validação e feedback de erro
-  Carregamento automático de logo
-  Transição para menu principal

### Menu Principal
-  Cabeçalho com informações do usuário
-  Grid 2x2 de cards funcionais
-  Efeitos hover e cliques
-  Logout com confirmação
-  Rodapé informativo

### Cards do Menu
-  **Usuários**: Gerenciamento completo (listagem, cadastro, edição)
-  **Chamados**: Sistema de chamados completo (criação, edição, alteração de status)
-  **Relatórios**: Dashboards e KPIs implementados e funcionando
-  **Configurações**: Interface de preferências implementada (UI); aplicação das preferências em runtime parcialmente funcional

### Gerenciamento de Usuários (IMPLEMENTADO v1.1)
-  **Cadastro de Usuários**: Interface moderna organizada por seções temáticas
-  **Edição de Usuários**: Tela profissional com campos preenchidos e validação
-  **Listagem de Usuários**: Tabela organizada com busca, filtros e ações
-  **Exclusão de Usuários**: Funcionalidade completa com confirmação e validações
-  **Validação Avançada**: Campos obrigatórios e feedback visual em tempo real
-  **Tipos de Usuário**: Admin, Gerente, Técnico, Usuário com campos específicos
-  **Gestão de Senhas**: Criação segura e alteração opcional com dicas de segurança
-  **Interface Responsiva**: Layout adaptativo e scroll personalizado
-  **Integração com API**: Conectado aos endpoints reais para CRUD completo

### Configurações / Preferências (implementado — UI)

- Tela de configurações implementada com opções visuais de preferência:
  - Tema do sistema: Claro / Escuro (radio buttons)
  - Salvar alterações automaticamente: checkbox que persiste a preferência localmente
  - Idioma da interface: ComboBox com Português / English (protótipo de i18n)
  - Seção "Sobre o sistema" no rodapé com informações institucionais

Observação importante: a interface de Configurações já existe e grava preferências localmente via `SettingsService`,
porém a aplicação automática dessas preferências em tempo de execução (por exemplo, aplicar tema imediatamente ou
trocar ResourceBundle de idioma dinamicamente) está parcialmente funcional e requer integração adicional.

Implementação técnica relevante:
- `br.com.suptec.utils.SettingsService` — serviço simples que grava/ler `Properties` em `user.home` (`.suptech_settings.properties`).
- `br.com.suptec.controllers.ConfigController` — controller da tela de configurações que inicializa controles e persiste preferências.
- Navegação: foi adicionada a função `SceneManager.replaceRootPreserveStage(...)` para preservar o tamanho da janela ao trocar de telas (substitui apenas o root da Scene existente).


## Status do Projeto (v1.1)

### Módulos Completos e Funcionais

####  Sistema de Autenticação
- Interface de login profissional e responsiva
- Validação completa de credenciais
- Integração com API REST funcional
- Tratamento de erros e feedback visual

#### Gerenciamento de Usuários (100% Funcional)
- **CRUD Completo**: Criação, listagem, edição e exclusão
- **Interface Unificada**: Design consistente e profissional
- **Funcionalidades Avançadas**:
  - Sistema de busca e filtros em tempo real
  - Formulários com validação robusta
  - Tipos de usuário (Técnico, Gerente) com campos condicionais
  - Ações em lote com confirmações de segurança
  - Feedback visual para todas as operações

#### Sistema Visual (Otimizado)
- **CSS Modular**: 10 arquivos organizados por responsabilidade
- **Design System**: Paleta de cores e tipografia padronizadas
- **Componentes Reutilizáveis**: Headers, cards, formulários unificados
- **Responsividade**: Layouts adaptativos para diferentes resoluções


**SUPTEC Desktop v1.1.0** - Sistema de Gerenciamento e Chamados Técnicos  
*Desenvolvido com JavaFX 21 e Java 17 - Arquitetura Modular e API-First*
