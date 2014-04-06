package com.github.davidmihola.experiment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;

import com.github.davidmihola.experiment.model.Answer;
import com.github.davidmihola.experiment.model.Item;

public class ExperimentActivity extends Activity implements ExperimentFragment.ItemAnsweredListener {

    private static final String TAG = "ExperimentActivity";
    private ImmutableList<Answer> allPossibleAnswers;
    private int numberOfAnswersPerItem;

    private int itemsShown = 0;
    private int itemsCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        //suppose that for one run of the experiment the available answers stay the same:
        // for example: all red ones
        allPossibleAnswers = Answer.getAnswers()
//                .withColor(Answer.Color.RED)
                .now();

        // the same goes for the number of answers per item:
        numberOfAnswersPerItem = 2;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.experiment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showNextItem();
    }

    private void showNextItem() {
        itemsShown ++;
        Item item = Item.newItem(allPossibleAnswers, numberOfAnswersPerItem);

        Fragment fragment = ExperimentFragment.newInstance(item);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }

    @Override
    public void onItemAnswered(Item item, Answer answer) {
        boolean answerIsCorrect = answer.equals(item.getCorrectAnswer());
        Toast.makeText(this, "Answer " + answer + " is " + answerIsCorrect, Toast.LENGTH_SHORT).show();

        if (answerIsCorrect) {
            itemsCorrect ++;
        }

        float correctRatio = itemsCorrect / (float) itemsShown;
        Log.d(TAG, "correctRatio = " + correctRatio);

        if (correctRatio > 0.5) {
            showNextItem();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_experiment, container, false);
            return rootView;
        }
    }

}