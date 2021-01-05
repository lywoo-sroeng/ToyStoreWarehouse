package com.example.toy_store_warehouse;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class ToyFormActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri prevImgUri;
    private Uri imgUri;

    private Toy edToy;
    private boolean isAddForm;

    private String selectedCategory = "Figure";

    private TextView txtToyFormTitle;
    private EditText edTxtName, edTxtPrice, edTxtReleaseDate, edTxtSpecification, edTxtQty;
    private RadioGroup radioGroupBattery, radioGroupPoseable;
    private RadioButton radioBattery, radioPoseable;
    private MaterialButton btnChooseImage, btnSubmitToyForm;
    private Spinner spinnerCategory;
    ArrayAdapter<CharSequence> categoryAdapter;

    ProgressDialog progressDialog;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_form);

        edToy = getIntent().getParcelableExtra("toy");

        isAddForm = edToy == null;

        initToolbar();

        initForm();
    }

    private void initForm() {
        txtToyFormTitle = findViewById(R.id.toy_form_title);
        initEditTexts();
        initChooseImageButton();
        initDropdownCategory();
        initRadioButtons();
        initSubmitButton();

        if (!isAddForm) {
            txtToyFormTitle.setText(R.string.edit_toy_form_title);
            btnSubmitToyForm.setText(R.string.update_toy_form_button);
            setFormValues(edToy);
        }
        ;
    }

    private void setFormValues(Toy toy) {
        edTxtName.setText(toy.getName());
        edTxtPrice.setText(String.valueOf(toy.getPrice()));
        edTxtQty.setText(String.valueOf(toy.getQty()));
        edTxtReleaseDate.setText(toy.getReleaseDate());
        edTxtSpecification.setText(toy.getSpecification());

        if (toy.getImgUri().isEmpty()) {
            imgUri = null;
        } else {
            ImageView img = findViewById(R.id.img_view_toy_form);
            imgUri = Uri.parse(toy.getImgUri());
            prevImgUri = imgUri;
            Picasso.get().load(imgUri).into(img);
        }

        int spinnerCategoryPosition = categoryAdapter.getPosition(toy.getCategory());
        spinnerCategory.setSelection(spinnerCategoryPosition);

        if (toy.isFigure()) {
            boolean poseable = ((Figure) toy).isPoseable();
            RadioButton rb;
            if (poseable) rb = findViewById(R.id.radio_poseable_yes);
            else rb = findViewById(R.id.radio_poseable_no);
            rb.setChecked(true);
        } else if (toy.isDoll()) {
            boolean hasBattery = ((Doll) toy).isHasBattery();
            RadioButton rb;
            if (hasBattery) rb = findViewById(R.id.radio_battery_yes);
            else rb = findViewById(R.id.radio_battery_no);
            rb.setChecked(true);
        }
    }

    private void initEditTexts() {
        edTxtName = findViewById(R.id.edt_name);
        edTxtPrice = findViewById(R.id.edt_price);
        edTxtReleaseDate = findViewById(R.id.edt_release_date);
        edTxtSpecification = findViewById(R.id.edt_specification);
        edTxtQty = findViewById(R.id.edt_qty);

        edTxtName.requestFocus();
    }

    private void initSubmitButton() {
        btnSubmitToyForm = findViewById(R.id.btn_submit_toy_form);
        btnSubmitToyForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void initRadioButtons() {
        radioGroupBattery = findViewById(R.id.radio_group_battery);
        radioBattery = findViewById(R.id.radio_battery_yes);
        radioGroupPoseable = findViewById(R.id.radio_group_poseable);
        radioPoseable = findViewById(R.id.radio_poseable_yes);

        radioGroupBattery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioBattery = radioGroup.findViewById(i);
            }
        });

        radioGroupPoseable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioPoseable = radioGroup.findViewById(i);
            }
        });
    }

    private void initDropdownCategory() {
        View dollCategoryLayout = findViewById(R.id.doll_category_layout);
        View actionFigureCategoryLayout = findViewById(R.id.action_figure_category_layout);
        dollCategoryLayout.setVisibility(View.GONE);
        actionFigureCategoryLayout.setVisibility(View.GONE);

        spinnerCategory = findViewById(R.id.spinner_category);
        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.toy_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapterView.getItemAtPosition(i).toString();
                if (Toy.isFigure(selectedCategory)) {
                    actionFigureCategoryLayout.setVisibility(View.VISIBLE);
                    dollCategoryLayout.setVisibility(View.GONE);
                } else {
                    actionFigureCategoryLayout.setVisibility(View.GONE);
                    dollCategoryLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initChooseImageButton() {
        btnChooseImage = findViewById(R.id.btn_choose_toy_image);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void submitForm() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(isAddForm ? "Adding new toy" : "Updating toy");
        progressDialog.show();

        String category = selectedCategory;
        storageReference = FirebaseStorage.getInstance().getReference(category);
        databaseReference = FirebaseDatabase.getInstance().getReference(category);

        try {
            boolean shouldUpdateWithinCategory = !isAddForm && edToy.getCategory().equals(selectedCategory);

            String id = shouldUpdateWithinCategory ? edToy.getId() : databaseReference.push().getKey();
            String imgUriPath = isAddForm ? "" : edToy.getImgUri();
            String name = edTxtName.getText().toString().trim();
            double price = Double.parseDouble(edTxtPrice.getText().toString().trim());
            String specification = edTxtSpecification.getText().toString().trim();
            int qty = Integer.parseInt(edTxtQty.getText().toString().trim());
            String releaseDate = edTxtReleaseDate.getText().toString().trim();
            boolean hasBattery = radioBattery.getText().equals("Yes") && radioBattery.isChecked();
            boolean poseable = radioPoseable.getText().equals("Yes") && radioPoseable.isChecked();

            Toy toy;
            if (Toy.isFigure(selectedCategory)) {
                toy = new Figure(id, name, imgUriPath, category, price, qty, releaseDate, specification, poseable);
            } else {
                toy = new Doll(id, name, imgUriPath, category, price, qty, releaseDate, specification, hasBattery);
            }

            // Add new toy form
            if (isAddForm) {
                uploadImageAndSubmitForm(toy, "New toy added successfully.", "New toy failed to add.", true);
            } else {
                String updateSuccessMsg = "Toy updated successfully.";
                String updateFailureMsg = "Toy failed to update.";

                // Update toy form only
                boolean isUpdateFormOnly = imgUri == prevImgUri;
                if (isUpdateFormOnly) {
                    databaseReference.child(id).setValue(toy).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            showSnackBar(updateSuccessMsg, R.color.green);
                            // TODO: AFTER UPDATE(CREATE) A TOY WITH NEW CATEGORY, REMOVE THE OLD TOY
                            if (!shouldUpdateWithinCategory) {
                                FirebaseDatabase.getInstance().getReference(edToy.getCategory()).child(edToy.getId()).removeValue();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), updateFailureMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                // Update toy form and image
                else {
                    uploadImageAndSubmitForm(toy, updateSuccessMsg, updateFailureMsg, false);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Each input must be filled!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageAndSubmitForm(Toy toy, String successMsg, String failureMsg, boolean reset) {
        if (imgUri != null) {
            String imgName = System.currentTimeMillis() + "." + getFileExtension(imgUri);
            StorageReference fileReference = storageReference.child(imgName);
            fileReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String imgUriPath = task.getResult().toString();
                                        toy.setImgUri(imgUriPath);

                                        databaseReference.child(toy.getId()).setValue(toy).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                showSnackBar(successMsg, R.color.green);
                                                if (reset) resetForm();
                                            }
                                        });
                                    }
                                });
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please choose image", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        if (Toy.isFigure(selectedCategory)) {
            setFormValues(new Figure());
        } else if (Toy.isDoll(selectedCategory)) {
            setFormValues(new Doll());
        }

        edTxtName.requestFocus();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_toy_form);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setTitle(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String msg, int snackbarColor) {
        Snackbar sb = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(ContextCompat.getColor(ToyFormActivity.this, snackbarColor));
        sb.show();
    }
}