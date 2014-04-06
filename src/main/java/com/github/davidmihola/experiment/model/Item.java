package com.github.davidmihola.experiment.model;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by david on 05.03.14.
 */

public final class Item implements Serializable {

    private final ImmutableList<Answer> answers;
    private final Answer correctAnswer;

    private static Random random = new Random();

    public Item(ImmutableList<Answer> answers, Answer correctAnswer) {
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public ImmutableList<Answer> getAnswers() {
        return answers;
    }

    public static Item newItem(final ImmutableList<Answer> possibleAnswers, int numberOfAnswers) {

        // 1. pick of of the possible answers randomly to be the correct answers
        final Answer correctAnswer = possibleAnswers.get(random.nextInt(possibleAnswers.size()));

        // 2. pick the remaining answerws randomly from possible

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                while(true) {
                    subscriber.onNext(random.nextInt(possibleAnswers.size()));
                }
            }
        })
        .map(new Func1<Integer, Answer>() {
            @Override
            public Answer call(Integer integer) {
                return possibleAnswers.get(integer);
            }
        })
        .filter(new Func1<Answer, Boolean>() {
            @Override
            public Boolean call(Answer answer) {
                return ! answer.equals(correctAnswer);
            }
        })
        .take(numberOfAnswers - 1)
        .subscribe(new Subscriber<Answer>(){

            ImmutableList.Builder<Answer> builder = ImmutableList.<Answer>builder();

            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Answer answer) {
                builder.add(answer);
            }
        });
//        List<Answer> shuffled = new ArrayList(possibleAnswers);
//        Collections.shuffle(shuffled);
//
//        ImmutableList<Answer> answersUsedInNewItem = ImmutableList.copyOf(shuffled.subList(0, numberOfAnswers));
//        Answer correctAnswer = answersUsedInNewItem.get(random.nextInt(numberOfAnswers));
//
        return new Item(null, correctAnswer);
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }
}
