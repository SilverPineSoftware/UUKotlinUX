package com.silverpine.uu.sample.ux.dialog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.UUAlertDialog
import com.silverpine.uu.ux.UUButton
import com.silverpine.uu.ux.UUMenuHandler
import com.silverpine.uu.ux.uuShowAlertDialog
import com.silverpine.uu.ux.uuShowToast

class AlertDialogActivity : AppCompatActivity()
{
    private lateinit var menuHandler: UUMenuHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_dialog)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val m = menu ?: return false
        menuHandler = UUMenuHandler(m)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        menu?.removeGroup(0)

        menuHandler.add(R.string.message_alert)
        {
            val dlg = UUAlertDialog()
            dlg.message = "This is a dialog message"
            dlg.positiveButton = UUButton(R.string.ok)
            {
                uuShowToast("Ok was tapped on a message dialog")
            }

            uuShowAlertDialog(dlg)
        }

        menuHandler.add(R.string.title_message_alert)
        {
            val dlg = UUAlertDialog()
            dlg.title = "Dialog Title"
            dlg.message = "This is a dialog message"
            dlg.positiveButton = UUButton(R.string.ok)
            {
                uuShowToast("Ok was tapped on a message+title dialog")
            }

            uuShowAlertDialog(dlg)
        }

        menuHandler.add(R.string.yes_no_alert)
        {
            val dlg = UUAlertDialog()
            dlg.message = "This is a yes/no dialog message"
            dlg.positiveButton = UUButton(R.string.yes)
            {
                uuShowToast("Yes was tapped")
            }

            dlg.negativeButton = UUButton(R.string.no)
            {
                uuShowToast("No was tapped")
            }

            uuShowAlertDialog(dlg)
        }

        menuHandler.add(R.string.choices_alert)
        {
            val dlg = UUAlertDialog()
            dlg.title = "Pick an option"
            dlg.items = arrayListOf(
                UUButton("One")
                {
                    uuShowToast("One was tapped")
                },
                UUButton("Two")
                {
                    uuShowToast("Two was tapped")
                },
                UUButton("Three")
                {
                    uuShowToast("Three was tapped")
                },
                UUButton("Four")
                {
                    uuShowToast("Four was tapped")
                }
            )

            uuShowAlertDialog(dlg)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}