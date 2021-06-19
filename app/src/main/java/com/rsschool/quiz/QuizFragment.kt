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

    // TODO : Implement Submit functionality
    // TODO : Implement theme changing

    interface OnQuizFragmentListener {
        fun onQuizFragmentListener(position: Int, selectedOptions: IntArray)
    }

    private lateinit var binding: FragmentQuizBinding
    private var listener: OnQuizFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnQuizFragmentListener?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(POSITION_KEY) ?: 0
        val selectedOptions =
            arguments?.getIntArray(SELECTED_OPTIONS_KEY) ?: intArrayOf(-1, -1, -1, -1, -1)

        if (position == 0) {
            binding.previousButton.isEnabled = false
        }

        if (position == 4) {
            binding.nextButton.text = "Submit"
            binding.nextButton.isEnabled = false
        }

        val currentQuestion = SampleData.questions[position]
        binding.question.text = currentQuestion.question
        for ((index, option) in currentQuestion.options.withIndex()) {
            val radioGroup = binding.radioGroup
            val radioButton = RadioButton(context)
            radioButton.apply {
                id = index
                text = option
                if (selectedOptions[position] == index) {
                    isChecked = true
                }
            }
            radioGroup.addView(radioButton)
        }

        if (selectedOptions[position] != -1) {
            binding.nextButton.isEnabled = true
        }

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            if (binding.radioGroup.checkedRadioButtonId != -1) {
                binding.nextButton.isEnabled = true
                selectedOptions[position] = binding.radioGroup.checkedRadioButtonId
            }
        }

        binding.nextButton.setOnClickListener {
            listener?.onQuizFragmentListener(position + 1, selectedOptions)
        }

        binding.previousButton.setOnClickListener {
            listener?.onQuizFragmentListener(position - 1, selectedOptions)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(position: Int, selectedOptions: IntArray): QuizFragment {
            val fragment = QuizFragment()
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