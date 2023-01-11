package br.com.phs.sshconn.shared.cmd

actual class SSHCmd {

    actual fun sshCommand(
        attr: ArrayList<String>,
        command: String,
        callBack: (value: String) -> Unit,
        finish: () -> Unit
    ) {
    }

}