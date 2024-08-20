package com.example.noted.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noted.MainActivity

import com.example.noted.R
import com.example.noted.adapter.NoteAdapter
import com.example.noted.adapter.NoteViewModel
import com.example.noted.databinding.FragmentHomeBinding
import com.example.noted.model.Note


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider {

    //setup binding
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initializing data binding
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Menu host
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initialize main activity view model
        notesViewModel = (activity as MainActivity).noteViewModel

        setupHomeRecyclerView()

        binding.addNoteFab.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun updateUI(note: List<Note>?){
        if(note != null){
            if(note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        noteAdapter= NoteAdapter()

        binding.homeRecyclerView.apply {
            //spanCount means no. of grids in a row, here it is 2
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter= noteAdapter
        }

        activity?.let{
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){ note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private fun searchNote(query: String?){
        // % is a wild card character
        val searchQuery= "%$query"

        notesViewModel.searchNote(searchQuery).observe(this){ list ->
            noteAdapter.differ.submitList(list)
        }
    }

    // I dont want to show search results only after typing it whole and submitting it so keeping it false
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    // I want to show search result as I keep typing or in coding sense , on Change
    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText !=null){
            searchNote(newText)
        }
        return true
    }

    //when home fragment is no longer in use
    override fun onDestroy(){
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch= menu.findItem(R.id.searchMenu).actionView as SearchView
        //we dont require any submit button
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}