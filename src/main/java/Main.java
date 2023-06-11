import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"}; // массив строк, содержащий информацию о предназначении колонок в CVS файле
        String fileName = "data.csv"; //имя CSV
        List<Employee> list = parseCSV(columnMapping, fileName); //список сотрудников
        String json = listToJson(list); //список  в строку в формате JSON
        writeString(json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> allRows = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();

            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            allRows = csv.parse();

        } catch (IOException ioException) {
            ioException.getStackTrace();
        }
        return allRows;
    }

    //
    public static <T> String listToJson(List<T> list) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }


    public static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json");) {
            file.write(json);
            file.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}