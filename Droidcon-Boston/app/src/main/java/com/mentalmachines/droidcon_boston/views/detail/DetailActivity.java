package com.mentalmachines.droidcon_boston.views.detail;

/**
 * Created by jinn on 2/27/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mentalmachines.mvptemplate.R;
import com.mentalmachines.mvptemplate.views.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

import timber.log.Timber;

public class DetailActivity extends BaseActivity implements DetailView, ErrorView.ErrorListener {

    public static final String EXTRA_POKEMON_NAME = "EXTRA_POKEMON_NAME";

    @Inject
    DetailPresenter mDetailPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    ImageView mPokemonImage;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static Intent getStartIntent(Context context, String pokemonName) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_POKEMON_NAME, pokemonName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mDetailPresenter.attachView(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mErrorView.setErrorListener(this);

        mDetailPresenter.getPokemon(mPokemonName);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }


    @Override
    public void showProgress(boolean show) {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        mPokemonLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was a problem retrieving the pokemon...");
    }

    @Override
    public void onReloadData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.detachView();
    }
}