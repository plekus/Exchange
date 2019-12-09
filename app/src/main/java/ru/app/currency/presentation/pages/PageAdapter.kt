package ru.app.currency.presentation.pages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import ru.app.domain.model.CurrencyDetails

class PageAdapter(
    fm: FragmentManager,
    val vp: ViewPager,
    val isStart: Boolean
) : FragmentStatePagerAdapter(fm) {

    var items = listOf<CurrencyDetails>()
    private set

    override fun getItem(position: Int): Fragment = CurrencyFragment.getFragment(items[position], isStart)

    override fun getCount(): Int = items.size

    fun update(details: List<CurrencyDetails>) {
        if (items.isEmpty()) {
            items = details
            notifyDataSetChanged()
            return
        }
        items.forEachIndexed { index, oldDetails ->
            details.firstOrNull { it.currency == oldDetails.currency }?.let { newDetails ->
                if (newDetails != oldDetails) {
                    getFragment(index)?.updateDetails(newDetails)
                }
            }
        }
    }

    fun getFragment(position: Int): CurrencyFragment? {
        return (instantiateItem(vp, position) as? CurrencyFragment)
    }

    fun getCurrentFragment(): CurrencyFragment? {
        return (instantiateItem(vp, vp.currentItem) as? CurrencyFragment)
    }

    fun getCurrentDetails(): CurrencyDetails? {
        return items.getOrNull(vp.currentItem)
    }

}