package ru.app.currency.presentation.pages.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.app.currency.R
import ru.app.currency.di.AppComponent
import ru.app.currency.presentation.pages.PageAdapter
import ru.app.domain.model.Currency
import ru.app.domain.model.CurrencyDetails
import ru.app.domain.utility.format2decimals
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(),
    MainView,
    MainListener {

    private lateinit var startPageAdapter: PageAdapter
    private lateinit var endPageAdapter: PageAdapter

    private var alertDialog: AlertDialog? = null

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun provide() = presenter

    init {
        AppComponent.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvExchange.setOnClickListener { presenter.convert(startPageAdapter.getCurrentDetails(), endPageAdapter.getCurrentDetails()?.currency, getAmountForStart().toFloat()) }
        setUpViewPagers()
    }

    private fun setUpViewPagers() {
        startPageAdapter = PageAdapter(supportFragmentManager, vpCurrencyFrom, true)
        vpCurrencyFrom.adapter = startPageAdapter
        vpCurrencyFrom.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            private var page = 0

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                startPageAdapter.getFragment(page)?.onHide()
                startPageAdapter.getFragment(position)?.onShow()
                endPageAdapter.getCurrentFragment()?.notifyChange()
                tvRate.text = getStartCurrencyRate()
                page = position
            }
        })

        endPageAdapter = PageAdapter(supportFragmentManager, vpCurrencyTo, false)
        vpCurrencyTo.adapter = endPageAdapter
        vpCurrencyTo.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            private var page = 0

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                endPageAdapter.getFragment(page)?.onHide()
                endPageAdapter.getFragment(position)?.onShow()
                startPageAdapter.getCurrentFragment()?.notifyChange()
                tvRate.text = getStartCurrencyRate()
                page = position
            }
        })

    }

    override fun getAvailableAmount(currency: Currency): String {
        return presenter.getAvailableAmount(currency).format2decimals()
    }

    override fun getStartCurrencyRate(): String {
        return getStringCurrenciesRate(
            startPageAdapter.getCurrentDetails(),
            endPageAdapter.getCurrentDetails()?.currency
        )
    }

    override fun getEndCurrencyRate(): String {
        return getStringCurrenciesRate(
            endPageAdapter.getCurrentDetails(),
            startPageAdapter.getCurrentDetails()?.currency
        )
    }

    override fun getAmountForStart(): String {
        val rate = startPageAdapter.getCurrentDetails()?.rates?.get(endPageAdapter.getCurrentDetails()?.currency)
        rate ?: return ""
        val fromFloat = endPageAdapter.getCurrentFragment()?.getEnteredText()?.toFloatOrNull()
        fromFloat ?: return ""
        return (fromFloat / rate).format2decimals()
    }

    override fun getAmountForEnd(): String {
        val rate = startPageAdapter.getCurrentDetails()?.rates?.get(endPageAdapter.getCurrentDetails()?.currency)
        rate ?: return ""
        val fromFloat = startPageAdapter.getCurrentFragment()?.getEnteredText()?.toFloatOrNull()
        fromFloat ?: return ""
        return (fromFloat * rate).format2decimals()
    }

    private fun getStringCurrenciesRate(
        fromCurrencyDetails: CurrencyDetails?,
        toCurrency: Currency?
    ): String {
        val currency = toCurrency ?: fromCurrencyDetails?.currency ?: return ""
        val rate = fromCurrencyDetails?.rates?.get(currency) ?: return ""
        return "${fromCurrencyDetails.currency.icon}1 = ${currency.icon}${rate.format2decimals()}"
    }

    override fun onStartCurrencyEdited() {
        endPageAdapter.getCurrentFragment()?.updateAmount(getAmountForEnd())
    }

    override fun onEndCurrencyEdited() {
        startPageAdapter.getCurrentFragment()?.updateAmount(getAmountForStart())
    }

    override fun updateAdapters(items: List<CurrencyDetails>) {
        startPageAdapter.update(items)
        endPageAdapter.update(items)
        ivArrow.visibility = View.VISIBLE
        tvRate.text = getStartCurrencyRate()
    }

    override fun showNotEnoughAmount() {
        alertDialog = AlertDialog.Builder(this)
            .setMessage(R.string.not_enough)
            .setPositiveButton(R.string.action_ok) { _, _ ->  }
            .show()
    }

    override fun onConvertSuccess(fromCurrency: Currency, toCurrency: Currency, amount: Float) {
        val receipt = getString(R.string.receipt, "${toCurrency.icon}${amount.format2decimals()}", toCurrency.title, "${toCurrency.icon}${presenter.getAvailableAmount(toCurrency)}")
        val accounts = startPageAdapter.items.joinToString(separator = "") { "${it.currency.title}: ${it.currency.icon}${presenter.getAvailableAmount(it.currency)}\n" }
        val availableAccounts = getString(R.string.accounts, accounts)
        alertDialog = AlertDialog.Builder(this)
            .setMessage("$receipt \n\n$availableAccounts")
            .setPositiveButton(R.string.action_ok) { _, _ ->  }
            .show()
        startPageAdapter.getCurrentFragment()?.onConvertSuccess()
        endPageAdapter.getCurrentFragment()?.onConvertSuccess()
    }

    override fun onStop() {
        super.onStop()
        alertDialog?.dismiss()
    }

}
