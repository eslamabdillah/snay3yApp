package dataBase.fireStore

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dataBase.models.Client
import dataBase.models.Worker


object DAO {
    private val db = Firebase.firestore
    private val documentId = "Sns0qKZQMYp1CKGFR44t"
    private val docRef = db.collection("workers_demo").document(documentId)


    fun getAllWorkers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("workers_demo")
            .get()
            .addOnCompleteListener(onCompleteListener)

    }


    fun getAllJobs(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection("jobs_demo")
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
        val docRef = db.collection("jobs_demo").document(jobId).get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getOffersforJob(jobId: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        val docRef = db.collection("jobs_demo")
            .document(jobId)
            .collection("workerOffers")
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

    fun getUser(
        id: String,
        onSuccessListenerWorker: OnSuccessListener<DocumentSnapshot>,
        onFailureListenerWorker: OnFailureListener,
        onSuccessListenerClient: OnSuccessListener<DocumentSnapshot>,
        onFailureListenerClient: OnFailureListener,
    ) {
        db.collection("worker").document(id).get()
            .addOnSuccessListener { workerDoc ->
                if (workerDoc.exists()) {
                    onSuccessListenerWorker
                } else {
                    db.collection("clients").document(id).get()
                        .addOnSuccessListener { clientDoc ->
                            if (clientDoc.exists()) {
                                onSuccessListenerClient
                            } else {
                                onFailureListenerClient.onFailure(Exception("Document not found in either collection"))
                            }

                        }
                        .addOnFailureListener {
                            onFailureListenerClient
                        }
                }
            }

            .addOnFailureListener {
                onFailureListenerWorker
            }

    }


}