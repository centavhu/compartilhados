package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CadastroClienteController {

    private static final Logger LOGGER = Logger.getLogger(CadastroClienteController.class.getName());

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField dataNascimentoField;

    @FXML
    private TextField UFField;

    @FXML
    private void salvarCliente() {
        String query = "INSERT INTO persons (Name, Email, DateOfBirth, UF) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomeField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, dataNascimentoField.getText());
            ps.setString(4, UFField.getText());
            ps.executeUpdate();
            showAlert("Cliente salvo com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
            showAlert("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?serverTimezone=UTC", "root", "2165");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
