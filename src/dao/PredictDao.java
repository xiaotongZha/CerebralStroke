package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import java.nio.file.Paths;

public class PredictDao {
    private String url = "jdbc:mysql://101.37.17.111:3306/CerebralStroke?useSSL=false&serverTimezone=UTC";
    private String user = "zxt";
    private String password = "123456";
    private String outPathDir = Paths.get(System.getProperty("user.dir"),"py", "rawdata").toString();
    public void generateFirstExamCSV() {
        exportTableToCSV(
                "SELECT * FROM FirstExam ORDER BY patient_id ASC",
                Paths.get(outPathDir,"FirstExam.csv").toString()
        );
    }

    public void generateOtherExamCSV() {
        exportTableToCSV(
                "SELECT * FROM OtherExam ORDER BY patient_id ASC, exam_no ASC",
                Paths.get(outPathDir,"OtherExam.csv").toString()
        );
    }

    public void generatePatientExamCSV() {
        exportTableToCSV(
                "SELECT * FROM Patient ORDER BY id ASC",
                Paths.get(outPathDir,"Patient.csv").toString()
        );
    }

    private void exportTableToCSV(String sql_query, String outputFilePath){
        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql_query);
                FileWriter csvWriter = new FileWriter(outputFilePath)
        ) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // 写入表头
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.append(meta.getColumnName(i));
                if (i < columnCount) csvWriter.append(",");
            }
            csvWriter.append("\n");

            // 写入数据
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null) value = value.replaceAll(",", " "); // 防止逗号导致错列
                    csvWriter.append(value != null ? value : "");
                    if (i < columnCount) csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            System.out.println("导出成功: " + outputFilePath);
        } catch (SQLException | IOException e) {
            System.err.println("导出失败: " + sql_query);
            e.printStackTrace();
        }
    }
}
