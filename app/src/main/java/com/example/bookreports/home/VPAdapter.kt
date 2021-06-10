import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookreports.home.category.OverViewBusinessFragment
import com.example.bookreports.home.category.OverViewHealthFragment
import com.example.bookreports.home.category.OverViewHumanFragment
import com.example.bookreports.home.category.OverViewRecommendFragment


class VPAdapter(fm:FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fm,lifecycle){

    val fragmentList = arrayListOf<Fragment>(

            OverViewRecommendFragment(),
            OverViewBusinessFragment(),
            OverViewHealthFragment(),
            OverViewHumanFragment(),
            OverViewBusinessFragment()


    )
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}
