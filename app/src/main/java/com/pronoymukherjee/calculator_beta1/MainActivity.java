package com.pronoymukherjee.calculator_beta1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    AppCompatEditText e;
    TextView ans;
    TextView mem;
    String m = "";
    AppCompatButton sol, memR, memA, clearButton, deleteButton;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (AppCompatEditText) findViewById(R.id.e);
        ans = (TextView) findViewById(R.id.ans);
        mem = (TextView) findViewById(R.id.mem);
        sol = (AppCompatButton) findViewById(R.id.ansButton);
        memR = (AppCompatButton) findViewById(R.id.mem1);
        memA = (AppCompatButton) findViewById(R.id.mem2);
        clearButton = (AppCompatButton) findViewById(R.id.clearButton);
        deleteButton = (AppCompatButton) findViewById(R.id.delete);
        view = MainActivity.this.getCurrentFocus();
        sol.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    String pexp = "";
                    String exp = e.getText().toString();
                    ExpressionSolver solver = new ExpressionSolver();
                    exp = exp.trim();
                    exp = solver.Edit_Exp(exp);
                    exp = solver.calculateRoots(exp);
                    exp = solver.Trigonometry(exp);
                    pexp = solver.InfixTo_PostFix(exp);
                    String a1 = solver.SolvePostfix(pexp);
                    ans.setText(a1);
                    e.setText(a1);
                    m = "";
                    m = a1;
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Activity.INPUT_METHOD_SERVICE);
                    try {
                        if (view == null)
                            view = MainActivity.this.getCurrentFocus();
                        inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
                    } catch (NullPointerException e) {
                        Log.d(MainActivity.class.getSimpleName(), e.toString());
                    }
                } catch (Exception e) {
                    Log.d(ExpressionSolver.class.getSimpleName(), e.toString());
                    ans.setText("Invalid Expression");
                    //startActivity(new Intent(MainActivity.this, MainActivity.class));
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
                String temp = e.getText().toString();
                try {
                    temp = temp.substring(0, temp.length() - 1);
                }
                catch (IndexOutOfBoundsException e){
                    Log.d(MainActivity.class.getSimpleName(),e.toString());
                }
                e.setText(temp);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.tutorial_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tutorialMenu:Intent intent=new Intent(MainActivity.this,TutorialActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}