package com.pad.repitdoit.features.catalogue.presentation

sealed interface CatalogueAction {
    data class ChangeQuery(val query: String) : CatalogueAction
}
