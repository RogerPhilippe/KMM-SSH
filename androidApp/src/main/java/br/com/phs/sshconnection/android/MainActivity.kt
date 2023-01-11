package br.com.phs.sshconnection.android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import br.com.phs.sshconnection.android.utils.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BuildMainView()
                }
            }
        }
    }

}

@Composable
fun BuildMainView() {

    AndroidView(
        factory = { View.inflate(it, R.layout.activity_main, null) },
        modifier = Modifier.fillMaxSize(),
        update = {
            authenticate(it)
        }
    )
}

private fun authenticate(view: View) {

    view.findViewById<Button>(R.id.sendBtn)?.setOnClickListener {

        // Get input data from fields
        val host = view.findViewById<EditText>(R.id.hostET)?.text.toString()
        val port = view.findViewById<EditText>(R.id.portET)?.text.toString()
        val userName = view.findViewById<EditText>(R.id.userNameET)?.text.toString()
        val password = view.findViewById<EditText>(R.id.passwordET)?.text.toString()

        if (host.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(view.context, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        val intent = Intent(view.context, SSHActivity::class.java)
        intent.putExtra(HOST, host)
        intent.putExtra(PORT, port)
        intent.putExtra(USER_NAME, userName)
        intent.putExtra(PASSWORD, password)
        view.context.startActivity(intent)

    }

}
