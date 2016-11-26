package com.strajerii.parkingguardian.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.strajerii.parkingguardian.Drone.PlateNumberDB;
import com.strajerii.parkingguardian.R;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.openalpr.OpenALPR;
import org.openalpr.model.Result;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlateRecognitionActivity extends AppCompatActivity
{
    private static final int STORAGE = 1;
    private String ANDROID_DATA_DIR;
    private TextView resultTextView;

    ArrayList<String> parkedPlates = new ArrayList<>();

    private static final String lineSeparator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platerecognition);

        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        resultTextView = (TextView) findViewById(R.id.textView);
        resultTextView.setText("Press the button below to start a request.");
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, STORAGE);
        } else { // We already have permissions, so handle as normal
            processFrames();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE:{
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // permission was granted, yay!
                    processFrames();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Storage permission is needed to analyse the picture.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public void processFrames() {
        // Use a folder to store all results
        final File folderPics = new File(Environment.getExternalStorageDirectory() + "/ParkingGuardian/");
        if (!folderPics.exists()) {
            resultTextView.setText("No pictures found" + lineSeparator);
            return;
        }
//
//        // Generate the path for the next photo
//        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
//        destination = new File(folder, name + ".jpg");

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
//        startActivityForResult(intent, REQUEST_IMAGE);

        final ProgressDialog progress = ProgressDialog.show(this, "Loading", "Parsing result...", true);
        final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

        resultTextView.setText("Processing" + lineSeparator);
        resultTextView.setMovementMethod(new ScrollingMovementMethod());

        final OpenALPR alpr = OpenALPR.Factory.create(PlateRecognitionActivity.this, ANDROID_DATA_DIR);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                File[] fileList = folderPics.listFiles();
                if ( fileList == null || fileList.length == 0 ) {
                    resultTextView.setText("No pictures found" + lineSeparator);
                    return;
                }

                parkedPlates.clear();

                for ( int i = 0; i < fileList.length; i++ ) {
                    File file = fileList[i];
                    String absoluteFilePath = file.getAbsolutePath().toLowerCase();
                    if ( absoluteFilePath.contains( ".jpg" ) && absoluteFilePath.contains( "bebop2" ) ) {
                        Log.d( "OPEN ALPR", "Found file at absolute path: " + file.getAbsolutePath() );
                        Log.d( "OPEN ALPR", "Found file with path: " + file.getPath() );
                        Log.d( "OPEN ALPR", "Found file with name: " + file.getName() );

                        String result = alpr.recognizeWithCountryRegionNConfig("eu", "", absoluteFilePath, openAlprConfFile, 10);

                        Log.d("OPEN ALPR", result );

                        addRecognitionResult( result, file.getName() );
                    }
                    if ( i == fileList.length - 1 ) {
                        makeToast("Processing finished");
                    }
                }

                PlateNumberDB platesDB = new PlateNumberDB();
                ArrayList<String> foreignPlates = platesDB.checkPlates(parkedPlates);
                if (foreignPlates.size() > 0) {
                    Intent intent = new Intent(PlateRecognitionActivity.this, ForeignPlatesActivity.class);
                    intent.putExtra("ForeignPlates", foreignPlates);
                    startActivity(intent);
                } else {
                    Toast.makeText( PlateRecognitionActivity.this, "No foreign cars", Toast.LENGTH_LONG ).show();
                }

                progress.dismiss();
            }
        });
    }

    private void addRecognitionResult( String strResult, String strFileName )
    {
        String message = "Output for " + strFileName + lineSeparator;
        try {
            final Results results = new Gson().fromJson( strResult, Results.class );
            if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                message += "It was not possible to detect the licence plate.";
            } else {
                int iPlates =  results.getResults().size();
                for ( int i = 0; i < iPlates; i++ ) {
                    Result result = results.getResults().get( i );
                    message += "Plate " + i + ":" + result.getPlate() + lineSeparator
                            // Trim confidence to two decimal places
                            + " Confidence: " + String.format("%.2f", result.getConfidence()) + "%" + lineSeparator;
                    parkedPlates.add(result.getPlate());
                }
                // Convert processing time to seconds and trim to two decimal places
                message += " Processing time: " + String.format("%.2f", ((results.getProcessing_time_ms() / 1000.0) % 60)) + " seconds";
            }
        } catch ( JsonSyntaxException exception ) {
            try {
                final ResultsError resultsError = new Gson().fromJson(strResult, ResultsError.class);
                message += resultsError.getMsg();
            } catch ( JsonSyntaxException e ) {
                message += "Failed to process error result. Exception " + e.getMessage();
            }
        }
        printInTextView( message + lineSeparator );
    }

    private void printInTextView( final String message )
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.append( message );
            }
        });
    }

    private void makeToast( final String message )
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText( PlateRecognitionActivity.this, message, Toast.LENGTH_LONG ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
