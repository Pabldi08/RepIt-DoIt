package com.pad.repitdoit.features.catalogue.presentation

// Define las acciones de consulta y selección del catálogo.
sealed interface CatalogueAction {
    data class ChangeQuery(val query: String) : CatalogueAction
}
