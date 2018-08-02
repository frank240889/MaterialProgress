package franco.dev.mx.materialprogressexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.dev.materialprogress.MaterialProgress;

public class MainActivity extends AppCompatActivity implements MaterialProgress.CancellableAction {
    MaterialProgress materialProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        materialProgress = findViewById(R.id.material_progress);
        FloatingActionButton floatingActionButton2 = findViewById(R.id.show_progress);
        FloatingActionButton floatingActionButton1 = findViewById(R.id.change_progress);
        FloatingActionButton floatingActionButton = findViewById(R.id.hide_progress);

        final EditText editText = findViewById(R.id.message_string);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "Showing snackbar with cancel action.";
                if(!editText.getText().toString().isEmpty())
                    str = editText.getText().toString();

                materialProgress.showWithSnackbar(str,MainActivity.this);

            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                    return;
                String str = editText.getText().toString();
                materialProgress.showWithSnackbar(str);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialProgress.hideProgress();

            }
        });

        Button button = findViewById(R.id.button_show_progress);
        Button button1 = findViewById(R.id.button_change_progress);
        Button button2 = findViewById(R.id.button_hide_progress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "Showing with no snackbar.";
                if(!editText.getText().toString().isEmpty())
                    str = editText.getText().toString();

                materialProgress.show(str);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                    return;

                materialProgress.show(editText.getText().toString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialProgress.hideProgress();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCancel() {
        Toast.makeText(getApplicationContext(),R.string.cancelled,Toast.LENGTH_SHORT).show();
    }
}
