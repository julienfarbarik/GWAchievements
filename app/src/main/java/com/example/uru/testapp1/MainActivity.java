package com.example.uru.testapp1;

import android.os.Bundle;
import android.os.Process;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Retrieving Achievements...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                testMe();
            }
        });

    // Example of a call to a native method
    TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());
    }

    public void testMe()
    {
//        try{

            APIAccess jsonDisp = new APIAccess();
            Thread jDThread = new Thread(jsonDisp);
            jDThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

            try {
                jDThread.start();
                while(jDThread.getState() != Thread.State.TERMINATED){}          ///implement as executor? This freezes main thread. Bad.
                HashMap<Integer, GWAchievement> result = new HashMap<>(jsonDisp.getResult());

                String ListOfStuff = "";
                for (int i = 0; i < result.size(); ++i){
                    ListOfStuff += result.get(i).getName();
                    ListOfStuff += ", ";
                    ListOfStuff += result.get(i).getObjective();
                    ListOfStuff += ", ";
                }
                TextView tv = (TextView) findViewById(R.id.sample_text);
                tv.setText(ListOfStuff);

            }
            catch(Exception e)
            {
                printErrorToScreen(e.toString());
            }
//            catch(java.lang.InterruptedException e) {printErrorToScreen("get was interrupted");}
//            catch(java.util.concurrent.ExecutionException e) {printErrorToScreen("get failed to execute");}
//            catch(java.util.concurrent.TimeoutException e) {printErrorToScreen("get timed out");}
//        }
//        catch(java.net.MalformedURLException e){
//            throw new RuntimeException();
//        }
    }

    public void printErrorToScreen(String eText)
    {
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(eText);
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
