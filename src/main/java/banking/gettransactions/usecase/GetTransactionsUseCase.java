package banking.gettransactions.usecase;

import banking.common.repository.OperacionesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;

@RequiredArgsConstructor
@Service
public class GetTransactionsUseCase implements GetTransactionsUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public void execute() {
        String excelFilePath = "C:\\Users\\xboxa\\IdeaProjects\\ExpensesManagementApp\\src\\main\\transactionshistory\\movimientos.xlsx";

        String jsonFilePath = "output.json";
        int startRow = 11; // 0-based index, so row 12 is index 11
        int startColumn = 1; // Column B is index 1
        int endColumn = 9; // Column J is index 9

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            JSONArray jsonArray = new JSONArray();

            Row namesRow = null;
            int currentRow = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (currentRow == 9) {
                    namesRow = rowIterator.next();
                    currentRow++;
                }

                if (currentRow >= startRow) {
                    JSONObject jsonObject = new JSONObject();
                    String fechaOperacion = null;
                    Double importe = null;
                    Double saldo = null;
                    String concepto = null;
                    String etiqueta = null;
                    String original = "yes";

                    for (int col = startColumn; col <= endColumn; col++) {
                        if (col != 2 && col != 3 && col != 4 && col != 6 && col != 3 && col != 8) {
                            Cell cell = row.getCell(col);
                            Cell cellName = namesRow.getCell(col);
                            String cellValue = getCellValueAsString(cell);
                            String cellDescription = getCellValueAsString(cellName);

                            jsonObject.put(cellDescription, cellValue);

                            switch (cellDescription.toLowerCase()) {
                                case "fecha operación":
                                    fechaOperacion = cellValue.isEmpty() ? null : cellValue;
                                    break;
                                case "importe":
                                    importe = cellValue.isEmpty() ? null : Double.parseDouble(cellValue);
                                    break;
                                case "saldo":
                                    saldo = cellValue.isEmpty() ? null : Double.parseDouble(cellValue);
                                    break;
                                case "concepto":
                                    concepto = cellValue.isEmpty() ? null : cellValue;
                                    break;
                            }

                           if (concepto != null) {
                                if (concepto.contains("TRANSFERENCIA A FAVOR DE MANCHA NUÑEZ ANGEL JOSE CONCEPTO") ||
                                        concepto.contains("RECARGA TARJETA PREPAGO") ||
                                        concepto.contains("DESCARGA TARJETA PREPAGO")) {
                                    etiqueta = "ASUMIDO";
                                } else if (importe != null && importe > 0) {
                                    etiqueta = "ingresos";
                                } else {
                                    etiqueta = "otros";
                                }
                            }
                        }
                    }

                    jsonArray.put(jsonObject);

                    if (fechaOperacion != null && concepto != null) {
                        Timestamp fechaOperacionTimestamp = Timestamp.valueOf(fechaOperacion);
                        int count = operacionesRepository.countByFechaOperacionAndImporteAndSaldoAndConcepto(fechaOperacionTimestamp, importe, saldo, concepto);

                        if (count == 0) {
                            operacionesRepository.insertOperacion(fechaOperacionTimestamp, importe, saldo, concepto, etiqueta, original);
                        }
                    }
                }
                currentRow++;
            }

            try (FileWriter file = new FileWriter(jsonFilePath)) {
                file.write(jsonArray.toString(4));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new java.sql.Timestamp(cell.getDateCellValue().getTime()).toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}