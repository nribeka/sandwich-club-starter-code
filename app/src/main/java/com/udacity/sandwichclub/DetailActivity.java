package com.udacity.sandwichclub;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
        activityDetailBinding.imageIv.setContentDescription(sandwich.getMainName());
        activityDetailBinding.detailElements.descriptionTv.setText(sandwich.getDescription());
        activityDetailBinding.detailElements.ingredientsTv.setText(concatenate(sandwich.getIngredients()));
        if (!sandwich.getAlsoKnownAs().isEmpty()) {
            activityDetailBinding.detailElements.alsoKnownLabelTv.setVisibility(View.VISIBLE);
            activityDetailBinding.detailElements.alsoKnownTv.setVisibility(View.VISIBLE);
            activityDetailBinding.detailElements.alsoKnownTv.setText(concatenate(sandwich.getAlsoKnownAs()));
        } else {
            activityDetailBinding.detailElements.alsoKnownLabelTv.setVisibility(View.GONE);
            activityDetailBinding.detailElements.alsoKnownTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sandwich.getPlaceOfOrigin())) {
            activityDetailBinding.detailElements.originLabelTv.setVisibility(View.VISIBLE);
            activityDetailBinding.detailElements.originTv.setVisibility(View.VISIBLE);
            activityDetailBinding.detailElements.originTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            activityDetailBinding.detailElements.originLabelTv.setVisibility(View.GONE);
            activityDetailBinding.detailElements.originTv.setVisibility(View.GONE);
        }
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
