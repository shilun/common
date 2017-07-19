//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class Money implements Serializable, Comparable {
    private static final long serialVersionUID = 3761410806910104373L;
    public static final String DEFAULT_CURRENCY_CODE = "CNY";
    public static final int DEFAULT_ROUNDING_MODE = 6;
    private static final int[] centFactors = new int[]{1, 10, 100, 1000};
    public static final Money ZERO = new Money(0L);
    private long cent;
    private Currency currency;

    public Money() {
        this(0L);
    }

    public Money(long yuan, int cent) {
        this(yuan, cent, Currency.getInstance("CNY"));
    }

    public Money(long cent) {
        this.currency = Currency.getInstance("CNY");
        this.cent = cent;
    }

    public Money(long yuan, int cent, Currency currency) {
        this.currency = currency;
        this.cent = yuan * (long)this.getCentFactor() + (long)(cent % this.getCentFactor());
    }

    public Money(String amount) {
        this(amount, Currency.getInstance("CNY"));
    }

    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    public Money(String amount, Currency currency, int roundingMode) {
        this(new BigDecimal(amount), currency, roundingMode);
    }

    public Money(double amount) {
        this(amount, Currency.getInstance("CNY"));
    }

    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.cent = Math.round(amount * (double)this.getCentFactor());
    }

    public Money(BigDecimal amount) {
        this(amount, Currency.getInstance("CNY"));
    }

    public Money(BigDecimal amount, int roundingMode) {
        this(amount, Currency.getInstance("CNY"), roundingMode);
    }

    public Money(BigDecimal amount, Currency currency) {
        this((BigDecimal)amount, currency, 6);
    }

    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency = currency;
        this.cent = this.rounding(amount.movePointRight(currency.getDefaultFractionDigits()), roundingMode);
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(this.cent, this.currency.getDefaultFractionDigits());
    }

    public void setAmount(BigDecimal amount) {
        if(amount != null) {
            this.cent = this.rounding(amount.movePointRight(2), 6);
        }

    }

    public long getCent() {
        return this.cent;
    }

    /** @deprecated */
    public Currency getCurrency() {
        return this.currency;
    }

    public String getCurrencyCode() {
        return this.currency.getCurrencyCode();
    }

    public int getCentFactor() {
        return centFactors[this.currency.getDefaultFractionDigits()];
    }

    public boolean equals(Object other) {
        return other instanceof Money && this.equals((Money)other);
    }

    public boolean equals(Money other) {
        return this.currency.equals(other.currency) && this.cent == other.cent;
    }

    public int hashCode() {
        return (int)(this.cent ^ this.cent >>> 32);
    }

    public int compareTo(Object other) {
        return this.compareTo((Money)other);
    }

    public int compareTo(Money other) {
        this.assertSameCurrencyAs(other);
        return this.cent < other.cent?-1:(this.cent == other.cent?0:1);
    }

    public String money(){
        return this.toString();
    }

    public boolean greaterThan(Money other) {
        return this.compareTo(other) > 0;
    }

    public Money add(Money other) {
        this.assertSameCurrencyAs(other);
        return this.newMoneyWithSameCurrency(this.cent + other.cent);
    }

    public Money addTo(Money other) {
        this.assertSameCurrencyAs(other);
        this.cent += other.cent;
        return this;
    }

    public Money subtract(Money other) {
        this.assertSameCurrencyAs(other);
        return this.newMoneyWithSameCurrency(this.cent - other.cent);
    }

    public Money subtractFrom(Money other) {
        this.assertSameCurrencyAs(other);
        this.cent -= other.cent;
        return this;
    }

    public Money multiply(long val) {
        return this.newMoneyWithSameCurrency(this.cent * val);
    }

    public Money multiplyBy(long val) {
        this.cent *= val;
        return this;
    }

    public Money multiply(double val) {
        return this.newMoneyWithSameCurrency(Math.round((double)this.cent * val));
    }

    public Money multiplyBy(double val) {
        this.cent = Math.round((double)this.cent * val);
        return this;
    }

    public Money multiply(BigDecimal val) {
        return this.multiply(val, 6);
    }

    public Money multiplyBy(BigDecimal val) {
        return this.multiplyBy(val, 6);
    }

    public Money multiply(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);
        return this.newMoneyWithSameCurrency(this.rounding(newCent, roundingMode));
    }

    public Money multiplyBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);
        this.cent = this.rounding(newCent, roundingMode);
        return this;
    }

    public Money divide(double val) {
        return this.newMoneyWithSameCurrency(Math.round((double)this.cent / val));
    }

    public Money divideBy(double val) {
        this.cent = Math.round((double)this.cent / val);
        return this;
    }

    public Money divide(BigDecimal val) {
        return this.divide(val, 6);
    }

    public Money divide(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);
        return this.newMoneyWithSameCurrency(newCent.longValue());
    }

    public Money divideBy(BigDecimal val) {
        return this.divideBy(val, 6);
    }

    public Money divideBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);
        this.cent = newCent.longValue();
        return this;
    }

    public Money[] allocate(int targets) {
        Money[] results = new Money[targets];
        Money lowResult = this.newMoneyWithSameCurrency(this.cent / (long)targets);
        Money highResult = this.newMoneyWithSameCurrency(lowResult.cent + 1L);
        int remainder = (int)this.cent % targets;

        int i;
        for(i = 0; i < remainder; ++i) {
            results[i] = highResult;
        }

        for(i = remainder; i < targets; ++i) {
            results[i] = lowResult;
        }

        return results;
    }

    public Money[] allocate(long[] ratios) {
        Money[] results = new Money[ratios.length];
        long total = 0L;
        long[] remainder = ratios;
        int var6 = ratios.length;

        int i;
        for(i = 0; i < var6; ++i) {
            long ratio = remainder[i];
            total += ratio;
        }

        long var10 = this.cent;

        for(i = 0; i < results.length; ++i) {
            results[i] = this.newMoneyWithSameCurrency(this.cent * ratios[i] / total);
            var10 -= results[i].cent;
        }

        for(i = 0; (long)i < var10; ++i) {
            ++results[i].cent;
        }

        return results;
    }

    public String toString() {
        return this.getAmount().toString();
    }

    protected void assertSameCurrencyAs(Money other) {
        if(!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    protected long rounding(BigDecimal val, int roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    protected Money newMoneyWithSameCurrency(long cent1) {
        Money money = new Money(0.0D, this.currency);
        money.cent = cent1;
        return money;
    }

    public void setCent(long cent) {
        this.cent = cent;
    }
}
