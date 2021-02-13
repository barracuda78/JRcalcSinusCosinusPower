package com.javarush.task.task34.task3404;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 
Рекурсия для мат. выражения
*/

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.recurse("sin ( 2 * ( -5 + 1.5 * 4 ) + 28)", 0); //expected output 0.5 6
    }

//    public void recurse(final String expression, int countOperation) {
//        //implement
//        List<Lexeme> lexemes = lexAnalyse(expression);
//
//
//
//        for(Lexeme l : lexemes){
//            System.out.print(l.value + "   ");
//        }
//    }

    public void recurse(final String expression, int countOperation) {
        String expression2 = expression;
        //удалить все пробелы:
        expression2 = expression2.replaceAll(" ", "");
        //привести к нижнему регистру:
        expression2 = expression2.toLowerCase();
        //implement

        // Подсчёт знаков
        if (countOperation == 0){
            String test = expression2;
            //удалить все пробелы:
            test = test.replaceAll(" ", "");
            //привести к нижнему регистру:
            test = test.toLowerCase();


            while (test.contains("sin") || test.contains("cos") || test.contains("tan")){
                test = test.replace("sin","s");
                test = test.replace("cos","c");
                test = test.replace("tan","t");
            }
            for (char d : test.toCharArray()){
                switch (d){
                    case 'c':
                        countOperation++;
                        break;
                    case 's':
                        countOperation++;
                        break;
                    case 't':
                        countOperation++;
                        break;
                    case '*':
                        countOperation++;
                        break;
                    case '/':
                        countOperation++;
                        break;
                    case '^':
                        countOperation++;
                        break;
                    case '+':
                        countOperation++;
                        break;
                    case '-':
                        countOperation++;
                        break;
                }
            }
        }


        List<String> commonArr = new ArrayList<>();
        String s = expression2;
        //удалить все пробелы:
        s = s.replaceAll(" ", "");
        //привести к нижнему регистру:
        s = s.toLowerCase();
        String[] numbers;

        if (expression2.contains("(") && expression2.contains(")")) {
            s = expression2.substring(expression2.lastIndexOf("(") + 1);
            s = s.substring(0, s.indexOf(")"));
        }
        String s2 = s;

        while(s.contains("sin") || s.contains("cos") || s.contains("tan")) {
            if (s.contains("sin-")) {
                s = s.replace("sin-","s-");
            } else if (s.contains("sin")) {
                s = s.replace("sin","s");
            }
            if (s.contains("cos-")) {
                s = s.replace("cos-","c-");
            } else if (s.contains("cos")) {
                s = s.replace("cos", "c");
            }
            if (s.contains("tan-")) {
                s = s.replace("tan-","t-");
            } else if (s.contains("tan")) {
                s = s.replace("tan", "t");
            }
        }
        String s1 = s;

        //"разбиение" строки на элементы (числа и мат. действия) и помещение их в commonArr
        s = s.startsWith("-") ? s.substring(1,s.length()) : s;
        numbers = s.split("[s,c,t/^,/*,//,+,/-]");
        int i = 0;
        for (Character ch : s1.toCharArray()) {
            if (ch == '^' || ch == '*' || ch == '/' || ch == '+' || ch == '-') {
                if (s1.startsWith("-")) {
                    commonArr.add(String.valueOf(ch));
                    commonArr.add(numbers[i]);
                } else {
                    commonArr.add(numbers[i]);
                    commonArr.add(String.valueOf(ch));
                }
                i++;
            }
            if (ch == 's' || ch == 'c' || ch == 't'){
                commonArr.add(String.valueOf(ch));
                commonArr.add(numbers[i]);
                i++;
            }

            if (i == numbers.length - 1 && !s1.startsWith("-")) {
                commonArr.add(numbers[i]);
                break;
            }
        }

        // для случая, когда подряю идут два знака
        if (commonArr.contains("")) {
            for (int j = 0; j < commonArr.size(); j++) {
                if (commonArr.get(j).equals("")) {
                    if (commonArr.get(j+1).equals("-")){
                        double r = - Double.valueOf(commonArr.get(j+2));
                        commonArr.set(j+1,String.valueOf(r));
                        commonArr.remove(j+2);
                    }
                    commonArr.remove(j);
                    j = 0;
                }
            }
        }


        // вычисление sin, cos, tan
        if (commonArr.contains("s") || commonArr.contains("c") || commonArr.contains("t")){
            double x;
            for (int j = 0; j < commonArr.size(); j++) {
                switch (commonArr.get(j)){
                    case "s":
                        if (j > 0 && commonArr.get(j-1).equals("-")) {
                            x = -Math.sin(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        }else x = Math.sin(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        commonArr.set(j,String.valueOf(x));
                        commonArr.remove(j+1);
                        commonArr.set(j,String.valueOf(x));
                        if (j > 0 && commonArr.get(j-1).equals("-")) commonArr.remove(j-1);
                        break;
                    case "c":
                        if (j > 0 && commonArr.get(j-1).equals("-")) {
                            x = -Math.cos(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        }else x = Math.cos(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        commonArr.set(j,String.valueOf(x));
                        commonArr.remove(j+1);
                        commonArr.set(j,String.valueOf(x));
                        if (j > 0 && commonArr.get(j-1).equals("-")) commonArr.remove(j-1);
                        break;
                    case "t":
                        if (j > 0 && commonArr.get(j-1).equals("-")) {
                            x = -Math.tan(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        }else x = Math.tan(Math.toRadians(Double.valueOf(commonArr.get(j + 1))));
                        commonArr.set(j,String.valueOf(x));
                        commonArr.remove(j+1);
                        commonArr.set(j,String.valueOf(x));
                        if (j > 0 && commonArr.get(j-1).equals("-")) commonArr.remove(j-1);
                        break;
                }
            }
        }

        // возведение в степень
        if (commonArr.contains("^")){
            for (int j = 0; j < commonArr.size(); j++) {
                if (commonArr.get(j).equals("^")) {
                    commonArr.set(j - 1, String.valueOf(Math.pow(Double.valueOf(commonArr.get(j - 1)),Double.valueOf(commonArr.get(j + 1)))));
                    commonArr.remove(j + 1);
                    commonArr.remove(j);
                    j = 0;
                }
            }
        }

        // перемножение и деление элементов commonArr
        if (commonArr.contains("*") || commonArr.contains("/")) {
            for (int j = 0; j < commonArr.size(); j++) {
                double z;
                switch (commonArr.get(j)) {
                    case "*":
                        z = Double.valueOf(commonArr.get(j - 1)) * Double.valueOf(commonArr.get(j + 1));
                        commonArr.set(j - 1, String.valueOf(z));
                        commonArr.remove(j + 1);
                        commonArr.remove(j);
                        j = 0;
                        break;
                    case "/":
                        z = Double.valueOf(commonArr.get(j - 1)) / Double.valueOf(commonArr.get(j + 1));
                        commonArr.set(j - 1, String.valueOf(z));
                        commonArr.remove(j + 1);
                        commonArr.remove(j);
                        j = 0;
                        break;
                }
            }
        }

        // Расстановка "минусов"
        if (commonArr.contains("-") || commonArr.contains("+")) {
            for (int j = 0; j < commonArr.size(); j++) {
                double x;
                switch (commonArr.get(j)) {
                    case "-":
                        x = Double.valueOf(commonArr.get(j + 1)) * (-1);
                        commonArr.set(j + 1, String.valueOf(x));
                        commonArr.remove(j);
                        j = 0;
                        break;
                    case "+":
                        x = Double.valueOf(commonArr.get(j + 1));
                        commonArr.set(j + 1, String.valueOf(x));
                        commonArr.remove(j);
                        j = 0;
                        break;
                }
            }
        }

        double res = 0.0;

        // Сложение всех элементов commonArr
        for (String numb : commonArr) res += Double.valueOf(numb);

        if (expression2.contains("(") && expression2.contains(")")) {
            String jp = expression2.replace("(" + s2 + ")", String.valueOf(res));
            recurse(jp, countOperation);
        } else {
            String k = new BigDecimal(res).setScale(2, RoundingMode.HALF_UP).toString();
            k = new DecimalFormat("#.##").format(Double.valueOf(k));
            System.out.println(k.replace(",", ".") + " " + countOperation);
            return;
        }
    }

    public Solution() {
        //don't delete
    }

    public enum LexemeType{
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        SIN,
        COS,
        TAN,
        POW,
        LEFT_BRACE,
        RIGHT_BRACE,
        NUMBER,
        EOF
    }

    public static class Lexeme{
        public LexemeType type;
        public String value;

        public Lexeme(LexemeType type, String value){
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value){
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "Lexeme{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class LexemeBuffer{
        private int pos;
        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes){
            this.lexemes = lexemes;
        }

        public Lexeme next(){
            return lexemes.get(pos++);
        }

        public void back(){
            pos--;
        }

        public int getPos(){
            return pos;
        }
    }

    public static List<Lexeme> lexAnalyse(String expText){
        //удалить все пробелы:
        expText = expText.replaceAll(" ", "");
        //привести к нижнему регистру:
        expText = expText.toLowerCase();

        if(!validityCheck(expText))
            throw new RuntimeException("the given string is not a valid math expression");


        List<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while(pos < expText.length()){
            char c = expText.charAt(pos);
            switch(c){
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACE, c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add((new Lexeme(LexemeType.RIGHT_BRACE, c)));
                    pos++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.PLUS, c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.MINUS, c));
                    pos++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.MULTIPLY, c));
                    pos++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.DIVIDE, c));
                    pos++;
                    continue;
                case 's':
                    if(expText.charAt(pos + 1) == 'i' && expText.charAt(pos + 2) == 'n'){
                        lexemes.add(new Lexeme(LexemeType.SIN, c));
                        pos++;
                        pos++;
                        pos++;
                        continue;
                    }else{
                        throw new RuntimeException("Unexpected character in SIN: " + c);
                    }
                case 'c':
                    if(expText.charAt(pos + 1) == 'o' && expText.charAt(pos + 2) == 's'){
                        lexemes.add(new Lexeme(LexemeType.COS, c));
                        pos++;
                        pos++;
                        pos++;
                        continue;
                    }else{
                        throw new RuntimeException("Unexpected character in COS: " + c);
                    }
                case 't':
                    if(expText.charAt(pos + 1) == 'a' && expText.charAt(pos + 2) == 'n'){
                        lexemes.add(new Lexeme(LexemeType.TAN, c));
                        pos++;
                        pos++;
                        pos++;
                        continue;
                    }else{
                        throw new RuntimeException("Unexpected character in TAN: " + c);
                    }
                case '^':
                    lexemes.add(new Lexeme(LexemeType.POW, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0' || c == '.');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        pos++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    //this method checks if the given statement is valid:
    private static boolean validityCheck(String statement){

        //check if the statement contains any other characters except permitted ones.
        if(!statement.matches("[0-9\\+\\-\\*/\\(\\)\\.sincota]+")){
            return false;
        }

        //check if 'sin' sequence is correct
        if(statement.contains("i")){
            if(!statement.matches(".*sin\\(.+")){
                  System.out.println("УУУУПССС 1 !!!");
                  return false;
            }
            if(statement.matches(".*[^s]i[^n].*")){
                System.out.println("УУУУПССС 2 !!!");
                return false;
            }
            //check if after sin is brace (:
            if(statement.matches(".*sin[^(].*")){
                System.out.println("УУУУПССС 2.2 !!!");
                return false;
            }
        }
        //check if 'cos' sequence is correct
        if(statement.contains("o")){
            if(!statement.matches(".*cos\\(.+")){
                System.out.println("УУУУПССС 3 !!!");
                return false;
            }
            if(statement.matches(".*[^c]o[^s].*")){
                System.out.println("УУУУПССС 4 !!!");
                return false;
            }
            //check if after cos is brace (:
            if(statement.matches(".*cos[^(].*")){
                System.out.println("УУУУПССС 4.2 !!!");
                return false;
            }
        }
        //check if 'tan' sequence is correct
        if(statement.contains("a")){
            if(!statement.matches(".*tan\\(.+")){
                System.out.println("УУУУПССС 5 !!!");
                return false;
            }
            if(statement.matches(".*[^t]a[^n].*")){
                System.out.println("УУУУПССС 6 !!!");
                return false;
            }
            //check if after tan is brace (:
            if(statement.matches(".*tan[^(].*")){
                System.out.println("УУУУПССС 6.2 !!!");
                return false;
            }
        }

        //check if the statement contains ")" prior to "("
        if(statement.matches("[^(]*\\)+.*")){
            return false;
        }

        //check if the statement has amount of ( that doesn't match to the amount of )
        int countOpeningParenthesis = 0;
        int countClosingParenthesis = 0;
        Pattern p1 = Pattern.compile("\\(");
        Pattern p2 = Pattern.compile("\\)");
        Matcher m1 = p1.matcher(statement);
        Matcher m2 = p2.matcher(statement);
        while(m1.find()){
            countOpeningParenthesis++;
        }
        while(m2.find()){
            countClosingParenthesis++;
        }
        if(countClosingParenthesis!=countOpeningParenthesis){
            return false;
        }

        //check if ( and ) go together.
        Pattern p3 = Pattern.compile("\\(\\)");
        Matcher m3 = p3.matcher(statement);

        boolean areTogether = false;
        while(m3.find()){
            areTogether = true;
        }
        if(areTogether){
            return false;
        }

        //if we have "(" in the statement: check if there are parenthesis, there should be correct substatement within (containing at least 2 numbers with one +-*/ sign in between;.
        //doesn't work properly: can find last correct statement and return true; Before that can be uncorrect statements;
        //String statement = "-1.0+((5/8*9)-(44+5.55555)/888.888)-(0.33+)";
        //avoid this situation^ (0.33+)
        //avoid this situation^ (0.33)
        //permit situation (-1.033)

        if(statement.contains("(")){
            boolean correctStatementWithinParenthesis = false;
            boolean correctStatementWithinParenthesis1 = false;
            boolean correctStatementWithinParenthesis2 = false;



            Pattern p4 = Pattern.compile("\\((\\+|\\-)?\\d+([^\\+\\-\\*\\/]*)?(\\+|\\-|\\*|\\/)\\d+(.*)?\\)");
            Matcher m4 = p4.matcher(statement);
            while(m4.find()){
                correctStatementWithinParenthesis = true;
            }
            //avoid this situation: (0.33)
            //avoid this situation: (0.33+)
            //avoid this situation: (+0.33)
            //avoid this situation: (*0.33)
            //avoid this situation: (/0.33)
            //permit situation (-1.033)
//            Pattern p42 = Pattern.compile("\\([\\+\\*\\/]*\\d+(\\.\\d+)?[\\+\\-\\*\\/]*\\)");
//            Matcher m42 = p42.matcher(statement);
//            while(m42.find()){
//                correctStatementWithinParenthesis = false;
//            }
//
//            //permit situation (+0.33) or ((-0.33):
//            Pattern p422 = Pattern.compile("\\([\\+]*\\d+(\\.\\d+)?[\\+\\-\\*\\/]*\\)");
//            Matcher m422 =p422.matcher((statement));
//            while(m422.find()){
//                correctStatementWithinParenthesis = true;
//            }

            if(!correctStatementWithinParenthesis) return false;
        }

        //check that we don't have several signs of +-*/ or . going together:
        Pattern p5 = Pattern.compile("[\\+\\-\\*\\/\\.]{2,}");
        Matcher m5 = p5.matcher(statement);
        if(m5.find()){
            return false;
        }

        //check if we do not have the situation: 24.555.43
        Pattern p6 = Pattern.compile("\\d*\\.\\d*\\.");
        Matcher m6 = p6.matcher(statement);
        if(m6.find()){
            return false;
        }

        //check : only digits can be around the dot.
        //Doesn't work correct. Corrected in later logic of this method.
        if(statement.contains(".")){
            Pattern p7 = Pattern.compile("\\d\\.\\d");
            Matcher m7 = p7.matcher(statement);
            if(!m7.find()){
                return false;
            }
        }


        //check if . or / or * isn't the first sign in the statement:
        if(statement.charAt(0) == '.' || statement.charAt(0) == '*' || statement.charAt(0) == '/' ){
            return false;
        }

        //check if ( and ) create correct pairs. Avoid situation: (   )  ) ( (   )
        //counter of opening ( never can be less than 0
        String onlyParenthesis = statement.replaceAll("[^\\(\\)]", "");
        int counter = 0;
        char[] cArray = statement.toCharArray();
        for(int i = 0; i < cArray.length; i++){
            if(cArray[i] == '('){
                counter++;
            }else if(cArray[i] == ')'){
                counter--;
            }
            if(counter < 0){
                return false;
            }
        }

        //avoid this situation: 1(  together or )55 together:
        Pattern p8 = Pattern.compile("(\\)\\d|\\d\\()");
        Matcher m8 = p8.matcher(statement);
        if(m8.find()){
            return false;
        }

        //avoid this situation: .(  together or ). together:
        Pattern p9 = Pattern.compile("(\\)\\.|\\.\\()");
        Matcher m9 = p9.matcher(statement);
        if(m9.find()){
            return false;
        }

        //avoid this situation^ (0.33+)
        //avoid this situation^ (0.33)



        return true;
    }
}
