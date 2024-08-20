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
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noted.MainActivity
import com.example.noted.R
import com.example.noted.adapter.NoteViewModel
import com.example.noted.databinding.FragmentEditNoteBinding
import com.example.noted.model.Note


class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {

    //setup binding
    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup Menu host
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initialize main activity view model
        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        binding.editNoteFab.setOnClickListener{
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if(noteTitle.isNotEmpty()){
                val note = Note(currentNote.id, noteTitle, noteDesc)
                notesViewModel.updateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }else{
                Toast.makeText(context, "Please Enter Note Title.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote(){
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Delete Note")
                setMessage("Are you sure you want to delete this note?")
                setPositiveButton("Delete"){_,_ ->
                    notesViewModel.deleteNote(currentNote)
                    Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
                    view?.findNavController()?.popBackStack(R.id.homeFragment, false)
                }
                setNegativeButton("Cancel", null)
            }.create().show()
        }
    }
    private fun saveNote() {
        val noteTitle = binding.editNoteTitle.text.toString().trim()
        val noteDesc = binding.editNoteDesc.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(currentNote.id, noteTitle, noteDesc)
            notesViewModel.updateNote(note)
            view?.findNavController()?.navigate(R.id.action_editNoteFragment_to_homeFragment)
        } else {
            Toast.makeText(context, "Please Enter Note Title.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_node, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return  when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            }
            R.id.saveMenu -> {
                saveNote()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }
}