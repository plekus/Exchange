package ru.app.currency.presentation.pages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import moxy.MvpAppCompatFragment
import ru.app.currency.R
import ru.app.currency.presentation.pages.main.MainListener
import ru.app.currency.presentation.updateText
import ru.app.domain.model.CurrencyDetails

class CurrencyFragment : MvpAppCompatFragment(), IFragmentListener, TextWatcher {

    private lateinit var etAmount: EditText
    private lateinit var tvTitle: TextView
    private lateinit var tvAvailableAmount: TextView
    private lateinit var tvRate: TextView

    private val currencyDetails: CurrencyDetails
        get() = arguments?.getSerializable(ARG_CURRENCY) as? CurrencyDetails
            ?: throw RuntimeException(" no arg $ARG_CURRENCY")

    private val isStartCurrency: Boolean
        get() = arguments?.getBoolean(ARG_IS_START_CURRENCY)
            ?: throw RuntimeException(" no arg $ARG_IS_START_CURRENCY")

    private val mainListener: MainListener
        get() = (activity as? MainListener) ?: throw RuntimeException("activity not MainListener")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false).apply {
            etAmount = findViewById(R.id.etAmount)
            tvTitle = findViewById(R.id.tvTitle)
            tvAvailableAmount = findViewById(R.id.tvAvailableAmount)
            tvRate = findViewById(R.id.tvRate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etAmount.addTextChangedListener(this)
        setUpViews()
    }

    override fun onShow() {
        setUpViews()
    }

    override fun onHide() {
        etAmount.clearFocus()
        etAmount.updateText("")
    }

    override fun updateDetails(newDetails: CurrencyDetails) {
        val bundle = Bundle()
        bundle.putSerializable(ARG_CURRENCY, newDetails)
        bundle.putSerializable(ARG_IS_START_CURRENCY, isStartCurrency)
        arguments = bundle
        setUpViews()
    }

    override fun onConvertSuccess() {
        val details = currencyDetails
        val currency = details.currency
        etAmount.clearFocus()
        etAmount.updateText("")
        tvAvailableAmount.text = getString(R.string.currency_amount, mainListener.getAvailableAmount(currency), currency.icon)
    }

    override fun notifyChange() {
        tvRate.text = if (isStartCurrency) mainListener.getStartCurrencyRate() else mainListener.getEndCurrencyRate()
    }

    override fun getEnteredText(): String? {
        return etAmount.text?.toString()
            ?.replace("-", "")
            ?.replace("+", "")
            ?.replace(" ", "")
    }

    override fun updateAmount(string: String) {
        etAmount.updateText(string)
    }

    private fun setUpViews() {
        val details = currencyDetails
        val currency = details.currency
        tvTitle.text = currency.title
        tvAvailableAmount.text = getString(R.string.currency_amount, mainListener.getAvailableAmount(currency), currency.icon)
        tvRate.text =
            if (isStartCurrency) mainListener.getStartCurrencyRate() else mainListener.getEndCurrencyRate()
        val text = if (isStartCurrency) mainListener.getAmountForStart()
        else mainListener.getAmountForEnd()
        etAmount.updateText(text)

    }

    override fun afterTextChanged(s: Editable?) {
        s ?: return
        val char = if (isStartCurrency) "-" else "+"
        val regex = if (isStartCurrency) "- [0-9]{0,3}\\.?[0-9]{0,2}".toRegex() else "\\+ [0-9]{0,3}\\.?[0-9]{0,2}".toRegex()
        if (!s.toString().startsWith(char) && s.isNotBlank()) {
            s.insert(0, "$char ")
        } else if (s.toString() == "$char ") {
            s.clear()
        } else {
            while (!regex.matches(s.toString()) && s.isNotEmpty()) {
                s.delete(s.lastIndex, s.length)
            }
        }

        if (!etAmount.hasFocus()) return
        if (isStartCurrency) mainListener.onStartCurrencyEdited()
        else mainListener.onEndCurrencyEdited()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)= Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        etAmount.removeTextChangedListener(this)
    }

    companion object {

        private const val ARG_CURRENCY = "currency"
        private const val ARG_IS_START_CURRENCY = "is_start"

        fun getFragment(
            currencyDetails: CurrencyDetails,
            isStartCurrency: Boolean
        ): CurrencyFragment {
            val fragment = CurrencyFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_CURRENCY, currencyDetails)
            bundle.putBoolean(ARG_IS_START_CURRENCY, isStartCurrency)
            fragment.arguments = bundle
            return fragment
        }

    }

}
