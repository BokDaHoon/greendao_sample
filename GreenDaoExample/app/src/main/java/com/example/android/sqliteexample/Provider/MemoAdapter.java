package com.example.android.sqliteexample.Provider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sqliteexample.DetailActivity;
import com.example.android.sqliteexample.R;
import com.example.android.sqliteexample.dao.MemoDao;


/**
 * Created by DaHoon on 2017-01-14.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private static int mCount;
    private static MemoDao mMemoDao;
    private static Context mContext;

    public MemoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MemoAdapter.MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        MemoViewHolder viewHolder = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list_item, parent, false);
        viewHolder = new MemoViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemoAdapter.MemoViewHolder holder, int position) {
        // SQLite Version

        holder.mTitleOfMemo.setText(mMemoDao.loadAll().get(position).getTitle());
        holder.itemView.setTag(mMemoDao.loadAll().get(position).getId());

    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleOfMemo;

        public MemoViewHolder(View itemView) {
            super(itemView);
            mTitleOfMemo = (TextView) itemView.findViewById(R.id.memo_title);
            mTitleOfMemo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("id", itemView.getTag().toString());

            mContext.startActivity(intent);
        }
    }

    public void swapCursor(MemoDao data){
        mCount = data.loadAll().size();
        mMemoDao = data;

        this.notifyDataSetChanged();
    }
}
