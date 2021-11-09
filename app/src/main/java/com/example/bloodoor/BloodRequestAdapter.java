package com.example.bloodoor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodoor.sendEmail.userMailApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.MyViewHolder>{

    Context context;
    ArrayList<RequestBlood> list;
    FirebaseAuth mAuth;

    public BloodRequestAdapter(Context context, ArrayList<RequestBlood> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BloodRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_item,parent,false);
        return new BloodRequestAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdapter.MyViewHolder holder, int position) {
        RequestBlood requestBlood = list.get(position);
        holder.requestBloodgrp.setText(requestBlood.getBloodgrp());
        holder.requestStatus.setText(requestBlood.getRequestStatus());
        holder.patientName.setText(requestBlood.getPatientName());
        holder.patientNumber.setText(requestBlood.getPatientNumber());
        holder.patientEmail.setText(requestBlood.getPatientEmail());
        holder.hospitalName.setText(requestBlood.getHospitalName()+", "+requestBlood.getPinCode());
//        holder.pinCode.setText(requestBlood.getPinCode());
        holder.requestReason.setText(requestBlood.getRequestReason());

        final String nameOFReceiver = requestBlood.getPatientName();
        final String idOfReceiver = requestBlood.getPatientEmail();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Request For Blood!!");
                alertDialogBuilder.setIcon(R.drawable.ic_flag);
                alertDialogBuilder.setMessage("Change status ?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Fulfilled", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Function to change status in the database
                        setRequestStatus(holder, "Fulfilled");
                    }
                });
                alertDialogBuilder.setNegativeButton("Pending", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Function to change status in the database
                        setRequestStatus(holder, "Pending");
                    }
                });
                alertDialogBuilder.show();
            }
        });



//        holder.helpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Send Email")
//                        .setMessage("Send Email to " + requestBlood.getPatientName() + "?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                                        .child("BloodBanks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                reference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        String nameOfBank = snapshot.child("name").getValue().toString();
//                                        String nameOfSender = snapshot.child("handlerName").getValue().toString();
//                                        String email = snapshot.child("email").getValue().toString();
//                                        String address = snapshot.child("address").getValue().toString();
//                                        String phoneNo = snapshot.child("phoneNo").getValue().toString();
//                                        String pinCode = snapshot.child("bbPinCode").getValue().toString();
//                                        String mEmail = user.getEmail();
//                                        String mSubject = "BLOOD REQUEST";
//                                        String mMessage = "Hello " + nameOFReceiver + "," + nameOfSender + "from" + nameOfBank + ","
//                                                + "Would like to Blood donation from you. Here's his/her details :\n"
//                                                + "Name : " + nameOfSender + "\n"
//                                                + "Phone Number : " + phoneNo + "\n"
//                                                + "Email : " + email + "\n"
//                                                + "Address : " + address + "\n"
//                                                + "City Pin Code : " + pinCode + "\n"
//                                                + "Kindly reach out to him/her. Thank You...\n"
//                                                + "BlooDoor... DONATE BLOOD, SAVE LIFE :)";
//
//                                        userMailApi userMailApi = new userMailApi(context, mEmail, mSubject, mMessage);
//                                        userMailApi.execute();
//
//                                        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("email")
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                        senderRef.child(idOfReceiver).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//                                                    DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("email")
//                                                            .child(idOfReceiver);
//                                                    receiverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
//                                                }
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
//
//            }
//        });
    }

    public void setRequestStatus(MyViewHolder holder, String s){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user  = mUser.getUid();
        String name = holder.patientName.getText().toString();
        String number = holder.patientNumber.getText().toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("bloodRequests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    for(DataSnapshot snap1 : snap.getChildren()){
                    if (snap1.getKey().equals(current_user)) {
                        String patient_name = String.valueOf(snap1.child("patientName").getValue());
                        String patient_number = String.valueOf(snap1.child("patientNumber").getValue());
                        if (name.equals(patient_name) && number.equals(patient_number))
                            reference.child(snap1.getKey()).child("requestStatus").setValue(s);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView requestBloodgrp, requestStatus, patientName, patientNumber, patientEmail, hospitalName, pinCode, requestReason;
        Button helpButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            requestBloodgrp = itemView.findViewById(R.id.requestBloodgrp);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            patientName = itemView.findViewById(R.id.patientName);
            patientNumber = itemView.findViewById(R.id.patientNumber);
            patientEmail = itemView.findViewById(R.id.patientEmail);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            pinCode = itemView.findViewById(R.id.pinCode);
            requestReason = itemView.findViewById(R.id.requestReason);
            helpButton = itemView.findViewById(R.id.helpButton);
        }
    }
}
