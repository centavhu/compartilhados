package controller;

import javafx.geometry.Insets;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.application.Platform;
import java.util.Optional;

import crud.Person;

import java.sql.*;

public class ConsultaClientesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, Integer> idColumn;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private TableColumn<Person, String> dobColumn;

    @FXML
    private TableColumn<Person, String> UFColumn;

    @FXML
    private void initialize() {
        showPersons();
    }

    @FXML
    private void searchButton() {
        String searchText = searchField.getText();
        showPersons(searchText);
    }

    @FXML
    private void deleteButton() {
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Tem certeza que deseja excluir este cliente?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    deletePerson(selectedPerson.getId());
                    showPersons();
                }
            });
        } else {
            showAlert("Por favor, selecione um cliente para excluir.");
        }
    }

    @FXML
    private void updateButton() {
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            Dialog<Person> dialog = new Dialog<>();
            dialog.setTitle("Atualizar Cliente");
            dialog.setHeaderText(null);

            // Botão de atualização
            ButtonType updateButtonType = new ButtonType("Atualizar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Layout do diálogo
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Campos de entrada para atualização
            TextField nameField = new TextField(selectedPerson.getName());
            TextField emailField = new TextField(selectedPerson.getEmail());
            TextField dobField = new TextField(selectedPerson.getDateOfBirth());
            TextField ufField = new TextField(selectedPerson.getUF());

            grid.add(new Label("Nome:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Data de Nascimento:"), 0, 2);
            grid.add(dobField, 1, 2);
            grid.add(new Label("UF:"), 0, 3);
            grid.add(ufField, 1, 3);

            // Habilitando o botão de atualização somente quando todos os campos estiverem preenchidos
            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);
            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Focar no campo de nome por padrão
            Platform.runLater(() -> nameField.requestFocus());

            // Convertendo o resultado do diálogo para um objeto Person atualizado
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    selectedPerson.setName(nameField.getText());
                    selectedPerson.setEmail(emailField.getText());
                    selectedPerson.setDateOfBirth(dobField.getText());
                    selectedPerson.setUF(ufField.getText());
                    return selectedPerson;
                }
                return null;
            });

            Optional<Person> result = dialog.showAndWait();
            result.ifPresent(person -> {
                updatePerson(person);
                showPersons();
                showAlert("Cliente atualizado com sucesso!");
            });
        } else {
            showAlert("Por favor, selecione um cliente para atualizar.");
        }
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?serverTimezone=UTC", "root", "2165");
    }

    private void deletePerson(int id) {
        String query = "DELETE FROM persons WHERE ID=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    private void updatePerson(Person person) {
        String query = "UPDATE persons SET Name=? WHERE ID=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, person.getName());
            ps.setInt(2, person.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    private ObservableList<Person> getPersonsList(String searchText) {
        ObservableList<Person> personsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM persons";

        if (searchText != null && !searchText.isEmpty()) {
            query += " WHERE Name LIKE ?";
        }

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            if (searchText != null && !searchText.isEmpty()) {
                ps.setString(1, "%" + searchText + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Person person = new Person(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getString("DateOfBirth"), rs.getString("UF"));
                personsList.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao buscar clientes: " + e.getMessage());
        }
        return personsList;
    }

    private void showPersons() {
        showPersons(null);
    }

    private void showPersons(String searchText) {
        ObservableList<Person> list = getPersonsList(searchText);

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        dobColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfBirth()));
        UFColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUF()));

        tableView.setItems(list);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
