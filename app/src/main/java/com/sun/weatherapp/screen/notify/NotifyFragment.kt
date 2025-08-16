package com.sun.weatherapp.screen.notify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.databinding.FragmentNotifyBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.notify.adapter.NotifyAdapter
import com.sun.weatherapp.utils.showTimePickerDialog

class NotifyFragment : BaseFragment<FragmentNotifyBinding, NotifyPresenter>(), NotifyContract.View {

    private lateinit var notifyAdapter: NotifyAdapter

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
                showTimePickerDialog { formattedDateTime ->
                    presenter?.addNotification(formattedDateTime)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        notifyAdapter = NotifyAdapter { notification ->
            presenter?.removeNotification(notification)
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