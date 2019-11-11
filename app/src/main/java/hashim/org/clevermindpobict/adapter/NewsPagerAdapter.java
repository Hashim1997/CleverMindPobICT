package hashim.org.clevermindpobict.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import hashim.org.clevermindpobict.LatestNewsActivity;
import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.News;

public class NewsPagerAdapter extends PagerAdapter {

    private final List<News> newsList;
    private final Context context;
    private final RequestOptions option;


    public NewsPagerAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        final News news=newsList.get(position);
        Log.i("title",news.getNewsTitle());
        Log.i("sub",news.getNewsSubTitle());
        Log.i("image", news.getNewsImage());

        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.item_card_news,null);

        ImageView newsImage=view.findViewById(R.id.newsImageCard);
        TextView newsTitle=view.findViewById(R.id.cardNewsTitle);
        TextView newsSubTitle=view.findViewById(R.id.cardNewsSubtitle);

        newsTitle.setText(news.getNewsTitle());
        newsSubTitle.setText(news.getNewsSubTitle());
        Glide.with(context).load(news.getNewsImage()).apply(option).into(newsImage);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsIntent=new Intent(view.getContext(),LatestNewsActivity.class);
                view.getContext().startActivity(newsIntent);

            }
        });

        container.addView(view);
        return view;
    }
}
