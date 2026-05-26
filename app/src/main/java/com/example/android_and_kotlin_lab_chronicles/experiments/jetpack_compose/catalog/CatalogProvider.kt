package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components.*
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.CatalogItem

object CatalogProvider {
    @Composable
    fun getCatalogItems(): List<CatalogItem> {
        return remember {
            listOf(
                CatalogItem("Badge") { BadgeLab() },
                CatalogItem("Button") { ButtonsLab() },
                CatalogItem("Card") { CardLab() },
                CatalogItem("CheckBox") { CheckBoxLab() },
                CatalogItem("ConstraintLayout") { ConstraintLayoutComposeLab() },
                CatalogItem("Dialogs") { DialogsLab() },
                CatalogItem("Divider") { DividerLab() },
                CatalogItem("DropDown") { DropDownLab() },
                CatalogItem("FloatingActionButton") { FloatingActionButtonLab() },
                CatalogItem("Image") { ImageLab() },
                CatalogItem("InteractionSource") { InteractionSourceLab() },
                CatalogItem("LazyColumn") { LazyColumnLab() },
                CatalogItem("NavigationDrawer", requiresRawScreen = true) { ModalNavigationDrawerLab() },
                CatalogItem("NavigationBar") { NavigationBarLab() },
                CatalogItem("ProgressIndicators") { ProgressIndicatorsLab() },
                CatalogItem("RadioButton") { RadioButtonLab() },
                CatalogItem("Slider") { SliderLab() },
                CatalogItem("Snackbar", requiresRawScreen = true) { SnackbarLab() },
                CatalogItem("Switch") { SwitchLab() },
                CatalogItem("Text") { TextLab() },
                CatalogItem("TopAppBar", requiresRawScreen = true) { TopAppBarLab() }
            ).sortedBy { it.name }
        }
    }
}
