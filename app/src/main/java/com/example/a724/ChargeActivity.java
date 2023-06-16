package com.example.a724;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChargeActivity extends AppCompatActivity {
    public String postUrl = "https://topup.pec.ir/";
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Button button;
    EditText phoneNo, amount;
    RadioGroup radioGroup;
    String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        button = findViewById(R.id.button);
        phoneNo = findViewById(R.id.editText);
        amount = findViewById(R.id.editText2);
        radioGroup = findViewById(R.id.radioGroup);

        button.setOnClickListener(view -> {

            if (radioGroup.getCheckedRadioButtonId() == R.id.irancel) {
                operator = "1";
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.hamrah) {
                operator = "2";
            } else {
                operator = "3";
            }
            try {
                chargeSIM("{MobileNo:" + phoneNo.getText() + "OperatorType: " + operator +
                        ",AmountPure: " + amount.getText() + ",mid:0}");
            } catch (IOException e) {
                Toast.makeText(this, "ارور ناشناخته", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void chargeSIM(String data) throws IOException {

        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChargeActivity.this, "ارنباط با سرور برقرار نشد!",
                                Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            assert response.body() != null;
                            String jsonData = response.body().string();

                            JSONObject object = new JSONObject(jsonData);
                            if (object.has("error")) {
                                Toast.makeText(ChargeActivity.this, object.get("error").toString()
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.get("url").toString()));
                                startActivity(browserIntent);
                            }
                        } catch (JSONException | IOException e) {
                            Toast.makeText(ChargeActivity.this, "پاسخی از سرور دریافت نشد", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}