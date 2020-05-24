package com.example.artwokmabel.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;

public class RegistrationViewModel extends ViewModel {

    enum RegistrationState {
        COLLECT_EMAIL,
        DUPLICATE_EMAIL,
        COLLECT_USERNAME,
        DUPLICATE_USERNAME,
        COLLECT_PASSWORD,
        REGISTRATION_COMPLETE,
        REGISTRATION_FAILED
    }

    private String email;
    private String username;
    private String password;

    private MutableLiveData<RegistrationState> registrationState =
            new MutableLiveData<>(RegistrationState.COLLECT_EMAIL);

    public MutableLiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }

    public void collectUserEmail(String email){
        FirestoreRepo.getInstance().isEmailDuplicate(email, (isDuplicate) -> {
            if(isDuplicate){
                registrationState.setValue(RegistrationState.DUPLICATE_EMAIL);
            }else{
                this.email = email;
                registrationState.setValue(RegistrationState.COLLECT_USERNAME);
            }
        });
    }

    public void collectUserUsername(String username){
        FirestoreRepo.getInstance().isUsernameDuplicate(username, (isDuplicate) -> {
            if(isDuplicate){
                registrationState.setValue(RegistrationState.DUPLICATE_USERNAME);
            }else{
                this.username = username;
                registrationState.setValue(RegistrationState.COLLECT_PASSWORD);
            }
        });
    }

    public void createAccountAndLogin(String password) {
        this.password = password;
        FirestoreRepo.getInstance().createAccount(email, username, password, (successful) -> {
            if(successful){
                registrationState.setValue(RegistrationState.REGISTRATION_COMPLETE);
            }else{
                registrationState.setValue(RegistrationState.REGISTRATION_FAILED);
            }
        });

    }

    public boolean userCancelledRegistration() {
        // Clear existing registration data
        //Do more testing on this
        registrationState.setValue(RegistrationState.COLLECT_EMAIL);
        return true;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public interface CollectDetailCallback{
        void collectDetailCallback(boolean isDuplicate);
    }

    public interface CreateAccountCallback{
        void createAccountCallback(boolean successful);
    }

}
