package com.example.kim.realapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Kim on 2015-02-10.
 */
public class DetailActivity extends Activity
{
    String items[] = {"중요도", "1", "2", "3", "4", "5"}; // Spinner에 나오게 될 단어들
    ArrayAdapter<String> list;
    Spinner spinner;

    DateFormat fmDate = DateFormat.getDateInstance(); // Date 포맷을 받음
    DateFormat fmTime = DateFormat.getTimeInstance(); // Time 포맷을 받음

    TextView dateLabel; // Date 포맷을 나타내줄 TextView
    TextView timeLabel; // Time 포맷을 나타내줄 TextView
    Calendar dateAndtime = Calendar.getInstance(); //Calendar의 instance를 받음

    @Override
    public void onCreate (Bundle savedInstanceState) // 생성
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // activity_detail과 연결

        list = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner = (Spinner) findViewById(R.id.importance_spinner);
        spinner.setAdapter(list);
        spinner.setSelection(0); // Spinner 구현

        Button btn = (Button) findViewById(R.id.datebutton); // datebutton을 나타나게 함
        btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v) // 클릭이 되면 DatePicker가 대화상자로 나옴
                    {
                        new DatePickerDialog(DetailActivity.this, d, dateAndtime.get(Calendar.YEAR), dateAndtime.get(Calendar.MONTH), dateAndtime.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
        );

        btn = (Button) findViewById(R.id.timebutton); // timebutton을 나타나게 함

        btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v) // 클릭 되면 TimePicker가 대화상자로 나옴
                    {
                        new TimePickerDialog(DetailActivity.this, t, dateAndtime.get(Calendar.HOUR_OF_DAY), dateAndtime.get(Calendar.MINUTE), false).show();
                    }
                }
        );

        dateLabel = (TextView) findViewById(R.id.datebutton); // 날짜와 시간이 각각 버튼에 TextView 형태로 나옴
        timeLabel = (TextView) findViewById(R.id.timebutton);

        updateLabelDATE(); // 날짜와 시간 Update
        updateLabelTIME();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar); // Toolbar 기능

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick (MenuItem item)
                    {
                        return true;
                    }
                }
        );
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() //DatePick를 대화상자로 띄움
    {
        @Override
        public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            dateAndtime.set(Calendar.YEAR, year); // 각각 년도,월,일을 받아 변수에 넣음
            dateAndtime.set(Calendar.MONTH, monthOfYear);
            dateAndtime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelDATE(); // 표시되는 것을 Update 시킴
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet (TimePicker view, int hourOfDay, int minute)
        {
            dateAndtime.set(Calendar.HOUR_OF_DAY, hourOfDay); // 각각 시,분을 받아 변수에 넣음
            dateAndtime.set(Calendar.MINUTE, minute);
            updateLabelTIME(); // 표시되는 것을 Update 시킴
        }
    };

    private void updateLabelDATE () // 날짜의 Update
    {
        dateLabel.setText(fmDate.format(dateAndtime.getTime()));
    }

    private void updateLabelTIME () // 시간의 Update
    {
        timeLabel.setText(fmTime.format(dateAndtime.getTime()));
    }

    public void SaveData (View view) // 저장 버튼 눌렀을 시
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void AddContact (View view) // 연락처 추가 버튼
    {
        Intent intent = new Intent(this, ContactsListActivity.class);
        startActivity(intent);
    }

    public void onBackPressed () // Back 버튼을 누를 시
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
