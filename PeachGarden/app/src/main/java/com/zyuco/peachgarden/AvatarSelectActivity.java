package com.zyuco.peachgarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AvatarSelectActivity extends AppCompatActivity {
    public static final int SELECT_AVARTAR = 0x233;
    private static final int AVATAR_PRESET_SIZE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_select);

        setListeners();
    }

    private void setListeners() {
        // stupid solution. for time's sake
        for (int i = 1; i <= AVATAR_PRESET_SIZE; i++) {
            int id = getResources().getIdentifier("avatar_" + String.valueOf(i), "id", getPackageName());
            final int finalI = i;
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeResult(finalI);
                }
            });
        }
    }

    private void makeResult(int avatarId) {
        Log.i("PeachGarden", String.format("makeResult: %d", avatarId));
        Intent intent = new Intent();
        intent.putExtra("avatar", avatarId);
        setResult(SELECT_AVARTAR, intent);
        finish(); // close
    }
}
