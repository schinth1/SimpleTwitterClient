package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by sc043016 on 8/6/16.
 */
public class ComposeActivity extends AppCompatActivity {
    private final int TWEET_RESPONSE_CODE = 1;
    @Bind(R.id.btnCompose)
    Button btnCompose;
    @Bind(R.id.etCompose)
    EditText etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ButterKnife.bind(this);

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                i.putExtra("tweet", etCompose.getText().toString());
                setResult(TWEET_RESPONSE_CODE, i);
                finish();
            }
        });
    }

}
