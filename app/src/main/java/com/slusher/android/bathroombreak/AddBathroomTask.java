package com.slusher.android.bathroombreak;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AddBathroomTask extends AsyncTask<Bathroom, Void, Boolean> {
    private final String TAG = "AddBathroomTask";
    private Context _context;

    public AddBathroomTask(Context context)
    {
        _context = context;
    }

    @Override
    protected Boolean doInBackground(Bathroom... bathrooms) {
        boolean isSuccess = false;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(_context);

        try{
            for(Bathroom bathroom : bathrooms)
            {
                isSuccess = databaseAccess.addBathroom(bathroom);
            }
        }catch (Exception e){
            Log.e(TAG, "Error adding bathroom " + e.toString());
            return false;
        }

        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //TextView txt = (TextView) findViewById(R.id.output);
        //txt.setText("Executed"); // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
        Toast.makeText(_context, "Finished executing", Toast.LENGTH_LONG);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
