package com.basebox.roomwordssample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basebox.roomwordssample.entities.Word;
import com.basebox.roomwordssample.repository.CustomReceiver;
import com.basebox.roomwordssample.word.WordListAdapter;
import com.basebox.roomwordssample.word.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private View mView;
    private WordViewModel mWordViewModel;
    private CustomReceiver mCustomReceiver = new CustomReceiver();
    private IntentFilter mFilter;
    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_first, container, false);
        return mView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
        FloatingActionButton fab = mView.getRootView().findViewById(R.id.fab);
        Button braodast_button = mView.findViewById(R.id.sendBroadcast);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getActivity(), NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
        RecyclerView recyclerView = mView.findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        mWordViewModel.getAllWords().observe(getActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                adapter.setWords(words);
            }
        });

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);


    }

    @Override
    public void onResume() {
        getContext().registerReceiver(mCustomReceiver, mFilter);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mCustomReceiver,
                        new IntentFilter(ACTION_CUSTOM_BROADCAST));
        super.onResume();
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(mCustomReceiver);
        LocalBroadcastManager.getInstance(getContext())
                .unregisterReceiver(mCustomReceiver);
        super.onPause();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(getActivity(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }
}