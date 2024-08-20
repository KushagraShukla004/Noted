package com.example.noted.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.noted.MainActivity
import com.example.noted.R

import com.example.noted.adapter.NoteViewModel
import com.example.noted.databinding.FragmentAddNoteBinding
import com.example.noted.model.Note


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    //setup binding
    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initializing data binding
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Menu host
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initialize main activity view model
        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

    }

    private fun saveNote(view: View) {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Note Saved!", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addNoteView.context, "Please Enter Note Title.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_node, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }

            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }
}