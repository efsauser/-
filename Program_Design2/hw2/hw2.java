import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

import java.nio.file.Files;
import java.nio.file.Paths;
// import java.util.ArrayList;
import java.util.regex.*;

public class CodeGenerator {
    public static void main(String[] args) {
        //初始化
        myParser Parser = new myParser();
        myFileWriter CodeWriter = new myFileWriter();
        String mermaidLine = "";
        String className = "";
        boolean isClass = false;
        boolean isPublic = false;
        boolean isMethod = false;
        boolean isFileStart = true;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            while ((mermaidLine = reader.readLine()) != null) {
                // 讀取文件的一行(借用RegExp的內容)
                Parser.lineTest(mermaidLine);
                className = Parser.className;
                isClass = Parser.isClass;
                isPublic = Parser.isPublic;
                isMethod = Parser.isMethod;
                System.err.println("{"+mermaidLine+"} className: "+className+"| is class:"+isClass+"| is public:"+isPublic+"| is Method:"+ isMethod);
                // 寫入文件
                if(isClass==true && isFileStart==false) {
                    CodeWriter.writeRightBrace(className); 
                }
                CodeWriter.write(className, isClass, isPublic, isMethod);
                isFileStart = false;
            }  
            CodeWriter.writeRightBrace(className);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }
}

class myParser{
    public String className;
    public boolean isClass;
    public boolean isPublic;
    public boolean isMethod;

    public void lineTest(String input){
        // Is class declaration?
        if(Pattern.matches(".*class .*", input)){//注意regex有一個空格
            String[] words = input.split(" ");
            this.className = words[1];
            this.isClass = true;
            this.isPublic = false;
            this.isMethod = false;
        }
        // Not class declaration
        else{
            this.isClass = false;
            // Is public?
            if(Pattern.matches(".*+.*", input)){
                this.isPublic = true;
            }else{
                this.isPublic = false;
            }
            // Is a Method?
            if(Pattern.matches(".*\\(.*", input)){
                this.isMethod = true;
            }else{
                this.isMethod = false;
            }
        }
    }
}

class myFileWriter{
    public void write(String className, boolean isClass, boolean isPublic, boolean isMethod){
        try {
            String fileName = className + ".java";
            String codeLine = "";
            if(isClass){
                codeLine = "public class " + className + " {\n" ;
            }else{
                if (isPublic){
                    codeLine += "   public";
                }else{
                    codeLine += "   private";
                }
            }
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(codeLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRightBrace(String className){
        System.out.println("write { into "+ className + ".java");
        try {
            String fileName = className + ".java";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("}abckdar\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class myFileReader{// 我很可能不會用到這個
    public String read(String[] input){
        if (input.length == 0) {
            System.err.println("請輸入檔案名稱");
            return "NO INPUT ERROR";
        }
        String fileName = input[0];
        System.out.println("File name: " + fileName);
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
            return mermaidCode;
        }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
            return "CANNOT READ FILE";
        }
    }
}
