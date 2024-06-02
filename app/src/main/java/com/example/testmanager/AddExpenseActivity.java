package com.example.testmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.testmanager.databinding.ActivityAddExpenseBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
    private ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");

        if (expenseModel == null) {
            type = "Expense";
            clearFields();
        } else {
            type = expenseModel.getType();
            populateFields(expenseModel);
        }

        binding.back.setOnClickListener(v -> {
            Intent intent = new Intent(AddExpenseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(expenseModel == null ? R.menu.add_menu : R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saveExpense) {
            if (expenseModel == null) {
                createOrUpdateExpense(false);
            } else {
                createOrUpdateExpense(true);
            }
            return true;
        } else if (id == R.id.deleteExpense && expenseModel != null) {
            deleteExpense();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteExpense() {
        FirebaseFirestore.getInstance()
                .collection("expenses")
                .document(expenseModel.getExpenseId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete transaction", Toast.LENGTH_SHORT).show());
    }


    private void createOrUpdateExpense(boolean isUpdate) {
        String expenseId = isUpdate ? expenseModel.getExpenseId() : UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();
        boolean incomeChecked = binding.incomeRadio.isChecked();

        if (incomeChecked) {
            binding.amount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            binding.amount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        Timestamp currentTime = Timestamp.now();

        ExpenseModel expense = new ExpenseModel(
                expenseId,
                note,
                category,
                incomeChecked ? "Income" : "Expense",
                Long.parseLong(amount),
                currentTime,
                FirebaseAuth.getInstance().getUid()
        );

        FirebaseFirestore.getInstance()
                .collection("expenses")
                .document(expenseId)
                .set(expense)
                .addOnSuccessListener(aVoid -> {
                    if (isUpdate) {
                        Toast.makeText(this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Transaction created successfully", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (isUpdate) {
                        Toast.makeText(this, "Failed to update transaction", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to create transaction", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        binding.amount.setText("");
        binding.category.setText("");
        binding.note.setText("");
        binding.expenseRadio.setChecked(true);
    }

    private void populateFields(ExpenseModel expenseModel) {
        binding.amount.setText(String.valueOf(expenseModel.getAmount()));
        binding.category.setText(expenseModel.getCategory());
        binding.note.setText(expenseModel.getNote());
        if ("Income".equals(expenseModel.getType())) {
            binding.incomeRadio.setChecked(true);
        } else {
            binding.expenseRadio.setChecked(true);
        }
    }
}
