package com.sh.atzcfastlibrary.image_show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.sh.atzcfastlibrary.R;
import com.sh.atzcfastlibrary.R2;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 仿微信图片浏览器
 */
public class AtImageShowActivity extends Activity {
    public static final int REQ_CODE = 12301;
    public static final int RESULT_CODE = 12302;

    public static final String AIMG_SHOW_POS = "AIMG_SHOW_POS";

    @BindView(R2.id.imageViewPager)
    ViewPager imageViewPager;
    @BindView(R2.id.pageNum)
    TextView pageNum;

    private ArrayList<String> mImagePaths = new ArrayList<>();

    public static void startActivity(Activity act, ArrayList<String> imagePaths, int defPos) {
        startActivityForResult(act, imagePaths, defPos, REQ_CODE);
    }

    public static void startActivityForResult(Activity act, ArrayList<String> imagePaths, int defPos, int requestCode) {
        Intent intent = new Intent(act, AtImageShowActivity.class);
        intent.putStringArrayListExtra("imagePaths", imagePaths);
        intent.putExtra(AIMG_SHOW_POS, defPos);
        act.startActivityForResult(intent, requestCode);
//        act.overridePendingTransition(R.anim.activity_enter_anim, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.at_image_show_act, null);
        setContentView(rootView);
        ButterKnife.bind(this);

        mImagePaths = getIntent().getStringArrayListExtra("imagePaths");

        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String pageStr = "" + (position + 1) + "/" + mImagePaths.size();
                pageNum.setText(pageStr);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageViewPager.setAdapter(new ViewPagerAdapter(mImagePaths));
        int defPos = getIntent().getIntExtra(AIMG_SHOW_POS, 0);
        imageViewPager.setCurrentItem(defPos < mImagePaths.size() ? defPos : 0);
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<String> paths = null;//大图地址 如果为网络图片 则为大图url

        public ViewPagerAdapter(List<String> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            if (null == paths)
                return 0;
            return paths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            //注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
            //因为直接加到viewGroup下会导致返回的view为viewGroup
            View imageLayout = getLayoutInflater().inflate(R.layout.at_image_show_act_item, null);
            viewGroup.addView(imageLayout);
            assert imageLayout != null;
            PinchImageView imageView = (PinchImageView) imageLayout.findViewById(R.id.theImage);
            imageView.setCloseImageShowCb(new PinchImageView.CloseImageShowCb() {
                @Override
                public void onPictureRelease() {
                    finish();
                }
            }, imageViewPager);

            final String path = paths.get(position);
            imageLayout.setTag(path);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    showImgOptDialog(path);
                    return false;
                }
            });
            Glide.with(AtImageShowActivity.this).load(path)
                    .placeholder(R.color.black2)
                    .error(R.color.black2)
                    .into(imageView);
            return imageLayout;
        }

        @Override
        public int getItemPosition(Object object) {
            //在notifyDataSetChanged时返回None，重新绘制
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int arg1, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }


    @Override
    public void finish() {
        int pos = imageViewPager.getCurrentItem();
        Intent ret = new Intent();
        ret.putExtra(AIMG_SHOW_POS, pos);
        setResult(RESULT_CODE, ret);
        super.finish();
//        overridePendingTransition(0, R.anim.activity_exit_anim);
    }

}