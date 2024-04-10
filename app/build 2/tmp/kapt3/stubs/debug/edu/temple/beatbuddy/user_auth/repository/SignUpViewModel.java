package edu.temple.beatbuddy.user_auth.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R7\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\u0015"}, d2 = {"Ledu/temple/beatbuddy/user_auth/repository/SignUpViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Ledu/temple/beatbuddy/user_auth/model/AuthRepository;", "(Ledu/temple/beatbuddy/user_auth/model/AuthRepository;)V", "<set-?>", "Ledu/temple/beatbuddy/user_auth/model/AuthResult;", "", "signUpResponse", "getSignUpResponse", "()Ledu/temple/beatbuddy/user_auth/model/AuthResult;", "setSignUpResponse", "(Ledu/temple/beatbuddy/user_auth/model/AuthResult;)V", "signUpResponse$delegate", "Landroidx/compose/runtime/MutableState;", "signUp", "Lkotlinx/coroutines/Job;", "email", "", "password", "fullName", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class SignUpViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final edu.temple.beatbuddy.user_auth.model.AuthRepository repo = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.compose.runtime.MutableState signUpResponse$delegate = null;
    
    @javax.inject.Inject
    public SignUpViewModel(@org.jetbrains.annotations.NotNull
    edu.temple.beatbuddy.user_auth.model.AuthRepository repo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final edu.temple.beatbuddy.user_auth.model.AuthResult<java.lang.Boolean> getSignUpResponse() {
        return null;
    }
    
    private final void setSignUpResponse(edu.temple.beatbuddy.user_auth.model.AuthResult<java.lang.Boolean> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job signUp(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    java.lang.String fullName) {
        return null;
    }
}