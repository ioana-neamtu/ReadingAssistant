package com.example.ioana.readingassistent;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ProgressFormular extends AppCompatActivity {
    enum ButtonSelect {
        NewBook, CurrentlyReading, DoneReading
    }
    private ButtonSelect mCurrentSelection = ButtonSelect.NewBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_formular);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        final TextView textView = (TextView) findViewById(R.id.page_number);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.new_book:
                        //textView.setText("");
                        textView.setEnabled(false);
                        break;
                    case R.id.done_reading:
                        //textView.setText("");
                        textView.setEnabled(false);
                        break;
                    case R.id.currently_reading:
                        textView.setEnabled(true);
                        break;
                }
            }
        });
    }
}
