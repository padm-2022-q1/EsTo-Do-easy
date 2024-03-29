package br.edu.ufabc.EsToDoeasy.model

import android.app.Application

/**
 * A repository factory.
 * @property application the application object
 */
class RepositoryFactory(private val application: Application) {

    /**
     * The types of repositories.
     */
    enum class Type {
        Firestore,
        //FirestoreAuth
    }

    /**
     * Create a new repository given its type.
     * @param type the repository type
     */
    fun create(type: Type = Type.Firestore) = when (type) {
        Type.Firestore -> RepositoryFirestore(application)
        //Type.FirestoreAuth -> RepositoryFirestoreAuth(application)
    }
}