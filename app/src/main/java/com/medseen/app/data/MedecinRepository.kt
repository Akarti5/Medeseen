package com.medseen.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository gérant l'accès aux données des médecins
 */
class MedecinRepository(private val medecinDao: MedecinDao) {

    val allMedecins: Flow<List<Medecin>> = medecinDao.getAllMedecins()

    fun searchMedecins(query: String): Flow<List<Medecin>> {
        return if (query.isBlank()) {
            medecinDao.getAllMedecins()
        } else {
            medecinDao.searchMedecins(query)
        }
    }

    suspend fun insert(medecin: Medecin) = medecinDao.insertMedecin(medecin)

    suspend fun update(medecin: Medecin) = medecinDao.updateMedecin(medecin)

    suspend fun delete(medecin: Medecin) = medecinDao.deleteMedecin(medecin)

    suspend fun deleteById(id: Int) = medecinDao.deleteMedecinById(id)

    suspend fun getById(id: Int) = medecinDao.getMedecinById(id)

    suspend fun seedSampleDataIfEmpty() {
        if (medecinDao.getCount() == 0) {
            val sampleDoctors = listOf(
                Medecin(
                    nom = "Dr. Sophie Martin",
                    datenais = "14/05/1982",
                    specialite = "Cardiologie",
                    hopital = "Centre Hospitalier Saint-Louis",
                    telephone = "+33 6 12 34 56 78",
                    email = "sophie.martin@hopital.fr",
                    photo = "",
                    isFavorite = true
                ),
                Medecin(
                    nom = "Dr. Alexandre Dubois",
                    datenais = "22/11/1975",
                    specialite = "Pédiatrie",
                    hopital = "Clinique Infantile des Lilas",
                    telephone = "+33 6 98 76 54 32",
                    email = "alexandre.dubois@pediatrie.fr",
                    photo = "",
                    isFavorite = true
                ),
                Medecin(
                    nom = "Dr. Amina Benali",
                    datenais = "03/08/1988",
                    specialite = "Neurologie",
                    hopital = "Institut des Neurosciences",
                    telephone = "+33 6 45 67 89 01",
                    email = "a.benali@neuro-inst.org",
                    photo = "",
                    isFavorite = false
                ),
                Medecin(
                    nom = "Dr. Thomas Moreau",
                    datenais = "19/02/1980",
                    specialite = "Médecine Générale",
                    hopital = "Cabinet Médical du Centre",
                    telephone = "+33 6 23 45 67 89",
                    email = "thomas.moreau@cabinet-sante.fr",
                    photo = "",
                    isFavorite = false
                ),
                Medecin(
                    nom = "Dr. Claire Bernard",
                    datenais = "30/09/1985",
                    specialite = "Dermatologie",
                    hopital = "Hôpital Dermatologique Est",
                    telephone = "+33 6 34 56 78 90",
                    email = "claire.bernard@dermato.fr",
                    photo = "",
                    isFavorite = true
                )
            )
            sampleDoctors.forEach { medecinDao.insertMedecin(it) }
        }
    }
}
