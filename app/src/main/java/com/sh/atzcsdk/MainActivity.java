package com.sh.atzcsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sh.atzcfastlibrary.common.CommonUtils;
import com.sh.atzcfastlibrary.image_show.AtImageShowActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View textHl = findViewById(R.id.textHl);

        textHl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAtImageShowActivityTest();
            }
        });
    }

    private void startAtImageShowActivityTest() {
        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add("https://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%253A%252F%252Fimg.ewebweb.com%252Fuploads%252F20191006%252F18%252F1570356372-DrJgfOUqjl.jpg%26refer%3Dhttp%253A%252F%252Fimg.ewebweb.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1612160138%26t%3D54619f535fb592301dd26e707c8cdf4c&thumburl=https%3A%2F%2Fss1.bdstatic.com%2F70cFuXSh_Q1YnxGkpoWK1HF6hhy%2Fit%2Fu%3D3112798566%2C2640650199%26fm%3D26%26gp%3D0.jpg");
        imagePaths.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F110814%2F6351-110Q4010J681.jpg&refer=http%3A%2F%2Fimg.juimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612160161&t=402389706ad6568481e005cc37c0301c");
        imagePaths.add("file:///sdcard/test1.jpg");
        imagePaths.add("file:///sdcard/test2.jpg");
        imagePaths.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fmobile%2Fd%2F53917b75357d8.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612160161&t=aa2fe3f09ef2ad62b1415aaffdcd2429");
        int defPos = 0;
        AtImageShowActivity.startActivity(MainActivity.this, imagePaths, defPos);
    }
}
