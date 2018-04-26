package com.udacity.sandwichclub;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ActivityDetailBinding activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (Exception e) {
            Log.e(TAG, "Unable to parse the sandwich info.", e);
        }

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich, activityDetailBinding);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(activityDetailBinding.imageIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich, ActivityDetailBinding activityDetailBinding) {
        activityDetailBinding.originTv.setText(sandwich.getPlaceOfOrigin());
        activityDetailBinding.descriptionTv.setText(sandwich.getDescription());
        activityDetailBinding.alsoKnownTv.setText(concatenate(sandwich.getAlsoKnownAs()));
        activityDetailBinding.ingredientsTv.setText(concatenate(sandwich.getIngredients()));
    }

    private String concatenate(List<String> stringParts) {
        String concatenatedParts = "";
        for (String stringPart : stringParts) {
            if (!"".equalsIgnoreCase(concatenatedParts)) {
                concatenatedParts = concatenatedParts + ", ";
            }
            concatenatedParts = concatenatedParts + stringPart;
        }
        return concatenatedParts;
    }
}
