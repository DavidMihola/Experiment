package com.github.davidmihola.experiment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.davidmihola.experiment.model.Answer;
import com.github.davidmihola.experiment.model.Item;
import com.github.davidmihola.experiment.timer.StopTimer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by david on 05.03.14.
 */
public class ExperimentFragment extends Fragment {

    private static final String TAG = "ExperimentFragment";
    private static final String ITEM = "ITEM";

    private ItemAnsweredListener listener;
    private Item item;
    private GridView itemGrid;

    private final ImmutableMap<Answer, Integer> imageMap = ImmutableMap.<Answer, Integer>builder()
            .put(new Answer(Answer.Size.BIG, Answer.Color.RED, Answer.Shape.ROUND, Answer.Thing.FACE), R.drawable.big_red_round_face)
            .put(new Answer(Answer.Size.BIG, Answer.Color.RED, Answer.Shape.SQUARE, Answer.Thing.FACE), R.drawable.big_red_square_face)
            .put(new Answer(Answer.Size.BIG, Answer.Color.RED, Answer.Shape.ROUND, Answer.Thing.HOUSE), R.drawable.big_red_round_house)
            .put(new Answer(Answer.Size.BIG, Answer.Color.RED, Answer.Shape.SQUARE, Answer.Thing.HOUSE), R.drawable.big_red_square_house)
            .put(new Answer(Answer.Size.BIG, Answer.Color.BLUE, Answer.Shape.ROUND, Answer.Thing.FACE), R.drawable.big_blue_round_face)
            .put(new Answer(Answer.Size.BIG, Answer.Color.BLUE, Answer.Shape.SQUARE, Answer.Thing.FACE), R.drawable.big_blue_square_face)
            .put(new Answer(Answer.Size.BIG, Answer.Color.BLUE, Answer.Shape.ROUND, Answer.Thing.HOUSE), R.drawable.big_blue_round_house)
            .put(new Answer(Answer.Size.BIG, Answer.Color.BLUE, Answer.Shape.SQUARE, Answer.Thing.HOUSE), R.drawable.big_blue_square_house)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.RED, Answer.Shape.ROUND, Answer.Thing.FACE), R.drawable.small_red_round_face)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.RED, Answer.Shape.SQUARE, Answer.Thing.FACE), R.drawable.small_red_square_face)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.RED, Answer.Shape.ROUND, Answer.Thing.HOUSE), R.drawable.small_red_round_house)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.RED, Answer.Shape.SQUARE, Answer.Thing.HOUSE), R.drawable.small_red_square_house)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.BLUE, Answer.Shape.ROUND, Answer.Thing.FACE), R.drawable.small_blue_round_face)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.BLUE, Answer.Shape.SQUARE, Answer.Thing.FACE), R.drawable.small_blue_square_face)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.BLUE, Answer.Shape.ROUND, Answer.Thing.HOUSE), R.drawable.small_blue_round_house)
            .put(new Answer(Answer.Size.SMALL, Answer.Color.BLUE, Answer.Shape.SQUARE, Answer.Thing.HOUSE), R.drawable.small_blue_square_house)
            .build();

    private ImageView answer1;
    private ImageView answer2;
    private StopTimer stopTimer;

    public static ExperimentFragment newInstance(Item item) {
        ExperimentFragment fragment = new ExperimentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.item = (Item) getArguments().getSerializable(ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment, null);

        answer1 = (ImageView) view.findViewById(R.id.answer1);
        answer2 = (ImageView) view.findViewById(R.id.answer2);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ItemAnsweredListener) activity;
    }

    @Override
    public void onResume() {
        super.onResume();

        showHint();
    }

    private void showHint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Click the " + item.getCorrectAnswer());

        final Dialog dialog;
        dialog = builder.create();
        dialog.show();

        Observable.just(Boolean.TRUE)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>(){

                    @Override
                    public void call(Boolean aBoolean) {
                        dialog.cancel();
                        dialog.dismiss();
                        showAnswers();
                    }
                });
    }

    private void showAnswers()  {
        final Item item = this.item;

        answer1.setImageResource(imageMap.get(item.getAnswers().get(0)));
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "item0 clicked");
                listener.onItemAnswered(item, item.getAnswers().get(0));
            }
        });
        answer2.setImageResource(imageMap.get(item.getAnswers().get(1)));
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "item1 clicked");
                listener.onItemAnswered(item, item.getAnswers().get(1));
            }
        });

        stopTimer = new StopTimer();

        Observable.just(Boolean.TRUE)
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>(){

                    @Override
                    public void call(Boolean aBoolean) {
                        hideAnswers();
                    }
                });
    }

    private void hideAnswers() {
        answer1.setImageResource(R.drawable.questionmark);
        answer2.setImageResource(R.drawable.questionmark);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ItemAnsweredListener {
        public void onItemAnswered(Item item, Answer answer);
    }

}