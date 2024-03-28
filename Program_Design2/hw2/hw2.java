import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.regex.*;
// import java.util.ArrayList;

public class CodeGenerator {
    public static void main(String[] args) {
        //初始化
        myParser Parser = new myParser();
        myFileWriter CodeWriter = new myFileWriter();
        String mermaidLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            // 讀取"classDiagram"然後不做任何事
            mermaidLine = reader.readLine();
            // 一行一行讀取文件(借用RegExp的內容)
            while ((mermaidLine = reader.readLine()) != null) {
                Parser.lineTest(mermaidLine);
                System.err.println("{"+mermaidLine+"}"+
                                " className:" +Parser.className+
                                "| is class:" +Parser.isClass+
                                "| is public:"+Parser.isPublic+
                                "| is Method:"+Parser.isMethod);
                // 寫入文件
                CodeWriter.write(Parser.className, Parser.isClass, 
                                 Parser.isPublic, Parser.isMethod, 
                                 Parser.idType, Parser.idName,
                                 Parser.get, Parser.set);
            }  
            // CodeWriter.writeRightBrace(Parser.className);
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
    public boolean isPrivate;
    public boolean isMethod; // 我知道getter跟setter也是一種Method，但這個變數不包含這兩種
    public boolean isGetter;
    public boolean isSetter;
    public String idType; // identifierName
    public String idName; // identifierType
    public String get;
    public String set;

    public void classifier(String inputLine){
        String[] words = inputLine.split(" ");
        for(String word:words){
            this.isClass = Pattern.matches("class*", word)? true:false;
            this.isPublic = Pattern.matches("\\+.*", word)? true:false;
            this.isPrivate = Pattern.matches("\\-.*", word)? true:false;
            this.isMethod = Pattern.matches(".*\\(.*", word)? true:false;
            this.isGetter = Pattern.matches(".*get.*", word)? true:false;
            this.isSetter = Pattern.matches(".*set.*", word)? true:false;
        }
    }

    public void classParser(String inputLine){
        String[] words = inputLine.split(" ");
        this.className = words[words.length-1];
    }

    public void publicParser(String inputLine){
        String[] words = inputLine.split(" ");
        for(String word:words){
            Pattern.matches(".*: \\+.*", word)
        }
        this.idName = words[words.length-1];
    }


    public void lineTest(String input){
        // Is class declaration?
        if(Pattern.matches(".*class .*", input)){//注意regex有一個空格
            String[] words = input.split(" ");
            this.className = words[words.length-1];
            isClass = true;
        }
        // Not class declaration
        else{
            this.isClass = false;
            // Is public?
            if(Pattern.matches(".*: \\+.*", input)){
                this.isPublic = true;
            }else{
                this.isPublic = false;
            }
            String[] words = input.split(" ");
            // Is a Method?
            if(Pattern.matches(".*\\(.*", input)){
                this.isMethod = true;
                this.idType = words[words.length-1];
                this.idName = words[words.length-2];
            }else{
                this.isMethod = false;
                this.idType = words[words.length-2];
                this.idName = words[words.length-1];
            }
            // Is a Getter?
            if(Pattern.matches(".*get.*", input)){
                this.get = idName.substring(4,idName.length()-2).toLowerCase();
                //大小寫問題晚點處理
            }else{
                this.get = "(>_<)";//這是不合法的變數名稱，用這個有趣又有效
            }
            // Is a Setter?
            if(Pattern.matches(".*set.*", input)){
                this.set = idName.substring(4,idName.length()-2).toLowerCase();
                //大小寫問題晚點處理
            }else{
                this.set = "(>_<)";
            }
        }
    }
}

class myFileWriter{
    public void write(  String className, boolean isClass, 
                        boolean isPublic, boolean isMethod, 
                        String idType, String idName,
                        String get, String set){
        String fileName = "";
        String mermaidCode = "";
        try {
            if(className!=null){
                fileName = className + ".java";
            }
            if(isClass){
                mermaidCode = "public class " + className + " {\n" ;
            }else{
                if(isPublic){
                    mermaidCode += "\tpublic";
                    idType = idType.replace("+", "");
                    idName = idName.replace("+", "");
                }else{
                    mermaidCode += "\tprivate";
                    idType = idType.replace("-", "");
                    idName = idName.replace("+", "");
                }
                mermaidCode += " " + idType + " " + idName;
                if(get.equals("(>_<)")!=true){
                    mermaidCode += " {\n\t\treturn " + get + ";\n\t}\n";
                    System.out.println("is getter!!");
                }else{
                    mermaidCode += ";\n";
                }
                if(set.equals("(>_<)")!=true){
                    mermaidCode += " {\n\t\tpublic void  " + get + ";\n\t}\n";
                    System.out.println("is getter!!");
                }else{
                    mermaidCode += ";\n";
                }
            }
            // mermaidCode += "}\n";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(mermaidCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRightBrace(String className){
        System.out.println("write } into "+ className + ".java");
        try {
            String fileName = className + ".java";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("}\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
