package com.kozlovskiy.mostocks.utils;

import android.content.Context;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stockInfo.Indicator;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;

import java.util.ArrayList;
import java.util.List;

import static com.kozlovskiy.mostocks.utils.Converter.toBigCurrencyFormat;
import static com.kozlovskiy.mostocks.utils.Converter.toCurrencyFormat;
import static com.kozlovskiy.mostocks.utils.Converter.toDefaultFormat;
import static com.kozlovskiy.mostocks.utils.Converter.toPercentFormat;

public class IndicatorUtil {

    public static List<Object> getIndicatorList(Context c, IndicatorsResponse.Indicators i) {
        List<Object> list = new ArrayList<>();

        if (i.getMarketCap() != null) {
            list.add(c.getString(R.string.financial_indicators));
            list.add(new Indicator(c.getString(R.string.market_cap), c.getString(R.string.comp_val), toBigCurrencyFormat(i.getMarketCap(), 0, 0)));
        }

        if (i.getPe() != null || i.getPs() != null || i.getEps() != null || i.getEpsGrowth5Y() != null || i.getRevenueGrowth5Y() != null)
            list.add(c.getString(R.string.cost_estimate));

        if (i.getPe() != null)
            list.add(new Indicator(c.getString(R.string.pe), c.getString(R.string.sp_per_p), toDefaultFormat(i.getPe(), 0, 2)));

        if (i.getPs() != null)
            list.add(new Indicator(c.getString(R.string.ps), c.getString(R.string.sp_per_pr), toDefaultFormat(i.getPs(), 0, 2)));

        if (i.getEps() != null)
            list.add(new Indicator(c.getString(R.string.dil_eps), c.getString(R.string.dil_eps_per_share), toCurrencyFormat(i.getEps(), 0, 2)));

        if (i.getEpsGrowth5Y() != null)
            list.add(new Indicator(c.getString(R.string.eps_growth), c.getString(R.string.avg_5_y), toPercentFormat(i.getEpsGrowth5Y(), 0, 2)));

        if (i.getRevenueGrowth5Y() != null)
            list.add(new Indicator(c.getString(R.string.rev_grwth), c.getString(R.string.avg_growth_5y), toPercentFormat(i.getRevenueGrowth5Y(), 0, 2)));


        if (i.getRoe() != null || i.getRoa5Y() != null || i.getRoi() != null || i.getDebtPerEquity() != null || i.getNetProfitMargin() != null)
            list.add(c.getString(R.string.profitability));

        if (i.getRoe() != null)
            list.add(new Indicator(c.getString(R.string.roe), c.getString(R.string.return_on_equity), toPercentFormat(i.getRoe(), 0, 2)));

        if (i.getRoa5Y() != null)
            list.add(new Indicator(c.getString(R.string.roa), c.getString(R.string.return_on_assets), toPercentFormat(i.getRoa5Y(), 0, 2)));

        if (i.getRoi() != null)
            list.add(new Indicator(c.getString(R.string.roi), c.getString(R.string.return_on_investment), toPercentFormat(i.getRoi(), 0, 2)));

        if (i.getDebtPerEquity() != null)
            list.add(new Indicator(c.getString(R.string.de), c.getString(R.string.debt_per_equity), toPercentFormat(i.getDebtPerEquity(), 0, 2)));

        if (i.getNetProfitMargin() != null)
            list.add(new Indicator(c.getString(R.string.net_profit_margin), c.getString(R.string.profit_in_perc_of_revenue), toPercentFormat(i.getNetProfitMargin(), 0, 2)));

        if (i.getPayoutRatio() != null || i.getDividendYield() != null || i.getDividendAnnual() != null)
            list.add(c.getString(R.string.dividends));

        if (i.getPayoutRatio() != null)
            list.add(new Indicator(c.getString(R.string.payout_ratio), c.getString(R.string.profit_dv_perc), toPercentFormat(i.getPayoutRatio(), 0, 2)));

        if (i.getDividendYield() != null)
            list.add(new Indicator(c.getString(R.string.avg_div_income), c.getString(R.string.div_income_in_5y), toPercentFormat(i.getDividendYield(), 0, 2)));

        if (i.getDividendAnnual() != null)
            list.add(new Indicator(c.getString(R.string.avg_div_income), c.getString(R.string.div_income_annual), toPercentFormat(i.getDividendAnnual(), 0, 2)));

        if (i.getWeekHigh() != null || i.getWeekLow() != null || i.getBeta() != null)
            list.add(c.getString(R.string.trade_indicators));

        if (i.getWeekHigh() != null)
            list.add(new Indicator(c.getString(R.string.high_52w), c.getString(R.string.max_in_52w), toCurrencyFormat(i.getWeekHigh(), 0, 2)));

        if (i.getWeekLow() != null)
            list.add(new Indicator(c.getString(R.string.low_52w), c.getString(R.string.min_in_52w), toCurrencyFormat(i.getWeekLow(), 0, 2)));

        if (i.getBeta() != null)
            list.add(new Indicator(c.getString(R.string.beta), c.getString(R.string.stocks_volatility), toDefaultFormat(i.getBeta(), 0, 2)));

        return list;
    }

}
