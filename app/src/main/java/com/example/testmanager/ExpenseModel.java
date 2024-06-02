package com.example.testmanager;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class ExpenseModel implements Serializable {
    private String expenseId;
    private String note;
    private String category;
    private String type;
    private long amount;
    private long dateMillis;
    private String uid;

    public ExpenseModel() {
    }

    public ExpenseModel(String expenseId, String note, String category, String type, long amount, Timestamp date, String uid) {
        this.expenseId = expenseId;
        this.note = note;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.dateMillis = date.toDate().getTime();
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
    public long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public Timestamp getTimestamp() {
        return new Timestamp(new Date(dateMillis));
    }

    public void setTimestamp(Timestamp date) {
        this.dateMillis = date.toDate().getTime();
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
