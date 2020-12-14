package `in`.thelosergeek.projectapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList;

class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitle = ArrayList<String>()



    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
    return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle[position]
    }

    fun addFragmnt(fragment: Fragment, title:String){
        mFragmentList.add(fragment)
        mFragmentTitle.add(title)

    }

}