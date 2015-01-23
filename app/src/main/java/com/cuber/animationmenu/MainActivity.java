package com.cuber.animationmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cuber.library.CuberMenu;
import com.cuber.library.CuberMenuAdapter;


public class MainActivity extends ActionBarActivity {

    String[] titles = {"", "Add to friends", "Add to favorites", "Send Email", "Chat", "Search"};
    int[] images = {R.drawable.ic_cancel_menu, R.drawable.ic_add_friend, R.drawable.ic_add_favorite, R.drawable.ic_send_emil, R.drawable.ic_chat, R.drawable.ic_search};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CuberMenu view = (CuberMenu) findViewById(R.id.view);
        final Button button = (Button) findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.start();
            }
        });

        view.setAdapter(new CuberMenuAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public String getTitle(int position) {
                return titles[position];
            }

            @Override
            public int getImageResource(int position) {
                return images[position];
            }
        });

        view.setOnMenuClickListener(new CuberMenu.OnMenuClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "click:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
