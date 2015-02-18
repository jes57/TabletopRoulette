package com.garufa.tabletoproulette;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jason on 2/17/2015.
 */
public class CollectionListView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        // Get the array for the listView
        Resources res = getResources();
        String[] gamesArray = res.getStringArray(R.array.games_array);

        // Assign the array to the listView
        final ListAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.adapter_layout, gamesArray);

        // Assign ArrayList
        ArrayList<String> gamesArrayList = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this,
                R.layout.adapter_layout, R.id.textViewAdapter, gamesArrayList);

        for (int i = 0; i < gamesArray.length; i++){
            gamesArrayList.add(gamesArray[i]);
        }

        // Set the ListView
        ListView memoryTestListView = (ListView) findViewById(R.id.collectionListView);
        memoryTestListView.setAdapter(arrayAdapter);


        // Set the onClick event
        memoryTestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gamePicked = "You selected " +
                        String.valueOf(parent.getItemAtPosition(position));

                arrayAdapter.add("New game added");

                Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();
//
//                Intent quizIntent = new Intent(CollectionListView.this, Quiz.class);
//
//                quizIntent.putExtra("quizNumber", position);
//                startActivity(quizIntent);
//                if (position == 0){
//                    Intent test1_intent = new Intent(MainActivity.this, QuizImage1.class);
//                    startActivity(test1_intent);
//                }
            }
        });
        // Set the OnItemClickListener
//        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                ((TextView)v).setText("selected");
//            }
//        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.collection_layout);
//
//        // Get the array for the listView
//        Resources res = getResources();
//        String[] memoryTestArray = res.getStringArray(R.array.games_array);
//
//        // Assign the array to the listView
//        final ListAdapter adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, memoryTestArray);
//
//        // Set the ListView
//        ListView memoryTestListView = (ListView) findViewById(R.id.quizListView);
//        memoryTestListView.setAdapter(adapter);
//
//        // Set the onClick event
//        memoryTestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String testPicked = "You selected " +
//                        String.valueOf(parent.getItemAtPosition(position));
//
//                Toast.makeText(MainActivity.this, testPicked, Toast.LENGTH_SHORT).show();
//
//                Intent quizIntent = new Intent(MainActivity.this, Quiz.class);
//
//                quizIntent.putExtra("quizNumber", position);
//                startActivity(quizIntent);
////                if (position == 0){
////                    Intent test1_intent = new Intent(MainActivity.this, QuizImage1.class);
////                    startActivity(test1_intent);
////                }
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.exit_the_app) {
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
