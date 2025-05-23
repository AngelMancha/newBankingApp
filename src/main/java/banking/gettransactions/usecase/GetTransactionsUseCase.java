package banking.gettransactions.usecase;

import banking.common.repository.OperacionesRepository;
import banking.common.repository.model.xlsConfigurationDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTransactionsUseCase implements GetTransactionsUseCaseInterface {

    private final OperacionesRepository operacionesRepository;

    @Override
    public void execute(byte[] file, xlsConfigurationDto request) {
        int headerRow = Integer.parseInt(request.getHeaderRow());
        int startColumn = 1;
        int endColumn = 10;

        int fechaColumna = Integer.parseInt(request.getFechaCelda());
        int conceptoColumna = Integer.parseInt(request.getConceptoCelda());
        int importeColumna = Integer.parseInt(request.getImporteCelda());

        String fechaNombre = "";
        String conceptoNombre = "";
        String importeNombre = "";

        try (ByteArrayInputStream bais = new ByteArrayInputStream(file);
             Workbook workbook = new XSSFWorkbook(bais)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            JSONArray jsonArray = new JSONArray();

            Row namesRow = null;
            int currentRow = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (currentRow == headerRow - 1) {
                    namesRow = rowIterator.next();
                    currentRow++;
                }

                if (currentRow > headerRow + 1) {
                    JSONObject jsonObject = new JSONObject();
                    String fechaOperacion = null;
                    Double importe = null;
                    String concepto = null;
                    String etiqueta = null;
                    String original = "yes";


                    for (int col = startColumn; col <= endColumn; col++) {
                        if (col == fechaColumna || col == conceptoColumna || col == importeColumna) {

                            fechaNombre = getCellValueAsString(namesRow.getCell(fechaColumna));
                            conceptoNombre = getCellValueAsString(namesRow.getCell(conceptoColumna));
                            importeNombre = getCellValueAsString(namesRow.getCell(importeColumna));
                            Cell cell = row.getCell(col);
                            Cell cellName = namesRow.getCell(col);
                            String cellValue = getCellValueAsString(cell);
                            String cellDescription = getCellValueAsString(cellName);

                            jsonObject.put(cellDescription, cellValue);

                            if (cellDescription.equalsIgnoreCase(fechaNombre)) {
                                fechaOperacion = cellValue.isEmpty() ? null : cellValue;
                            } else if (cellDescription.equalsIgnoreCase(importeNombre)) {
                                importe = cellValue.isEmpty() ? null : Double.parseDouble(cellValue);
                            } else if (cellDescription.equalsIgnoreCase(conceptoNombre)) {
                                concepto = cellValue.isEmpty() ? null : cellValue;
                            }

                            if (concepto != null) {
                                if (concepto.contains("TRANSFERENCIA A FAVOR DE MANCHA NUÑEZ ANGEL JOSE") ||
                                        concepto.contains("RECARGA TARJETA PREPAGO") ||
                                        concepto.contains("DESCARGA TARJETA PREPAGO")) {
                                    etiqueta = "ASUMIDO";
                                } else if (importe != null && importe > 0) {
                                    etiqueta = "ingresos";
                                } else {
                                    etiqueta = "otros";
                                }
                            }

                            if (concepto != null) {
                                concepto = findCardNumber(concepto);
                            }

                        }
                    }

                    jsonArray.put(jsonObject);

                    if (fechaOperacion != null && concepto != null) {
                        Timestamp fechaOperacionTimestamp = Timestamp.valueOf(fechaOperacion);
                        int count = operacionesRepository.countByFechaOperacionAndImporteAndConcepto(fechaOperacionTimestamp, importe, concepto);

                        if (count == 0) {
                            operacionesRepository.insertOperacion(fechaOperacionTimestamp, importe, concepto, etiqueta, original);
                        }
                    }
                }
                currentRow++;
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

    private static String blurCardNunmber(String cardNumber) {

        char[] cardNumberArray = cardNumber.toCharArray();
        List<String> blurredCardNumber = new ArrayList<>();
        int counter = 0;
        for (char i : cardNumberArray) {
            if (counter < 12) {
                blurredCardNumber.add("*");
            } else {
                blurredCardNumber.add(String.valueOf(i));
            }
            counter++;

        }
        return String.join("", blurredCardNumber);

    }

    public static String findCardNumber(String concepto) {
        String[] words = concepto.split(" ");
        int counter = 0;
        for (String word : words) {
            if (word.length() == 16 && word.matches("[0-9]+")) {
                String blurredCardNumber = blurCardNunmber(word);
                words[counter] = blurredCardNumber;
                return String.join(" ", words);
            }
            counter++;
        }
        return String.join(" ", words);
    }

    private static String getCellName(int columnNumber, int rowNumber) {
        StringBuilder columnName = new StringBuilder();
        while (columnNumber > 0) {
            int remainder = (columnNumber - 1) % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            columnNumber = (columnNumber - 1) / 26;
        }
        return columnName.toString() + rowNumber;
    }
}





