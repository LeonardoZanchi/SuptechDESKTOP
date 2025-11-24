package br.com.suptec.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import br.com.suptec.models.Chamado;
import br.com.suptec.models.Usuario;
import br.com.suptec.services.ChamadoService;
import br.com.suptec.services.UserManagementService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controller para a tela de relatórios (KPIs e gráficos)
 */
public class ReportsController implements Initializable {

    @FXML private Label lblTotalChamados;
    @FXML private Label lblFechados;
    @FXML private Label lblPendentes;
    @FXML private Label lblTecnicos;
    @FXML private Label lblUsuariosComuns;

    @FXML private PieChart pieStatus;
    @FXML private BarChart<String, Number> barPrioridade;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private LineChart<String, Number> lineMonthly;
    @FXML private CategoryAxis lineXAxis;
    @FXML private NumberAxis lineYAxis;

    @FXML private VBox insightsContainer;

    private final ChamadoService chamadoService;
    private final UserManagementService userService;

    public ReportsController() {
        this.chamadoService = ChamadoService.getInstance();
        this.userService = UserManagementService.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar e carregar dados
        carregarDados();
    }

    @FXML
    private void handleAtualizar() {
        carregarDados();
    }

    @FXML
    private void handleVoltar(javafx.event.ActionEvent event) {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) lblTotalChamados.getScene().getWindow();
            br.com.suptec.core.SceneManager.replaceRootPreserveStage(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu Principal");
        } catch (Exception e) {
            System.err.println("Erro ao voltar para o menu: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            List<Chamado> chamados = chamadoService.listarChamados();
            var usuarios = userService.listarUsuarios();

            int total = chamados.size();
            int fechados = (int) chamados.stream()
                .filter(c -> c.getStatus() != null && c.getStatus().equalsIgnoreCase("fechado"))
                .count();
            int pendentes = total - fechados;

            // KPIs
            lblTotalChamados.setText(String.valueOf(total));
            lblFechados.setText(String.valueOf(fechados));
            lblPendentes.setText(String.valueOf(pendentes));

            long tecnicos = usuarios.stream().filter(u -> u.getTipo() == Usuario.TipoUsuario.TECNICO).count();
            long usuariosComuns = usuarios.stream().filter(u -> u.getTipo() == Usuario.TipoUsuario.USUARIO).count();

            lblTecnicos.setText(String.valueOf(tecnicos));
            lblUsuariosComuns.setText(String.valueOf(usuariosComuns));

            // PieChart - distribuição por status
            Map<String, Long> statusCounts = chamados.stream()
                .collect(Collectors.groupingBy(c -> normalizeStatus(c.getStatus()), Collectors.counting()));

            pieStatus.getData().clear();
            for (Map.Entry<String, Long> e : statusCounts.entrySet()) {
                pieStatus.getData().add(new PieChart.Data(e.getKey(), e.getValue()));
            }

            // BarChart - prioridades
            Map<String, Long> prioridadeCounts = chamados.stream()
                .collect(Collectors.groupingBy(c -> normalizePrioridade(c.getPrioridade()), Collectors.counting()));

            barPrioridade.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Chamados");

            // garantir ordem: Baixa, Media, Alta
            String[] categorias = new String[] {"Baixa", "Media", "Alta"};
            for (String cat : categorias) {
                Number val = prioridadeCounts.getOrDefault(cat, 0L).intValue();
                series.getData().add(new XYChart.Data<>(cat, val));
            }

            barPrioridade.getData().add(series);

            // colorir as barras por prioridade (Baixa=verde, Media=amarela, Alta=vermelha)
            Map<String, String> prioridadeColorMap = Map.of(
                "Baixa", "#2ecc71",
                "Media", "#f1c40f",
                "Alta", "#e74c3c"
            );

            Platform.runLater(() -> {
                // aplicar cor a cada barra
                for (XYChart.Data<String, Number> d : series.getData()) {
                    String label = d.getXValue();
                    String color = prioridadeColorMap.getOrDefault(label, "#95a5a6");
                    Node node = d.getNode();
                    if (node != null) {
                        node.setStyle(String.format("-fx-bar-fill: %s;", color));
                    } else {
                        d.nodeProperty().addListener((obs, oldNode, newNode) -> {
                            if (newNode != null) newNode.setStyle(String.format("-fx-bar-fill: %s;", color));
                        });
                    }
                }

                // colorir símbolos da legenda do BarChart, se presentes
                var legendItems = barPrioridade.lookupAll(".chart-legend-item");
                for (Node item : legendItems) {
                    if (item instanceof Parent) {
                        Parent p = (Parent) item;
                        Node symbol = p.lookup(".chart-legend-item-symbol");
                        Node lblNode = p.lookup(".label");
                        String text = null;
                        if (lblNode instanceof Label) text = ((Label) lblNode).getText();
                        String color = prioridadeColorMap.getOrDefault(text, "#95a5a6");
                        if (symbol instanceof Region) {
                            ((Region) symbol).setStyle(String.format("-fx-background-color: %s; -fx-border-color: transparent;", color));
                        }
                    }
                }
            });

            // Insights: construir nós estilizados para exibição bonita
            insightsContainer.getChildren().clear();

            Label totalLabel = new Label(String.format("Total de chamados: %d", total));
            // usar classe específica para insights (texto escuro sobre fundo claro)
            totalLabel.getStyleClass().addAll("insight-total");
            insightsContainer.getChildren().add(totalLabel);

            // distribuição por status (cada linha com nome e badge de contagem/percentual)
            for (Map.Entry<String, Long> e : statusCounts.entrySet()) {
                double pct = total == 0 ? 0 : (e.getValue() * 100.0 / total);

                HBox row = new HBox(12);
                row.setAlignment(Pos.CENTER_LEFT);
                Label name = new Label(e.getKey());
                name.getStyleClass().addAll("info-label");

                Label badge = new Label(String.format("%d (%.0f%%)", e.getValue(), pct));
                badge.getStyleClass().addAll("kpi-value");
                HBox.setMargin(badge, new Insets(0,0,0,8));

                row.getChildren().addAll(name, badge);
                insightsContainer.getChildren().add(row);
            }

            // aplicar cores ao PieChart para combinar com os badges
            Map<String, String> colorMap = Map.of(
                "Aberto", "#2ecc71",
                "Fechado", "#e74c3c",
                "Pendente", "#f1c40f"
            );

            // Aplicar estilos após o layout garantir os nós do chart (fatias e legenda)
            Platform.runLater(() -> {
                for (PieChart.Data d : pieStatus.getData()) {
                    String color = colorMap.getOrDefault(d.getName(), "#95a5a6");
                    Node slice = d.getNode();
                    if (slice != null) slice.setStyle(String.format("-fx-pie-color: %s;", color));
                }

                // sincronizar legenda: cada item tem um símbolo com classe .chart-legend-item-symbol
                var legendItems = pieStatus.lookupAll(".chart-legend-item");
                for (Node item : legendItems) {
                    if (item instanceof Parent) {
                        Parent p = (Parent) item;
                        Node symbol = p.lookup(".chart-legend-item-symbol");
                        Node lblNode = p.lookup(".label");
                        String text = null;
                        if (lblNode instanceof Label) {
                            text = ((Label) lblNode).getText();
                        }
                        String color = colorMap.getOrDefault(text, "#95a5a6");
                        if (symbol instanceof Region) {
                            ((Region) symbol).setStyle(String.format("-fx-background-color: %s; -fx-border-color: transparent;", color));
                        }
                    }
                }
            });

            // prioridades
            Label priTitle = new Label("\nChamados por prioridade:");
            priTitle.getStyleClass().addAll("section-title");
            insightsContainer.getChildren().add(priTitle);

            for (String cat : categorias) {
                long v = prioridadeCounts.getOrDefault(cat, 0L);
                HBox prow = new HBox(12);
                prow.setAlignment(Pos.CENTER_LEFT);
                Label pname = new Label(cat);
                pname.getStyleClass().addAll("info-label");
                Label pval = new Label(String.valueOf(v));
                pval.getStyleClass().addAll("kpi-value");
                prow.getChildren().addAll(pname, pval);
                insightsContainer.getChildren().add(prow);
            }

            if (pendentes > 0) {
                Label alert = new Label("Atenção: existem chamados pendentes. Verifique a fila para evitar acúmulo.");
                alert.getStyleClass().addAll("kpi-red", "info-label");
                insightsContainer.getChildren().add(alert);
            } else {
                Label ok = new Label("Nenhum chamado pendente no momento.");
                ok.getStyleClass().addAll("kpi-green", "info-label");
                insightsContainer.getChildren().add(ok);
            }

            // --- Relatório Mensal: volume de chamados por mês (últimos 6 meses) ---
            try {
                java.time.YearMonth nowYm = java.time.YearMonth.now();
                int months = 6;
                java.time.format.DateTimeFormatter monthFmt = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", new java.util.Locale("pt", "BR"));

                // preparar mapa com últimos N meses (inicializa com zero)
                java.util.List<java.time.YearMonth> monthsList = new java.util.ArrayList<>();
                for (int i = months - 1; i >= 0; i--) {
                    monthsList.add(nowYm.minusMonths(i));
                }

                java.util.Map<java.time.YearMonth, Long> monthlyCounts = new java.util.HashMap<>();
                for (java.time.YearMonth ym : monthsList) monthlyCounts.put(ym, 0L);

                for (Chamado c : chamados) {
                    if (c.getDataAbertura() != null) {
                        java.time.YearMonth ym = java.time.YearMonth.from(c.getDataAbertura());
                        if (monthlyCounts.containsKey(ym)) {
                            monthlyCounts.put(ym, monthlyCounts.get(ym) + 1);
                        }
                    }
                }

                XYChart.Series<String, Number> seriesMonth = new XYChart.Series<>();
                seriesMonth.setName("Chamados");
                for (java.time.YearMonth ym : monthsList) {
                    String label = ym.format(monthFmt);
                    Number val = monthlyCounts.getOrDefault(ym, 0L).intValue();
                    seriesMonth.getData().add(new XYChart.Data<>(label, val));
                }

                lineMonthly.getData().clear();
                lineMonthly.getData().add(seriesMonth);
                // garantir que a linha e a legenda do gráfico mensal fiquem sempre azuis
                Platform.runLater(() -> {
                    String blue = "#3498db";
                    // estilizar a linha da série
                    Node seriesNode = seriesMonth.getNode();
                    if (seriesNode != null) {
                        Node line = seriesNode.lookup(".chart-series-line");
                        if (line != null) line.setStyle(String.format("-fx-stroke: %s; -fx-stroke-width: 2px;", blue));
                    }

                    // estilizar os símbolos (pontos) da série, se existirem
                    for (XYChart.Data<String, Number> d : seriesMonth.getData()) {
                        Node dataNode = d.getNode();
                        if (dataNode != null) {
                            dataNode.setStyle(String.format("-fx-background-color: %s, white; -fx-background-insets: 0, 2;", blue));
                        }
                    }

                    // estilizar símbolo da legenda para o LineChart
                    var legendItems = lineMonthly.lookupAll(".chart-legend-item");
                    for (Node item : legendItems) {
                        if (item instanceof Parent) {
                            Parent p = (Parent) item;
                            Node symbol = p.lookup(".chart-legend-item-symbol");
                            if (symbol instanceof Region) {
                                ((Region) symbol).setStyle(String.format("-fx-background-color: %s; -fx-border-color: transparent;", blue));
                            }
                        }
                    }
                });
            } catch (Exception ex) {
                System.err.println("Erro ao montar relatório mensal: " + ex.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar relatórios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String normalizeStatus(String s) {
        if (s == null) return "Pendente";
        String v = s.trim().toLowerCase();
    if (v.contains("aberto")) return "Aberto";
    if (v.contains("fechado")) return "Fechado";
        return "Pendente";
    }

    private String normalizePrioridade(String p) {
        if (p == null) return "Baixa";
        String v = p.trim().toLowerCase();
        if (v.contains("alta")) return "Alta";
        if (v.contains("media") || v.contains("média")) return "Media";
        return "Baixa";
    }
}
