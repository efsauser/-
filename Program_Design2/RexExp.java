import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;

public class RegExp {
    
    public static void test1(String s){
        int len = s.length();
        boolean isPalindrome = true;
        for(int i=0; i<len/2; i++){
            if(s.charAt(i) != s.charAt(len-i-1)){
                isPalindrome = false; break;
            }
        }
        if(isPalindrome == true){
            System.out.print("Y,");
        } else{
            System.out.print("N,");
        }
    }

    public static void test2(String s, String str1){
        if(s.contains(str1)){
            System.out.print("Y,");
        } else{
            System.out.print("N,");
        }
    }

    public static void test3(String s, String str2, int n){
        int counter = 0;
        int lenS = s.length();
        int lenStr2 = str2.length();
        String interval = "";
        for(int i=0; i<lenS-lenStr2; i++){
            interval = s.substring(i, i+lenStr2);
            if(interval.equals(str2)){
                counter++;
            }
        }
        if (counter >= n){
            System.out.print("Y,");
        } else{
            System.out.print("N,");
        }
    }

    public static void test4(String s){
        if (s.matches("(.*)(a)(.*)(bb)(.*)")){
            System.out.println("Y");
        } else{
            System.out.println("N");
        }
    }

    public static void main(String[] args) {
        String str1 = args[1];
        String str2 = args[2];
        int s2Count = Integer.parseInt(args[3]);

        //For your testing of input correctness，丟上伺服器記得刪掉這些
        System.out.println("The input file:"+args[0]);
        System.out.println("str1="+str1);
        System.out.println("str2="+str2);
        System.out.println("num of repeated requests of str2 = "+s2Count);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line;
            while ((line = reader.readLine()) != null) {
                //You main code should be invoked here
                line = line.toLowerCase();
                test1(line);
                test2(line, str1);
                test3(line, str2, s2Count);
                test4(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
