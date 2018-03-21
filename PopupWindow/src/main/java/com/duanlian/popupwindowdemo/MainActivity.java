package com.duanlian.popupwindowdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RewritePopwindow mPopwindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopwindow = new RewritePopwindow(MainActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(MainActivity.this, 1f);
            switch (v.getId()) {
                case R.id.weixinghaoyou:
                    Toast.makeText(MainActivity.this, "微信好友", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pengyouquan:
                    Toast.makeText(MainActivity.this, "朋友圈", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qqhaoyou:
                    Toast.makeText(MainActivity.this, "QQ好友", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qqkongjian:
                    Toast.makeText(MainActivity.this, "QQ空间", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
}
