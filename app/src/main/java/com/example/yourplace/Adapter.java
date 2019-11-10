package com.example.yourplace;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yourplace.Interface.ILoadMore;
import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);

        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{

    TextView textTitle;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.card_text_view);
    }
}

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;

    private List<MapPointsClass> data;
    ILoadMore loadMore;
    private boolean isLoading;
    private Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItemPosition, totalItemsCount;
    private RecyclerView recyclerView;

    public static final String EXTRA_ID = "com.example.yourplace.id";
    public static final String EXTRA_NAME = "com.example.yourplace.name";
    public static final String EXTRA_COUNTRY = "com.example.yourplace.country";
    public static final String EXTRA_LAT = "com.example.yourplace.lat";
    public static final String EXTRA_LON = "com.example.yourplace.lon";

    Adapter(Activity activity, RecyclerView recyclerView, List<MapPointsClass> data) {
        this.activity = activity;
        this.data = data;
        this.recyclerView = recyclerView;

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                assert manager != null;
                totalItemsCount = manager.getItemCount();
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if (!isLoading && totalItemsCount < (lastVisibleItemPosition + visibleThreshold)){
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.element_view, viewGroup, false);
            view.setOnClickListener(v -> {
                MapPointsClass mapPointsClass = data.get(recyclerView.getChildLayoutPosition(v));
                Intent intent = new Intent(activity, MapActivity.class);
                intent.putExtra(EXTRA_ID, mapPointsClass.getId());
                intent.putExtra(EXTRA_NAME, mapPointsClass.getName());
                intent.putExtra(EXTRA_COUNTRY, mapPointsClass.getCountry());
                intent.putExtra(EXTRA_LAT, mapPointsClass.getLat());
                intent.putExtra(EXTRA_LON, mapPointsClass.getLon());
                view.getContext().startActivity(intent);
            });
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.loading_view, viewGroup, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.textTitle.setText(data.get(position).getName());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setLoaded(){
        isLoading = false;
    }
}
