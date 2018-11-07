package com.termoneplus.sample.intents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class IntentSampleActivity extends AppCompatActivity {
    private static final String ACTION_OPEN_NEW_WINDOW = "com.termoneplus.OPEN_NEW_WINDOW";
    private static final String ACTION_RUN_SCRIPT = "com.termoneplus.RUN_SCRIPT";

    private static final String RUN_SCRIPT_WINDOW_HANDLE = "com.termoneplus.WindowHandle";
    private static final String RUN_SCRIPT_COMMAND = "com.termoneplus.Command";

    private static final int REQUEST_WINDOW_HANDLE = 1;
    private String mHandle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addClickListener(R.id.openNewWindow, v -> {
            // Intent for opening a new window without providing script
            Intent intent = new Intent(ACTION_OPEN_NEW_WINDOW)
                    .addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        });

        final EditText script = findViewById(R.id.script);
        script.setText(getString(R.string.default_script));
        addClickListener(R.id.runScript, v -> {
            /* Intent for opening a new window and running the provided
               script -- you must declare the permission
               com.termoneplus.permission.RUN_SCRIPT in your manifest
               to use */
            String command = script.getText().toString();
            Intent intent = new Intent(ACTION_RUN_SCRIPT)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra(RUN_SCRIPT_COMMAND, command);
            startActivity(intent);
        });
        addClickListener(R.id.runScriptSaveWindow, v -> {
            /* Intent for running a script in a previously opened window,
               if it still exists
               This will open another window if it doesn't find a match */
            String command = script.getText().toString();
            Intent intent = new Intent(ACTION_RUN_SCRIPT)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra(RUN_SCRIPT_COMMAND, command);
            if (mHandle != null) {
                // Identify the targeted window by its handle
                intent.putExtra(RUN_SCRIPT_WINDOW_HANDLE, mHandle);
            }
            /* The handle for the targeted window -- whether newly opened
               or reused -- is returned to us via onActivityResult()
               You can compare it against an existing saved handle to
               determine whether or not a new window was opened */
            startActivityForResult(intent, REQUEST_WINDOW_HANDLE);
        });
    }

    private void addClickListener(int buttonId, OnClickListener onClickListener) {
        findViewById(buttonId).setOnClickListener(onClickListener);
    }

    protected void onActivityResult(int request, int result, Intent data) {
        if (result != RESULT_OK) {
            return;
        }

        if (request == REQUEST_WINDOW_HANDLE && data != null) {
            mHandle = data.getStringExtra(RUN_SCRIPT_WINDOW_HANDLE);
            ((Button) findViewById(R.id.runScriptSaveWindow)).setText(
                    R.string.run_script_existing_window);
        }
    }
}