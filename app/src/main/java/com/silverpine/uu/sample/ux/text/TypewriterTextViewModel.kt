package com.silverpine.uu.sample.ux.text

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.silverpine.uu.sample.ux.viewmodels.BaseViewModel
import com.silverpine.uu.ux.UUMenuItem
import com.silverpine.uu.ux.UUToast
import com.silverpine.uu.ux.viewmodel.UUTypewriterTextViewModel

class TypewriterTextViewModel: BaseViewModel()
{
    private var _typewriter = UUTypewriterTextViewModel()
    val text: LiveData<String> = _typewriter.text

    init
    {
        _typewriter.completion =
        {
            onToast(UUToast("Typewriter completed", Toast.LENGTH_SHORT))
        }
    }

    fun update()
    {
        updateMenu()
    }

    override fun buildMenu(menu: ArrayList<UUMenuItem>)
    {
        menu.add(UUMenuItem("Type Short Text",
        {
            _typewriter.update("The quick brown fox jumps over the lazy dog.")

        }))

        menu.add(UUMenuItem("Type Long Text",
        {
            _typewriter.update("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
        }))

        menu.add(UUMenuItem("Set Short Text",
        {
            _typewriter.setText("The quick brown fox jumps over the lazy dog.")
        }))

        menu.add(UUMenuItem("Set Long Text",
        {
            _typewriter.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
        }))
    }

    fun onTap()
    {
        _typewriter.forceComplete()
    }
}





