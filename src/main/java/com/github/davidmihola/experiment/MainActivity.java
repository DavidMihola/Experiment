package com.github.davidmihola.experiment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

//        final Subscription subscription = Observable.create(new Observable.OnSubscribe<Long>() {
//
//            @Override
//
//            public void call(Subscriber<? super Long> subscriber) {
//
//                Log.d(TAG, "call test observable");
//
//                subscriber.onError(null);
//
//            }
//
//        }).retry(2000).subscribeOn(Schedulers.io()).subscribe();
//
//        Log.d(TAG, "subscribed");
//
//        Observable.just(Boolean.TRUE)
//                .delay(500, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Boolean>() {
//
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        subscription.unsubscribe();
//                        Log.d(TAG, "unsubscribed");
//                    }
//                });
//
//        Log.d(TAG, "init complete");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private MainActivity mActivity;

		public PlaceholderFragment() {
        }

        @Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.mActivity = (MainActivity) activity;
		}

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button startExperimentButton = (Button) rootView.findViewById(R.id.startExperimentButton);
            startExperimentButton.setOnClickListener(new View.OnClickListener() {
                
            	@Override
            	public void onClick(View view) {
                    mActivity.startExperiment();
                }
            });
            return rootView;
        }
    }

    public void startExperiment() {
        Intent intent = new Intent(this, ExperimentActivity.class);
        startActivity(intent);
    }

}