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
import java.text.DecimalFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
// import org.jsoup.select.*;

public class HtmlParser {
    public static void main(String[] args){
        myFileWriter writer = new myFileWriter();
        myFileReader reader = new myFileReader();
        myCalculator cal = new myCalculator();
        int mode = Integer.parseInt(args[0]);
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
            reader.readAllData();
            int task = Integer.parseInt(args[1]);
            if(task == 0){
                writer.task0Write();
            }else{
                String stockName = args[2];
                int start = Integer.parseInt(args[3]);
                int end = Integer.parseInt(args[4]);
                if(task == 1){
                    List<Float> resultList = new ArrayList<Float>();
                    resultList = cal.movingAvg(stockName, reader.nameToPrice.get(stockName), start, end);
                    writer.task1Write(stockName, resultList, start, end);
                }else if(task == 2){
                    float result = cal.sigma(reader.nameToPrice.get(stockName), start, end);
                    writer.task2Write(stockName, result, start, end);
                }else if(task == 3){
                    HashMap<Float, String> result = cal.top3(reader.nameToPrice, start, end);
                    writer.task3Write(result, start, end);
                }else if(task == 4){
                    float[] result = cal.regLine(reader.nameToPrice.get(stockName), start, end);
                    writer.task4Write(stockName, result, start, end);
                }
            }
        }

    }
}

class myFileReader{
    HashMap<Integer, List<Float>> dayToPrice = new HashMap<>();
    HashMap<String, List<Float>> nameToPrice = new HashMap<>();

    public void readAllData(){
        try{
            String fileName = "data.csv";
            String line = "";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
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
    public float sigma(List<Float> priceList, int start, int end){
        float sigma = 0.0f;
        float tempSum = 0.0f;
        float tempAvg = 0.0f;
        float RSS = 0.0f; //Residual sum of squares 殘差平方和
        int n = end-start+1;
        for(int day=start; day<=end; day++){
            tempSum += priceList.get(day-1);
        }
        tempAvg = tempSum/n;
        for(int day=start; day<=end; day++){
            RSS += (priceList.get(day-1)-tempAvg)*(priceList.get(day-1)-tempAvg);
        }
        sigma = (float)Math.sqrt(RSS/(n-1));
        sigma = Math.round(sigma*100.0f)/100.0f; // 四捨五入
        return sigma;
    }

    // 計算標準差top3
    public HashMap<Float, String> top3(HashMap<String, List<Float>> nameToPrice, int start, int end){
        HashMap<String, Float> tempSigmaMap = new HashMap<>();
        HashMap<Float, String> top3Map = new HashMap<>(); // 注意key跟value的順序，這是為了排序方便
        for(String name:nameToPrice.keySet()){
            float tempSigma = this.sigma(nameToPrice.get(name), start, end);
            tempSigmaMap.put(name, tempSigma);
        }
        for(int i=0;i<3;i++){ // put top3 into return map
            String maxName = Collections.max(tempSigmaMap.entrySet(), Map.Entry.comparingByValue()).getKey();
            Float maxValue = tempSigmaMap.get(maxName);
            top3Map.put(maxValue, maxName);
            tempSigmaMap.replace(maxName, -1.0f); // 可能改成remove也可以?
        }
        // System.out.println(top3Map);
        return top3Map;
    }

    // 計算迴歸直線
    public float[] regLine(List<Float> priceList, int start, int end){
        float priceSum = 0.0f;
        float priceAvg = 0.0f;
        float daySum = 0.0f;
        float dayAvg = 0.0f;
        float cov = 0.0f; // 共變數，即公式中b1的分子
        float var = 0.0f; // 變異數，即公式中b1的分母
        int n = end-start+1;
        for(int day=start; day<=end; day++){
            priceSum += priceList.get(day-1);
            daySum += day;
        }
        priceAvg = priceSum/n;
        dayAvg = daySum/n;

        for(int day=start; day<=end; day++){
            cov += (day-dayAvg)*(priceList.get(day-1)-priceAvg);
            var += (day-dayAvg)*(day-dayAvg);
        }
        float b1 = cov/var;
        float b0 = priceAvg-b1*dayAvg;
        float[] b = new float[2];
        b0 = Math.round(b0*100.0f)/100.0f; // 四捨五入
        b1 = Math.round(b1*100.0f)/100.0f; // 四捨五入
        b[0] = b0;
        b[1] = b1;
        return b;
    }
}

class myFileWriter{
    public void mode0Write(String rowName, String rowData){
        String fileName = "data.csv";
        try{
            File file = new File(fileName);
            if (!file.exists()){
                file.createNewFile();
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
                    file.createNewFile();
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
        String FileName = "output.csv";
        try{
            File file = new File(FileName);
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
        String FileName = "output.csv";
        try{
            File file = new File(FileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(stockName+","+start+","+end+"\n");
                writer.append(""+sigma+"\n"); // convert float to String
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task3Write(HashMap<Float, String> top3, int start, int end){
        String FileName = "output.csv";
        try{
            File file = new File(FileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                // 我這個寫法就是賭不可能有兩檔股票的標準差一樣，理論上是這樣吧
                List<Float> values = new ArrayList<>();
                values.addAll(top3.keySet());
                values.sort(Comparator.reverseOrder());
                DecimalFormat df=new DecimalFormat("#.##");
                // 這裡原本是用String.format寫的，但小數位數在搞我
                // (怎麼會有標準差剛好是整數的?)
                writer.append(top3.get(values.get(0))+","+
                              top3.get(values.get(1))+","+
                              top3.get(values.get(2))+","+
                              start+","+end+"\n");
                writer.append(df.format(values.get(0))+","+
                              df.format(values.get(1))+","+
                              df.format(values.get(2))+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task4Write(String stockName, float[] parameter, int start, int end){
        String FileName = "output.csv";
        try{
            File file = new File(FileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(stockName+","+start+","+end+"\n");
                writer.append(""+parameter[1]+","+parameter[0]+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
