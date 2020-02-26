package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddLoan extends AppCompatActivity {

    private EditText loanName;
    private EditText loanMoney;
    private Button confirmLoan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;

    /*public static void addDebt(String receive, String send, double value) {

        for (int i = 0; i < contacts[cReceive].list.size(); i++) {
            if (send.equals(contacts[cReceive].list.get(i).contact)) {
                contacts[cReceive].list.get(i).addMoney(value);
            }
        }

        for (int i = 0; i < contacts[cSend].list.size(); i++) {
            if (receive.equals(contacts[cSend].list.get(i).contact)) {
                contacts[cSend].list.get(i).addMoney(value * -1);
            }
        }
        //simpleDebt(contacts, receive, send);
    }

    public static int getPrimary(Contacts contacts[], String target) {
        for (int i = 0; i < contacts.length; i++) {
            if (contacts[i].user.equals(target)) {
                return i;
            }
        }
        return 0;
    }

    public static int getSecondary(Contacts contact, String target) {
        for (int i = 0; i < contact.list.size(); i++) {
            if (contact.list.get(i).contact.equals(target)) {
                return i;
            }
        }
        return 0;
    }

    public static void setNewBalances(Contacts contacts[], int pos, int neg, boolean moneyAmountSwitch, int parity, int receiveOrSend) {
        int moneyAmount = 0;
        if (moneyAmountSwitch) {
            moneyAmount = contacts[receiveOrSend].list.get(pos).money;
        } else {
            moneyAmount = contacts[receiveOrSend].list.get(neg).money;
        }
        contacts[receiveOrSend].list.get(pos).addMoney(moneyAmount * parity);
        contacts[receiveOrSend].list.get(neg).addMoney(-1 * moneyAmount * parity);
        String sPositive = contacts[receiveOrSend].list.get(pos).contact;
        String sNegative = contacts[receiveOrSend].list.get(neg).contact;
        String sMiddle = contacts[receiveOrSend].user;
        int iPositive = getPrimary(contacts, sPositive);
        int iNegative = getPrimary(contacts, sNegative);
        contacts[iPositive].list.get(getSecondary(contacts[iPositive], sMiddle)).addMoney(-1 * moneyAmount * parity);
        contacts[iPositive].list.get(getSecondary(contacts[iPositive], sNegative)).addMoney(moneyAmount * parity);
        contacts[iNegative].list.get(getSecondary(contacts[iNegative], sMiddle)).addMoney(moneyAmount * parity);
        contacts[iNegative].list.get(getSecondary(contacts[iNegative], sPositive)).addMoney(-1 * moneyAmount * parity);
    }

    public static void simpleDebt(Contacts contacts[], String receive, String send) {
        int cReceive = 0;
        int cSend = 0;
        for (int i = 0; i < contacts.length; i++) {
            if (receive.equals(contacts[i].user)) {
                cReceive = i;
            }
            if (send.equals(contacts[i].user)) {
                cSend = i;
            }
        }
        if (getOwedBalance(contacts[cReceive]) > 0 && getOutstandingBalance(contacts[cReceive]) > 0) {
            ArrayList<Integer> positiveReceive = new ArrayList<Integer>();
            ArrayList<Integer> negativeReceive = new ArrayList<Integer>();
            for (int i = 0; i < contacts[cReceive].list.size(); i++) {
                if (contacts[cReceive].list.get(i).money > 0) {
                    positiveReceive.add(i);
                }
                if (contacts[cReceive].list.get(i).money < 0) {
                    negativeReceive.add(i);
                }
            }
            if (getOwedBalance(contacts[cReceive]) >= getOutstandingBalance(contacts[cReceive])) {
                for (int i = 0; i < negativeReceive.size(); i++) {
                    for (int j = 0; j < positiveReceive.size(); j++) {
                        int posJ = getPrimary(contacts, contacts[cReceive].list.get(positiveReceive.get(j)).contact);
                        int negI = getPrimary(contacts, contacts[cReceive].list.get(negativeReceive.get(i)).contact);
                        if (contacts[posJ].isCloseContact(contacts[negI])) {
                            if (contacts[cReceive].list.get(positiveReceive.get(j)).money >= (-1 * contacts[cReceive].list.get(negativeReceive.get(i)).money)) {
                                setNewBalances(contacts, positiveReceive.get(j), negativeReceive.get(i), false, 1, cReceive);
                                break;
                            } else {
                                setNewBalances(contacts, positiveReceive.get(j), negativeReceive.get(i), true, -1, cReceive);
                                continue;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < positiveReceive.size(); i++) {
                    for (int j = 0; j < negativeReceive.size(); j++) {
                        int posI = getPrimary(contacts, contacts[cReceive].list.get(positiveReceive.get(i)).contact);
                        int negJ = getPrimary(contacts, contacts[cReceive].list.get(negativeReceive.get(j)).contact);
                        if (contacts[posI].isCloseContact(contacts[negJ])) {
                            if (contacts[cReceive].list.get(positiveReceive.get(i)).money >= (-1 * contacts[cReceive].list.get(negativeReceive.get(j)).money)) {
                                setNewBalances(contacts, positiveReceive.get(i), negativeReceive.get(j), false, 1, cReceive);
                                continue;
                            } else {
                                setNewBalances(contacts, positiveReceive.get(i), negativeReceive.get(j), true, -1, cReceive);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (getOwedBalance(contacts[cSend]) > 0 && getOutstandingBalance(contacts[cSend]) > 0) {
            ArrayList<Integer> positiveSend = new ArrayList<Integer>();
            ArrayList<Integer> negativeSend = new ArrayList<Integer>();
            for (int i = 0; i < contacts[cSend].list.size(); i++) {
                if (contacts[cSend].list.get(i).money > 0) {
                    positiveSend.add(i);
                }
                if (contacts[cSend].list.get(i).money < 0) {
                    negativeSend.add(i);
                }
            }
            if (getOwedBalance(contacts[cSend]) >= getOutstandingBalance(contacts[cSend])) {
                for (int i = 0; i < negativeSend.size(); i++) {
                    for (int j = 0; j < positiveSend.size(); j++) {
                        int posJ = getPrimary(contacts, contacts[cSend].list.get(positiveSend.get(j)).contact);
                        int negI = getPrimary(contacts, contacts[cSend].list.get(negativeSend.get(i)).contact);
                        if (contacts[posJ].isCloseContact(contacts[negI])) {
                            if (contacts[cSend].list.get(positiveSend.get(j)).money >= (-1 * contacts[cSend].list.get(negativeSend.get(i)).money)) {
                                setNewBalances(contacts, positiveSend.get(j), negativeSend.get(i), false, 1, cSend);
                                break;
                            } else {
                                setNewBalances(contacts, positiveSend.get(j), negativeSend.get(i), true, -1, cSend);
                                continue;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < positiveSend.size(); i++) {
                    for (int j = 0; j < negativeSend.size(); j++) {
                        int posI = getPrimary(contacts, contacts[cSend].list.get(positiveSend.get(i)).contact);
                        int negJ = getPrimary(contacts, contacts[cSend].list.get(negativeSend.get(j)).contact);
                        if (contacts[posI].isCloseContact(contacts[negJ])) {
                            if (contacts[cSend].list.get(positiveSend.get(i)).money >= (-1 * contacts[cSend].list.get(negativeSend.get(j)).money)) {
                                setNewBalances(contacts, positiveSend.get(i), negativeSend.get(j), false, 1, cSend);
                                continue;
                            } else {
                                setNewBalances(contacts, positiveSend.get(i), negativeSend.get(j), true, -1, cSend);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan2);
        username = getIntent().getExtras().getString("username");
        confirmLoan = (Button) findViewById(R.id.confirmLoan);
        confirmLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanName = (EditText) findViewById(R.id.loanName);
                loanMoney = (EditText) findViewById(R.id.loanMoney);
                addNewLoan2db(username,loanName.getText().toString(), loanMoney.getText().toString());
            }
        });

    }
    public void addNewLoan2db(String username,String name, String amount){


        Map<String, Object> data = new HashMap<>();
        data.put("Amount", amount);
        data.put("Creditor", name);
        data.put("Debtor", username);

        db.collection("Loans").add(data);
        Intent intent = new Intent(this, AddLoanActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }
}
