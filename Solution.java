package com.javarush.task.task34.task3404;

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
        solution.recurse("cos(2*(-5+1.5*4)+28)+sin(30)+tan(30+15)", 0); //expected output 0.5 6
    }

    public void recurse(final String expression, int countOperation) {
        //implement
        List<Lexeme> lexemes = lexAnalyse(expression);



        for(Lexeme l : lexemes){
            System.out.print(l.value + "   ");
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
