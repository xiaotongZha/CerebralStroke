package service;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


public class PredictService {

    public String getMetricsPlotPath() {
        return Paths.get(System.getProperty("user.dir"),"py","sharedata", "pic", "four_metrics.png").toString();
    }

    public String getROCPlotPath() {
        return Paths.get(System.getProperty("user.dir"),"py", "sharedata","pic", "roc_curve.png").toString();
    }

    public String getProbabilityReport() {
        String patientIdPath = Paths.get(System.getProperty("user.dir"), "py", "sharedata", "testset_name.txt").toString();
        String patientProbaPath = Paths.get(System.getProperty("user.dir"), "py", "sharedata", "testset_predict.txt").toString();

        try {
            // 读取两个文件的第一行
            String idsLine = Files.readAllLines(Paths.get(patientIdPath)).get(0).trim();
            String probLine = Files.readAllLines(Paths.get(patientProbaPath)).get(0).trim();

            String[] ids = idsLine.split("\\s+");
            String[] probas = probLine.split("\\s+");

            if (ids.length != probas.length) {
                return "错误：患者数与预测概率数不一致";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                sb.append(ids[i]).append(": ").append(probas[i]);

                if ((i+1) % 5 == 0) {
                    sb.append("\n");
                } else {
                    sb.append("    ");
                }
            }

            return sb.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "读取文件出错: " + e.getMessage();
        }
    }

    public String getWarningPatients() {
        String patientIdPath = Paths.get(System.getProperty("user.dir"), "py", "sharedata", "testset_name.txt").toString();
        String patientProbaPath = Paths.get(System.getProperty("user.dir"), "py", "sharedata", "testset_predict.txt").toString();

        try {
            // 读取两个文件的第一行
            String idsLine = Files.readAllLines(Paths.get(patientIdPath)).get(0).trim();
            String probLine = Files.readAllLines(Paths.get(patientProbaPath)).get(0).trim();

            String[] ids = idsLine.split("\\s+");
            String[] probas_str = probLine.split("\\s+");
            double[] probas=Arrays.stream(probas_str)
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            if (ids.length != probas.length) {
                return "错误：患者数与预测概率数不一致";
            }

            StringBuilder sb = new StringBuilder();
            int cnt=0;
            for (int i = 0; i < ids.length; i++) {
                if(probas[i]>0.4) {
                    sb.append(ids[i]);
                    cnt+=1;
                    if (cnt % 10 == 0) {
                        sb.append("\n");
                    } else {
                        sb.append("    ");
                    }
                }
            }

            return sb.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "读取文件出错: " + e.getMessage();
        }
    }

    public String getTrainLabels(){
        String labelPath = Paths.get(System.getProperty("user.dir"), "py", "sharedata", "trainset_label.txt").toString();
        try {
            List<String> lines = Files.readAllLines(Paths.get(labelPath));
            String pos_line=lines.get(0).trim();
            String neg_line=lines.get(1).trim();

//            String[] pos_ids = pos_line.split("\\s+");
//            String[] neg_ids = neg_line.split("\\s+");

            return "发病患者：\n" + pos_line + "\n" +
                    "未发病患者：\n" + neg_line + "\n";

        } catch (IOException e) {
            e.printStackTrace();
            return "读取文件出错: " + e.getMessage();
        }
    }
}
