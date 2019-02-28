package one.marcomass.ezeat.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import one.marcomass.ezeat.models.SignResponse;
import one.marcomass.ezeat.repos.BackendRepository;

public class UserViewModel extends ViewModel {

    private BackendRepository backendRepository;

    public UserViewModel() {
        backendRepository = BackendRepository.getInstance();
    }

    public LiveData<SignResponse> login(String identifier, String password) {
        return backendRepository.login(identifier, password);
    }

    public LiveData<SignResponse> register(String username, String password, String email) {
        return backendRepository.register(username, password, email);
    }
}
