package com.basebox.roomwordssample.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.basebox.roomwordssample.entities.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("Delete FROM word_table")
    void deleteAll();

    @Query("SELECT * FROM word_table LIMIT 1")
    Word[] getAnyWord();

    @Query("SELECT * FROM word_table ORDER BY word ASC ")
    LiveData<List<Word>> getAllWords();
}
