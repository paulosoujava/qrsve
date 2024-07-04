package com.soujava.mydoctor.presenter.screens.profile

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.soujava.mydoctor.domain.models.Cep
import com.soujava.mydoctor.presenter.screens.commons.AppSpace
import com.soujava.mydoctor.presenter.screens.commons.AppText
import com.soujava.mydoctor.presenter.screens.commons.BottomButtonCard
import com.soujava.mydoctor.presenter.screens.commons.CepField
import com.soujava.mydoctor.presenter.screens.commons.LoadingButton
import com.soujava.mydoctor.presenter.screens.commons.NumberField
import com.soujava.mydoctor.presenter.screens.commons.SpaceType
import com.soujava.mydoctor.presenter.screens.commons.TextField
import com.soujava.mydoctor.presenter.screens.commons.Types
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray
import com.soujava.mydoctor.presenter.ui.theme.white
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


@Composable
fun AddressTab(
    isSave: Boolean,
    state: State<ProfileUI>,
    cep: MutableState<String>,
    address: MutableState<String>,
    city: MutableState<String>,
    uf: MutableState<String>,
    neiborhood: MutableState<String>,
    number: MutableState<String>,
    complement: MutableState<String>,
    ddd: MutableState<String>,
    onSearchCep: (String) -> Unit,
    onNext: () -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    val focusRequesterAddress = remember { FocusRequester() }
    val focusRequesterNumber = remember { FocusRequester() }

    LaunchedEffect(key1 = focusRequester) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(key1 = state.value) {
        if (state.value.showContainerAddress) {
            if (state.value.cep != null) {
                uf.value = state.value.cep?.uf ?: ""
                address.value = state.value.cep?.address ?: ""
                number.value = state.value.cep?.number ?: ""
                complement.value = state.value.cep?.complement ?: ""
                city.value = state.value.cep?.city ?: ""
                neiborhood.value = state.value.cep?.neiborhood ?: ""
                ddd.value = state.value.cep?.ddd ?: ""

                if (address.value.isEmpty()) {
                    focusRequesterAddress.requestFocus()

                } else {
                    focusRequesterNumber.requestFocus()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .imePadding()
            .padding(top = 20.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,

        ) {

        AppSpace(SpaceType.MEDIUM)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CepField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .width(250.dp),
                input = cep,
                label = "Cep",
                placeholder = "Digite seu cep",
                onSearch = {
                    onSearchCep(cep.value)

                }
            ) {
                cep.value = it
                Log.d("CEP", cep.value)

            }
            IconButton(
                modifier = Modifier
                    .height(50.dp)
                    .padding(top = 10.dp)
                    .background(black, RoundedCornerShape(50.dp)),
                enabled = cep.value.isNotEmpty(),
                onClick = {
                    onSearchCep(cep.value)
                }) {
                if (state.value.isLoadingCep) {
                    CircularProgressIndicator(
                        color = white,
                        modifier = Modifier.size(30.dp),
                        strokeWidth = 3.dp
                    )
                } else
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = white)
            }
        }

        AppSpace(SpaceType.MEDIUM)
        AnimatedVisibility(visible = state.value.showContainerAddress) {
            Column {
                TextField(
                    modifier = Modifier.focusRequester(focusRequesterAddress),
                    input = address,
                    label = "Endereço",
                    placeholder = "Digite seu endereço",
                    imeAction = ImeAction.Next
                )
                AppSpace(SpaceType.MEDIUM)
                TextField(
                    input = neiborhood,
                    label = "Bairro",
                    placeholder = "Digite o bairro",
                    imeAction = ImeAction.Next
                )
                AppSpace(SpaceType.MEDIUM)
                TextField(
                    modifier = Modifier.focusRequester(focusRequesterNumber),
                    input = number,
                    label = "Número",
                    placeholder = "Número",
                    imeAction = ImeAction.Next
                )

                AppSpace(SpaceType.MEDIUM)
                TextField(
                    input = complement,
                    label = "Complemento",
                    placeholder = "Alguma referência",
                    imeAction = ImeAction.Next
                )
                AppSpace(SpaceType.MEDIUM)
                TextField(
                    input = city,
                    label = "Cidade",
                    placeholder = "Digite a cidade",
                    imeAction = ImeAction.Next
                )
                AppSpace(SpaceType.MEDIUM)
                TextField(
                    input = uf,
                    label = "Uf",
                    placeholder = "Estado",
                    onDone = {
                        if (cep.value.isNotEmpty() && address.value.isNotEmpty() && city.value.isNotEmpty() && uf.value.isNotEmpty() &&
                            neiborhood.value.isNotEmpty() && number.value.isNotEmpty()
                        ) {
                            onNext()
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = cep.value.isNotEmpty() &&
                        address.value.isNotEmpty() && city.value.isNotEmpty() && uf.value.isNotEmpty() &&
                        neiborhood.value.isNotEmpty() && number.value.isNotEmpty() ||
                        isSave
            ) {
                FloatingActionButton(
                    onClick = onNext,
                    containerColor = black,
                    contentColor = white
                ) {
                    if (state.value.isLoading) {
                        CircularProgressIndicator(
                            color = white,
                            modifier = Modifier.padding(10.dp),
                            strokeWidth = 1.dp
                        )
                    } else
                        Icon(
                            if (isSave) Icons.Rounded.Save else Icons.AutoMirrored.Rounded.NavigateNext,
                            contentDescription = null
                        )
                }
            }
        }
        AppSpace(SpaceType.MEDIUM)
    }

}
