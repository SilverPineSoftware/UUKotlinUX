package com.silverpine.uu.ux

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom
import com.silverpine.uu.core.UUTimer
import com.silverpine.uu.core.uuDispatchMain
import com.silverpine.uu.logging.UULog

class UUTypewriterTextViewModel: ViewModel()
{
    private val timerId: String = UURandom.uuid()
    enum class AnimationType
    {
        Letters,
        Words
    }

    private data class TextSection(val text: String, val delayMillis: Long = 20L)

    private var _text: MutableLiveData<String> = MutableLiveData()
    var text: LiveData<String> = _text
    var completion: ()->Unit = { }

    private var model: String = ""
    private var animationType = AnimationType.Letters
    private var sections: ArrayList<TextSection> = arrayListOf()
    private var didCompleteTyping = false

    fun update(model: String)
    {
        this.model = model
        _text.value = ""
        didCompleteTyping = false
        splitIntoParts()
        typeNextSection()
    }

    fun forceComplete()
    {
        UULog.d(javaClass, "forceComplete", "Text playback force quit")
        notifyCompletion()
        _text.value = model
    }

    private fun typeNextSection()
    {
        if (didCompleteTyping)
        {
            return
        }

        val section = sections.removeFirstOrNull()

        if (section == null)
        {
            notifyCompletion()
            return
        }

        _text.value = _text.value + section.text

        UUTimer.startTimer(timerId, section.delayMillis, section)
        { _, _ ->

            uuDispatchMain(this::typeNextSection)
        }
    }

    private fun notifyCompletion()
    {
        didCompleteTyping = true
        UUTimer.cancelActiveTimer(timerId)
        sections.clear()
        completion.invoke()
    }

    private fun splitIntoParts()
    {
        sections.clear()
        when (animationType)
        {
            AnimationType.Letters ->
            {
                model.toCharArray().forEach()
                { letter ->
                    sections.add(TextSection(letter.toString(), letter.delayForLetter()))
                }
            }

            AnimationType.Words ->
            {
                model.toWordArray().forEach()
                { word ->
                    sections.add(TextSection(word, word.delayForWord()))
                }
            }
        }
    }

    private fun Char.delayForLetter(): Long
    {
        return when (this)
        {
            '.', '\n' -> 750
            else -> 20
        }
    }

    private fun String.delayForWord(): Long
    {
        if (this.contains('.') ||
                this.contains('\n'))
        {
            return 1400
        }
        else
        {
            return (this.count() * 30L)
        }
    }

    private fun String.toWordArray(): Array<String>
    {
        return this.split(" ").toTypedArray()
    }
}