package com.kursor.chroniclesofww2.view.menu.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SimpleDialogFragment private constructor(builder: Builder) : DialogFragment() {

    private var _dialog: Dialog? = builder.createDialog()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return _dialog!!
    }

    class Builder(context: Context?) {

        private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        fun setTitle(title: String?): Builder {
            dialogBuilder.setTitle(title)
            return this
        }

        fun setMessage(message: String?): Builder {
            dialogBuilder.setMessage(message)
            return this
        }

        fun setMessage(messageId: Int): Builder {
            dialogBuilder.setMessage(messageId)
            return this
        }

        fun setPositiveButton(
            title: String?,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setPositiveButton(title, onClickListener)
            return this
        }

        fun setPositiveButton(
            titleId: Int,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setPositiveButton(titleId, onClickListener)
            return this
        }

        fun setNeutralButton(
            title: String?,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setNeutralButton(title, onClickListener)
            return this
        }

        fun setNeutralButton(
            titleId: Int,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setNeutralButton(titleId, onClickListener)
            return this
        }

        fun setNegativeButton(
            title: String?,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setNegativeButton(title, onClickListener)
            return this
        }

        fun setNegativeButton(
            titleId: Int,
            onClickListener: DialogInterface.OnClickListener?
        ): Builder {
            dialogBuilder.setNegativeButton(titleId, onClickListener)
            return this
        }

        fun setCancelable(b: Boolean): Builder {
            dialogBuilder.setCancelable(b)
            return this
        }

        fun setOnCancelListener(listener: DialogInterface.OnCancelListener?): Builder {
            dialogBuilder.setOnCancelListener(listener)
            return this
        }

        fun createDialog(): Dialog {
            return dialogBuilder.create()
        }

        fun build(): SimpleDialogFragment {
            return SimpleDialogFragment(this)
        }

    }

}