package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.ViewHolder> {

    private ArrayList<Map<String, Object>> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView createTime;
        private final TextView content;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_cont);
            createTime = (TextView) view.findViewById(R.id.create_time_cont);
            content = (TextView) view.findViewById(R.id.content_cont);
        }

        public TextView getTitleView() {
            return title;
        }

        public TextView getCreateTimeView() {
            return createTime;
        }

        public TextView getContentView() {
            return content;
        }
    }

    public DashboardListAdapter(ArrayList<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dashboard_item, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Map<String, Object> item = localDataSet.get(position);

        viewHolder.getTitleView().setText(item.get("title").toString());
        viewHolder.getContentView().setText(item.get("content").toString());

        Date date = ((Timestamp) item.get("create_time")).toDate();
        viewHolder.getCreateTimeView().setText(
                new SimpleDateFormat("yyyy-MM-dd").format(date)
        );
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
