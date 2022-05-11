package com.example.happyplacesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.adapter.ItemAdapter
import com.example.happyplacesapp.auth.SignInActivity
import com.example.happyplacesapp.data.Datasource
import com.example.happyplacesapp.databinding.FragmentTitleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_title.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TitleFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ItemAdapter.ItemViewHolder>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)
        setHasOptionsMenu(true)

        if (container != null) {
            var context = container.context
        }

        return binding.root
    }

//    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(itemView, savedInstanceState)
//        val myDataset = Datasource().loadPlaces()
//        R.id.recycler_view.apply {
//            // set a LinearLayoutManager to handle Android
//            // RecyclerView behavior
//            layoutManager = LinearLayoutManager(activity)
//            // set the custom adapter to the RecyclerView
//            adapter = context?.let { ItemAdapter(it, myDataset) }
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
        checkCurrentUser(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        if ( id == R.id.redirect_sign_in) {
            val intent : Intent = Intent(activity, SignInActivity::class.java)
            activity?.startActivity(intent)
            return true
        }
        else if (id == R.id.log_out_button) {
            Firebase.auth.signOut()
            val refresh = Intent(activity, MainActivity::class.java)
            activity?.startActivity(refresh)
            activity?.finish()
            return true
        }
        return NavigationUI.
        onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    public fun checkCurrentUser(menu: Menu) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            menu.getItem(0).setVisible(false)
            menu.getItem(1).setVisible(true)
        } else {
            menu.getItem(0).setVisible(true)
            menu.getItem(1).setVisible(false)
        }
    }
}