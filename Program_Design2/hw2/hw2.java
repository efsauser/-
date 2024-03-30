import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.lang.String;
import java.util.regex.*;

public class CodeGenerator {
    public static void main(String[] args) {
        //初始化
        myParser lineParser = new myParser();
        myFileWriter CodeWriter = new myFileWriter();
        String mermaidLine = "";
        String lineCategory = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            // 讀取 "classDiagram" 然後不做任何事
            mermaidLine = reader.readLine();
            // 一行一行讀取文件(借用RegExp的內容)
            while ((mermaidLine = reader.readLine()) != null){
                // Parser.lineTest(mermaidLine);
                lineCategory = lineParser.classifier(mermaidLine);
                if(lineCategory.equals("class")){
                    // 如果不是第一個class，就在前一個class寫下右大括號然後建立新的class檔
                    if(!CodeWriter.fileName.equals("(>_<)")){
                        CodeWriter.writeRightBrace(lineParser.className);
                    }
                    lineParser.classParser(mermaidLine);
                    CodeWriter.classWrite(lineParser.className);
                }else{
                    if(lineCategory.equals("attribute")){
                        String security = Pattern.matches(".*: \\+.*", mermaidLine)? "public": "private";
                        lineParser.attributeParser(mermaidLine);
                        CodeWriter.attributeWrite( lineParser.className, lineParser.type, 
                                                   lineParser.name, security);
                    }else if(lineCategory.equals("getter")){
                        lineParser.getterParser(mermaidLine);
                        CodeWriter.getterWrite( lineParser.className, lineParser.type, 
                                                lineParser.name);
                    }else if(lineCategory.equals("setter")){
                        lineParser.setterParser(mermaidLine);
                        CodeWriter.setterWrite( lineParser.className, lineParser.type, 
                                                lineParser.name, lineParser.parameter);
                    }else if(lineCategory.equals("method")){
                        String security = Pattern.matches(".*: \\+.*", mermaidLine)? "public": "private";
                        lineParser.methodParser(mermaidLine);
                        CodeWriter.methodWrite( lineParser.className, lineParser.type, 
                                                lineParser.name, lineParser.parameter, security);
                    }else if(lineCategory.equals("empty")){
                        continue;
                    }
                }
                // for debug
                // System.err.println("{"+mermaidLine+"}"+
                //                 " className:" +lineParser.className+
                //                 "| category:" +lineCategory+
                //                 "| type:"+lineParser.type+
                //                 "| name:"+lineParser.name);
            }
            // 讀到EOF也要寫下右大括號
            CodeWriter.writeRightBrace(lineParser.className);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class myParser{
    public String className;
    public String type; // identifierName
    public String name; // identifierType
    public String parameter; // parameter of setter or simple method(不包含setter或getter) 

    public String classifier(String inputLine){
        if(Pattern.matches(".*class .*", inputLine)){
            return "class";
        }else{
            if(Pattern.matches(".*get.*", inputLine)){
                return "getter";
            }else if(Pattern.matches(".*set.*", inputLine)){
                return "setter";
            }else if(Pattern.matches(".*\\(.*", inputLine)){
                return "method";
            }else if(inputLine.length()>5){
                return "attribute";
            }else{
                return "empty";
            }
        }
    }

    public void classParser(String inputLine){
        String[] words = inputLine.split(" ");
        className = words[words.length-1];
    }

    public void attributeParser(String inputLine){
        String[] words = inputLine.split(" ");
        type = words[words.length-2];
        name = words[words.length-1];
        type =  type.replace("+", "");
        type =  type.replace("-", "");
    }

    public void getterParser(String inputLine){
        String[] words = inputLine.split(" ");
        type = words[words.length-1];
        name = words[words.length-2];
        // 切出getXXX的XXX
        name = name.substring(4);
    }

    public void setterParser(String inputLine){
        // 切出setXXX的XXX <- 為什麼VScode會覺得這行註解有問題? ( XXX Java(536871362) )
        String[] temp1 = inputLine.split("\\+");
        String[] temp2 = temp1[temp1.length-1].split("\\(");
        type = "void";
        name = temp2[0];
        // 切出parameter，整個括號裡都切出來
        temp1 = inputLine.split("\\(");
        temp2 = temp1[temp1.length-1].split("\\)");
        parameter = temp2[0];
    }

    public void methodParser(String inputLine){
        String[] words = inputLine.split(" ");
        String[] temp1 = inputLine.split("\\+");
        String[] temp2 = temp1[temp1.length-1].split("\\(");
        // 切出method的name
        if(Pattern.matches(".*\\).*", words[words.length-1])){
            // 如果method的)後「竟然」沒有東西的話，則視為要return void
            // 六個測資都沒有這個情況，難道陷阱藏在隱藏測資裡?
            type = "void";
        }else{
            type = words[words.length-1];
        }
        name = temp2[0];
        // 切出parameter，整個括號裡都切出來
        temp1 = inputLine.split("\\(");
        temp2 = temp1[temp1.length-1].split("\\)");
        parameter = temp2[0];
    }
}

class myFileWriter{
    // 用來標記是不是正在寫入第一個class
    // 有趣又有效，對吧?
    public String fileName = "(>_<)";

    public void writeRightBrace(String className){
        try {
            String fileName = className + ".java";
            File file = new File(fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("}\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void classWrite(String className){
        try{
            fileName = className + ".java";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("public class " + className + " {\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void attributeWrite( 
    String className, String attributeType, String attributeName, String security){
        try{
            fileName = className + ".java";
            File file = new File(fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("\s\s\s\s"+ security + " " + attributeType + " " + attributeName + ";\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getterWrite( 
    String className, String getterType, String getterName){
        try{
            fileName = className + ".java";
            File file = new File(fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                // 先經歷一個麻煩的首字母轉小寫過程
                String firstLetter = getterName.substring(0,1);
                String lowerCaseLetter = firstLetter.toLowerCase();
                String get = lowerCaseLetter + getterName.substring(1);
                getterName = getterName.substring(0,getterName.length()-2);
                get = get.substring(0,get.length()-2);
                bw.write("\s\s\s\spublic" + " " + getterType + " get" + getterName + "() {\n" 
                        + "\s\s\s\s\s\s\s\sreturn " + get + ";\n\s\s\s\s}\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setterWrite( 
    String className, String setterType, String setterName, String parameter){
        try{
            fileName = className + ".java";
            File file = new File(fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                String[] temp = parameter.split(" ");
                String set = temp[temp.length - 1];
                bw.write("\s\s\s\spublic void " + setterName +"("+ parameter + ") {\n"
                        + "\s\s\s\s\s\s\s\sthis." + set + " = " + set +";\n\s\s\s\s}\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 都寫完了才發現是不是這裡可以繼承自setterWrite就好
    public void methodWrite( 
    String className, String methodType, String methodName, String parameter, String security){
        try{
            fileName = className + ".java";
            File file = new File(fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("\s\s\s\spublic "+ methodType + " " + methodName +"("+ parameter + ") ");
                if(methodType.equals("int")){
                    bw.write("{return 0;}\n");
                }else if(methodType.equals("boolean")){
                    bw.write("{return false;}\n");
                }else if(methodType.equals("String")){
                    bw.write("{return \"\";}\n");
                }else if(methodType.equals("void")){
                    bw.write("{;}\n");
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
