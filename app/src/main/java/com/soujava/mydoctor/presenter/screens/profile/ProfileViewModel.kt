package com.soujava.mydoctor.presenter.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.soujava.mydoctor.domain.contract.IApi
import com.soujava.mydoctor.domain.contract.IExternalRepository
import com.soujava.mydoctor.domain.contract.ILocalRepository
import com.soujava.mydoctor.domain.models.Cep
import com.soujava.mydoctor.domain.models.Profile
import com.soujava.mydoctor.domain.models.allFieldsFilled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ProfileUI(
    val profile: Profile? = null,
    val cep: Cep? = null,
    val fullDataOk: Boolean = false,
    val showContainerAddress: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingCep: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)


class ProfileViewModel(
    private val repositoryApi: IApi,
    private val repositoryFirestore: IExternalRepository,
    private val localRepository: ILocalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUI())
    val state = _state.asStateFlow()


    fun getFullProfile() {
        val profile = loadProfile()
        if (profile?.allFieldsFilled() == true) {
            _state.update {
                it.copy(
                    profile = profile,
                    fullDataOk = true,
                    showContainerAddress = true
                )
            }
        }
        _state.update { it.copy(isLoading = false) }

    }

    fun getCep(cep: String) {
        _state.update { it.copy(isLoadingCep = true) }
        repositoryApi.fetchCep(cep) { cep ->
            _state.update {
                it.copy(
                    cep = cep.getOrNull(),
                    isLoadingCep = false,
                    showContainerAddress = true
                )
            }
        }
    }

    fun resetError() {
        _state.update {
            it.copy(
                error = null,
            )
        }
    }

    fun saveFullProfile(profileData: Profile) {
        val profile = loadProfile()
        Log.d("PROFILE", " --->>>> $profile")
        if (profile != null) {
            val newProfile = profile.copy(
                name = profileData.name,
                phone = profileData.phone,
                cpf = profileData.cpf,
                address = profileData.address
                    ?.copy(
                        address = profileData.address?.address,
                        city = profileData.address?.city,
                        state = profileData.address?.state,
                        cep = profileData.address?.cep,
                        number = profileData.address?.number,
                        complement = profileData.address?.complement,
                        neiborhood = profileData.address?.neiborhood,
                        ddd = profileData.address?.ddd
                    ),
                clincal = profileData.clincal
                    ?.copy(
                        hasMedication = profileData.clincal?.hasMedication,
                        medication = profileData.clincal?.medication,
                        hasAllergy = profileData.clincal?.hasAllergy,
                        allergyContent = profileData.clincal?.allergyContent
                    )
            )
            Log.d("PROFILE", "SAVE $newProfile")
            localRepository.saveProfile(newProfile)
            repositoryFirestore.saveProfile(newProfile) { data ->
                if (data) {
                    _state.update {
                        it.copy(
                            profile = profileData,
                            success = "Seu perfil foi atualizado com sucesso!",
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            error = "Ops, algo inexperado aconteceu. [FB]",
                            isLoading = false
                        )
                    }
                }
            }
        } else {
            _state.update {
                it.copy(
                    error = "Ops, algo inexperado aconteceu. [LOCAL_ERROR]",
                    isLoading = false
                )
            }
        }


    }

    fun resetSuccess() {
        _state.update {
            it.copy(
                success = null
            )
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        cpf: String,
    ) {

        val profile = loadProfile()
        if (profile != null) {
            val newProfile = profile.copy(
                name = name,
                phone = phone,
                cpf = cpf
            )
            update(newProfile)

        } else {
            error("1")
        }
    }

    fun updateAddress(
        address: String,
        city: String,
        state: String,
        cep: String,
        number: String,
        complement: String,
        neiborhood: String
    ) {
        val profile = loadProfile()
        if (profile != null) {
            val newProfile = profile.copy(
                address = profile.address?.copy(
                    address = address,
                    city = city,
                    state = state,
                    cep = cep,
                    number = number,
                    complement = complement,
                    neiborhood = neiborhood
                )
            )
            update(newProfile)
        } else {
            error("2")
        }
    }

    fun updateClinic(
        hasMedication: Boolean,
        medication: String,
        hasAllergy: Boolean,
        allergyContent: String
    ) {
        val profile = loadProfile()
        if (profile != null) {
            val newProfile = profile.copy(
                clincal = profile.clincal?.copy(
                    hasMedication = hasMedication,
                    medication = medication,
                    hasAllergy = hasAllergy,
                    allergyContent = allergyContent
                )
            )
            update(newProfile)
            Log.d("PROFILE", "SAVE $newProfile")

        } else {
            error("3")
        }
    }

    private fun error(number: String) {
        _state.update {
            it.copy(
                error = "Ops, algo inexperado aconteceu. [LOCAL_ERROR::$number]",
                isLoading = false
            )
        }
    }

    private fun loadProfile(): Profile? {
        _state.update { it.copy(isLoading = true) }
        return localRepository.getProfile()
    }

    private fun update(newProfile: Profile) {
        localRepository.saveProfile(newProfile)
        repositoryFirestore.saveProfile(
            newProfile
        ) { data ->
            if (data) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        profile = newProfile,
                        success = "Seu perfil foi atualizado com sucesso"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Ops, algo inexperado aconteceu."
                    )
                }
            }
        }
    }
}