import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeGenerator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("請輸入mermaid檔案名稱");           
        }
        else {
            // get input
            String fileName = args[0];
            String mermaidCode = "";
            System.out.println("File name: " + fileName);

            FileReader mermaidCodeReader = new FileReader();
            mermaidCode = mermaidCodeReader.read(fileName);
            classes = Parser.splitByClass(mermaidCode);
            System.out.println(classes);
       }
    }
}
class FileReader {
    public String read(String fileName){
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
        }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
            return "";
        }
        return mermaidCode;
    }
}
class Parser {
    public static String splitByClass(String input){
        if (input.matches("(.*)(a)(.*)(bb)(.*)"))
            System.out.println("Y");
        return input;
    }
}

class Printer {
    public String printCode(int public, String variable){
        System.out.println("printer test")
    }
}
