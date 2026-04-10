package com.rjw.bunpoun3.model

import kotlinx.serialization.Serializable

@Serializable
data class WeekSeed(
    val num: Int,
    val title: String,
    val titleId: String,
    val days: List<DaySeed>,
)

@Serializable
data class DaySeed(
    val day: Int,
    val title: String,
    val titleId: String,
    val grammar: List<GrammarSeed>,
)

@Serializable
data class GrammarSeed(
    val pattern: String,
    val badge: String,
    val meaning: String,
    val meaningId: String,
    val examples: List<ExampleSeed>,
)

@Serializable
data class ExampleSeed(
    val ja: String,
    val id: String,
)

@Serializable
data class VerbFormSeed(
    val title: String,
    val rows: List<VerbFormRowSeed>,
)

@Serializable
data class VerbFormRowSeed(
    val label: String,
    val body: String,
    val id: String,
    val ex: String = "",
)
