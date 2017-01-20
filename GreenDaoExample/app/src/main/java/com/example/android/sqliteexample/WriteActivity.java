package com.example.android.sqliteexample;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.sqliteexample.DBUtil.MemoDbHelper;
import com.example.android.sqliteexample.dao.DaoMaster;
import com.example.android.sqliteexample.dao.DaoSession;
import com.example.android.sqliteexample.dao.Memo;
import com.example.android.sqliteexample.dao.MemoDao;

import android.support.v7.app.ActionBar;

public class WriteActivity extends AppCompatActivity {

    //GreenDao
    private SQLiteDatabase mDb;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private MemoDao mMemoDao;

    private EditText mEditTxtTitle;
    private EditText mEditTxtContent;

    private String mFlag;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mEditTxtTitle = (EditText) findViewById(R.id.edit_title);
        mEditTxtContent = (EditText) findViewById(R.id.edit_content);

        MemoDbHelper empDb = new MemoDbHelper(this);
        mDb = empDb.getWritableDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra("modify")){
            mFlag = "modify";
            mEditTxtTitle.setText(intent.getStringExtra("title"));
            mEditTxtContent.setText(intent.getStringExtra("content"));
            mId = intent.getStringExtra("_id");
        }else{
            mFlag = "insert";
        }

        //GreenDao
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "green.db", null);
        mDb = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
        mMemoDao = mDaoSession.getMemoDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_memo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_memo:
                finish();
                saveMemo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveMemo(){
        String title = mEditTxtTitle.getText().toString();
        String content = mEditTxtContent.getText().toString();

        if(mFlag.equals("modify")){
            Memo memo = new Memo(Long.valueOf(mId), title, content);
            mMemoDao.update(memo);
        }else{
            Memo memo = new Memo(null, title, content);
            mMemoDao.insert(memo);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
