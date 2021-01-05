package com.example.toy_store_warehouse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toy_store_warehouse.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class ToyDetailActivity extends AppCompatActivity {
    Toy toy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_detail);

        toy = getIntent().getParcelableExtra("toy");

        initToolbar();

        displayToyInfo();

        initFabEditButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toy_detail_menu, menu);

        initDeleteToyMenuIconButton(menu);

        return true;
    }

    private void initDeleteToyMenuIconButton(Menu menu) {
        String id = toy.getId();
        String category = toy.getCategory();

        MenuItem item = menu.findItem(R.id.menu_delete_toy);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new MaterialAlertDialogBuilder(ToyDetailActivity.this)
                        .setTitle("Delete Toy")
                        .setMessage("Are you sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(category).child(id);
                                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(findViewById(android.R.id.content), "Toy successfully deleted.", Snackbar.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return true;
            }
        });
    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_toy_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }
    }

    private void displayToyInfo() {
        Resources res = getResources();

        ImageView imgViewToy = findViewById(R.id.img_view_toy_d);
        TextView txtName = findViewById(R.id.txt_name_d);
        TextView txtCategory = findViewById(R.id.txt_category_d);
        LinearLayout figureLayout = findViewById(R.id.figure_layout_d);
        TextView txtPoseable = findViewById(R.id.txt_poseable_d);
        LinearLayout dollLayout = findViewById(R.id.doll_layout_d);
        TextView txtBattery = findViewById(R.id.txt_battery_d);
        TextView txtPrice = findViewById(R.id.txt_price_d);
        TextView txtQty = findViewById(R.id.txt_qty_d);
        TextView txtReleaseDate = findViewById(R.id.txt_release_date_d);
        TextView txtSpecification = findViewById(R.id.txt_specification_d);

        Picasso.get().load(toy.getImgUri()).into(imgViewToy);
        txtName.setText(toy.getName());
        txtCategory.setText(toy.getCategory());
        txtPrice.setText(res.getString(R.string.d_price, toy.getPrice()));
        txtQty.setText(res.getString(R.string.d_qty, toy.getQty()));
        txtReleaseDate.setText(toy.getReleaseDate());
        txtSpecification.setText(toy.getSpecification());

        if (toy.isFigure()) {
            figureLayout.setVisibility(View.VISIBLE);
            dollLayout.setVisibility(View.GONE);
            txtPoseable.setText(((Figure) toy).isPoseable() ? "Yes" : "No");
        } else if (toy.isDoll()) {
            figureLayout.setVisibility(View.GONE);
            dollLayout.setVisibility(View.VISIBLE);
            txtBattery.setText(((Doll) toy).isHasBattery() ? "Yes" : "No");
        }
    }

    private void initFabEditButton() {
        FloatingActionButton fabEditButton = findViewById(R.id.fab_edit_toy_d);
        fabEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toyFormIntent = new Intent(getApplicationContext(), ToyFormActivity.class);
                toyFormIntent.putExtra("toy", toy);
                startActivity(toyFormIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}