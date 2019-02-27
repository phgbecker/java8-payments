package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Subscription {
    private Customer customer;
    private BigDecimal monthlyFee;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;

    public Subscription(Customer customer, BigDecimal monthlyFee, LocalDateTime beginDate) {
        this.customer = customer;
        this.monthlyFee = monthlyFee;
        this.beginDate = beginDate;
    }

    public Subscription(Customer customer, BigDecimal monthlyFee, LocalDateTime beginDate, LocalDateTime endDate) {
        this.customer = customer;
        this.monthlyFee = monthlyFee;
        this.beginDate = beginDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public Optional<LocalDateTime> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalPaid(ChronoUnit chronoUnit) {
        return monthlyFee.multiply(
                new BigDecimal(Optional.ofNullable(chronoUnit).orElse(ChronoUnit.MONTHS).between(
                        beginDate,
                        getEndDate().orElse(LocalDateTime.now()
                        )
                ))
        );
    }

    public long getTimeElapsed(ChronoUnit chronoUnit) {
        return chronoUnit.between(
                beginDate,
                getEndDate().orElse(LocalDateTime.now())
        );
    }

}
