package com.soujava.mydoctor.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.soujava.mydoctor.core.generateRandomKey
import com.soujava.mydoctor.domain.contract.IAuthentication
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.models.Profile
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.domain.models.UserAuth


class AuthImpl(
    private val repository: IExternalRepository,
    private val sessionPreference: ILocalRepository
) : IAuthentication {

    override fun logout() {
        sessionPreference.clear()
    }

    override fun forgetPassword(email: String, onResult: (Boolean, String) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(
                        true,
                        "Enviamos um link para o email cadastrado. Verifique sua caixa de entrada."
                    )
                } else {
                    onResult(false, it.exception?.message.toString())
                }
            }
    }

    override fun authenticate(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    sessionPreference.saveProfile(
                        Profile(
                            userAuth = UserAuth(
                                displayName = currentUser?.displayName ?: "",
                                email = currentUser?.email,
                                photoUrl = currentUser?.photoUrl,
                                uid = currentUser?.uid ?: generateRandomKey()
                            )
                        )
                    )
                    onResult(true, null)
                } else {
                    onResult(false, it.exception?.message.toString())
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("pt-BR")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    //RECOVER DATA FROM FIREBASE
                    repository.saveProfile(profile(currentUser)) { isSuccess ->
                        // PUT IN SESSION
                        sessionPreference.saveProfile(profile(currentUser))
                        if (isSuccess)
                            onResult(
                                false,
                                "Obaaa, Sucesso ao registrar sua conta.\nVamos redirecionar você a tela inicial."
                            )
                        else
                            onResult(true, "Opss, algo deu errado, tente novamente")
                    }
                } else {
                    onResult(true, it.exception?.message.toString())
                }
            }
    }

    private fun profile(currentUser: FirebaseUser?) = Profile(
        userAuth = UserAuth(
            displayName = currentUser?.displayName ?: "",
            email = currentUser?.email,
            photoUrl = currentUser?.photoUrl,
            uid = currentUser?.uid ?: generateRandomKey()
        )
    )
}