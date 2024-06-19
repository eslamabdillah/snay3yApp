package dataBase.fireStore

import android.net.Uri
import android.util.Log
import com.example.sanay3yapp.ui.StatesJob
import com.example.sanay3yapp.ui.UserTypes
import com.example.sanay3yapp.ui.chat.ChatAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dataBase.models.ChatRoom
import dataBase.models.Client
import dataBase.models.ClientOpinion
import dataBase.models.Complaint
import dataBase.models.DailyWorker
import dataBase.models.GallaryProject
import dataBase.models.Job
import dataBase.models.Message
import dataBase.models.Offer
import dataBase.models.Worker


object DAO {
    public val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()


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

            val workerRef = db.collection("workers")
                .document(newWorker.id)

            val addOneRef = db.collection("admins")
                .document("appInformation")

            workerRef.set(newWorker).addOnSuccessListener({
                addOneRef.update("numWorkers", FieldValue.increment(1))
                    .addOnCompleteListener(onCompleteListener)

            })
                .addOnFailureListener({})
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


            val dailyWorkerRef = db.collection("dailyWorkers")
                .document(newDailyWorker.id)

            val addOneRef = db.collection("admins")
                .document("appInformation")


            dailyWorkerRef
                .set(newDailyWorker)
                .addOnSuccessListener({
                    addOneRef.update("numDailyWorkers", FieldValue.increment(1))
                        .addOnCompleteListener(onCompleteListener)
                })
                .addOnFailureListener({})
        }
    }

    fun addNewClient(newClient: Client, onCompleteListener: OnCompleteListener<Void>) {
        // Check if the worker has a valid ID
        if (newClient.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid ID for the worker.")
        } else {
            // Proceed with setting the document in Firestore
            val newClientRef = db.collection("clients")
                .document(newClient.id)

            val addOneRef = db.collection("admins")
                .document("appInformation")

            newClientRef.set(newClient)
                .addOnSuccessListener({
                    addOneRef.update("numClients", FieldValue.increment(1))
                        .addOnCompleteListener(onCompleteListener)
                })
                .addOnFailureListener({})

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

    fun incrementJob(
        addOnCompleteListener: OnCompleteListener<Void>
    ) {
        val incrementJobRef = db.collection("admins")
            .document("appInformation")

        incrementJobRef.update("numJobs", FieldValue.increment(1))
            .addOnCompleteListener(addOnCompleteListener)
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

    fun addClientOpinion(
        workerId: String,
        opinion: ClientOpinion,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val workerRef = db.collection("workers")
            .document(workerId)
            .collection("clientsOpinions")
            .document()

        opinion.id = workerRef.id
        workerRef.set(opinion).addOnCompleteListener(onCompleteListener)
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
                clientRef.update("newJobs", FieldValue.arrayRemove(jobId))
                    .addOnCompleteListener(onCompleteListener)
            })
    }

    fun makeOfferAcceptableForJob(
        jobId: String,
        offerId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val offerRef = db.collection("jobs")
            .document(jobId)
            .collection("workersOffers")
            .document(offerId)

        offerRef.update("state", "accept")
            .addOnCompleteListener(onCompleteListener)
    }

    fun makeOfferAcceptableForWorker(
        workerId: String,
        offerId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val offerRef = db.collection("workers").document(workerId)
            .collection("myOffers").document(offerId)

        offerRef.update("state", "accept")


    }

    fun updateConfirmJobForWorker(
        workerId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        var workerRef = db.collection("workers")
            .document(workerId)
            .update("currentJob", jobId)
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

    fun getCompleteJobArray(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                val completeJobs =
                    document.data?.get("completeJobs") as? List<String> ?: emptyList()
                onCompleteListener(Result.success(completeJobs))
            } else {
                onCompleteListener(Result.success(emptyList()))
            }
        }.addOnFailureListener { exception ->
            onCompleteListener(Result.failure(exception))
        }
    }

    fun getCompleteJobArrayForWorker(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("workers")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                val completeJobs =
                    document.data?.get("completeJobsList") as? List<String> ?: emptyList()
                onCompleteListener(Result.success(completeJobs))
            } else {
                onCompleteListener(Result.success(emptyList()))
            }
        }.addOnFailureListener { exception ->
            onCompleteListener(Result.failure(exception))
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

    fun getCompleteJobsForClient(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                var newList = document.data?.get("completeJobs") as List<String>
                onCompleteListener(Result.success(newList))

            }

        }.addOnFailureListener {

        }
    }

    fun finishJob(
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val jobRef = db.collection("jobs")
            .document(jobId)

        jobRef.update("state", StatesJob.FINISHED).addOnCompleteListener(onCompleteListener)

    }

    // TODO: kkkkkkkkkkkkkkkk
    fun finishJobForWorker(
        workerId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val workerRef = db.collection("workers")
            .document(workerId)

        workerRef.update("completeJobsList", FieldValue.arrayUnion(jobId))
            .addOnSuccessListener({
                workerRef.update("currentJob", "")
                    .addOnCompleteListener(onCompleteListener)
            })
            .addOnFailureListener({})

    }

    fun finishJobForClient(
        clientId: String,
        jobId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.update("inWorkJob", FieldValue.arrayRemove(jobId))
            .addOnSuccessListener({
                clientRef.update("completeJobs", FieldValue.arrayUnion(jobId))
                    .addOnCompleteListener(onCompleteListener)
            })
            .addOnFailureListener({})
    }

    fun uploadPhotosToProjectFolder(
        projectId: String,
        photos: List<Uri>,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val storge = Firebase.storage.reference
        val photoCount = photos.size
        var uploadedCount = 0

        photos.forEach { uri ->
            val photoRef = storge.child("projects/$projectId/${uri.lastPathSegment}")
            photoRef.putFile(uri).addOnCompleteListener({
                onCompleteListener
            })
        }

    }

    fun addNewProject(
        workerId: String,
        newProject: GallaryProject,
        onCompleteListener: (String) -> Unit

    ) {

        val galleryRef = db.collection("workers")
            .document(workerId)
            .collection("gallery")
            .document()

        var projectId = galleryRef.id
        newProject.projectId = projectId



        galleryRef.set(newProject).addOnSuccessListener({
            onCompleteListener(projectId)

        }).addOnFailureListener({

        })


    }

    fun uploadImages(
        projectId: String,
        imageUris: MutableList<Uri>,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val storageRef = FirebaseStorage.getInstance().reference
        val folderName = projectId // Unique folder name
        val uploads = mutableListOf<Task<Uri>>()

        imageUris.forEachIndexed { index, uri ->
            val imageRef = storageRef.child("projects/$folderName/photo${index + 1}.jpg")
            val uploadTask = imageRef.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }
            uploads.add(uploadTask)
        }

        // Wait for all upload tasks to complete
        Tasks.whenAll(uploads).addOnCompleteListener { task ->
            val taskCompletionSource = TaskCompletionSource<Void>()
            if (task.isSuccessful) {
                taskCompletionSource.setResult(null) // Successfully complete the task
            } else {
                taskCompletionSource.setException(
                    task.exception ?: Exception("Failed to complete all uploads")
                )
            }
            onCompleteListener.onComplete(taskCompletionSource.task) // Pass the task from TaskCompletionSource
        }
    }

    fun getProjectForWorker(
        workerId: String,
        onCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        val gallaryRef = db.collection("workers")
            .document(workerId)
            .collection("gallery")

        gallaryRef.get().addOnCompleteListener(onCompleteListener)
    }


    //for chats
    fun makeChatRoom(
        chatRoom: ChatRoom,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val refChat = db.collection("chats").document(chatRoom.id)
        refChat.set(chatRoom).addOnCompleteListener(onCompleteListener)

    }

    fun storeChatRoomWithClient(
        chatRoomId: String,
        clientId: String,
        onCompleteListener: OnCompleteListener<Void>

    ) {
        db.collection("clients").document(clientId)
            .update("chatRooms", FieldValue.arrayUnion(chatRoomId))
            .addOnCompleteListener(onCompleteListener)

    }

    fun storeChatRoomWithWorker(
        chatRoomId: String,
        workerId: String,
        onCompleteListener: OnCompleteListener<Void>

    ) {
        db.collection("workers").document(workerId)
            .update("chatRooms", FieldValue.arrayUnion(chatRoomId))
            .addOnCompleteListener(onCompleteListener)

    }

    fun sendMessage(
        message: Message,
        chatRoomId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        var refMessage = db.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .document()

        refMessage.set(message).addOnCompleteListener(onCompleteListener)

    }

    fun getChatRoom(
        roomId: String,
        adapter: ChatAdapter
    ) {
        val messagesRef = db.collection("chats").document(roomId).collection("messages")
        val query = messagesRef.orderBy("time", Query.Direction.ASCENDING)

        query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }

            /*    val messages=snapshots!!.toObjects(Message::class.java)
                adapter.changeAdapterList(messages.toMutableList())
    */
            snapshots?.documentChanges?.forEach { documentChange ->
                var message = documentChange.document.toObject(Message::class.java)
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        adapter.addMessage(message)

                    }

                    DocumentChange.Type.MODIFIED -> {

                    }

                    DocumentChange.Type.REMOVED -> {

                    }
                }

            }
        }


    }

    fun getChatRoom(
        roomId: String,
        onCompleteListener: OnCompleteListener<DocumentSnapshot>
    ) {
        var doucRef = db.collection("chats").document(roomId)
            .get().addOnCompleteListener(onCompleteListener)
    }

    fun getChatRoomsArrayForClient(
        clientId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val clientRef = db.collection("clients")
            .document(clientId)

        clientRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                val chatrooms = document.data?.get("chatRooms") as? List<String> ?: emptyList()
                onCompleteListener(Result.success(chatrooms))
            } else {
                onCompleteListener(Result.success(emptyList()))
            }
        }.addOnFailureListener { exception ->
            onCompleteListener(Result.failure(exception))
        }
    }

    fun getChatRoomsArrayForWorker(
        workerId: String,
        onCompleteListener: (Result<List<String>>) -> Unit
    ) {
        val workerRef = db.collection("workers")
            .document(workerId)

        workerRef.get().addOnSuccessListener { document ->
            if (document.exists() && document != null) {
                val chatrooms = document.data?.get("chatRooms") as? List<String> ?: emptyList()
                onCompleteListener(Result.success(chatrooms))
            } else {
                onCompleteListener(Result.success(emptyList()))
            }
        }.addOnFailureListener { exception ->
            onCompleteListener(Result.failure(exception))
        }
    }

    fun signOut(onCompleteListener: OnCompleteListener<Void>) {
        firebaseAuth.signOut().apply { onCompleteListener }
    }


    //admin functions////////////////////////////////////////////////
    fun getAdmin(
        adminId: String,
        onCompleteListener: OnCompleteListener<DocumentSnapshot>
    ) {
        val adminRef = db.collection("admins")
            .document(adminId)
            .get()
            .addOnCompleteListener(onCompleteListener)

    }

    fun getInfoApp(
        onCompleteListener: OnCompleteListener<DocumentSnapshot>
    ) {
        val infoRef = db.collection("admins")
            .document("appInformation")
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    //complaint //////////////////////////////////////////////////////
    fun sendComplaint(
        newComplaint: Complaint,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val complaintRef = db.collection("complaints")
            .document()
        newComplaint.id = complaintRef.id

        complaintRef.set(newComplaint)
            .addOnCompleteListener(onCompleteListener)
    }


}
