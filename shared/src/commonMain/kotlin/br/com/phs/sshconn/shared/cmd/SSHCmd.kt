package br.com.phs.sshconn.shared.cmd

expect class SSHCmd {
    fun sshCommand(attr: ArrayList<String>, command: String, callBack: (value: String) -> Unit, finish: () -> Unit)
}