package banking.common.repository;

import banking.common.repository.model.Operacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public int countByFechaOperacionAndImporteAndSaldoAndConcepto(Timestamp fechaOperacion, Double importe, Double saldo, String concepto) {
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE fecha_operacion = ? AND importe = ? AND saldo = ? AND concepto = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {

            checkStatement.setTimestamp(1, fechaOperacion);
            checkStatement.setObject(2, importe);
            checkStatement.setObject(3, saldo);
            checkStatement.setString(4, concepto);

            ResultSet rs = checkStatement.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void insertOperacion(Timestamp fechaOperacion, Double importe, Double saldo, String concepto, String etiqueta, String original) {
        String insertSql = "INSERT INTO " + tableName + " (fecha_operacion, importe, saldo, concepto, etiqueta, original) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setTimestamp(1, fechaOperacion);
            insertStatement.setObject(2, importe);
            insertStatement.setObject(3, saldo);
            insertStatement.setString(4, concepto);
            insertStatement.setObject(5, etiqueta);
            insertStatement.setString(6, original);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTag(Timestamp fechaOperacion, Double importe, Double saldo, String concepto, String nuevaEtiqueta) {
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
    public List<Operacion> findAllWithNegativeImporte() {
        String selectSql = "SELECT * FROM " + tableName + " WHERE importe < 0 AND etiqueta != 'ASUMIDO'";
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
    public List<Operacion> findAllWithPositiveImporte() {
        String selectSql = "SELECT * FROM " + tableName + " WHERE importe > 0 AND etiqueta != 'ASUMIDO'";
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
    public Operacion findLatestOperacion() {
        String selectSql = "SELECT * FROM " + tableName + " ORDER BY fecha_operacion DESC LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                Operacion operacion = new Operacion();
                operacion.setFechaOperacion(rs.getTimestamp("fecha_operacion"));
                operacion.setImporte(rs.getDouble("importe"));
                operacion.setSaldo(rs.getDouble("saldo"));
                operacion.setConcepto(rs.getString("concepto"));
                operacion.setEtiqueta(rs.getString("etiqueta"));
                return operacion;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}