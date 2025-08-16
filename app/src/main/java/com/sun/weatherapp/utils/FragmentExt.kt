package com.sun.weatherapp.utils

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sun.weatherapp.R
import com.sun.weatherapp.databinding.DialogChangePasswordBinding
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog

fun Fragment.showProgressDialog(): AlertDialog? {
    return if (context != null) {
        AlertDialog.Builder(context!!, R.style.AFUtilProgressBarStyle)
            .setCancelable(false)
            .setView(R.layout.dialog_loading)
            .show()
    } else null
}

fun Fragment.showErrorDialog(message: String): AlertDialog? {
    return if (context != null) {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.error)
            .setMessage(message)
            .setCancelable(true)
            .show()
    } else null
}

fun Fragment.showWarningDialog(message: String, onOkClick: () -> Unit): AlertDialog? {
    return if (context != null) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.warning)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                onOkClick()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    } else null
}

fun Fragment.showChangePasswordDialog(
    onOkClick: (String, String, AlertDialog) -> Unit
): AlertDialog? {
    return if (context != null) {
        val binding = DialogChangePasswordBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_password)
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel) { d, _ ->
                d.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val newPassword = binding.edtNewPassword.text.toString().trim()
                val confirmNewPassword = binding.edtConfirmNewPassword.text.toString().trim()
                onOkClick(newPassword, confirmNewPassword, dialog)
            }
        }

        dialog.show()
        dialog
    } else null
}

fun Fragment.showTimePickerDialog(
    onOkClick: (String) -> Unit
): AlertDialog? {
    val calendar = Calendar.getInstance()

    // Dummy dialog để quản lý cancel/back
    val dummyDialog = AlertDialog.Builder(requireContext()).create()
    dummyDialog.setCancelable(true)

    val timePickerDialog = TimePickerDialog(
        requireContext(),
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            // Format giờ
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = sdf.format(calendar.time)

            // Callback trả về string
            onOkClick(formattedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // 24h format
    )

    timePickerDialog.show()
    return dummyDialog
}