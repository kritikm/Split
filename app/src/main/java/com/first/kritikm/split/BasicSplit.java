package com.first.kritikm.split;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

public class BasicSplit extends AppCompatActivity {

    private String eventName;
    private String eventDate;
    private String eventLocation;
    private Button currencyViewer;
    private EditText payersText;
    private Button paidBy;
    private EditText totalAmoutText;
    private double[] payersPaid;
    private double[] payersOwe;
    private int paidByIndex = -1;
    private boolean multiple = false;
    private double totalAmount;
    private String currencyUsed;
    private ArrayList<String> payerList;
    private ArrayList<Integer> checked;
    private boolean shared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_split);
        eventName = getIntent().getStringExtra(EventDetailsActivity.SPLIT_EVENT);
        eventDate = getIntent().getStringExtra(EventDetailsActivity.SPLIT_DATE);
        eventLocation = getIntent().getStringExtra(EventDetailsActivity.SPLIT_LOCATION);
        currencyViewer = (Button)findViewById(R.id.currencyViewer);
        payersText = (EditText)findViewById(R.id.payers_text);
        paidBy = (Button)findViewById(R.id.paid_by_text);

        String title = eventName;
        if(eventLocation != "")
            title = title + "@" + eventLocation;
        getSupportActionBar().setTitle(title);

        Currency currency = Currency.getInstance(Locale.getDefault());
        currencyViewer.setText(currency.getSymbol());
        currencyUsed = currency.getSymbol();
    }

    public void getCurrencyList(View view)
    {
        final Set<Currency> allCurrencies = Currency.getAvailableCurrencies();
        int nCurrencies = allCurrencies.size();
        final ArrayList<String> stringArrayList = new ArrayList<>();
        CharSequence currencies[] = new CharSequence[nCurrencies];
        for(Currency currency : allCurrencies)
            stringArrayList.add(currency.getSymbol());
        for(int i = 0; i < nCurrencies; i++)
            currencies[i] = stringArrayList.get(i);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(currencies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currencyViewer.setText(stringArrayList.get(which));
                currencyUsed = stringArrayList.get(which);
            }
        });
        builder.show();
    }

    public void showPayers(View view)
    {
        final ArrayList<String> payerList = new ArrayList<>(Arrays.asList(getPayers()));
        payerList.add("Your majesty");
        payerList.add("Multiple");
        final String payers[] = new String[payerList.size()];
        for(int i = 0; i < payerList.size(); i++)
        {
            payers[i] = payerList.get(i);
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(payers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                paidBy.setText(payers[which]);
                if(payers[which].equals("Multiple"))
                {
                    multiple = true;
                    final AlertDialog.Builder multipleBuilder = new AlertDialog.Builder(BasicSplit.this);
                    final LayoutInflater layoutInflater = getLayoutInflater();
                    View multipleView = layoutInflater.inflate(R.layout.multiple_pay_layout, null);
                    final EditText payersPaidEdit[] = new EditText[payerList.size() - 1];
                    final TextView payerNamesText[] = new TextView[payerList.size() - 1];
                    LinearLayout horizontalLayouts[] = new LinearLayout[payerList.size() - 1];
                    LinearLayout outerLayout = (LinearLayout)multipleView.findViewById(R.id.outer_layout);

                    LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams editTextParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);

                    for(int i = 0; i < payerList.size() - 1; i++)
                    {
                        horizontalLayouts[i] = new LinearLayout(multipleBuilder.getContext());
                        horizontalLayouts[i].setLayoutParams(layoutParam);
                        payerNamesText[i] = new TextView(multipleBuilder.getContext());
                        payerNamesText[i].setLayoutParams(textViewParam);
                        payerNamesText[i].setText(payerList.get(i));
                        payerNamesText[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                        payersPaidEdit[i] = new EditText(multipleBuilder.getContext());
                        payersPaidEdit[i].setLayoutParams(editTextParam);
                        payersPaidEdit[i].setInputType(InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        horizontalLayouts[i].addView(payerNamesText[i]);
                        horizontalLayouts[i].addView(payersPaidEdit[i]);
                        outerLayout.addView(horizontalLayouts[i]);
                    }

                    Button multipleCheck = new Button(multipleBuilder.getContext());
                    multipleCheck.setText("Confirm");
                    multipleCheck.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    multipleCheck.setBackgroundColor(Color.parseColor("#ffffff"));
                    outerLayout.addView(multipleCheck);
                    multipleBuilder.setView(multipleView);
                    Log.d("SPLITS", "Showing multiple dialog");
                    final AlertDialog builtDialog = multipleBuilder.show();
                    payersPaid = new double[payerList.size() - 1];
                    multipleCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i = 0; i < payerList.size() - 1; i++)
                            {
                                if(!payersPaidEdit[i].getText().toString().equals("") && !payersPaidEdit[i].getText().toString().equals("0")) {
                                    payersPaid[i] = Double.parseDouble(payersPaidEdit[i].getText().toString());
                                }
                                else
                                    payersPaid[i] = Double.valueOf(0);
                            }
                            builtDialog.dismiss();
                        }
                    });
                }
                else
                {
                    multiple = false;
                    paidByIndex = which;
                }
            }
        });
        builder.show();
    }

    private String[] getPayers()
    {
        String[] payers;
        payers = payersText.getText().toString().split(",");
        for(int i = 0; i < payers.length; i++)
        {
            if(payers[i].startsWith(" "))
                payers[i] = payers[i].substring(1);
        }
        return payers;
    }

    public void showSplitOptions(View view)
    {
        CharSequence splitOptions[] = {"Equally", "By Share"};
        final Button splitOptionButton = (Button)findViewById(R.id.splitOption);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(splitOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        splitOptionButton.setText("Equally");
                        //set equally indicator here
                        break;
                    case 1:
                        splitOptionButton.setText("By share");

                        shared = true;

                        payerList = new ArrayList<>(Arrays.asList(getPayers()));

                        payerList.add("Your Majesty");

                        AlertDialog.Builder share = new AlertDialog.Builder(BasicSplit.this);

                        View dialogView = getLayoutInflater().inflate(R.layout.multiple_pay_layout, null);

                        TextView payerNamesText[] = new TextView[payerList.size()];
                        final EditText payersAteEdit[] = new EditText[payerList.size()];
                        LinearLayout horizontalLayouts[] = new LinearLayout[payerList.size()];
                        LinearLayout outerLayout = (LinearLayout)dialogView.findViewById(R.id.outer_layout);

                        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams editTextParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);

                        for(int i = 0; i < payerList.size(); i++)
                        {
                            horizontalLayouts[i] = new LinearLayout(share.getContext());
                            horizontalLayouts[i].setLayoutParams(layoutParam);
                            payerNamesText[i] = new TextView(share.getContext());
                            payerNamesText[i].setLayoutParams(textViewParam);
                            payerNamesText[i].setText(payerList.get(i));
                            payerNamesText[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                            payersAteEdit[i] = new EditText(share.getContext());
                            payersAteEdit[i].setLayoutParams(editTextParam);
                            payersAteEdit[i].setInputType(InputType.TYPE_CLASS_NUMBER |
                                                        InputType.TYPE_NUMBER_FLAG_DECIMAL);

                            horizontalLayouts[i].addView(payerNamesText[i]);
                            horizontalLayouts[i].addView(payersAteEdit[i]);
                            outerLayout.addView(horizontalLayouts[i]);
                        }
                        Button confirmButton = new Button(share.getContext());
                        confirmButton.setText("Confirm");
                        confirmButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                        confirmButton.setBackgroundColor(Color.parseColor("#ffffff"));

                        outerLayout.addView(confirmButton);
                        share.setTitle("Share of each eater");
                        share.setView(outerLayout);

                        final AlertDialog shareBuild = share.show();

                        payersOwe = new double[payerList.size()];

                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(int i = 0; i < payerList.size(); i++)
                                {
                                    if(!payersAteEdit[i].getText().toString().equals("") && !payersAteEdit[i].getText().toString().equals("0")) {
                                        payersOwe[i] = Double.parseDouble(payersAteEdit[i].getText().toString());
                                        totalAmount += payersOwe[i];
                                    }
                                    else
                                        payersOwe[i] = Double.valueOf(0);
                                }
                                totalAmoutText = (EditText) findViewById(R.id.total_amount);
                                totalAmoutText.setText(String.valueOf(totalAmount));
                                shareBuild.dismiss();
                            }
                        });
                        break;
                }
            }
        });
        builder.show();
    }


    public void splitBill(View view)
    {
        payerList = new ArrayList<>(Arrays.asList(getPayers()));
        payerList.add("Your majesty");

        if(!multiple && !shared)
        {
            //WORKS :D

            if(paidByIndex == -1)
                paidByIndex = payerList.size() - 1;
            totalAmoutText = (EditText) findViewById(R.id.total_amount);
            totalAmount = Double.parseDouble(totalAmoutText.getText().toString());

            payersPaid = new double[payerList.size()];
            payersOwe = new double[payerList.size()];

            for (int i = 0; i < payerList.size(); i++) {
                payersPaid[i] = 0;
                payersOwe[i] = totalAmount / payerList.size();
            }
            payersPaid[paidByIndex] = totalAmount;
        }
        else if(multiple && !shared)
        {
            //WORKS :D

            totalAmoutText =  (EditText)findViewById(R.id.total_amount);
            totalAmount = Double.parseDouble(totalAmoutText.getText().toString());

            payersOwe = new double[payerList.size()];
            for(int i = 0; i < payerList.size(); i++)
                payersOwe[i] = totalAmount / payerList.size();
        }
        else if(!multiple && shared)
        {
            //WORKS :D

            if(paidByIndex == -1)
                paidByIndex = payerList.size() - 1;
            payersPaid = new double[payerList.size()];
            for(int i = 0; i < payerList.size(); i++)
                payersPaid[i] = 0;
            payersPaid[paidByIndex] = totalAmount;
        }

        new SaveEvent().execute();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    class SaveEvent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            UserDataContract.SplitEntry splitDB = new UserDataContract.SplitEntry(getApplicationContext());
            int userId = ((GlobalData) getApplication()).getUserId();
            totalAmount = Double.parseDouble(decimalFormat.format(totalAmount));
            if (!splitDB.insert(userId, eventName, eventDate, eventLocation, currencyUsed, totalAmount))
                Log.d("SPLIT", "Cannot insert Split");
            int eventId = splitDB.getEventId(eventName, eventDate, eventLocation);
            splitDB.close();


            UserDataContract.EaterEntry eaterDB = new UserDataContract.EaterEntry(getApplicationContext());
            for (int i = 0; i < payerList.size(); i++) {
                double ate = payersOwe[i];
                double paid = payersPaid[i];
                ate = Double.parseDouble(decimalFormat.format(ate));
                paid = Double.parseDouble(decimalFormat.format(paid));
                if (!eaterDB.insert(eventId, payerList.get(i), ate, paid))
                    Log.d("SPLIT", "Cannot insert Eater");
            }

            return null;
        }
    }
}
