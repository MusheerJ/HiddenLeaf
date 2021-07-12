package com.leaf.hiddenleadapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.leaf.hiddenleadapp.R;
import com.leaf.hiddenleadapp.UserModels.Users;
import com.leaf.hiddenleadapp.databinding.ActivitySignUp2Binding;
//import com.leaf.hiddenleadapp.databinding.ActivitySignUpBinding;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUp2Binding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    @Override


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUp2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Objects.requireNonNull(getSupportActionBar()).hide();// hides the status bar
        // creating the instance so that we can communicate with the database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
//        progressDialog.show();
        progressDialog.setMessage("We're creating your Account");


        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // SIGN IN using google !!!!
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        // Sign up With Phone
//        binding.PhoneSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(SignUpActivity.this
////                        , PhoneAuthActivity.class);
////                startActivity(intent);
//                Toast.makeText(SignUpActivity.this,"Will be available soon ..",Toast.LENGTH_SHORT).show();
//            }
//        });

        // Auth with Email and Password
        binding.SIgnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.Username.getText().toString().isEmpty()){
                    binding.Username.setError("Username Cant be empty");
                    return;
                }

                if(binding.mail.getText().toString().isEmpty()){
                    binding.mail.setError("Email Cant be empty");
                    return;
                }

                if(binding.password.getText().toString().isEmpty()){
                    binding.password.setError("Password Cant be empty");
                    return;
                }

                progressDialog.show();// shows the loading bar

                auth.createUserWithEmailAndPassword(binding.mail.getText().toString()
                        ,binding.password.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //authentication
                        if(task.isSuccessful()){
                            String Uid = task.getResult().getUser().getUid();
                            Users user = new Users(binding.Username.getText().toString()
                                    ,binding.mail.getText().toString()
                                    ,binding.password.getText().toString(),Uid);
                            progressDialog.dismiss();

                            //saving the user's data into the database

                            database.getReference().child("Users").child(Uid).setValue(user);


                            Intent intent = new Intent(SignUpActivity.this
                                    , SetUpProfileActivity.class);
                            intent.putExtra("ID",Uid);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this,
                                    "Account Created Successfully ",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this,
                                    task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        // Already Have an Account
        binding.already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // FaceBook Sign In
        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Toast.makeText(SignUpActivity.this,"Will be available soon"
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        //Google SignUp
        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }


        });
    }

    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent =  mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            // saving the data user who have login through Google
                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            users.setMail(user.getEmail());
                            users.setUserBio("I am using HiddenLeaf");
                            database.getReference().child("Users").child(user.getUid())
                                    .setValue(users);

                            Intent intent = new Intent(SignUpActivity.this
                                    ,MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignUpActivity.this,"SignIn with Google",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(user);
                        } else {
                            Toast.makeText(SignUpActivity.this
                                    ,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure"
                                    , task.getException());
                            Toast.makeText(SignUpActivity.this
                                    ,task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.",
//                            Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
//    public void alReadyHaveAccount(View view)
//    {
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("message");
////        myRef.setValue("Hello, World!");
//        Intent intent = new Intent(this, SignInActivity.class);
//        startActivity(intent);
//    }
}