package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetData {
    private static volatile Map<String, List<Object[]>> allData;// Use List<Object[]> to allow multiple rows per scenario
    static DataFormatter formatter = new DataFormatter();// Create a DataFormatter instance to format the cell values

    public static Map<String, List<Object[]>> getCompleteData() {
        if (allData != null) {// Check if data is already cached
            return allData;
        }
        synchronized (GetData.class) {// Ensure thread safety
            if (allData == null) {// Double-check inside synchronized block
                Map<String, List<Object[]>> tempMap = new ConcurrentHashMap<>();// Use ConcurrentHashMap for thread-safe operations
                String filePath = System.getProperty("user.dir") + "//src//main//java//Resources//TestData.xlsx";

                try (FileInputStream fis = new FileInputStream(filePath);
                     XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                    int numberOfSheets = workbook.getNumberOfSheets();
                    for (int i = 0; i < numberOfSheets; i++) {
                        if (workbook.getSheetName(i).equalsIgnoreCase("UserTestData")) {
                            XSSFSheet sheet = workbook.getSheetAt(i);
                            int rowCount = sheet.getPhysicalNumberOfRows();
                            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

                            for (int j = 1; j < rowCount; j++) {
                                XSSFRow row = sheet.getRow(j);
                                if (row == null) continue;

                                String scenarioName = formatter.formatCellValue(row.getCell(0)).trim();
                                Object[] rowData = new Object[colCount];

                                for (int k = 0; k < colCount; k++) {
                                    rowData[k] = formatter.formatCellValue(row.getCell(k));
                                }

                                tempMap.computeIfAbsent(scenarioName, key -> new ArrayList<>()).add(rowData);
                            }
                            break; // Sheet found and processed, exit loop
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading the Excel file: " + e.getMessage());
                    e.printStackTrace();
                }

                allData = Collections.unmodifiableMap(tempMap);// Make the map unmodifiable to prevent further changes
            }
        }
        return allData;
    }
    
    private static Map<String, List<Object[]>> cachedData;

    public static void initialize() {
        // This reads Excel and caches data, only once
        cachedData = GetData.getCompleteData();
        //System.out.println("cachedData- "+cachedData);
    }

    public static List<Object[]> getScenarioData(String scenarioName) {
        if (cachedData == null) {
            throw new IllegalStateException("Test data not fetched fromt the excel. Call initialize() first.");
        }
        
        List<Object[]> scenarioData = cachedData.get(scenarioName);// Fetch the list of rows for the given scenario name
        
        if (scenarioData == null) {
            throw new IllegalArgumentException("No data found for scenario: " + scenarioName);
        }
        
        System.out.println("Data fetched for " + scenarioName + ": " + scenarioData.get(0)[0]+" "+scenarioData.get(0)[1]+" "+scenarioData.get(0)[2]+" "+scenarioData.get(0)[3]+" "+scenarioData.get(0)[4]+" "+scenarioData.get(0)[5]);// Debugging line to print the first element of the scenario data
        
        return scenarioData;
    }
}
