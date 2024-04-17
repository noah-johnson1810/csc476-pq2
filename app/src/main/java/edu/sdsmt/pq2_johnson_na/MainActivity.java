package edu.sdsmt.pq2_johnson_na;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.sdsmt.pq2_johnson_na.R;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    //references to the views
    private TextView left;
    private TextView center;
    private TextView right;
    private PQ2_View myView;

    private EditText aText;
    private EditText bText;
    private EditText cText;

    private int doubleDownCount = 0;
    private int yDownCount = 0;
    private int releaseCount = 0;

    private int buttonZeroCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        left = findViewById(R.id.outputLeft);
        center = findViewById(R.id.outputCenter);
        right = findViewById(R.id.outputRight);
        myView = findViewById(R.id.PQ2_View);
        aText = findViewById(R.id.inputA);
        bText = findViewById(R.id.inputB);
        cText = findViewById(R.id.inputC);
    }

    public void onTask0(View view) {
        buttonZeroCount += 1;
        saveToDatabase(String.valueOf(buttonZeroCount), "count");
    }

    public void onTask1(View view) {
        saveToDatabase(String.valueOf(this.doubleDownCount), "countDoubleDown");
        saveToDatabase(String.valueOf(this.yDownCount), "countSingleDown");
        saveToDatabase(String.valueOf(this.releaseCount), "countRelease");
    }

    public void onTask2(View view) {
        saveToDatabase(String.valueOf(this.aText.getText()), "text/a");
        saveToDatabase(String.valueOf(this.bText.getText()), "text/b");
        saveToDatabase(String.valueOf(this.cText.getText()), "text/c");
    }

    public void onTask3(View view) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String databaseContents = dataSnapshot.getValue().toString();
                showDialog(databaseContents, "Database contents");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("main", "error reading database", databaseError.toException());
            }
        });
    }

    private void showDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onTask4(View view) {
        readFromDatabase("text", new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String value = dataSnapshot.getValue().toString();
                    showDialog(value, "Database text node contents");
                } else {
                    showDialog("empty", "Database text node contents");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error (if any)
                // ...
            }
        });
    }


    public void updateUI() {
        //TODO pull values

        //TODO all of these will return 0 right now (fix the gets)
        left.setText(String.valueOf(myView.getDoubleDownCount()));
        center.setText(String.valueOf(myView.getYDownCount()));
        right.setText(String.valueOf(myView.getCrossCount()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle b) {
        super.onRestoreInstanceState(b);

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle b) {
        super.onSaveInstanceState(b);

    }

    public void saveToDatabase(String value, String path) {
        DatabaseReference databaseReference = db.child(path);
        databaseReference.setValue(value);
    }

    /*
    // Example usage
    String path = "myMessage/candle/burntwick";
    readFromDatabase(path, new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String value = dataSnapshot.getValue(String.class);
                Log.i("Emily", value);
            } else {
                // Node does not exist or has no value
                // Handle accordingly
                // ...
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Handle database error (if any)
            // ...
        }
    }); */
    public void readFromDatabase(String path, ValueEventListener valueEventListener) {
        DatabaseReference databaseReference = db.child(path);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public void setDoubleDownCount(int newCount) {
        this.doubleDownCount = newCount;
        left.setText(String.valueOf(this.doubleDownCount));
    }

    public int getDoubleDownCount() {
        return this.doubleDownCount;
    }

    public void setYDownCount(int newCount) {
        this.yDownCount = newCount;
        center.setText(String.valueOf(this.yDownCount));
    }

    public int getYDownCount() {
        return this.yDownCount;
    }

    public void setReleaseCount(int newCount) {
        this.releaseCount = newCount;
        right.setText(String.valueOf(newCount));
    }

    public int getReleaseCount() {
        return this.releaseCount;
    }
}