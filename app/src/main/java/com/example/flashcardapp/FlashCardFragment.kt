package com.example.flashcardapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.flashcardapp.databinding.FragmentFlashCardBinding

class FlashCardFragment : Fragment() {

    private lateinit var binding: FragmentFlashCardBinding
    private var isQuestionVisible = true

    companion object {
        private const val ARG_QUESTION = "question"
        private const val ARG_ANSWER = "answer"

        fun newInstance(question: String, answer: String): FlashCardFragment {
            val fragment = FlashCardFragment()
            val args = Bundle()
            args.putString(ARG_QUESTION, question)
            args.putString(ARG_ANSWER, answer)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlashCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = arguments?.getString(ARG_QUESTION)
        val answer = arguments?.getString(ARG_ANSWER)

        binding.questionTextView.text = question
        binding.answerTextView.text = answer
        binding.answerTextView.visibility = View.GONE

        binding.cardView.setOnClickListener {
            flipCard()
        }
    }

    private fun flipCard() {
        val cardView = binding.cardView
        val questionTextView = binding.questionTextView
        val answerTextView = binding.answerTextView

        val flipOutAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_card_out)
        val flipInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_card_in)

        flipOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (isQuestionVisible) {
                    questionTextView.visibility = View.GONE
                    answerTextView.visibility = View.VISIBLE
                } else {
                    questionTextView.visibility = View.VISIBLE
                    answerTextView.visibility = View.GONE
                }
                isQuestionVisible = !isQuestionVisible
                cardView.startAnimation(flipInAnim)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        cardView.startAnimation(flipOutAnim)
    }
}
