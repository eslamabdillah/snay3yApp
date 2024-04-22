package dataBase.fireStore

import com.example.sanay3yapp.ui.StatesJob
import com.example.sanay3yapp.ui.UserTypes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dataBase.models.Client
import dataBase.models.ClientOpinion
import dataBase.models.DailyWorker
import dataBase.models.Job
import dataBase.models.Offer
import dataBase.models.Worker


object DAO {
    private val db = Firebase.firestore

    fun getAllWorkers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("workers")
            .get()
            .addOnCompleteListener(onCompleteListener)

    }


    fun getAllJobs(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("jobs")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(onCompleteListener)

    }


    fun getWorker(workerId: String, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
        val docRef = db.collection("workers").document(workerId).get()
            .addOnCompleteListener(onCompleteListener)

    }

    fun getClient(clientId: String, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
        val docRef = db.collection("clients").document(clientId).get()
            .addOnCompleteListener(onCompleteListener)

    }

    fun getAllDailyWorkers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("dailyWorkers")
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getJob(jobId: String, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
        val docRef = db.collection("jobs").document(jobId).get()
            .addOnCompleteListener(onCompleteListener)
    }


    fun getOffersforJob(jobId: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        val docRef = db.collection("jobs")
            .document(jobId)
            .collection("workersOffers")
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getOffersforWorker(
        workerId: String,
        onCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        val docRef = db.collection("workers")
            .document(workerId)
            .collection("myOffers")
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getSelectedOffer(
        jobId: String,
        offerId: String,
        onCompleteListener: OnCompleteListener<DocumentSnapshot>
    ) {
        val offerRef = db.collection("jobs")
            .document(jobId)
            .collection("workersOffers")
            .document(offerId)
            .get().addOnCompleteListener(onCompleteListener)

    }

    fun addNewWorker(newWorker: Worker, onCompleteListener: OnCompleteListener<Void>) {
        // Check if the worker has a valid ID
        if (newWorker.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            db.collection("workers")
                .document(newWorker.id)
                .set(newWorker)
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun addNewDailyWorker(
        newDailyWorker: DailyWorker,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        // Check if the worker has a valid ID
        if (newDailyWorker.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            db.collection("dailyWorkers")
                .document(newDailyWorker.id)
                .set(newDailyWorker)
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun addNewClient(newClient: Client, onCompleteListener: OnCompleteListener<Void>) {
        // Check if the worker has a valid ID
        if (newClient.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            db.collection("clients")
                .document(newClient.id)
                .set(newClient)
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun addPhotoUrl(
        workerType: String,
        photoUrl: String,
        id: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        if (workerType == "worker") {
            var userRef = db.collection("workers").document(id)
            userRef.update("photoUrl", photoUrl).addOnCompleteListener(onCompleteListener)
        } else {
            var userRef = db.collection("dailyWorkers").document(id)
            userRef.update("photoUrl", photoUrl).addOnCompleteListener(onCompleteListener)
        }

    }

    fun addJobAndUpdateClient(
        newJob: Job,
        clientId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {


        val jobsCollectionRef = db.collection("jobs").document()

        newJob.id = jobsCollectionRef.id


        jobsCollectionRef.set(newJob).addOnSuccessListener { jobDocumentRef ->

            val clientDocRef = db.collection("clients").document(clientId)

            clientDocRef.update("newJobs", FieldValue.arrayUnion(newJob.id))
                .addOnCompleteListener(onCompleteListener)

        }.addOnFailureListener { exception ->


        }
    }

    fun addOfferInJob(
        jobId: String,
        newOffer: Offer,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        // Reference to the new document in the workersOffers subcollection of the job
        val offerDocRef = db.collection("jobs")
            .document(jobId)
            .collection("workersOffers")
            .document()

        // Generate and assign a new Offer ID
        val offerId = offerDocRef.id
        newOffer.id = offerId

        // Set the new offer in the workersOffers subcollection
        offerDocRef.set(newOffer).addOnSuccessListener {
            // On success, add the same offer under the worker's myOffers subcollection
            val workerOfferRef = db.collection("workers")
                .document(newOffer.workerId)
                .collection("myOffers")
                .document(offerId)

            workerOfferRef.set(newOffer).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {
                    task.exception?.let {
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // If the initial set fails, propagate the failure
        }
    }

    fun getClientOpinionForWorker(
        workerId: String,
        onCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {

        db.collection("workers")
            .document(workerId)
            .collection("clientsOpinions")
            .get()
            .addOnCompleteListener(onCompleteListener)

    }

    fun setClientOpinionForWorker(
        workerId: String,
        jobId: String,
        newOpinion: ClientOpinion,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val documentWorkerRef = db.collection("workers")
            .document(workerId)
            .collection("clientsOpinions")
            .document()

        var docId = documentWorkerRef.id


        documentWorkerRef.set(newOpinion)
            .addOnSuccessListener {
                val documentJobRef = db.collection("jobs")
                    .document(jobId)
                    .collection("clientOpinion")
                    .document(docId)


                documentJobRef.set(newOpinion)
                    .addOnCompleteListener(onCompleteListener)
            }


    }


    //confirm JOB


    fun setConfirmedJob(
        offerId: String,
        workerId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>

    ) {
        var jobRef = db.collection("jobs")
            .document(jobId)

        jobRef.update(
            mapOf(
                "state" to StatesJob.INWORK,
                "selectedOffer" to offerId,
                "selectedWorker" to workerId
            )
        ).addOnCompleteListener(onCompleteListener)


    }

    fun updateConfirmJobForClient(
        clientId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        var clientRef = db.collection("clients")
            .document(clientId)



        clientRef.update("inWorkJob", FieldValue.arrayUnion(jobId))
            .addOnSuccessListener({
                clientRef.update("myJobs", FieldValue.arrayRemove(jobId))
                    .addOnCompleteListener(onCompleteListener)
            })
    }

    fun updateConfirmJobForWorker(
        workerId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        var workerRef = db.collection("workers")
            .document(workerId)

        workerRef.update("currentJob", jobId)
            .addOnCompleteListener(onCompleteListener)
    }

    fun getJobAgreement(
        userId: String,
        userType: Int,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        if (userType == UserTypes.CLIENT) {


        } else if (userType == UserTypes.WORKER) {
            db.collection("workers").document(userId)
                .get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val currentJob = document.getString("currentJob")
                    }

                }
                .addOnFailureListener {

                }

        } else {

        }
    }

    fun getInWorkJobArray(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                var inWorkList = document.data?.get("inWorkJob") as List<String>
                onCompleteListener(Result.success(inWorkList))

            }

        }.addOnFailureListener {

        }
    }

    fun getNewJobsForClient(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                var newList = document.data?.get("newJobs") as List<String>
                onCompleteListener(Result.success(newList))

            }

        }.addOnFailureListener {

        }
    }


}