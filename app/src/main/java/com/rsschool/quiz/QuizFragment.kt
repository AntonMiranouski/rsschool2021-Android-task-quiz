package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var listener: OnChangeFragmentListener? = null
    private var position = 0
    private var selectedOptions = intArrayOf(-1, -1, -1, -1, -1)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnChangeFragmentListener?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setTheme()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedOptions =
            arguments?.getIntArray(SELECTED_OPTIONS_KEY) ?: intArrayOf(-1, -1, -1, -1, -1)

        if (position == 0) {
            binding.previousButton.isEnabled = false
            binding.toolbar.navigationIcon = null
        }

        if (position == 4) {
            binding.nextButton.text = "Submit"
        }

        binding.toolbar.title = "Question " + (position + 1).toString()

        val currentQuestion = SampleData.questions[position]
        binding.question.text = currentQuestion.question
        for ((index, option) in currentQuestion.options.withIndex()) {
            val radioGroup = binding.radioGroup
            val radioButton = RadioButton(context)
            radioButton.apply {
                id = index
                text = option
                textSize = 16F
                height = 120
                if (selectedOptions[position] == index) {
                    isChecked = true
                }
            }
            radioGroup.addView(radioButton)
        }

        if (selectedOptions[position] == -1) {
            binding.nextButton.isEnabled = false
        }

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            if (binding.radioGroup.checkedRadioButtonId != -1) {
                binding.nextButton.isEnabled = true
                selectedOptions[position] = binding.radioGroup.checkedRadioButtonId
            }
        }

        binding.nextButton.setOnClickListener {
            listener?.onChangeFragmentListener(position + 1, selectedOptions)
        }

        binding.previousButton.setOnClickListener {
            listener?.onChangeFragmentListener(position - 1, selectedOptions)
        }

        binding.toolbar.setNavigationOnClickListener {
            listener?.onChangeFragmentListener(position - 1, selectedOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
        _binding = null
    }

    private fun setTheme() {
        when (position) {
            0 -> {
                activity?.setTheme(R.style.Theme_Quiz_First)
                activity?.window?.statusBarColor =
                    resources.getColor(R.color.deep_orange_100_dark, activity?.theme)
            }
            1 -> {
                activity?.setTheme(R.style.Theme_Quiz_Second)
                activity?.window?.statusBarColor =
                    resources.getColor(R.color.yellow_100_dark, activity?.theme)
            }
            2 -> {
                activity?.setTheme(R.style.Theme_Quiz_Third)
                activity?.window?.statusBarColor =
                    resources.getColor(R.color.deep_purple_100_dark, activity?.theme)
            }
            3 -> {
                activity?.setTheme(R.style.Theme_Quiz_Fourth)
                activity?.window?.statusBarColor =
                    resources.getColor(R.color.light_green_100_dark, activity?.theme)
            }
            4 -> {
                activity?.setTheme(R.style.Theme_Quiz_Fifth)
                activity?.window?.statusBarColor =
                    resources.getColor(R.color.cyan_100_dark, activity?.theme)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(position: Int, selectedOptions: IntArray): QuizFragment {
            val fragment = QuizFragment()
            fragment.position = position
            fragment.selectedOptions = selectedOptions

            val args = Bundle().apply {
                putInt(POSITION_KEY, position)
                putIntArray(SELECTED_OPTIONS_KEY, selectedOptions)
            }

            return fragment.apply {
                arguments = args
            }
        }

        private const val POSITION_KEY = "POSITION"
        private const val SELECTED_OPTIONS_KEY = "SELECTED_OPTIONS"
    }
}