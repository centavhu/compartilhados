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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.application.Platform;
import java.util.Optional;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

import crud.DateCount;

import java.sql.*;

public class RelatorioCountBirthController {

    @FXML
    private TableView<DateCount> tableView;

    @FXML
    private TableColumn<DateCount, String> yearColumn;

    @FXML
    private TableColumn<DateCount, String> monthColumn;

    @FXML
    private TableColumn<DateCount, Integer> countColumn;

    @FXML
    private void initialize() {
        showDateCounts();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?serverTimezone=UTC", "root", "2165");
    }

    private ObservableList<DateCount> getDateCountsList() {
        ObservableList<DateCount> dateCountsList = FXCollections.observableArrayList();

        String query = "SELECT YEAR(STR_TO_DATE(DateOfBirth, '%d/%m/%Y')) AS Year, " + //
                        "DATE_FORMAT(STR_TO_DATE(DateOfBirth, '%d/%m/%Y'), '%M') AS Month, " + //
                        "COUNT(*) AS Count " + //
                        "FROM persons " + //
                        "GROUP BY Year, Month " + //
                        "ORDER BY Year desc, Month desc";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DateCount dateCount = new DateCount(rs.getString("Year"), rs.getString("Month"), rs.getInt("Count"));
                dateCountsList.add(dateCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao buscar clientes: " + e.getMessage());
        }
        return dateCountsList;
    }

    private void showDateCounts() {
        ObservableList<DateCount> list = getDateCountsList();

        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getYear()));
        monthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()).asObject());

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
