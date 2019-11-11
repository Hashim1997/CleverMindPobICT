package hashim.org.clevermindpobict.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import hashim.org.clevermindpobict.R;
import hashim.org.clevermindpobict.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolderNews> {

    private final List<News> newsList;
    private final Context context;
    private final RequestOptions option;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        option=new RequestOptions().placeholder(R.drawable.loading_image).error(R.drawable.error_load);
    }

    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_news_full,null);
        return new ViewHolderNews(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNews holder, int position) {
        News news=newsList.get(position);
        holder.newsTitle.setText(news.getNewsTitle());
        holder.newsSubTitle.setText(news.getNewsSubTitle());
        holder.newsDescription.setText(news.getNewsDescription());
        Glide.with(context).load(news.getNewsImage()).apply(option).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolderNews extends RecyclerView.ViewHolder{
        final TextView newsTitle;
        final TextView newsSubTitle;
        final TextView newsDescription;
        final ImageView newsImage;

        ViewHolderNews(View itemLayoutView){
            super(itemLayoutView);
            newsTitle=itemLayoutView.findViewById(R.id.newsTitle);
            newsSubTitle=itemLayoutView.findViewById(R.id.newsSubTitle);
            newsDescription=itemLayoutView.findViewById(R.id.newsDesc);
            newsImage=itemLayoutView.findViewById(R.id.newsImage);
        }
    }
}
