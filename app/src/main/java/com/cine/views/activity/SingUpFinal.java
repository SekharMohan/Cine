package com.cine.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.R;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SingUpFinal extends AppCompatActivity {
@BindView(R.id.language_spinner)
    AppCompatSpinner spiLanguage;
    @BindView(R.id.category_spinner)
    AppCompatSpinner spiCategory;
    @BindArray(R.array.category)
    String[] arrCategory;
    @BindArray(R.array.language)
    String[] arrLanguage;
    @BindView(R.id.createMyAccountButton)
    public AppCompatButton createMyAccountButton;
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SingUpFinal.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);
        ButterKnife.bind(this);
        spinnerSetup(spiCategory,arrCategory,getString(R.string.category_hint));
        spinnerSetup(spiLanguage,arrLanguage,getString(R.string.language_hint));
        createMyAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent();
            }
        });
    }
public void spinnerSetup(AppCompatSpinner spinner,String[] values,String hint){
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = super.getView(position, convertView, parent);
            if (position == getCount()) {
                ((TextView)v.findViewById(android.R.id.text1)).setText("");
                ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
            }

            return v;
        }

        @Override
        public int getCount() {
            return super.getCount()-1; // you dont display last item. It is used as hint.
        }

    };

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    adapter.addAll(values);
    adapter.add(hint);

    spinner.setAdapter(adapter);
    spinner.setSelection(adapter.getCount());
}

    void onClickEvent(){
        startActivity(new Intent(this,LoginActivity.class));
    }
}
