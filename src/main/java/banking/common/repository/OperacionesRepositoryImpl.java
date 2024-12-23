package banking.common.repository;

import banking.common.repository.model.Operacion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static banking.gettransactions.usecase.GetTransactionsUseCase.findCardNumber;

@Repository
public class OperacionesRepositoryImpl implements OperacionesRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.table-name}")
    private String tableName;

    @Override
    public int countByFechaOperacionAndImporteAndConcepto(Timestamp fechaOperacion, Double importe, String concepto) {
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE fecha_operacion = ? AND importe = ? AND concepto = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {

            checkStatement.setTimestamp(1, fechaOperacion);
            checkStatement.setObject(2, importe);
            checkStatement.setString(3, concepto);

            ResultSet rs = checkStatement.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void insertOperacion(Timestamp fechaOperacion, Double importe, String concepto, String etiqueta, String original) {
        String insertSql = "INSERT INTO " + tableName + " (fecha_operacion, importe, concepto, etiqueta, original) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setTimestamp(1, fechaOperacion);
            insertStatement.setObject(2, importe);
            insertStatement.setString(3, concepto);
            insertStatement.setObject(4, etiqueta);
            insertStatement.setString(5, original);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTag(Timestamp fechaOperacion, Double importe, String concepto, String nuevaEtiqueta) {
        String updateSql = "UPDATE " + tableName + " SET etiqueta = ? WHERE importe = ? AND concepto = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {

            updateStatement.setString(1, nuevaEtiqueta);
            updateStatement.setObject(2, importe);
            updateStatement.setString(3, concepto);

            updateStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Operacion> findAllWithNegativeImporte(String yearJson) {
        String selectSql = "SELECT * FROM " + tableName + " WHERE importe < 0 AND etiqueta != 'ASUMIDO' AND EXTRACT(YEAR FROM fecha_operacion) = ?";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            // Parse the JSON string to extract the year
            JSONObject jsonObject = new JSONObject(yearJson);
            int year = jsonObject.getInt("year");

            selectStatement.setInt(1, year);

            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

    @Override
    public List<Operacion> findAllWithPositiveImporte() {
        String selectSql = "SELECT * FROM " + tableName + " WHERE importe > 0 AND etiqueta != 'ASUMIDO' AND concepto NOT LIKE '%TRANSFERENCIA DE OPEN DIGITAL SERVICES SL%' AND concepto NOT LIKE '%LIQUIDACION CUENTA%'";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

    @Override
    public List<Operacion> findAllWithOriginalTag() {
        String selectSql = "SELECT * FROM " + tableName + " WHERE original = 'yes'";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

    @Override
    public List<Operacion> auxiliarDbOperation() {
        String selectSql = "SELECT * FROM " + tableName;
        String updateSql = "UPDATE " + tableName + " SET concepto = ? WHERE importe = ? AND fecha_operacion = ?";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                String newConcepto = findCardNumber(operacion.getConcepto());
                operacion.setEtiqueta(rs.getString("etiqueta"));
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setString(1, newConcepto);
                    updateStatement.setObject(2, operacion.getImporte());
                    updateStatement.setTimestamp(3, operacion.getFechaOperacion());
                    updateStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                operacion.setConcepto(newConcepto);

                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

    @Override
    public List<Operacion> findAllWithPayrollTag(String yearJson) {
        String selectSql = "SELECT * FROM " + tableName + " WHERE concepto LIKE '%TRANSFERENCIA DE OPEN DIGITAL SERVICES SL%'  AND EXTRACT(YEAR FROM fecha_operacion) = ?";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            // Parse the JSON string to extract the year
            JSONObject jsonObject = new JSONObject(yearJson);
            int year = jsonObject.getInt("year");

            selectStatement.setInt(1, year);


            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

    @Override
    public List<Operacion> findAllWithInterestTag() {
        String selectSql = "SELECT * FROM " + tableName + " WHERE concepto LIKE 'LIQUIDACION CUENTA'";
        List<Operacion> operaciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                operaciones.add(operacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operaciones;
    }

}