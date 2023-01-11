package br.com.phs.sshconn.shared.cmd

import org.apache.sshd.client.SshClient
import org.apache.sshd.client.channel.ClientChannelEvent
import org.apache.sshd.common.channel.Channel
import org.apache.sshd.server.forward.AcceptAllForwardingFilter
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

actual class SSHCmd {

    actual fun sshCommand(attr: ArrayList<String>, command: String, callBack: (value: String) -> Unit, finish: () -> Unit) {

        // Creating a client instance
        val client = SshClient.setUpDefaultClient()
        client.forwardingFilter = AcceptAllForwardingFilter.INSTANCE
        client.start()

        Thread {

            try {
                // Connection establishment and authentication
                try {

                    val (host, portStr, userName, password) = attr
                    val port = if (portStr.none { it.isDigit() }) 22 else portStr.filter { it.isDigit() }.toInt()

                    client.connect(userName, host, port).verify(10000).session.use { session ->

                        session.addPasswordIdentity(password)
                        session.auth().verify(50000)
                        callBack.invoke("Connection establihed")

                        // Create a channel to communicate
                        val channel = session.createChannel(Channel.CHANNEL_SHELL)
                        callBack.invoke("Starting shell")
                        val responseStream = ByteArrayOutputStream()
                        channel.setOut(responseStream)

                        // Open channel
                        channel.open().verify(5, TimeUnit.SECONDS)
                        channel.invertedIn.use { pipedIn ->
                            pipedIn.write(command.toByteArray())
                            pipedIn.flush()
                        }

                        // Close channel
                        channel.waitFor(
                            EnumSet.of(ClientChannelEvent.CLOSED),
                            TimeUnit.SECONDS.toMillis(5)
                        )

                        // Output after converting to string type
                        val responseString = String(responseStream.toByteArray())
                        callBack.invoke(responseString)

                    }

                } catch (e: IOException) {
                    callBack.invoke(e.message?: "UNKNOWN")
                    finish.invoke()
                } finally {
                    client.stop()
                    finish.invoke()
                }

            } catch (e: Exception) {
                callBack.invoke(e.message?: "UNKNOWN")
                finish.invoke()
            }

        }.start()

    }
}