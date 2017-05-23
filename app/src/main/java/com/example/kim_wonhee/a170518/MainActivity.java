package com.example.kim_wonhee.a170518;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    MyPainter myPainter;
    CheckBox stamp_checkBox;

    int check_Bluring, check_Coloring, check_Penwidthbig = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Canvas");

        init();

        stamp_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    myPainter.stamp = true;
                else
                    myPainter.stamp = false;
            }
        });

    }

    void init() {
        myPainter = (MyPainter) findViewById(R.id.canvas);
        stamp_checkBox = (CheckBox) findViewById(R.id.stamp_checkBox);
    }

    //--- Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 0, "Bluring").setCheckable(true);
        menu.add(11, 2, 0, "Coloring").setCheckable(true);
        menu.add(111, 3, 0, "Pen Width Big").setCheckable(true);
        menu.add(1111, 4, 0, "Pen Color Red");
        menu.add(1111, 5, 0, "Pen Color Blue");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            if (check_Bluring == 0) {
                check_Bluring = 1;
                myPainter.setOperationType("bluring");
                item.setChecked(true);
            } else {
                check_Bluring = 0;
                item.setChecked(false);
            }
        } else if (item.getItemId() == 2) {
            if (check_Coloring == 0) {
                check_Coloring = 1;
                myPainter.setOperationType("coloring");
                item.setChecked(true);
            } else {
                check_Coloring = 0;
                item.setChecked(false);
            }
        } else if (item.getItemId() == 3) {
            if (check_Penwidthbig == 0) {
                check_Penwidthbig = 1;
                myPainter.setOperationType("penwidthbig");
                item.setChecked(true);
            } else {
                check_Coloring = 0;
                item.setChecked(false);
            }
        } else if (item.getItemId() == 4) {
            myPainter.setOperationType("pencolorred");
        } else if (item.getItemId() == 5) {
            myPainter.setOperationType("pencolorblue");
        }

//            case 2:
//                myPainter.setOperationType("coloring");
//            case 3:
//                myPainter.setOperationType("penwidthbig");
//            case 4:
//                myPainter.setOperationType("pencolorred");
//            case 5:
//                myPainter.setOperationType("pencolorblue");
//            default:

        return super.onOptionsItemSelected(item);
    }

    //--- Button
    public void onMyClick (View v) {
        if (v.getId() == R.id.eraser_button) {
            myPainter.setOperationType("eraser");
            stamp_checkBox.setChecked(false);
        } else if (v.getId() == R.id.open_button) {
            myPainter.setOperationType("open");
            stamp_checkBox.setChecked(false);
        } else if (v.getId() == R.id.save_button) {
            myPainter.setOperationType("save");
            stamp_checkBox.setChecked(false);
        } else if (v.getId() == R.id.rotate_button) {
            myPainter.setOperationType("rotate");
            stamp_checkBox.setChecked(true);
        } else if (v.getId() == R.id.move_button) {
            myPainter.setOperationType("move");
            stamp_checkBox.setChecked(true);
        } else if (v.getId() == R.id.scale_button) {
            myPainter.setOperationType("scale");
            stamp_checkBox.setChecked(true);
        } else if (v.getId() == R.id.skew_button) {
            myPainter.setOperationType("skew");
            stamp_checkBox.setChecked(true);
        }
    }


}
