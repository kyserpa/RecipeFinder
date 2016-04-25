package com.example.paul.testconect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;


public class Main extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.my_button).setOnClickListener(this);
    }


    private EditText ingredient;
    private String ingredientStr;
    @SuppressLint("WrongViewCast")
    @Override

    public void onClick(View arg0) {

        Button b = (Button)findViewById(R.id.my_button);
        ingredient = (EditText) findViewById(R.id.editText);
        ingredientStr = ingredient.getText().toString();
        b.setClickable(false);
        new Connector().execute();
    }

    private class Connector extends AsyncTask <Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }

            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://api.campbellskitchen.com/brandservice.svc/api/search?ingredient="+ ingredientStr+"&format=xml&app_id=56716e32&app_key=92153bbdf5604fb6a663e1eb2778f8a7");
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            Interpreter interpreter = new Interpreter();
            text = interpreter.parseXML(text);
            return text;
        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                EditText et = (EditText)findViewById(R.id.my_edit);
                et.setText(results);
            }
            Button b = (Button)findViewById(R.id.my_button);
            b.setClickable(true);
        }
    }
}