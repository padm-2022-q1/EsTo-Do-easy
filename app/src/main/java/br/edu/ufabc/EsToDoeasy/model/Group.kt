package br.edu.ufabc.EsToDoeasy.model

typealias Groups = List<Group>

data class Group(
    /**
     * The task's unique identifier.
     */
    val id: String,

    /**
     * The task's given name.
     */
    val userId: String,

    /**
     * The task's given name.
     */
    val name: String,
)
