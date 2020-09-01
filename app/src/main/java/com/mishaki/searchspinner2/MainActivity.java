package com.mishaki.searchspinner2;

import android.os.Bundle;
import android.widget.Button;

import com.mishaki.libsearchspinner.adapter.StringMarkSpinnerAdapter;
import com.mishaki.libsearchspinner.view.StringSearchSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/01 14:00
 */
public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StringSearchSpinner sss = findViewById(R.id.sss);
        Button position_btn = findViewById(R.id.position_btn);
        List<Integer> intList = ListUtil.range(0, 10);
        List<String> dataList = new ArrayList<>();
        ListUtil.mapTo(intList, dataList, String::valueOf);
        ListUtil.mapTo(intList, dataList, String::valueOf);
        ListUtil.mapTo(intList, dataList, String::valueOf);
        ListUtil.mapTo(intList, dataList, String::valueOf);
        StringMarkSpinnerAdapter adapter = new StringMarkSpinnerAdapter();
        sss.setAdapter(adapter, dataList);
        sss.setOnSelectedListener((position -> position_btn.setText(String.valueOf(position))));
    }
}
