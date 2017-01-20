package com.example.android.sqliteexample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sqliteexample.DBUtil.MemoDbHelper;
import com.example.android.sqliteexample.DBUtil.MemoScheme.MemoTable;
import com.example.android.sqliteexample.Provider.MemoAdapter;
import com.example.android.sqliteexample.dao.DaoMaster;
import com.example.android.sqliteexample.dao.DaoSession;
import com.example.android.sqliteexample.dao.MemoDao;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //GreenDao
    private SQLiteDatabase mDb;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private MemoDao mMemoDao;

    private static final int TASK_LOADER_ID = 0;

    private RecyclerView mMemoList;
    private MemoAdapter mMemoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMemoList = (RecyclerView) findViewById(R.id.memo_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mMemoAdapter = new MemoAdapter(this);

        mMemoList.setAdapter(mMemoAdapter);
        mMemoList.setLayoutManager(layoutManager);

        MemoDbHelper memoDbHelper = new MemoDbHelper(this);

        //GreenDao
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "green.db", null);
        mDb = mDevOpenHelper.getReadableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
        mMemoDao = mDaoSession.getMemoDao();

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_memo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.go_add_memo:
                goAddMemo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void goAddMemo(){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }

    private void updateGuest(int eno, String ename, String job, int manager,
                             String hiredate, int salary, int commission, int dno) {
        ContentValues cv = new ContentValues();
        cv.put(MemoTable.MemoEntry.COLUMN_TITLE, ename);
        cv.put(MemoTable.MemoEntry.COLUMN_CONTENT, job);

        mDb.update(MemoTable.TABLE_NAME,
                cv,
                MemoTable.MemoEntry._ID + " = ?",
                new String[]{String.valueOf(eno)});
    }

    private boolean removeGuest(long id) {

        return mDb.delete(MemoTable.TABLE_NAME, MemoTable.MemoEntry._ID + "=" + id, null) > 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData;

            @Override
            protected void onStartLoading() {
                if(mTaskData != null){
                    deliverResult(mTaskData);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try{
                    return mDb.query(mMemoDao.getTablename(), mMemoDao.getAllColumns(),
                            null, null, null, null, null);
                }catch(Exception e){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMemoAdapter.swapCursor(mMemoDao);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
