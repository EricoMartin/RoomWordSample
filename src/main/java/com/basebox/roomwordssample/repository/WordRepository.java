package com.basebox.roomwordssample.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import com.basebox.roomwordssample.dao.WordDao;
import com.basebox.roomwordssample.db.WordRoomDatabase;
import com.basebox.roomwordssample.entities.Word;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mWords, mWordTasked;

    public WordRepository(Application application){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mWords = mWordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords(){
        return mWords;
    }

    public void insert(Word word){

//        new insertAsyncTask(mWordDao).execute(word);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler mHandler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                mWordDao.insert(word);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    public void deleteAll()  {

        new deleteAllWordsAsyncTask(mWordDao).execute();
    }

//    private static  class insertAsyncTask extends AsyncTask<Word, Void, Void> {
//
//        private WordDao mAsyncTaskDao;
//
//        insertAsyncTask(WordDao dao){
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Word... words) {
//            mAsyncTaskDao.insert(words[0]);
//            return null;
//        }
//    }

    private static class deleteAllWordsAsyncTask extends  AsyncTask<Word, Void, Void>{

        private WordDao mDeleteWord;
        
        deleteAllWordsAsyncTask(WordDao word){
            mDeleteWord = word;
        }
        @Override
        protected Void doInBackground(Word... words) {
            mDeleteWord.deleteAll();
            return null;
        }
    }
}
