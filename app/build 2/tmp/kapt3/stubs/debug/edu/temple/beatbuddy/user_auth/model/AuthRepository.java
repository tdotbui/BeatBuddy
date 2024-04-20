package edu.temple.beatbuddy.user_auth.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J3\u0010\u0006\u001a\f\u0012\u0004\u0012\u00020\b0\u0007j\u0002`\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2 = {"Ledu/temple/beatbuddy/user_auth/model/AuthRepository;", "", "currentUser", "Lcom/google/firebase/auth/FirebaseUser;", "getCurrentUser", "()Lcom/google/firebase/auth/FirebaseUser;", "firebaseSignUpWithEmailAndPassword", "Ledu/temple/beatbuddy/user_auth/model/AuthResult;", "", "Ledu/temple/beatbuddy/user_auth/model/SignUpResponse;", "email", "", "password", "fullName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface AuthRepository {
    
    @org.jetbrains.annotations.Nullable
    public abstract com.google.firebase.auth.FirebaseUser getCurrentUser();
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object firebaseSignUpWithEmailAndPassword(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    java.lang.String fullName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super edu.temple.beatbuddy.user_auth.model.AuthResult<java.lang.Boolean>> $completion);
}