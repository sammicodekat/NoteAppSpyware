package com.example.cleanarchitecturenoteapp
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
//import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

//Class is extending AsyncTask because this class is going to perform a networking operation
class SendMail(//Declaring Variables
    private val context: Context, //Information to send email
    private var email: String, subject: String, message: String
) :
    AsyncTask<Void?, Void?, Void?>() {
    private val subject: String
    private val message: String
    override fun onPreExecute() {
        //Showing progress dialog while sending email
        Log.v("msg", "sending mail...")
    }

    override fun onPostExecute(aVoid: Void?) {
        //Showing a success message
        Log.v("msg", "mail sent")
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        //Creating properties
        val props = Properties()

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"

        //Creating a new session
        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
             override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(Config.EMAIL, Config.PASSWORD)
            }
        })

        try {
            //Creating MimeMessage object
            val mm = MimeMessage(session)

            //Setting sender address
            mm.setFrom(InternetAddress(Config.EMAIL))
            //Adding receiver
            mm.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
            //Adding subject
            mm.subject = subject
            //Adding message
            mm.setText(message)

            //Sending email
            val smtpTransport = session.getTransport("smtp")
            smtpTransport.connect()
            smtpTransport.sendMessage(mm, mm.allRecipients)
            smtpTransport.close()
        } catch (messagingException: MessagingException) {
            messagingException.printStackTrace()
        }
        return null
    }

    //Class Constructor
    init {
        //Initializing variables
        this.email = email
        this.subject = subject
        this.message = message
    }
}