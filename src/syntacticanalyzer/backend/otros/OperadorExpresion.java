package syntacticanalyzer.backend.otros;

import java.util.ArrayList;
import java.util.Stack;

public class OperadorExpresion {
    
    public static int Operar(ArrayList<String> ex){
        
        Stack<String> pila = new Stack();
        ArrayList<String> postfija = new ArrayList();
        
        for (String e : ex) {
            if (isNumeric(e)) {
                postfija.add(e);
            } else {
                switch (e) {
                    case "(":
                        pila.push(e);
                        break;
                    case ")":
                        for (int i = pila.size(); i > 0 ; i--) {
                            if (pila.peek().equals("(")) {
                                break;
                            } else {
                                postfija.add(pila.peek());
                                pila.pop();
                            }
                        }
                        break;
                    case "*":
                        if (!pila.isEmpty() && pila.peek().equals("*")) {
                            postfija.add(pila.peek());
                            pila.pop();
                        }
                        pila.push(e);
                        break;
                    case "+":
                        for (int i = pila.size(); i > 0 ; i--) {
                            if (pila.peek().equals("(")) {
                                break;
                            } else {
                                postfija.add(pila.peek());
                                pila.pop();
                            }
                        }
                        pila.push(e);
                        break;
                }
            }
        }
        
        if (!pila.isEmpty()) {
            for (int i = pila.size(); i > 0; i--) {
                if (pila.peek().equals("(")) {
                    pila.pop();
                } else {
                    postfija.add(pila.peek());
                    pila.pop();
                }
            }
        }
        
        ////////////////////////////////////////
        //operar notacion polaca inversa
        ///////////////////////////////////////
        
        pila.clear();
        int num1;
        int num2;
        for (String s : postfija) {
            if (isNumeric(s)) {
                pila.push(s);
            } else if (s.equals("+")) {
                num2 = Integer.parseInt(pila.peek());
                pila.pop();
                num1 = Integer.parseInt(pila.peek());
                pila.pop();
                pila.push(String.valueOf(num1 + num2));
            } else if (s.equals("*")) {
                num2 = Integer.parseInt(pila.peek());
                pila.pop();
                num1 = Integer.parseInt(pila.peek());
                pila.pop();
                pila.push(String.valueOf(num1 * num2));
            }
        }
        System.out.println("\n" + pila.peek());
        return Integer.parseInt(pila.peek());
    }
    
    public static boolean isNumeric(String s){
        try {
            int n = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
