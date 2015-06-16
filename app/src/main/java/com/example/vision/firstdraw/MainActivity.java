package com.example.vision.firstdraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    private DrawViewGroup viewGroup;
    /**
     * 图片标题名
     */
    private String[] titleName = new String[]{"Ren", "Hello", "Video",
            "Chess", "Lifebuoy", "Mail", "Ebay", "Facebook", "Ren", "Hello", "Video",
            "Chess"};
    /**
     * 图片资源ID
     */
    private int[] imageRid = new int[]{R.mipmap.im0, R.mipmap.im1,
            R.mipmap.im2, R.mipmap.im3, R.mipmap.im4, R.mipmap.im5,
            R.mipmap.im6, R.mipmap.im7, R.mipmap.im0, R.mipmap.im1,
            R.mipmap.im2, R.mipmap.im3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewGroup = (DrawViewGroup) findViewById(R.id.viewgroup);
        viewGroup.initView(imageRid, titleName, 3500, 1000);
        viewGroup.setOnClickListener(new DrawViewGroup.OnClickListener() {
            @Override
            public void onClick(int selectedItem) {
//                Log.d("--------", String.valueOf(selectedItem));
                Toast.makeText(MainActivity.this, titleName[selectedItem], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
