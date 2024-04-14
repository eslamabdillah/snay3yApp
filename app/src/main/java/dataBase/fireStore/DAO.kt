package dataBase.fireStore

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dataBase.models.Client
import dataBase.models.Job
import dataBase.models.Offer
import dataBase.models.Worker


object DAO {
    private val db = Firebase.firestore
    private val documentId = "Sns0qKZQMYp1CKGFR44t"
    private val docRef = db.collection("workers_demo").document(documentId)


    fun getAllWorkers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("workers")
            .get()
            .addOnCompleteListener(onCompleteListener)

    }


    fun getAllJobs(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("jobs")
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
        db.collection("daily_workers_demo")
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

    fun addNewWorker(newWorker: Worker, onCompleteListener: OnCompleteListener<Void>) {
        // Check if the worker has a valid ID
        if (newWorker.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            db.collection("workers").document(newWorker.id)
                .set(newWorker)
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun addNewClient(newClient: Client, onCompleteListener: OnCompleteListener<Void>) {
        // Check if the worker has a valid ID
        if (newClient.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            db.collection("clients").document(newClient.id)
                .set(newClient)
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun addPhotoUrl(photoUrl: String, id: String, onCompleteListener: OnCompleteListener<Void>) {
        var userRef = db.collection("workers").document(id)

        userRef.update("photoUrl", photoUrl).addOnCompleteListener(onCompleteListener)
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

            clientDocRef.update("myJobs", FieldValue.arrayUnion(newJob.id))
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


}