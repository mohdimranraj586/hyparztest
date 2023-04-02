package com.hyparz.utils

abstract class ClickListener : EvenListener {

    override fun onFilterApply(
        status: Boolean,
        tagStrArray: MutableList<String>,
        sortByPostLike: String,
        sortByPostComment: String
    ) {
    }

    override fun onPickImagefromcamera() {
    }

    override fun onPickImagefromgallery() {
    }

    override fun onReport(reason: String) {
    }

    override fun ondeleteAccount() {
    }

    override fun onExitApplication() {
    }

    override fun onShareCommunity() {
    }

    override fun onForceUpdate() {
    }

    override fun onsuccessEvent() {
    }

    override fun onSelectCurrency(currency: String) {
    }

    override fun onSignout() {
    }

    override fun onPodtype(podType: String) {
    }

    override fun omManageQuestion() {
    }

    override fun onResetStats() {
    }

    override fun onResetDate() {
    }

    override fun onShareFB() {
    }

    override fun onShareTwitter() {
    }

    override fun onCravingRegistance() {
    }

    override fun onGuidedBreathingNext() {
    }

    override fun onAccountabilityPostNext() {
    }

    override fun onSavingGoalsNext() {
    }

    override fun onMoveToSignIn() {
    }

    override fun onGuestYesOrNo(guestUser: String) {
    }
}