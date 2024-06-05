package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void openCadastroCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CadastroCliente.fxml"));
            Parent root = loader.load();
            CadastroClienteController controller = loader.getController();
            Stage cadastroStage = new Stage();
            cadastroStage.setScene(new Scene(root));
            cadastroStage.setTitle("Cadastro de Cliente");
            cadastroStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir tela de cadastro de cliente: " + e.getMessage());
        }
    }

    public void openConsultaClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ConsultaClientes.fxml"));
            Parent root = loader.load();
            ConsultaClientesController controller = loader.getController();
            Stage consultaStage = new Stage();
            consultaStage.setScene(new Scene(root));
            consultaStage.setTitle("Consulta de Clientes");
            consultaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir tela de consulta de clientes: " + e.getMessage());
        }
    }

    public void openRelatorioCountBirth() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RelatorioCountBirth.fxml"));
            Parent root = loader.load();
            RelatorioCountBirthController controller = loader.getController();
            Stage consultaStage = new Stage();
            consultaStage.setScene(new Scene(root));
            consultaStage.setTitle("Relatório de quantidade de aniversário por mês");
            consultaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir tela de relatório de aniversários por mês: " + e.getMessage());
        }
    }

    public void openRelatorioCurrentYearUFCount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RelatorioCurrentYearUFCount.fxml"));
            Parent root = loader.load();
            RelatorioCurrentYearUFCountController controller = loader.getController();
            Stage consultaStage = new Stage();
            consultaStage.setScene(new Scene(root));
            consultaStage.setTitle("Relatório de quantidade de clientes por UF");
            consultaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir tela de relatório de idades dos clientes: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
