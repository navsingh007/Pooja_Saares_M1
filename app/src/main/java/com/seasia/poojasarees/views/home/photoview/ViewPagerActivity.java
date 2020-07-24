package com.seasia.poojasarees.views.home.photoview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.seasia.poojasarees.R;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ArrayList<String> urls = getIntent().getStringArrayListExtra("URL");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SamplePagerAdapter(this, urls));
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private ArrayList<String> imageUrls;
        private Context context;

        public SamplePagerAdapter(Context context, ArrayList<String> urls) {
            this.context = context;
            this.imageUrls = urls;
        }

//        private static final int[] sDrawables = {R.drawable.app_icon};

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            Glide.with(context)
                    .load(imageUrls.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_logo_image)
                    .into(photoView);

//            photoView.setImageResource(sDrawables[position]);


            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
