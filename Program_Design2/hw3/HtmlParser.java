import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
// import javax.lang.model.util.Elements;
import java.util.stream.Collectors; 
import java.lang.Math;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
// import org.jsoup.select.*;

public class HtmlParser {
    public static void main(String[] args){
        myFileWriter writer = new myFileWriter();
        myFileReader reader = new myFileReader();
        myCalculator cal = new myCalculator();
        int mode = Integer.parseInt(args[0]);
        int task = Integer.parseInt(args[1]);
        String stockName = "";
        int start = 0;
        int end = 0;
        if(task != 0){
            stockName = args[2];
            start = Integer.parseInt(args[3]);
            end = Integer.parseInt(args[4]);
        }
        reader.readAllData();
        if(mode == 0){
            try {
                Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
                // System.out.println(doc.title());
                Element rowName = doc.select("tr").get(0);
                Element rowPrice = doc.select("tr").get(1);
                writer.mode0Write(rowName.text(), rowPrice.text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (mode == 1){
            if(task == 0){
                writer.task0Write();
            }else if(task == 1){
                List<Float> resultList = new ArrayList<Float>();
                resultList = cal.movingAvg(stockName, reader.nameToPrice.get(stockName), start, end);
                writer.task1Write(stockName, resultList, start, end);
            }else if(task == 2){
                float result = cal.sigma(stockName, reader.nameToPrice.get(stockName), start, end);
                writer.task2Write(stockName, result, start, end);
            }
        }

    }
}

class myFileReader{
    HashMap<Integer, List<Float>> dayToPrice = new HashMap<>();
    HashMap<String, List<Float>> nameToPrice = new HashMap<>();

    public void readAllData(){
        String inputFileName = "data.csv";
        String line = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFileName));
            line = br.readLine();
            String[] rowName = line.split(",");
            int nameNumber = rowName.length;

            // read data into dayToPrice
            Integer day=1;
            while ((line = br.readLine()) != null) {
                String[] rowPrice = line.split(",");
                //就是這一行讓我放棄challenge point
                List<Float> priceList = Arrays.stream(rowPrice).map(Float::valueOf).collect(Collectors.toList());

                dayToPrice.put(day,priceList);
                day++;
            }

            // read data into nameToPrice
            for(int i=0; i<nameNumber; i++){
                List<Float> priceTemp = new ArrayList<>();
                for(day=1; day<=30; day++){
                    Float dayPrice = dayToPrice.get(day).get(i);
                    priceTemp.add(dayPrice);
                }
                nameToPrice.put(rowName[i], priceTemp);
            }
            
        }catch (IOException e) {
            e.printStackTrace();
        }

        // for debug
        // System.out.println(dayToPrice);
        // System.out.println(nameToPrice);
    }

}

class myCalculator{
    // 計算移動平均
    public List<Float> movingAvg(String name, List<Float> priceList, int start, int end){
        List<Float> avgPrice= new ArrayList<Float>();
        float tempSum = 0.0f;
        float tempAvg = 0.0f;
        for(int i=start-1; i<end-4; i++){
            tempSum = 0.0f;
            for(int j=i; j<i+5; j++){
                tempSum += priceList.get(j);
            }
            tempAvg = tempSum*0.2f;
            tempAvg = Math.round(tempAvg*100.0f)/100.0f; // 四捨五入，第二個逼我放棄challenge point的地方
            avgPrice.add(tempAvg);
        }
        return avgPrice;
    }

    // 計算標準差
    public float sigma(String name, List<Float> priceList, int start, int end){
        float sigma = 0.0f;
        float tempSum = 0.0f;
        float tempAvg = 0.0f;
        float RSS = 0.0f; //Residual sum of squares 殘差平方和
        int n = end-start+1;
        for(int i=start-1; i<end; i++){
            tempSum += priceList.get(i);
        }
        tempAvg = tempSum/n;
        for(int i=start-1; i<end; i++){
            RSS += (priceList.get(i)-tempAvg)*(priceList.get(i)-tempAvg);
        }
        sigma = (float)Math.sqrt(RSS/(n-1));
        sigma = Math.round(sigma*100.0f)/100.0f; // 四捨五入
        return sigma;
    }
}

class myFileWriter{
    public void mode0Write(String rowName, String rowData){
        String outputFileName = "data.csv";
        try{
            File file = new File(outputFileName);
            if (!file.exists()){
                // 好像FileWriter會幫我create file，所以要寫在if裡面
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.append(rowName+"\n");
                }
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(rowData+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public void task0Write(){
        String inputFileName = "data.csv";
        String outputFileName = "output.csv";
        String line = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFileName));
            String firstRow = br.readLine();
            while((line = br.readLine())!= null){
                File file = new File(outputFileName);
                if (!file.exists()){
                    // 好像FileWriter會幫我create file，所以要寫在if裡面
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.append(firstRow+"\n");
                    }
                }
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.append(line+"\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task1Write(String stockName, List<Float> avgPriceList, int start, int end){
        String outputFileName = "output.csv";
        try{
            File file = new File(outputFileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(stockName+","+start+","+end+"\n");
                int counter = 0;
                for(float price: avgPriceList){
                    writer.append(""+price); // convert float to String
                    if(counter < avgPriceList.size()-1){
                        writer.append(","); // 這裡在處理有一點麻煩的逗號
                    }
                    counter++;
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task2Write(String stockName, float sigma, int start, int end){
        String outputFileName = "output.csv";
        try{
            File file = new File(outputFileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(stockName+","+start+","+end+"\n");
                writer.append(""+sigma+"\n"); // convert float to String
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
