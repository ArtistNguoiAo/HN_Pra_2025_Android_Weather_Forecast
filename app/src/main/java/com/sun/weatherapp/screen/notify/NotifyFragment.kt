package com.sun.weatherapp.screen.notify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.databinding.FragmentNotifyBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.notify.adapter.NotifyAdapter
import com.sun.weatherapp.utils.showTimePickerDialog

class NotifyFragment : BaseFragment<FragmentNotifyBinding, NotifyPresenter>(), NotifyContract.View {

    private lateinit var notifyAdapter: NotifyAdapter
    val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentNotifyBinding {
        return FragmentNotifyBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = NotifyPresenter()
        presenter?.attachView(this)
    }

    override fun setupViews() {
        setupRecyclerViews()
        presenter?.getNotifications()
    }

    override fun setupListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnAddNotify.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            REQUEST_CODE_POST_NOTIFICATIONS
                        )
                        return@setOnClickListener
                    }
                }

                showTimePickerDialog { formattedDateTime ->
                    presenter?.addNotification(formattedDateTime)
                    context?.let { ctx ->
                        AlarmHelper.scheduleAlarm(ctx, formattedDateTime)
                    }
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        notifyAdapter = NotifyAdapter { notification ->
            presenter?.removeNotification(notification)
            context?.let { ctx ->
                AlarmHelper.cancelAlarm(ctx, notification)
            }
        }
        binding.rvNotify.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notifyAdapter
        }
    }

    override fun showNotifications(notifications: List<String>) {
        notifyAdapter.submitList(notifications)
    }
}