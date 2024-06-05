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

import crud.CurrentYearUFCount;

import java.sql.*;

public class RelatorioCurrentYearUFCountController {

    @FXML
    private TableView<CurrentYearUFCount> tableView;

    @FXML
    private TableColumn<CurrentYearUFCount, String> yearColumn;

    @FXML
    private TableColumn<CurrentYearUFCount, String> ufColumn;

    @FXML
    private TableColumn<CurrentYearUFCount, Integer> countColumn;

    @FXML
    private void initialize() {
        showCurrentYearUFCounts();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/crud?serverTimezone=UTC", "root", "2165");
    }

    private ObservableList<CurrentYearUFCount> getCurrentYearUFCountsList() {
        ObservableList<CurrentYearUFCount> currentYearUFCountsList = FXCollections.observableArrayList();

        String query = "SELECT " +
                            "YEAR(STR_TO_DATE(DateOfBirth, '%d/%m/%Y')) AS Year, "+
                            "UF, "+
                            "COUNT(*) AS Count "+
                        "FROM persons "+
                        "GROUP BY Year, UF "+
                        "ORDER BY Year desc, UF";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CurrentYearUFCount currentYearUFCount = new CurrentYearUFCount(rs.getString("Year"), rs.getString("UF"), rs.getInt("Count"));
                currentYearUFCountsList.add(currentYearUFCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao buscar clientes: " + e.getMessage());
        }
        return currentYearUFCountsList;
    }

    private void showCurrentYearUFCounts() {
        ObservableList<CurrentYearUFCount> list = getCurrentYearUFCountsList();

        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getYear()));
        ufColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUF()));
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
