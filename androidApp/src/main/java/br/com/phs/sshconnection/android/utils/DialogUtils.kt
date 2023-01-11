package br.com.phs.sshconnection.android.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


object DialogUtils {

    @JvmStatic
    fun okDialog(
        title: String?,
        msg: String?,
        context: Context?,
        ok: DialogInterface.OnClickListener?
    ) {

        if (context == null) {
            return
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setNeutralButton("OK", ok)
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}