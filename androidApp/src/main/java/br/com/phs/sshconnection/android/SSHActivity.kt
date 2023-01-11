package br.com.phs.sshconnection.android

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import br.com.phs.sshconn.shared.cmd.SSHCmd
import br.com.phs.sshconnection.android.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SSHActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BuildSSHView(LocalContext.current as? Activity)
                }
            }
        }
    }
}

@Composable
fun BuildSSHView(activity: Activity?) {
    AndroidView(
        factory = { View.inflate(it, R.layout.activity_ssh, null) },
        modifier = Modifier.fillMaxSize(),
        update = { view ->

            activity?.intent.letOr(
                block = {
                    val host = it?.getStringExtra(HOST)?: ""
                    val port = it?.getStringExtra(PORT)?: ""
                    val userName = it?.getStringExtra(USER_NAME)?: ""
                    val password = it?.getStringExtra(PASSWORD)?: ""
                    val attr = arrayListOf(host, port, userName, password)
                    processCommand(view, attr)
                },
                isNull = {
                    DialogUtils.okDialog("Atenção", "Erro interno!", activity) { _, _ ->
                        activity?.finishAffinity()
                    }
                }
            )

        }
    )
}

private fun processCommand(view: View, attr: ArrayList<String>) {

    val command = "pwd && exit\n"
    val key = "user.home"
    val value = view.context.applicationInfo.dataDir
    System.setProperty(key, value)

    SSHCmd().sshCommand(attr, command,
        callBack = {
            runBlocking {
                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.cmdET)?.let{ tv ->
                        tv.movementMethod = ScrollingMovementMethod()
                        tv.append(it)
                    }
                }
            }
        },
        finish = {
            //DialogUtils.okDialog("CMD", "Finalizado!", view.context) { _, _ ->

            //}
        }
    )

}