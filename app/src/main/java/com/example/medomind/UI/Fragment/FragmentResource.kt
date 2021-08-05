package com.example.medomind.UI.Fragment

interface FragmentResource {
    companion object {
        val tabList = listOf("Pending", "Accepted", "Rejected")
        val pagerFragments = listOf(PendingReqFragment.create(), AcceptedFragment.create(), RejectedFragment.create())
    }
}