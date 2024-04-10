import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
// import java.io.BufferedWriter;
import java.io.IOException;
// import javax.lang.model.util.Elements;
import java.util.stream.Collectors; 
// import java.lang.math.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
// import org.jsoup.select.*;

public class HtmlParser {
    public static void main(String[] args){
        myFileWriter writer = new myFileWriter();
        myFileReader reader = new myFileReader();
        int mode = Integer.parseInt(args[0]);
        int task = Integer.parseInt(args[1]);
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
            writer.mode1Write(task, reader.nameToPrice.keySet(), reader.dayToPrice);
        }

    }
}
class myCalculator{

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
                // List<Float> priceList = new ArrayList<>();
                // for(int i=0; i<rowPrice.length; i++){
                //     priceList.add(Float.parseFloat(rowName[i]));
                // }
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
        System.out.println(dayToPrice);
        System.out.println(nameToPrice);
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
    public void mode1Write(int task, Set<String> rowName, HashMap<Integer, List<Float>> map){
        String outputFileName = "output.csv";
        if(task == 0){
            String line = "";
            try{
                BufferedReader br = new BufferedReader(new FileReader("data.csv"));
                String firstRow = br.readLine();
                while((line = br.readLine())!= null){
                    File file = new File(outputFileName);
                    if (!file.exists()){
                        // 好像FileWriter會幫我create file，所以要寫在if裡面
                        try (FileWriter writer = new FileWriter(file, true)) {
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

        if(task == 1){

        }
    }
}
