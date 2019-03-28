package com.mentalmachines.droidcon_boston.views.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.R.string
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.EventSpeaker
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.AuthController
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import com.mentalmachines.droidcon_boston.utils.ServiceLocator.Companion.gson
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import com.mentalmachines.droidcon_boston.views.MainActivity
import com.mentalmachines.droidcon_boston.views.rating.RatingDialog
import com.mentalmachines.droidcon_boston.views.rating.RatingRepo
import com.mentalmachines.droidcon_boston.views.transform.CircleTransform
import kotlinx.android.synthetic.main.agenda_detail_fragment.*

class AgendaDetailFragment : Fragment() {

    val ratingRepo = RatingRepo(AuthController.userId.orEmpty(), FirebaseHelper.instance.userDatabase)

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val scheduleRowItem = gson.fromJson(
                arguments?.getString(Schedule.SCHEDULE_ITEM_ROW),
                ScheduleRow::class.java
            )

            val userAgendaRepo = UserAgendaRepo.getInstance(requireContext())

            @Suppress("UNCHECKED_CAST")
            return AgendaDetailViewModel(scheduleRowItem, userAgendaRepo, ratingRepo) as T
        }
    }

    private lateinit var viewModel: AgendaDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.agenda_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()
        loadData()
        populateView()

        (activity as? MainActivity)?.uncheckAllMenuItems()
    }

    private fun loadData() {
        viewModel.loadData()
    }

    private fun initializeViewModel() {
        viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(AgendaDetailViewModel::class.java)

        viewModel.scheduleDetail.observe(viewLifecycleOwner, Observer {
            it?.let(this::showAgendaDetail)
        })

        viewModel.ratingValue.observe(viewLifecycleOwner, Observer {
            it?.let{
                session_rating.rating = it.toFloat()
            }
        })
    }

    private fun populateView() {
        tv_agenda_detail_title.text = viewModel.talkTitle

        // WORKAROUND FOR https://github.com/Droidcon-Boston/conference-app-android/issues/165
        // If the talk title is Check-In then hardcode the room name to be the lobby
        if (viewModel.talkTitle.toLowerCase().contains("check-in")) {
            tv_agenda_detail_room.text =
                resources.getString(R.string.str_agenda_detail_room, "Calderwood Pavilion Lobby")

        } else {
            tv_agenda_detail_room.text =
                resources.getString(R.string.str_agenda_detail_room, viewModel.room)
        }
        tv_agenda_detail_time.text = resources.getString(
            R.string.str_agenda_detail_time,
            viewModel.startTime,
            viewModel.endTime
        )

        fab_agenda_detail_bookmark.setOnClickListener {
            viewModel.toggleBookmark()

            if (viewModel.isBookmarked) {
                NotificationUtils(requireContext()).scheduleMySessionNotifications()
            } else {
                NotificationUtils(requireContext()).cancelNotificationAlarm(viewModel.schedulerowId)
            }

            Snackbar.make(
                agendaDetailView,
                viewModel.bookmarkSnackbarRes,
                Snackbar.LENGTH_SHORT
            ).show()

            showBookmarkStatus()
        }

        session_rating_overlay.setOnClickListener {
            if (AuthController.isLoggedIn) {
                showRatingDialog()
            } else {
                AuthController.login(this, RC_SIGN_IN_FEEDBACK, R.mipmap.ic_launcher)
            }
        }

        populateSpeakersInformation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN_FEEDBACK) {
            ratingRepo.userId = AuthController.userId.orEmpty()
            loadData()

            showRatingDialog()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.removeListener()
    }

    private fun populateSpeakersInformation() = when {
        viewModel.speakerNames.isEmpty() -> {
            tv_agenda_detail_speaker_name.visibility = View.GONE
            v_agenda_detail_speaker_divider.visibility = View.GONE
        }
        else -> {
            var speakerNames = ""
            val imgViewSize = resources.getDimension(R.dimen.imgv_speaker_size).toInt()
            var marginValue = resources.getDimension(R.dimen.def_margin).toInt()
            val offsetImgView = resources.getDimension(R.dimen.imgv_speaker_offset).toInt()
            val defaultLeftMargin = resources.getDimension(R.dimen.def_margin).toInt()

            viewModel.speakerNames.forEach { speakerName ->
                val orgName: String? = viewModel.getOrganizationForSpeaker(speakerName)
                // append org name to speaker name
                speakerNames += speakerName + when {
                    orgName != null -> " - $orgName"
                    else -> {
                        ' '
                    }
                }

                if (viewModel.speakerNames.size > 1) {
                    tv_agenda_detail_speaker_title.text = getString(string.header_speakers)

                    // if the current speaker name is not the last then add a line break
                    if (speakerName != viewModel.speakerNames.last()) {
                        speakerNames += "\n"
                    }
                } else {
                    tv_agenda_detail_speaker_title.text = getString(string.header_speaker)
                }

                // Add an imageview to the relative layout
                val tempImg = ImageView(activity)
                val lp = RelativeLayout.LayoutParams(imgViewSize, imgViewSize)
                if (speakerName == viewModel.speakerNames.first()) {
                    lp.setMargins(marginValue, 0, 0, defaultLeftMargin)
                } else {
                    marginValue += offsetImgView
                    lp.setMargins(marginValue, 0, 0, defaultLeftMargin)
                }

                // add the imageview above the textview for room data
                lp.addRule(RelativeLayout.ABOVE, tv_agenda_detail_room.id)
                lp.addRule(RelativeLayout.BELOW, session_rating_overlay.id)
                tempImg.layoutParams = lp

                // add speakerName as a child to the relative layout
                agendaDetailView.addView(tempImg)

                Glide.with(requireContext())
                    .load(viewModel.getPhotoForSpeaker(speakerName))
                    .transform(CircleTransform(tempImg.context))
                    .placeholder(R.drawable.emo_im_cool)
                    .crossFade()
                    .into(tempImg)

                tempImg.setOnClickListener {
                    val eventSpeaker = viewModel.getSpeaker(speakerName)
                    val arguments = Bundle()

                    arguments.putString(
                        EventSpeaker.SPEAKER_ITEM_ROW,
                        gson.toJson(eventSpeaker, EventSpeaker::class.java)
                    )

                    val speakerDetailFragment = SpeakerDetailFragment()
                    speakerDetailFragment.arguments = arguments

                    val fragmentManager = activity?.supportFragmentManager

                    fragmentManager?.beginTransaction()
                        ?.add(R.id.fragment_container, speakerDetailFragment)
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }

            tv_agenda_detail_speaker_name.text = speakerNames
        }
    }

    private fun showAgendaDetail(scheduleDetail: ScheduleDetail) {
        populateSpeakersInformation()
        showBookmarkStatus()

        tv_agenda_detail_title.text = scheduleDetail.listRow.talkTitle
        tv_agenda_detail_description.text =
                scheduleDetail.listRow.talkDescription.getHtmlFormattedSpanned()

        tv_agenda_detail_description.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showBookmarkStatus() {
        val color = ContextCompat.getColor(requireContext(), viewModel.bookmarkColorRes)

        fab_agenda_detail_bookmark.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun showRatingDialog() {
        RatingDialog.newInstance(viewModel.schedulerowId)
            .show(fragmentManager, RATE_DIALOG_TAG)
    }

    companion object {
        private const val RATE_DIALOG_TAG = "RATE_DIALOG"
        private const val RC_SIGN_IN_FEEDBACK = 2

        fun addDetailFragmentToStack(
            supportFragmentManager: FragmentManager,
            itemData: Schedule.ScheduleRow
        ) {
            val arguments = Bundle()
            arguments.putString(
                Schedule.SCHEDULE_ITEM_ROW,
                gson.toJson(itemData, ScheduleRow::class.java)
            )

            val agendaDetailFragment = AgendaDetailFragment()
            agendaDetailFragment.arguments = arguments

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, agendaDetailFragment).addToBackStack(null).commit()
        }
    }
}
