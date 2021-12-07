package com.br.appcontatos

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.ContactsContract.CommonDataKinds.Phone

import android.provider.ContactsContract.CommonDataKinds.Email
import android.content.ContentResolver
import android.database.Cursor


class MainActivity : AppCompatActivity() {

    val REQUEST_CONTACT = 1
    val LINEAR_LAYOUT_VERTICAL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACT)
        } else {
            setContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CONTACT) setContacts()
    }

    private fun setContacts() {

        val contactList: ArrayList<Contact> = ArrayList()
/*
        var cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
            null
        )

 */

        val contentResolver = contentResolver
        val cursor: Cursor? = contentResolver.query(
            Phone.CONTENT_URI, arrayOf(
                Phone._ID,
                Phone.DISPLAY_NAME,
                Phone.NUMBER,
                Phone.TYPE,
                Phone.LABEL,
                Phone.PHOTO_THUMBNAIL_URI
            ),
            null, null, Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactList.add(Contact(
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                ))
            }
            cursor.close()
        }

        val adapter = ContactsAdapter(contactList)
        val contactRecyclerView = findViewById<RecyclerView>(R.id.contacts_recycler_view)

        contactRecyclerView.layoutManager = LinearLayoutManager(this,
        LINEAR_LAYOUT_VERTICAL,
        false)
        contactRecyclerView.adapter = adapter

    }
}