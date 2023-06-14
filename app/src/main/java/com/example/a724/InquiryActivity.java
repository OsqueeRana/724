package com.example.a724;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class InquiryActivity extends AppCompatActivity {

    TextView midAmount,finalAmount,midId,finalId,billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        midAmount = findViewById(R.id.midTerm);
        finalAmount = findViewById(R.id.finalTerm);
        midId = findViewById(R.id.mid_payment_id);
        finalId = findViewById(R.id.final_payment_id);
        billId = findViewById(R.id.bill_id);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                JSONObject object = new JSONObject(bundle.getString("data"));
                midAmount.setText(object.getJSONObject("MidTerm").getString("Amount"));
                finalAmount.setText(object.getJSONObject("FinalTerm").getString("Amount"));
                midId.setText(object.getJSONObject("MidTerm").getString("PaymentID"));
                finalId.setText(object.getJSONObject("FinalTerm").getString("PaymentID"));
                billId.setText(object.getJSONObject("FinalTerm").getString("BillID"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}