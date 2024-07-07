package com.soujava.mydoctor.presenter.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soujava.mydoctor.domain.models.Address
import com.soujava.mydoctor.domain.models.Clinical
import com.soujava.mydoctor.domain.models.Profile
import com.soujava.mydoctor.domain.models.allFieldsFilled
import com.soujava.mydoctor.presenter.screens.commons.AppTopBar
import com.soujava.mydoctor.presenter.screens.commons.BottomButtonCard
import com.soujava.mydoctor.presenter.screens.commons.WarningTop
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white
import org.koin.androidx.compose.koinViewModel


@Preview
@Composable
fun ProfileScreen(navController: NavHostController = rememberNavController()) {


    val viewModel: ProfileViewModel = koinViewModel()
    val state = viewModel.state.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val titles = listOf("Dados Pessoais", "Endereço", "Dados Clínicos")

    val name = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }

    val cep = remember { mutableStateOf("") }
    var uf = remember { mutableStateOf("") }
    var address = remember { mutableStateOf("") }
    var number = remember { mutableStateOf("") }
    var complement = remember { mutableStateOf("") }
    var city = remember { mutableStateOf("") }
    var neiborhood = remember { mutableStateOf("") }
    var ddd = remember { mutableStateOf("") }


    val selectedOptionMedication = remember { mutableStateOf("") }
    var moreInfoMedication = remember { mutableStateOf("") }
    val selectedOptionAllergy = remember { mutableStateOf("") }
    var moreInfoAllergy = remember { mutableStateOf("") }
    var selectedOptionExercise = remember { mutableStateOf("") }
    var moreInfoExercise = remember { mutableStateOf("") }

    LaunchedEffect(key1 = viewModel) {
        viewModel.getFullProfile()
    }

    ////Cep 01153 000
    val profile = state.value.profile

    if (profile != null && !state.value.isLoading) {
        name.value = profile.name ?: ""
        phone.value = profile.phone ?: ""
        cpf.value = profile.cpf ?: ""

        if (!state.value.isLoadingCep) {
            cep.value = profile.address?.cep ?: ""
            uf.value = profile.address?.state ?: ""
            address.value = profile.address?.address ?: ""
            number.value = profile.address?.number ?: ""
            complement.value = profile.address?.complement ?: ""
            city.value = profile.address?.city ?: ""
            neiborhood.value = profile.address?.neiborhood ?: ""
            ddd.value = profile.address?.ddd ?: ""
        } else if (!state.value.isLoading) {
            cep.value = state.value.cep?.cep ?: ""
            uf.value = state.value.cep?.uf ?: ""
            address.value = state.value.cep?.address ?: ""
            number.value = state.value.cep?.number ?: ""
            complement.value = state.value.cep?.complement ?: ""
            city.value = state.value.cep?.city ?: ""
            neiborhood.value = state.value.cep?.neiborhood ?: ""
            ddd.value = state.value.cep?.ddd ?: ""
        }

        moreInfoMedication.value = profile.clincal?.medication ?: ""
        moreInfoAllergy.value = profile.clincal?.allergyContent ?: ""

        selectedOptionAllergy.value = if (profile.clincal?.hasAllergy == true) "Sim" else "Não"
        selectedOptionMedication.value =
            if (profile.clincal?.hasMedication == true) "Sim" else "Não"
    }


    val currentKeyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var isKeyboardOpen by remember { mutableStateOf(false) }
    val view = LocalView.current
    LaunchedEffect(key1 = view) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val windowInsets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
            isKeyboardOpen = windowInsets.isVisible(WindowInsetsCompat.Type.ime())
        }
    }



    Scaffold(
        containerColor = white,
        topBar = {
            Column {
                AppTopBar {
                    navController.popBackStack()
                }
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            color = black,
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(1.dp)
                                .background(color = black)
                        )
                    }

                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                Text(
                                    title,
                                    fontSize = 13.sp,
                                    fontFamily = AppFont.light,
                                    color = when (title) {
                                        "Endereço" -> black
                                        "Dados Clínicos" -> black
                                        else -> black
                                    }
                                )
                            },
                            selected = selectedTabIndex == index,
                            onClick = {
                                Log.d("PROFILE", "CLICK ${state.value.profile?.allFieldsFilled() == false}")
                                if (state.value.profile?.allFieldsFilled() == false) {
                                    Toast.makeText(
                                        context,
                                        "Por favor, preencha todos campos!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else
                                    when (title) {
                                        "Endereço" -> selectedTabIndex = index
                                        "Dados Clínicos" -> selectedTabIndex = index
                                        else -> selectedTabIndex = index
                                    }

                            },
                            selectedContentColor = black,
                            unselectedContentColor = black.copy(alpha = 0.9f),
                        )
                    }
                }

                WarningTop(
                    isError = false,
                    text = state.value.success ?: ""
                ) { viewModel.resetSuccess() }


                WarningTop(
                    isError = true,
                    text = state.value.error ?: ""
                ) { viewModel.resetError() }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (selectedTabIndex) {
                0 -> PersonalDataTab(
                    isSave = state.value.fullDataOk,
                    state,
                    name = name,
                    phone = phone,
                    cpf = cpf
                ) {
                    if (state.value.fullDataOk) {
                        currentKeyboard?.hide()
                        viewModel.updateProfile(
                            name = name.value,
                            phone = phone.value,
                            cpf = cpf.value
                        )
                    } else {
                        selectedTabIndex = 1
                    }

                }

                1 -> {
                    AddressTab(
                        isSave = state.value.fullDataOk,
                        state,
                        cep = cep,
                        uf = uf,
                        address = address,
                        number = number,
                        complement = complement,
                        city = city,
                        neiborhood = neiborhood,
                        ddd = ddd,
                        onSearchCep = { cep ->
                            viewModel.getCep(cep)
                        }) {
                        if (state.value.fullDataOk) {
                            currentKeyboard?.hide()
                            viewModel.updateAddress(
                                address = address.value,
                                city = city.value,
                                state = uf.value,
                                cep = cep.value,
                                number = number.value,
                                complement = complement.value,
                                neiborhood = neiborhood.value
                            )
                        } else {
                            selectedTabIndex = 2
                        }

                    }

                }

                2 -> {
                    ClinicalDataTab(
                        isSave = state.value.fullDataOk,
                        state,
                        selectedOptionMedication = selectedOptionMedication,
                        moreInfoMedication = moreInfoMedication,
                        selectedOptionAllergy = selectedOptionAllergy,
                        moreInfoAllergy = moreInfoAllergy,
                        selectedOptionExercise = selectedOptionExercise,
                        moreInfoExercise = moreInfoExercise
                    ) {
                        if (!state.value.fullDataOk) {
                            val profile = Profile(
                                name = name.value,
                                phone = phone.value,
                                cpf = cpf.value,
                                address = Address(
                                    address = address.value,
                                    city = city.value,
                                    state = uf.value,
                                    cep = cep.value,
                                    number = number.value,
                                    complement = complement.value,
                                    neiborhood = neiborhood.value,
                                    ddd = ddd.value
                                ),
                                clincal = Clinical(
                                    hasMedication = selectedOptionMedication.value == "Sim",
                                    medication = moreInfoMedication.value,
                                    hasAllergy = selectedOptionAllergy.value == "Sim",
                                    allergyContent = moreInfoAllergy.value,
                                    hasExercise = selectedOptionExercise.value == "Sim",
                                    howManyExercise = moreInfoExercise.value
                                )
                            )
                            viewModel.saveFullProfile(profile)

                        } else {
                            currentKeyboard?.hide()
                            viewModel.updateClinic(
                                hasMedication = selectedOptionMedication.value == "Sim",
                                medication = moreInfoMedication.value,
                                hasAllergy = selectedOptionAllergy.value == "Sim",
                                allergyContent = moreInfoAllergy.value
                            )
                        }
                    }
                }
            }
        }
    }
}


