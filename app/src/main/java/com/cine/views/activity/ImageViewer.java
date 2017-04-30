package com.cine.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cine.R;
import com.cine.views.widgets.ExtendedViewPager;
import com.cine.views.widgets.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Vijay.
  */

public class ImageViewer extends AppCompatActivity{


    ExtendedViewPager viewPager;
    PagerAdapter adapter;
    private TouchImageView feedEnlargeImageView;
    private String downloadUrl, imageName;
    private int mChildPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        viewPager = (ExtendedViewPager) findViewById(R.id.pager);

//        ArrayList<String> list=getIntent().getExtras().getStringArrayList("docimage");
        downloadUrl = getIntent().getStringExtra("downloadurl");
        imageName = getIntent().getStringExtra("postimage");

        adapter = new ViewPagerAdapter(this, downloadUrl , imageName);
        //adapter = new ViewPagerAdapter(ImageViewer.this, bitmapArray);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mChildPosition);
    }


      public class ViewPagerAdapter extends PagerAdapter {
        // Declare Variables
        Context context;
        String downloadUrl;
        String imageName;

        LayoutInflater inflater;

            public ViewPagerAdapter(Context context, String downloadUrl, String imageName) {
                this.context = context;
                this.downloadUrl = downloadUrl;
                this.imageName = imageName;

            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((LinearLayout) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View itemView = inflater.inflate(R.layout.image_view_pager, container,
                        false);
                feedEnlargeImageView = (TouchImageView) itemView.findViewById(R.id.feedEnlargeImageView);
                //TextView txtImageName = (TextView) itemView.findViewById(R.id.population);
                // Capture position and set to the ImageView
                Picasso.with(context).load("http://www.buyarecaplates.com/vpb-wall-photos/" + imageName).resize(400, 400).into(feedEnlargeImageView);
                /*if (list.get(position) != null) {

                    String URL = String.format(Locale.ENGLISH, TransactionConstants.DOWNLOAD_FILE,
                            list.get(position), patientID, documentID, rootName);
                            Log.i("URL------>","Image URL "+URL);
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        Picasso.Builder builder = new Picasso.Builder(context);
                        Picasso picasso = builder.build();
                        picasso.load(URL).placeholder(R.drawable.ic_folder).noFade().into(imgflag);
                        //Picasso.with(context).load(URL).resize(400, 400).centerCrop()
                        //        .error(R.drawable.ic_folder).placeholder(R.drawable.ic_folder).into(imgflag);
                        txtImageName.setText(documentType);



                    }
                }*/
                feedEnlargeImageView.setScaleType(TouchImageView.ScaleType.FIT_CENTER);
                // Add viewpager_item.xml to ViewPager
                ((ExtendedViewPager) container).addView(itemView);

                return itemView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // Remove viewpager_item.xml from ViewPager
                ((ExtendedViewPager) container).removeView((LinearLayout) object);

            }
        }


}



