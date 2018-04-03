package com.pronoymukherjee.calculator_beta1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    AppCompatEditText e;
    TextView ans;
    TextView mem;
    String m = "";
    AppCompatButton sol, memR, memA,clearButton,deleteButton;
    char prec[] = {'-', '+', '*', '/', '^'};
    char stack[];
    int top = -1;
    double stack1[];
    View view;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (AppCompatEditText) findViewById(R.id.e);
        ans = (TextView) findViewById(R.id.ans);
        mem = (TextView) findViewById(R.id.mem);
        sol = (AppCompatButton) findViewById(R.id.ansButton);
        memR = (AppCompatButton) findViewById(R.id.mem1);
        memA = (AppCompatButton) findViewById(R.id.mem2);
        clearButton= (AppCompatButton) findViewById(R.id.clearButton);
        deleteButton= (AppCompatButton) findViewById(R.id.delete);
        view=MainActivity.this.getCurrentFocus();
        sol.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    String pexp = "";
                    String exp = e.getText().toString();
                    exp = exp.trim();
                    exp = Edit_Exp(exp);
                    exp = Trigonometry(exp);
                    pexp = InfixTo_PostFix(exp);
                    String a1 = SolvePostfix(pexp);
                    ans.setText(a1);
                    e.setText(a1);
                    m = "";
                    m = a1;
                    InputMethodManager inputMethodManager= (InputMethodManager)
                            getSystemService(Activity.INPUT_METHOD_SERVICE);
                    try {
                        if(view==null)
                            view=MainActivity.this.getCurrentFocus();
                        inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
                    }
                    catch (NullPointerException e){
                        Log.d(MainActivity.class.getSimpleName(),e.toString());
                    }
                } catch (Exception ignored) {
                    ans.setText("Inavlid Expression");
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    onCreate(savedInstanceState);
                }
            }
        });
        memR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = e.getText().toString();
                t += m;
                e.setText(t);
            }
        });
        memA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m = e.getText().toString();
                mem.setText(m);
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e.getText().clear();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=e.getText().toString();
                temp=temp.substring(0,temp.length()-1);
                e.setText(temp);
            }
        });
    }

    public String InfixTo_PostFix(String exp) {
        String pexp = "";
        stack = new char[exp.length()];
        int i, ascii;
        char ch;
        boolean IsNumber = false;
        try {
            for (i = 0; i < exp.length(); i++) {
                ch = exp.charAt(i);
                ascii = (int) ch;
                if ((ascii >= 48 && ascii <= 57) || (ascii == 46)) {
                    pexp += ch;
                    IsNumber = false;
                    try {
                        if (((((int) exp.charAt((i + 1))) >= 48) && (((int) exp.charAt((i + 1))) <=
                                57)) || ((int) exp.charAt(i + 1) == 46)) {
                            while (!IsNumber) {
                                if ((exp.charAt(i + 1) == '+') || (exp.charAt(i + 1) == '-') ||
                                        (exp.charAt(i + 1) == '*') ||
                                        (exp.charAt(i + 1) == '/') || (exp.charAt(i + 1) == '^') ||
                                        (exp.charAt(i + 1) == '(') ||
                                        (exp.charAt(i + 1) == '{') || (exp.charAt(i + 1) == '[') ||
                                        (exp.charAt(i + 1) == ')') ||
                                        (exp.charAt(i + 1) == '}') || (exp.charAt(i + 1) == ']')) {
                                    IsNumber = true;
                                    break;
                                }
                                pexp += exp.charAt(++i);
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    pexp += " ";
                } else if ((ascii == 45) || (ascii == 43) || (ascii == 42) || (ascii == 47) ||
                        (ascii == 94)) {
                    if (top > -1) {
                        int sp = getPrec(stack[top]);
                        int cp = getPrec(ch);
                        if (cp > sp)
                            Push(ch);
                        else {
                            pexp += Pop();
                            Push(ch);
                        }
                    } else if (top == -1) Push(ch);
                } else if (ch == '(' || ch == '{' || ch == '[') {
                    Push(ch);
                } else if (ch == ')' || ch == '}' || ch == ']') {
                    if (ch == ')') {
                        char chh = Pop();
                        while (chh != '(') {
                            pexp += chh;
                            chh = Pop();
                        }
                    } else if (ch == '}') {
                        char chh = Pop();
                        while (chh != '{') {
                            pexp += chh;
                            chh = Pop();
                        }
                    } else if (ch == ']') {
                        char chh = Pop();
                        while (chh != '[') {
                            pexp += chh;
                            chh = Pop();
                        }
                    } else {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (top != -1) {
            while (top != -1) {
                pexp += Pop();
            }
        }
        return pexp;
    }

    public void Push(char ch) {
        if (top != stack.length - 1)
            stack[++top] = ch;
    }

    public char Pop() {
        return stack[top--];
    }

    public int getPrec(char ch) {
        int i;
        for (i = 0; i < prec.length; i++) {
            if (ch == prec[i])
                return i;
        }
        return -1;
    }

    public String SolvePostfix(String pexp) {
        int i;
        char ch;
        top = -1;
        boolean isNum = false;
        stack1 = new double[pexp.length()];
        for (i = 0; i < pexp.length(); i++) {
            ch = pexp.charAt(i);
            int ascii = (int) ch;
            if (ascii >= 48 && ascii <= 57) {
                isNum = false;
                String t = "";
                while (!isNum) {
                    t += pexp.charAt(i);
                    i++;
                    if (pexp.charAt(i) == ' ') {
                        isNum = true;
                        break;
                    }
                }
                double a1 = Double.parseDouble(t);
                Push1(a1);
            } else if ((ascii == 45) || (ascii == 43) || (ascii == 42) || (ascii == 47) || (ascii == 94)) {
                double a1 = Pop1();
                double a2 = Pop1();
                double ans = 0.0;
                if (ascii == 45)
                    ans = a2 - a1;
                else if (ascii == 43) ans = (float) a2 + a1;
                else if (ascii == 42) ans = (float) a2 * a1;
                else if (ascii == 47) ans = (float) a2 / a1;
                else if (ascii == 94) ans = (float) Math.pow(a2, a1);
                Push1((float) ans);
            }
        }
        double answer = (float) Pop1();
        String a1 = "";
        a1 += answer;
        return a1;
    }

    public void Push1(double n) {
        if (top != stack1.length - 1)
            stack1[++top] = n;
    }

    public double Pop1() {
        return stack1[top--];
    }

    public String Edit_Exp(String exp) {
        String t = "";
        int i, l = exp.length(), d, d1;
        char ch, chh;
        for (i = 0; i < l - 1; i++) {
            ch = exp.charAt(i);
            chh = exp.charAt(i + 1);
            d = (int) ch;
            d1 = (int) chh;
            if ((d >= 48 && d <= 57) && (d1 == 40)) {
                t += ch;
                t += '*';
                t += chh;
                i++;
            } else {
                t += ch;
            }
        }
        t += exp.charAt(l - 1);
        return t;
    }

    public String Trigonometry(String exp) {
        int i, j, l = exp.length();
        char ch, chh;
        exp += " ";
        String num = "", eExp = "";
        int si = 0, ei = 0;
        boolean isSi = false, isBracket = false;
        for (i = 0; i < l; i++) {
            ch = exp.charAt(i);
            num = "";
            if (ch == 's' || ch == 'c' || ch == 't') {
                for (j = i; j <= l; j++) {
                    chh = exp.charAt(j);
                    if ((chh == '(' || ((int) chh >= 48 && (int) chh <= 57) || chh == '{' ||
                            chh == '[') && (isSi == false)) {
                        if (chh == '(' || chh == '{' || chh == '[') {
                            si = j + 1;
                            isSi = true;
                            isBracket = true;
                        } else if (((int) chh >= 48 && (int) chh <= 57)) {
                            si = j;
                            isSi = true;
                        }
                    } else if ((chh == '-' || chh == '+' || chh == '*' || chh == '/' || chh == '^' ||
                            chh == ' ') && (isBracket == false) && (isSi)) {
                        ei = j;
                        break;
                    } else if ((chh == ')' || chh == '}' || chh == ']') && (isBracket)) {
                        ei = j;
                        break;
                    }
                }
                num = exp.substring(si, ei);
                num = InfixTo_PostFix(num);
                num = SolvePostfix(num);
                if (ch == 's' && exp.charAt(i + 1) == 'i') {
                    double n = Double.parseDouble(num);
                    n = n * 0.01745329252;
                    eExp += Math.sin(n);
                } else if (ch == 'c' && exp.charAt(i + 1) == 'o') {
                    double n = Double.parseDouble(num);
                    n = n * 0.01745329252;
                    eExp += Math.cos(n);
                } else if (ch == 't' && exp.charAt(i + 1) == 'a') {
                    double n = Double.parseDouble(num);
                    n = n * 0.01745329252;
                    eExp += Math.tan(n);
                }
                i = j - 1;
            } else eExp += ch;
        }
        return eExp;
    }
}