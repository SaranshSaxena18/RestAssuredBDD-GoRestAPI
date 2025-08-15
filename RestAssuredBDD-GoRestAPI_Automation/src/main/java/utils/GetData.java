/*
 * package utils;
 * 
 * import java.io.FileInputStream; import java.io.FileNotFoundException; import
 * java.io.IOException; import java.util.Map; import
 * java.util.concurrent.ConcurrentHashMap;
 * 
 * import org.apache.poi.ss.usermodel.DataFormatter; import
 * org.apache.poi.xssf.usermodel.XSSFRow; import
 * org.apache.poi.xssf.usermodel.XSSFSheet; import
 * org.apache.poi.xssf.usermodel.XSSFWorkbook; import
 * org.testng.annotations.DataProvider;
 * 
 * public class GetData { private static volatile Map<String, Object[]>
 * cachedScenarioData; static DataFormatter formatter = new
 * DataFormatter();//create a DataFormatter instance to format the cell values
 * public static Map<String, Object[]> getUserData() { if (cachedScenarioData !=
 * null) { return cachedScenarioData; } synchronized (GetData.class) {// Ensure
 * thread safety // Double-check inside synchronized block if
 * (cachedScenarioData != null) { return cachedScenarioData; }
 * cachedScenarioData = new ConcurrentHashMap<>(); String filePath =
 * System.getProperty("user.dir") +
 * "//src//main//java//Resources//TestData.xlsx";//get the file path of the
 * excel file //try-with-resources block to ensure the file is automatically
 * closed after use try(FileInputStream fis = new
 * FileInputStream(filePath);//create a FileInputStream to read the file
 * XSSFWorkbook workbook = new XSSFWorkbook(fis))//create a XSSFWorkbook
 * instance to read the excel file { int numberOfSheets =
 * workbook.getNumberOfSheets();//get the number of sheets in the workbook for
 * (int i = 0; i < numberOfSheets; i++) {//loop through each sheet if
 * (workbook.getSheetName(i).equalsIgnoreCase("UserTestData")) {//check if the
 * sheet name is "UserTestData" XSSFSheet sheet = workbook.getSheetAt(i);//get
 * the sheet int rowCount = sheet.getPhysicalNumberOfRows();//get the number of
 * rows in the sheet int colCount =
 * sheet.getRow(0).getPhysicalNumberOfCells();// get the number of columns in
 * the sheet Object[][] data = new Object[rowCount - 1][colCount];//create a 2D
 * array to store the data for (int j = 1; j < rowCount; j++) { XSSFRow row =
 * sheet.getRow(j); if (row == null) continue; String scenarioName =
 * formatter.formatCellValue(row.getCell(0)).trim(); for (int k = 0; k <
 * colCount; k++) { data[j - 1][k] = formatter.formatCellValue(row.getCell(k));
 * } cachedScenarioData.put(scenarioName, data);//put the data into the map with
 * the scenario name as the key }
 * 
 * 
 * } } } catch (IOException e) {
 * System.out.println("Error reading the Excel file: " + e.getMessage());
 * e.printStackTrace(); }
 * 
 * } return cachedScenarioData; } }
 */
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
        System.out.println("cachedData- "+cachedData);
    }

    public static List<Object[]> getScenarioData(String scenarioName) {
        if (cachedData == null) {
            throw new IllegalStateException("Test data not initialized. Call initialize() first.");
        }
        
        List<Object[]> scenarioData = cachedData.get(scenarioName);
        
        if (scenarioData == null) {
            throw new IllegalArgumentException("No data found for scenario: " + scenarioName);
        }
        
        System.out.println("First element in " + scenarioName + ": " + scenarioData.get(0)[0]);
        
        return scenarioData;
    }
}
