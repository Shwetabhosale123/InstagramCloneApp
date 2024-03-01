package com.example.instagramcloneapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramcloneapp.Models.Post
import com.example.instagramcloneapp.Models.Reel
import com.example.instagramcloneapp.R
import com.example.instagramcloneapp.adapters.MyPostRvAdapter
import com.example.instagramcloneapp.adapters.MyReelAdapter
import com.example.instagramcloneapp.databinding.FragmentMyPostBinding
import com.example.instagramcloneapp.databinding.FragmentMyReelsBinding
import com.example.instagramcloneapp.utils.REEL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyReelsFragment : Fragment() {
    private lateinit var binding: FragmentMyReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMyReelsBinding.inflate(inflater, container, false)

        var reelList=ArrayList<Reel>()
        var adapter= MyReelAdapter(requireContext(),reelList)
        binding.rv.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter=adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).get().addOnSuccessListener {

            var tempList=arrayListOf<Reel>()
            for (i in it.documents){
                var reel: Reel =i.toObject<Reel>()!!
                tempList.add(reel)
            }
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}