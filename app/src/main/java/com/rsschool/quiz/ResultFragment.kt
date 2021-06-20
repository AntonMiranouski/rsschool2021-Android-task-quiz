package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var listener: OnChangeFragmentListener? = null
    private var position = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnChangeFragmentListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    listener?.onChangeFragmentListener(0, intArrayOf(-1, -1, -1, -1, -1))
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedOptions =
            arguments?.getIntArray(SELECTED_OPTIONS_KEY) ?: intArrayOf(-1, -1, -1, -1, -1)

        var rightAnswers = 0

        for (i in selectedOptions.indices) {
            if (selectedOptions[i] == SampleData.questions[i].correctAnswer) {
                rightAnswers++
            }
        }

        val result = "Your result: $rightAnswers/" + selectedOptions.size
        var questionsAndAnswers = result

        binding.resultTv.text = result

        for (i in 1 until selectedOptions.size + 1) {
            questionsAndAnswers =
                questionsAndAnswers + "\n" + "\n $i) " + SampleData.questions[i - 1].question +
                        "\n Your answer: " +
                        SampleData.questions[i - 1].options[selectedOptions[i - 1]] + "\n"
        }

        binding.shareIv.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Quiz result")
                putExtra(Intent.EXTRA_TEXT, questionsAndAnswers)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        binding.backIv.setOnClickListener {
            listener?.onChangeFragmentListener(0, intArrayOf(-1, -1, -1, -1, -1))
        }

        binding.closeIv.setOnClickListener {
            activity?.finishAndRemoveTask()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(position: Int, selectedOptions: IntArray): ResultFragment {
            val fragment = ResultFragment()
            fragment.position = position

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